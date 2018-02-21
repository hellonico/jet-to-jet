(ns pipes.fn.traverse
  (:import [com.hazelcast.jet Traversers])
  (:gen-class
    :implements [java.io.Serializable com.hazelcast.jet.function.DistributedFunction]))

(defn -apply [this sentence]
  (-> sentence
    (.toLowerCase)
    (.split "\\W+")
    (Traversers/traverseArray)))
