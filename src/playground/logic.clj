(ns playground.logic
  (:require [clojure.core.logic :as l :refer [run* membero]]
            [clojure.core.logic.fd :as fd]
            [clojure.core.logic.datomic :as datomic]))

(run* [q]
  (membero q [1 2 3])
  (membero q [2 4])
  (membero q [2 3 4]))

(run* [q]
  (l/== q 1)
  (l/== q 2))

(run* [q]
  (l/fresh [a]
    (membero q [1 2 3])
    (membero a [3 4 5])
    (l/== q a)))

(run* [q]
  (l/fresh [a]
    (membero a [3 4 5])
    (l/== q a)
    (membero q [1 2 3])))

(run* [q]
  (l/fresh [a]
    (l/== q a)
    (membero a [3 4 5])
    (membero q [1 2 3])))

(run* [q]
  (l/conde
   [l/succeed]))

(run* [q]
  (membero 7 [1 3 8 q]))


(defn get-square [rows x y]
  (for [x (range x (+ x 3))
        y (range y (+ y 3))]
    (get-in rows [x y])))

(defn init [vars hints]
  (if (seq vars)
    (let [hint (first hints)]
      (l/all
        (if-not (zero? hint)
          (l/== (first vars) hint)
          l/succeed)
        (init (next vars) (next hints))))
    l/succeed))

(defn sudokufd [hints]
  (let [vars (repeatedly 81 l/lvar)
        rows (->> vars (partition 9) (map vec) (into []))
        cols (apply map vector rows)
        sqs  (for [x (range 0 9 3)
                   y (range 0 9 3)]
               (get-square rows x y))]
    (l/run 1 [q]
      (l/== q vars)
      (l/everyg #(fd/in % (fd/domain 1 2 3 4 5 6 7 8 9)) vars)
      (init vars hints)
      (l/everyg fd/distinct rows)
      (l/everyg fd/distinct cols)
      (l/everyg fd/distinct sqs))))

(def hints
  [2 0 7 0 1 0 5 0 8
   0 0 0 6 7 8 0 0 0
   8 0 0 0 0 0 0 0 6
   0 7 0 9 0 6 0 5 0
   4 9 0 0 0 0 0 1 3
   0 3 0 4 0 1 0 2 0
   5 0 0 0 0 0 0 0 1
   0 0 0 2 9 4 0 0 0
   3 0 6 0 8 0 4 0 9])

(sudokufd hints)
