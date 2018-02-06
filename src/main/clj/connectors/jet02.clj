(ns connectors.jet02
 "Streaming DAG with Read on a folder plus
 write to files whie using peekInputP
 to steal input from another vertex"
  (:import
    [com.hazelcast.jet.core DAG Edge]
    [com.hazelcast.jet.core.processor SourceProcessors DiagnosticProcessors SinkProcessors]
    [com.hazelcast.jet Jet]
    [com.hazelcast.jet.config JobConfig]
    [java.nio.charset StandardCharsets]
    [connectors f01]))

(def dag (DAG.))

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
  (new connectors.f01)
  StandardCharsets/UTF_8
  false))

(def writer
  (-> dag (.newVertex "writer" write-p)))

(def logger
  (.newVertex dag "logger"
  (DiagnosticProcessors/peekInputP
   (com.hazelcast.jet.core.processor.DiagnosticProcessors/writeLoggerP (new connectors.f01)))))

(.edge dag (Edge/between reader logger))
(.edge dag (Edge/between logger writer))

(def jet (Jet/newJetInstance))
(def config (JobConfig.))
(def job (.newJob jet dag config))

(comment
  (.cancel job)
  (doseq [j (.getJobs jet)] (.cancel j))

  ; important ! add a end of line
  (spit (str "in/more.txt") (str "bonjour\n" ) :append true)

  ; delete all files in input folder
  (doseq [f (.listFiles (clojure.java.io/as-file "in"))]
   (.delete f))

  )
