(ns pipes.fn.touppercase
  (:gen-class
    :implements [java.io.Serializable com.hazelcast.jet.function.DistributedFunction]))

(defn -apply [this input]
  (.toUpperCase input))
