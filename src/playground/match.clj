(ns playground.match
  (:require [clojure.core.match :refer [match]]))

(let [x 2]
  (match x
         1 :1
         2 :two!
         :3))

(match [false true false true]
       [_ true _ false] :1
       [false true false true] :ee
       [_ true false _] :bb)
;; => :ee

(match [[1 2 3] [4 5 6]]
       [[_ 1] [4 5 6]] :1
       [[1 2 3] [_ _ _]] :ee
       :else :not-found)
;; => :ee

(match '(1 2 3)
       ([1 3 _] :seq) :1
       ([1 _ _] :seq) :2
       ([1] :seq) :3
       ([1 2 3] :seq) :ee)
;; => :2

(match {:a 1 :b 2}
       {:a 1 :b 3} :1
       {:a _ :b 2} :ee)
;; => :ee


(match [1 2 3]
       [1 (:or 3 4) 3] :1
       [1 (:or 2 4) (:or 2 3)] :ee)
;; => :ee


(match [1 3]
       [(_ :guard odd?) (_ :guard #(even? %))] :1
       [(_ :guard (fn [x] (odd? x))) _] :2)
;; => :2


(defn fibbonachi [n]
  (match n
         (_ :guard #(<= % 0)) 0
         1 1
         _ (+ (fibbonachi (- n 1)) (fibbonachi (- n 2)))))

(fibbonachi 7)
;; => 13

(map fibbonachi (range 15))
;; => (0 1 1 2 3 5 8 13 21 34 55 89 144 233 377)

(defn green [names]
  (match (vec (re-seq #"\w+" names))
         [nickname] {:nickname nickname}
         [first-name last-name]
         {:first-name first-name :last-name last-name}
         [first-name middle-name last-name]
         {:first-name first-name :midlast (str middle-name "-#-" last-name)}
         [first-name last-name "of" "house" "name" (:or "ee" "boy")]
         {:obe-one last-name}
         [& all-names]
         (str (count all-names) " prikin! wrong")
         :else :not-found))

(green "nick dreem of house name boy")
;; => {:obe-one "dreem"}
