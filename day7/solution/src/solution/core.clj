(ns solution.core
  (:require [clojure.string :as str] 
            [clojure.set :as set])
  (:import (java.io BufferedReader FileReader))
  (:gen-class))

(defn abba? [ip] 
  (some 
    #(and (= (first %) (last %))
          (= (second %) (nth % 2)) 
          (not= (first %) (second %)))
    (partition 4 1 ip)))

(defn split-line [line] 
  (let [ips (str/split line #"\[([^\]]+)\]") 
        hypers (map second (re-seq #"\[([^\]]+)\]" line))] 
    [ips hypers]))

(defn valid-TLS? [line] 
  (let [[ips hypers] (split-line line)]
    (and 
      (some abba? ips)
      (every? (complement abba?) hypers))))

(defn aba? [[a b c]] 
  (when (and (= a c) (not= a b)) 
    [a b]))

(defn abas [s] 
  (keep aba? (partition 3 1 s)))

(defn valid-SSL? [line] 
  (let [[ips hypers] (split-line line)
        aba-s (set (mapcat abas ips))
        bab-s (set (mapcat (comp #(map reverse %) abas) hypers))]
    (boolean (seq (set/intersection aba-s bab-s)))))

(defn solve [path] 
  (with-open [rdr (BufferedReader. (FileReader. path))] 
    (count 
      (filter valid-SSL? (line-seq rdr)))))

(defn -main
  [& args]
  (time (println (solve "resources/input.txt"))))
