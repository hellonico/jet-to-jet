(ns pipes.p1
  "Present a first usage of using pipelines with Jet via Clojure")

(import [com.hazelcast.jet Pipeline Sources Sinks Jet])
(import [com.hazelcast.jet.config JobConfig])
(require '[chazel.core :refer :all])

(compile 'pipes.tolowercase)
(compile 'pipes.touppercase)

(def jet (Jet/newJetInstance))

(def my-list
  (-> jet
    (.getHazelcastInstance)
    (.getList "hello")))

(doseq [s ["one" "two" "three" "fourt"]]
  (.add my-list s))

; create pipeline
(def p (Pipeline/create))
(-> p
  (.drawFrom (Sources/list "hello"))
  (.map (new pipes.tolowercase))
  (.drainTo (Sinks/list "results")))

; config and run pipeline
(def config (JobConfig.))
(def job (.newJob jet p config))

; display result, see pipes.c1
(def results (-> jet (.getHazelcastInstance) (.getList "results")))
(clojure.pprint/pprint results)
