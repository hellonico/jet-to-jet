(ns realtime.f01
  (:gen-class
    :implements [java.io.Serializable com.hazelcast.jet.function.DistributedFunction]))

(defn -apply [this input]
  "p1")
