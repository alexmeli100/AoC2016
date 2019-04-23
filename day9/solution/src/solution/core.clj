(ns solution.core
  (:require [clojure.string :as str])
  (:gen-class))

(defn read-file [path] 
  (str/trim (slurp path)))

(defn get-marker [data]
  (let [cut-off (inc (.indexOf data ")"))] 
    (->> 
      (subs data 0 cut-off)
      (re-seq #"\d+")
      (map #(Integer. %))
      (concat (list cut-off)))))

(defn decompress [data total] 
  (cond 
    (str/blank? data) total
    (not= (first data) \( ) 
      (decompress (subs data 1) (inc total)) 
    (= (first data) \( ) 
      (let [[cut amt size] (get-marker data)] 
        (decompress 
          (subs data (+ cut amt)) 
          (+ total (* size (decompress (subs data cut (+ cut amt)) 0)))))))

(defn -main
  [& args]
  (time (println (decompress (read-file "resources/input.txt") 0))))
