(ns connectors.jet02
"Streaming DAG with Read on a folder
+
 write to files whie using peekInputP
 to steal input from another vertex
")

(import [com.hazelcast.jet.core DAG Edge])
(import [com.hazelcast.jet.core.processor DiagnosticProcessors])
(import [com.hazelcast.jet Jet])
(import [com.hazelcast.jet.config JobConfig])

(import [java.nio.charset StandardCharsets])
(import [com.hazelcast.jet.core.processor SinkProcessors])
(import [com.hazelcast.jet.core.processor SourceProcessors])

(compile 'connectors.f01)

(def dag (DAG.))

(def source-folder "in")

(def reader
    (.localParallelism
    (.newVertex dag "reader"
     (SourceProcessors/streamFilesP
      source-folder
      StandardCharsets/UTF_8
      "*" )) 1))

(def writer
  (.newVertex dag "writer"
    (SinkProcessors/writeFileP "out" (new connectors.f01) StandardCharsets/UTF_8 false )))

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
 (spit (str "in/more.txt") (str "logger and writer\n" ) :append true)

 ; delete all files in input folder
 (doseq [f (.listFiles (clojure.java.io/as-file "in"))]
   (.delete f))

  )
