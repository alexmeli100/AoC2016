(ns solution.core
  (:require [clojure.string :as str])
  (:import (java.io BufferedReader FileReader))
  (:gen-class))

(defn get-range [line] 
  (->> 
    (str/split line #"-")
    (map #(Long. %))
    (into [])))

(defn get-input [path] 
  (with-open [rdr (BufferedReader. (FileReader. path))] 
    (reduce 
      #(conj %1 (get-range %2))
      []
      (line-seq rdr))))

(defn part1 [input] 
  (loop [res 0 [[lo hi] & xs] input] 
    (cond  
      (> lo res) res 
      (<= res hi) (recur (inc hi) xs)
      :else (recur res xs))))

(defn part2 [input] 
  (let [a (first (first input))]
    (loop [tot a mx a [x & xs :as nums] input] 
      (cond 
        (empty? nums) (+ tot (- 4294967295 mx)) 
        (> (first x) (inc mx)) 
          (recur (+ tot (- (first x) mx 1)) (max mx (second x)) xs)
        :else 
          (recur tot (max mx (second x)) xs)))))

(defn -main
  [& args]
  (let [input (sort (get-input "resources/input.txt"))]
    (println (part2 input))))
