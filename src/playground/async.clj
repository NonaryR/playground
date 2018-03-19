(ns playground.async
  (:require [muse.core :as muse :refer [DataSource fmap run!! run!]]
            [muse.deferred :as md]
            [manifold.deferred :as d]
            [cats.core :as cats]
            [clojure.core.async :refer [go <!! <! timeout chan put!]]
            [clojure.set :refer [intersection union]]
            [org.httpkit.client :as http])
  (:refer-clojure :exclude [run!]))

(defn remote-req [id result]
  (let [wait (rand 1000)]
    (println "-->" id ".." wait)
    (go
      (<! (timeout wait))
      (println "<--" id)
      result)))

(defrecord FriendsOf [id]
  DataSource
  (fetch [_] (remote-req id (set (range id)))))

(FriendsOf. 10)
;; => #playground.async.FriendsOf{:id 10}

(run! (FriendsOf. 10))
;; => #object[clojure.core.async.impl.channels.ManyToManyChannel 0x2a2038e3 "clojure.core.async.impl.channels.ManyToManyChannel@2a2038e3"]

(<!! (run! (FriendsOf. 10)))
;; => #{0 7 1 4 6 3 2 9 5 8}
(run!! (FriendsOf. 10));; => #{0 7 1 4 6 3 2 9 5 8}

(fmap count (FriendsOf. 10))
;; => #object[muse.protocol.MuseMap 0x37329a6 "(clojure.core$count@6b71b813 playground.async.FriendsOf[10])"]

(run!! (fmap count (FriendsOf. 10)));; => 10

(fmap inc (fmap count (FriendsOf. 3)));; => #object[muse.protocol.MuseMap 0x4d76abb3 "(clojure.core$comp$fn__5529@75cf7761 playground.async.FriendsOf[3])"]

(run!! (fmap inc (fmap count (FriendsOf. 3))));; => 4

(defrecord ActivityScore [id]
  DataSource
  (fetch [_] (remote-req id (inc id))))

(defn first-friend-activity []
  (->> (FriendsOf. 10)
       (fmap sort)
       (fmap first)
       (muse/flat-map #(ActivityScore. %))))

(run!! (first-friend-activity))
;; => 1

(defn num-common-friends [x y]
  (fmap count (fmap intersection (FriendsOf. x) (FriendsOf. y))))

(run!! (num-common-friends 3 4))
;; => 3

(run!! (num-common-friends 5 5))
;; => 5

(defn friends-of-friends [id]
  (->> (FriendsOf. id)
       (muse/traverse #(FriendsOf. %))
       (fmap (partial apply union))))

(run!! (friends-of-friends 5))
;; => #{0 1 3 2}

(defrecord FriendsOf [id]
  DataSource
  (fetch [_] (remote-req id (set (range id))))

  muse/BatchedSource
  (fetch-multi [this others]
    (let [ids (cons id (map :id (cons this others)))]
      (->> ids
           (map #(vector %1 (set (range %1))))
           (into {})
           (remote-req ids)))))

(run!! (friends-of-friends 5))
;; => #{0 1 3 2}

(defrecord Numeric [n]
  md/DataSource
  ;; note that fetch returns a deferred value instead of a channel
  (fetch [_] (d/future (* 2 n)))

  md/LabeledSource
  (resource-id [_] n))

(md/run! (md/fmap inc (Numeric. 21)))
;; => << 43 >>

(md/run!! (md/fmap inc (Numeric. 21)))
;; => 43

(defrecord Post [id]
  DataSource
  (fetch [_] (remote-req id {:id id :author-id (inc id) :title "Muse"})))

(defrecord User [id]
  DataSource
  (fetch [_] (remote-req id {:id id :name "Alexey"})))

(defn get-post [id]
  (run! (cats/mlet [post (Post. id)
                    user (User. (:author-id post))]
                   (cats/return (assoc post :author user)))))

(<!! (get-post 10))
;; => {:id 10, :author-id 11, :title "Muse", :author {:id 11, :name "Alexey"}}

(defn async-get [url]
  (let [c (chan 1)] (http/get url (fn [res] (put! c res))) c))

(defrecord Gist [id]
  DataSource
  (fetch [_] (async-get (str "https://gist.github.com/" id))))

(defn gist-size [{:keys [headers]}]
  (get headers "Content-Size"))

(run!! (fmap gist-size (Gist. "nikototi/46cc69b626746eb895827ca8573b6bcd")))

(defn gist [id] (fmap gist-size (Gist. id)))

(run!! (fmap compare (gist "nikototi/46cc69b626746eb895827ca8573b6bcd")
             (gist "firemanxbr/d31dcefdf13458105203542449f7f4c5")))
