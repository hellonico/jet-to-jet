(ns realtime.p14
    (:import
      [com.hazelcast.jet Traverser Traversers]
      [com.hazelcast.jet.datamodel TimestampedEntry])
    (:gen-class
      :implements [java.io.Closeable]
      :exposes-methods { emitFromTraverser emit }
      :extends com.hazelcast.jet.core.AbstractProcessor))

(def counter (atom 0))
(defn -init[ this context]
  (println "hello p14")
  (reset! counter 0))

(defn -emit [this ta]
  (.emitFromTraverser this ta))

(defn -complete [ this ]
  (let [
     now (System/currentTimeMillis)
     t (TimestampedEntry. now "value" (rand 10))
     ta (Traverser/over (into-array TimestampedEntry [t]))]
  (.emit this ta)
  (swap! counter inc)
  (> @counter 100)))

(defn -isCooperative[ this]
  false)

(defn -close[this]
  (println "> closing p14..."))
