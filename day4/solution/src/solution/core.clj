(ns solution.core
  (:require [clojure.java.io :as io] 
            [clojure.string :as str])
  (:gen-class))

(def reg #"([a-z-]+)(\d+)\[(\w+)\]")

(defn parse-file [path] 
  (with-open [rdr (io/reader path)] 
    (doall (map #(re-find reg %) (line-seq rdr)))))

(defn get-str [name] 
  (->> 
    (remove #(= \- %) name)
    (frequencies)
    (reduce-kv #(conj %1 [(- %3) %2]) [])
    sort 
    (map second)
    (apply str)))

(defn valid? [[_ name _ check]] 
  (str/starts-with? (get-str name) check))

(defn part1 [lines] 
  (->> 
    (filter valid? lines) 
    (map #(Integer/parseInt (nth % 2)))
    (reduce +)))

(defn caesar-cipher [c id] 
  (if (= c \-)
    \space
    (char (+ 97 (mod (+ id (- (int c) 97)) 26)))))

(defn decrypt [line id] 
  (apply str (map #(caesar-cipher % id) line)))

(defn check-north [[_ name id _]] 
  (when (str/includes? (decrypt name (Integer/parseInt id)) "north") 
    id))

(defn part2 [lines] 
  (some check-north lines))

(defn -main
  [& args]
  (let [lines (parse-file "resources/input.txt")] 
    (println (part2 lines))))
