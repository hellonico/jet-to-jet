(ns client.simple
  "Simply Start a new Jet Instance"
  (:import [com.hazelcast.jet Jet])
  (:gen-class))

(defn -main [& args]
  (Jet/newJetInstance))
