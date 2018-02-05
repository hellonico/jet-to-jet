(ns realtime.p6
    (:require [util.convertbw :as bw])
    (:import
      [javax.imageio ImageIO]
      [SerializableBufferedImage]
      [com.hazelcast.jet.core AbstractProcessor])
    (:gen-class
      :implements [java.io.Closeable]
      :extends com.hazelcast.jet.core.AbstractProcessor))

(defn -tryProcess [ this & object ]
 (if (not (nil? object))
  (bw/to-blackwhitefile
  (.getImage (.getKey (second object)))
    (str "test_" (System/currentTimeMillis) ".jpg")))
  true)

(defn -isCooperative[ this]
  false)

(defn -close[this]
  (println "> closing p6..."))
