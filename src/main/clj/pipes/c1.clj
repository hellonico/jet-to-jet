(ns pipes.c1
  (:require [clojure.pprint])
  (:import [com.hazelcast.jet Jet]))

(defn -main[ & args]
  (let [  jet (Jet/newJetInstance)
          results (-> jet (.getHazelcastInstance) (.getList "results"))]
  (clojure.pprint/pprint results)
  (.shutdown jet)))
