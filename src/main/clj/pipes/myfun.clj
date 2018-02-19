(ns pipes.myfun
  (:import [com.hazelcast.jet Traversers])
  (:gen-class
    :implements [java.io.Serializable com.hazelcast.jet.function.DistributedFunction]))

(defn -apply [this word]
  (-> word
    (.toLowerCase)
    (.split "\\W+")
    (Traversers/traverseArray)
    ))
