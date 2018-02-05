(ns realtime.f02
  (:gen-class
    :implements [java.io.Serializable com.hazelcast.jet.function.DistributedSupplier]))

(defn -get [this]
  (new realtime.p1))
