(ns realtime.f03
  (:gen-class
   :implements [java.io.Serializable com.hazelcast.jet.function.DistributedToLongFunction]))

(def counter (atom 0))
(defn -applyAsLong [this value]
  (swap! counter inc)
  @counter)
