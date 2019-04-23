(ns solution.core
  (:gen-class))

(def input ".^^.^^^..^.^..^.^^.^^^^.^^.^^...^..^...^^^..^^...^..^^^^^^..^.^^^..^.^^^^.^^^.^...^^^.^^.^^^.^.^^.^.")

(defn next-row [input] 
  (for [[x _ y] (partition 3 1 (concat '(true) input '(true)))] 
    (= x y)))

(defn solve [input n] 
  (->> 
    (iterate next-row (map #(= % \.) input))
    (take n)
    (map #(count (filter true? %)))
    (reduce +)))

(defn -main
  [& args]
  (time (println (solve input 400000))))

