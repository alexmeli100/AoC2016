(ns solution.core
(:require [clojure.string :as str])
  (:gen-class))

(defn valid? [[a b c]] 
  (and 
    (> (+ a b) c)
    (> (+ b c) a)
    (> (+ a c) b)))

(defn to_int [line] 
  (map 
    #(Integer. %)
    (str/split (str/trim line) #"\s+")))

(defn part1 [lines] 
  (->> 
    lines
    (filter #(valid? (to_int %)))
    count))

(defn part2 [lines]
  (->> 
    (map to_int lines)
    (apply mapcat vector)
    (partition 3)
    (filter valid?)
    count))

(defn -main
  [& args]
  (with-open [rdr (clojure.java.io/reader "resources/input.txt")] 
    (println (part2 (line-seq rdr)))))

