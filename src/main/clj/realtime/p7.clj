(ns realtime.p7
  ""
  (:require
    [opencv3.core :refer :all]
    [opencv3.utils :as u])
  (:gen-class
    :implements [java.io.Closeable]
    :extends com.hazelcast.jet.core.AbstractProcessor))

(def cat-folder     (clojure.java.io/as-file "cat"))
(def others-folder  (clojure.java.io/as-file "others"))
(def detector
  (new-cascadeclassifier
    "src/main/resources/haarcascade_frontalcatface_extended.xml"))

(defn -init [ this context ]
  (.mkdirs cat-folder)()
  (.mkdirs others-folder))

(defn -tryProcess [ this & object ]
 (if (not (nil? object))
 (let [msg-img (.getImage (.getKey (second object)))
       buffer (u/buffered-image-to-mat msg-img)
       rects  (new-matofrect)
       fake   (.detectMultiScale detector buffer rects)
       has-cat? (> (count (.toArray rects)) 0)
       target-folder (if has-cat? cat-folder others-folder)
       ]
    (imwrite buffer (str (.getPath target-folder) "/jet_" (System/currentTimeMillis) ".jpg"))))
  true)

(defn -isCooperative[ this]
  false)

(defn -close[this]
  (println "> closing p6..."))
