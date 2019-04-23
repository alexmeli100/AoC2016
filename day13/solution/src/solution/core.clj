(ns solution.core
  (:require [clojure.data.priority-map :refer :all])
  (:gen-class))

;; eucladian distance between 2 points
(defn euclid-dis [[x1 y1]] 
  (let [x (- 31 x1)
        y (- 39 y1)]
    (Math/sqrt (+ (* x x) (* y y)))))

(def dirs [[0 -1] [0 1] [1 0] [-1 0]])

(def initial-state 
  {:g 0 
   :h (euclid-dis [1 1])
   :state [1 1]})

(defn even-ones? [num] 
  (->> 
    (Long/bitCount num) 
    even?))

(defn valid? [coord] 
  (not (some #(< % 0) coord)))

(defn trans [[x y]] 
  (+ (* x (+ x 3 (* 2 y)))
     (* y (+ 1 y))
     1350))

(defn children [{:keys [state g] :as node} h-fn] 
  (->> 
    (map #(mapv + state %) dirs)
    (filter #(and (valid? %) (even-ones? (trans %))))
    (map #(assoc {:g (inc g) :h (h-fn %)} :state %))))

(defn euclid-dis [[x1 y1]] 
  (let [x (- 31 x1)
        y (- 39 y1)]
    (Math/sqrt (+ (* x x) (* y y)))))

(defn goal-fn [node] 
  (= (:h node) 0.0))

(defn add-node [node queue] 
  (assoc queue node (+ (:g node) (:h node))))

(defn add-queue [nodes queue explored] 
  (reduce 
    (fn [[queue explored] node] 
      [(add-node node queue) (assoc explored (:state node) (:g node))]) 
    [queue explored]
    (filter 
      #(not (contains? explored (:state %)))
      nodes)))

(defn part1 [explored node] 
  (get explored node))

(defn part2 [explored] 
  (count (filter #(-> % val (< 51)) explored)))

(defn A*-search [start goal? neighbors h-fn ] 
  (let [h (memoize h-fn)]
    (loop [frontier (priority-map start 0) explored {}] 
      (let [front (first (peek frontier))] 
        (if (goal? front) 
          (part2 explored)
          (let [succs (neighbors front h)
                [new-f new-e] (add-queue succs (pop frontier) explored)]
            (recur new-f new-e)))))))

(defn -main
  [& args]
  (println (A*-search initial-state goal-fn children euclid-dis)))
