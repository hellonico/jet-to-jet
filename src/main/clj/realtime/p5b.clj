(ns realtime.p5b
  "This is a processor to emit serialized images inserted in the image folder of the desktop"
  (:require
    [opencv3.core :refer :all]
    [opencv3.utils :as u])
  (:import
      [javax.imageio ImageIO]
      [SerializableBufferedImage]
      [com.hazelcast.jet Traverser Traversers]
      [com.hazelcast.jet.datamodel TimestampedEntry]
      [com.hazelcast.jet.core AbstractProcessor])
    (:gen-class
      :implements [java.io.Closeable]
      :exposes-methods { emitFromTraverser emit }
      :extends com.hazelcast.jet.core.AbstractProcessor))

(defn -emit [this ta]
  (.emitFromTraverser this ta))

(defn -complete [ this ]
  (let [
    directory (clojure.java.io/file "/Users/niko/Desktop/images")
    files (filter #(.endsWith (.getName %) "jpg") (file-seq directory))
    ]
    (if (> (count files) 0)
    (let [
      now (System/currentTimeMillis)
      aa (map
        #(TimestampedEntry. now
          (SerializableBufferedImage.
            (u/mat-to-buffered-image (imread (.getPath %)))) nil)
         files)
      ta (Traverser/over (into-array TimestampedEntry aa))]
    (.emit this ta)
    (doseq [f files]
     (.delete (clojure.java.io/as-file f)))))
  Boolean/FALSE
  ))

(defn -isCooperative[ this]
  false)

(defn -close[this]
  (println "> closing p5..."))
