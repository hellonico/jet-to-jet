(ns realtime.jet1
  "First Dag With Java Vertices")

(import '[com.hazelcast.jet.core DAG])
(import 'WebcamSource)
(import 'FolderSource)
(import 'SimpleLogSink)
(import [com.hazelcast.jet.core.processor DiagnosticProcessors])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

; Dag Definition
(def dag (DAG.))

(def webcamSource
  (.newVertex dag "webcam source"
        		(OneSourceSupplier. "webcam" WebcamSource)))

(def folderSource
  (.newVertex dag "folder source"
        		(OneSourceSupplier. "folder" FolderSource)))

(def logSink
  (.newVertex dag "console"
    (DiagnosticProcessors/peekInputP
           		(OneSourceSupplier. "console" SimpleLogSink))))

(.edge dag (Edge/between folderSource logSink))

; JET Start
(def jet
  (Jet/newJetInstance))

; JET Job Start

(def config
  (JobConfig.))
(def job
   (.newJob jet dag config))
; don't join on REPL!
; (.join job)
(comment
  (.cancel job)
  )
