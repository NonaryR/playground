(defproject playground "0.1.0"
  :description "all brand new (for me, of course) libs I checked"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/core.async "0.4.474"
                  :exclusions [org.clojure/core.cache
                               org.clojure/core.memoize]]
                 [org.clojure/core.match "0.3.0-alpha5"]
                 [com.rpl/specter "1.0.4"]
                 [com.datomic/datomic-free "0.9.5656"]
                 [mount "0.1.12"]

                 [org.clojure/core.logic "0.8.11"]

                 [anglican "1.0.0"]
                 [deodorant "0.1.3"
                  :exclusions [com.taoensso/encore]]
                 [bopp "0.1.5"
                  :exclusions [com.taoensso/encore
                               com.taoensso/truss]]
                 [bandit/bandit-core "0.2.1-SNAPSHOT"]
                 [distributions "0.1.3-SNAPSHOT"
                  :exclusions [net.mikera/vectorz-clj
                               net.mikera/core.matrix]]

                 [muse2/muse "0.4.4-SNAPSHOT"
                  :exclusions [riddley]]
                 [cats "0.4.0"]
                 [com.climate/claypoole "1.1.4"]
                 [manifold "0.1.7-alpha6" :exclusions [riddley]]
                 [http-kit "2.3.0-beta2"]
                 [com.cerner/clara-rules "0.17.0"]
                 [ru.prepor/kern "1.2.0-SNAPSHOT"
                  :exclusions [org.clojure/tools.reader
                               com.google.guava/guava]]])
