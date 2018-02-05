(ns client.simple
  (:import [com.hazelcast.jet Jet])
  (:gen-class))

(defn -main [& args]
  (Jet/newJetInstance))
