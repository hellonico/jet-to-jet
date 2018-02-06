(ns realtime.jet1
  "First Dag With Java Vertices")

(import [com.hazelcast.jet.core DAG Edge])
(import [com.hazelcast.jet.core.processor DiagnosticProcessors])
(import [com.hazelcast.jet Jet])
(import [com.hazelcast.jet.config JobConfig])

(import WebcamSource)
(import FolderSource)
(import SimpleLogSink)

; Dag Definition
(def dag (DAG.))

(def webcamSource
  (.newVertex dag "webcam"
        		(OneSourceSupplier. "webcam" WebcamSource)))

(def folderSource
  (.newVertex dag "folder"
        		(OneSourceSupplier. "folder" FolderSource)))

(def logSink
  (.newVertex dag "console"
    (DiagnosticProcessors/peekInputP
           		(OneSourceSupplier. "console" SimpleLogSink))))

(.edge dag (Edge/between folderSource logSink))

; JET Start
(def jet (Jet/newJetInstance))
(def config (JobConfig.))
(def job (.newJob jet dag config))

(comment
  ; don't join on REPL!
  ; (.join job)
  (.cancel job)
  )
