(ns solution.core
  (:require [digest])
  (:gen-class))

(def moves [["U" [0 -1]] ["D" [0 1]] ["L" [-1 0]] ["R" [1 0]]])

(defn valid? [i _ path] 
  (let [md5 (digest/md5 (str "gdjjyniy" path))] 
    (contains? #{\b \c \d \e \f} (nth md5 i))))

(defn valid-doors [path] 
  (keep-indexed #(when (valid? %1 %2 path) %2) moves))

(defn new-state [[[x1 y1] p1] [p2 [x2 y2]]] 
  [[(+ x1 x2) (+ y1 y2)] (str p1 p2)])

(defn valid-state? [state] 
  (every? #(and (>= % 0) (< % 4)) state))

(defn neighbors [state] 
  (->> 
    (map #(new-state state %) (valid-doors (second state)))
    (filter #(valid-state? (first %)))))

(defn goal? [[[x y] p]] 
  (and (= x 3) (= y 3)))

(defn bfs [start] 
  (loop [s (conj (clojure.lang.PersistentQueue/EMPTY) start) s-p nil len 0] 
    (cond 
      (empty? s) 
        [(second s-p) len]
      (goal? (peek s))
      (recur 
        (pop s) (or s-p (peek s) ) (max len (count (second (peek s)))))
      :else
        (recur (reduce conj (pop s) (neighbors (peek s))) s-p len))))

(defn -main
  [& args]
  (time (println (bfs [[0 0] ""]))))
