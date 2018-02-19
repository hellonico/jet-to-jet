(ns pipes.interspace
  (:gen-class
    :implements [java.io.Serializable com.hazelcast.jet.function.DistributedFunction]))

(defn -apply [this input]
  (interpose " " input))
