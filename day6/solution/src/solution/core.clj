(ns solution.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn max-min [coll] 
  (let [data (frequencies coll)
        max-val (key (apply max-key val data))
        min-val (key (apply min-key val data))] 
    [max-val min-val]))

(defn split-input [path] 
  (->> 
    (slurp path)
    (str/split-lines)))

(defn get-cols [input] 
  (apply map vector input))

(defn solve [input] 
  (let [result (map max-min (get-cols input))] 
    [(map first result) (map second result)]))


(defn -main
  [& args]
  (let [input (split-input "resources/input.txt")]
    (time (println (solve input)))))
