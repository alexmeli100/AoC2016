(ns solution.core
  (:require [clojure.set :as set] 
            [clojure.math.combinatorics :as comb]
            [clojure.data.priority-map :refer :all])
  (:gen-class))

(def initial-state 
  {1 #{"TG" "TM" "PG" "SG" "EG" "EM" "DM" "DG"}
   2 #{"SM" "PM"}
   3 #{"OG" "OM" "RG" "RM"}
   4 #{}
   :E 1})

(def initial-node
  {:state initial-state
   :g 0
   :h 20})

(defn gen-floor [[f s] state] 
 (loop [floor 1] 
   (if (contains? (get state floor) (str f \G))
     floor 
     (recur (inc floor)))))

(defn add-group [floor elems state] 
 (->> 
   (filter #(= (second %) \M) elems)
   (map #(str floor (gen-floor % state)))))

;; string representation of state
(defn groups [state] 
  (->> 
    (dissoc state :E)
    (reduce-kv 
      #(concat %1 (add-group %2 %3 state)) [])
    sort
    (apply str (:E state))))

(defn get-comp [floor id] 
  (->> 
    (filter #(= (second %) id) floor) 
    (map first)
    set))

;; check if floor is valid
(defn valid-floor? [floor] 
  (let [chips (get-comp floor \M)
        gens (get-comp floor \G)]  
    (not 
      (some 
        #(and (not (contains? gens %)) (> (count gens) 0))
        chips))))

(defn valid-state? [state] 
  (and 
    (not-empty (get state (:E state)))
    (every? valid-floor? (vals (dissoc state :E)))))

(defn distance [state] 
  (reduce-kv 
    #(+ (* (- 4 %2) (count %3)) %1)
    0 
    (dissoc state :E)))

(defn valid-move? [move] 
  (and 
    (not-empty move)
    (< (count move) 3)))

(defn moves [state] 
  (->> 
    (vec (get state (:E state)))
    (comb/subsets)
    (filter valid-move?)
    (map set)))

(defn check-move [state dir] 
  (or 
    (and (< (:E state) 4) (= dir "U"))
    (and (> (:E state) 1) (= dir "D"))))

(defn move-dir [state move dir f] 
  (when (check-move state dir) 
    (let [old-pos (:E state)
          elem (get state old-pos)
          new-pos (update state :E f) 
          new (update new-pos (:E new-pos) #(set/union move %))]
      (assoc new old-pos (apply disj elem move)))))

(defn new-states [state] 
  (->> 
    (moves state)
    (mapcat 
      (juxt #(move-dir state % "U" inc) 
            #(move-dir state % "D" dec)))))

(defn children [{:keys [state g] :as node} h-fn] 
  (->> 
    (new-states state)
    (filter #(and (not (nil? %)) (valid-state? %)))
    (map #(assoc {:g (inc g) :h (h-fn %)} :state %))))

(defn goal-fn [node] 
  (= (:h node) 0))

(defn add-node [node queue] 
  (assoc queue node (+ (:g node) (:h node))))

(defn add-queue [nodes queue explored] 
  (reduce 
    (fn [[queue explored] node] 
      [(add-node node queue) (conj explored (groups (:state node)))]) 
    [queue explored]
    (filter 
      #(not (contains? explored (groups (:state %))))
      nodes)))

(defn A*-search [start goal? neighbors h-fn ] 
  (let [h (memoize h-fn)]
    (loop [frontier (priority-map start 0) explored #{}] 
      (let [front (first (peek frontier))] 
        (if (goal? front) 
          (:g front)
          (let [succs (neighbors front h)
                [new-f newe] (add-queue succs (pop frontier) explored)]
            (recur new-f new-e)))))))

(defn -main
  [& args]
  (time (println (A*-search initial-node goal-fn children distance))))

