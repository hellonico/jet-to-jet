(ns realtime.f14
  (:gen-class
    :implements [java.io.Serializable com.hazelcast.jet.function.DistributedToLongFunction]))

(defn -applyAsLong [this input]
  (.getValue input) )
