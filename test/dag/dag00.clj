(ns dag.dag00
    "Almost Empty DAG. 1 vertex, does not nothing but create a folder"
    (:import [java.nio.charset Charset StandardCharsets])
    (:require [jet.core :as jet]))

 ; start JET
(def jet
  (jet/new-instance))

(def mydag
  (jet/dag
    {:writer (jet/sink-processor   :writeFileP "out")
     :reader (jet/source-processor :streamFilesP "in" (Charset/forName "UTF-8") "*")
    }))

; deploy job
(def job (jet/new-job mydag))
 (.cancel job)

(.getClass "in")
