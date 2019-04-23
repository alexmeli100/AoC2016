(ns solution.core
  (:require [clojure.java.io :as io])
  (:gen-class))

(def patterns 
  [#"(swap position) (\d+) with position (\d+)"
   #"(swap letter) ([a-z]) with letter ([a-z])"
   #"(reverse) positions (\d+) through (\d+)"
   #"(move) position (\d+) to position (\d+)"
   #"rotate (left|right) (\d+) step"
   #"rotate (based) on position of letter ([a-z])"])

(defn rotate-left [s n] 
  (let [shift (mod n (count s))]
    (concat (drop shift s) (take shift s))))

(defn rotate-right [s n] 
  (let [shift (mod n (count s))]
    (rotate-left s (- (count s) shift))))

(defn rotate-based [s rev c] 
  (let [pos (.indexOf s c)] 
    (if rev 
      (rotate-left s (nth [1 1 6 2 7 3 0 4] pos))
      (rotate-right s (+ 1 pos (if (>= pos 4) 1 0))))))

(defn swap-pos [s i j] 
  (-> 
    (vec s)
    (assoc i (nth s j))
    (assoc j (nth s i))))

(defn swap-lett [s x y] 
  (swap-pos s (.indexOf s x) (.indexOf s y)))

(defn move [s x y] 
  (let [c (nth s x) 
        s1 (remove #(= c %) s)] 
    (concat (take y s1) (list c) (drop y s1))))

(defn reverse-pos [s x y] 
  (let [s1 (vec s)] 
    (concat 
      (take x s) 
      (reverse (subvec s1 x (inc y)))
      (drop (inc y) s))))

(defn process [line] 
  (rest (some #(re-find % line) patterns)))

(defn parse-lines [path] 
  (with-open [rdr (io/reader path)] 
    (doall (map process (line-seq rdr)))))

(defn transform [pass [op & r] rev] 
  (case op 
    "left"(apply (if rev rotate-right rotate-left) pass (map #(Integer. %) r))
    "right" (apply (if rev rotate-left rotate-right) pass (map #(Integer. %) r))
    "based" (apply rotate-based pass rev (map first r))
    "move" (apply move pass ((if rev reverse identity) (map #(Integer. %) r)))
    "swap position" (apply swap-pos pass  (map #(Integer. %) r))
    "swap letter" (apply swap-lett pass  (map first r))
    "reverse" (apply reverse-pos pass (map #(Integer. %) r))))

(defn solve [lines & {:keys [rev] :or {rev false}}] 
  (reduce 
    #(transform %1 %2 rev) 
    '(\f \b \g \d \c \e \a \h) 
    ((if rev reverse identity) lines)))

(defn -main
  [& args]
  (let [ins (parse-lines "resources/input.txt")] 
    (println (solve ins :rev true))))
