(ns pipes.fn.tolowercase
  (:gen-class
    :implements [java.io.Serializable com.hazelcast.jet.function.DistributedFunction]))

(defn -apply [this input]
  (.toLowerCase input))
