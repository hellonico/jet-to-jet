(ns connectors.jet05
   (:require [jet.core :as jet])
   (:import
    [com.hazelcast.jet.core Edge]
    [com.hazelcast.jet.core.processor SourceProcessors DiagnosticProcessors SinkProcessors]
    [java.nio.charset StandardCharsets]))

(def dag (jet/new-dag))

(def folder-stream
  (SourceProcessors/streamFilesP
      "in"
      StandardCharsets/UTF_8
      "*" ))

(def reader
  (-> dag
   (.newVertex "reader" folder-stream)
   (.localParallelism 1)))

(def write-p
  (SinkProcessors/writeFileP
  "out"
  (jet/new-fn 'connectors.f01)
  StandardCharsets/UTF_8
  false))

(def writer
  (-> dag (.newVertex "writer" write-p)))

(.edge dag (Edge/between reader writer))

(def jet (jet/new-instance)
(def job (jet/new-job dag))

(comment
  (.cancel job)
  (doseq [j (.getJobs jet)] (.cancel j))

  ; important ! add a end of line
  (spit (str "in/more2.txt") (str "bonjour3\n" ) :append true)

  ; delete all files in input folder
  (doseq [fol ["in" "out"]]
  (doseq [f (.listFiles (clojure.java.io/as-file fol))]
   (.delete f)))

  )
