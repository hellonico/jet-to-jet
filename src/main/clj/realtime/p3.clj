(ns realtime.p3
    (:import
      [com.hazelcast.jet Traverser Traversers]
      [com.hazelcast.jet.datamodel TimestampedEntry]
      [com.hazelcast.jet.core AbstractProcessor])
    (:gen-class
      :implements [java.io.Closeable]
      :exposes-methods { emitFromTraverser emit }
      :extends com.hazelcast.jet.core.AbstractProcessor))

(def counter (atom 0))
(defn -init[ this context]
  (reset! counter 0))

(defn -emit [this ta]
  (.emitFromTraverser this ta))

(defn -complete [ this ]
  (let [
    now (System/currentTimeMillis)
    t (TimestampedEntry. now "hello" (str "its me[" @counter "]"))
    ta (Traverser/over (into-array TimestampedEntry [t t]))
    ]
  (.emit this ta)
  (swap! counter inc)
  ;Boolean/TRUE
  (> @counter 10)
  ))

(defn -isCooperative[ this]
  false)

(defn -close[this]
  (println "> closing p3..."))
