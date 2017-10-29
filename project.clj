(defproject playground "0.1.0"
  :description "all brand new (for me, of course) libs I checked"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/core.match "0.3.0-alpha5"]
                 [org.clojure/clojure "1.9.0-alpha17"]
                 [org.clojure/core.async "0.3.443"
                  :exclusions [org.clojure/core.cache
                               org.clojure/core.memoize]]
                 [com.rpl/specter "1.0.4"]
                 [com.datomic/datomic-free "0.9.5561.62"]
                 [mount "0.1.12-SNAPSHOT"]

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
                 [uncomplicate/bayadera "0.1.0-SNAPSHOT"
                  :exclusions [org.clojure/core.memoize]]
                 [uncomplicate/neanderthal "0.16.1"
                  :exclusions [org.clojure/core.cache
                               org.clojure/core.memoize
                               uncomplicate/commons]]

                 [huri "0.10.0-SNAPSHOT"
                  :exclusions [io.aviso/pretty
                               com.google.guava/guava
                               com.taoensso/encore
                               com.taoensso/truss
                               com.taoensso/timbre]]
                 ])
