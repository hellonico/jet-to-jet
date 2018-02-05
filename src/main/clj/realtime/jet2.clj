;
; CLJ based vertices
;

(ns realtime.jet2)

(import '[com.hazelcast.jet.core DAG])
(import 'FolderSource)
; (import '[realtime p1])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

(def config (JobConfig.))
(def jet (Jet/newJetInstance))

(def dag (DAG.))

(def folderSource
  (.newVertex dag "folder source"
    (OneSourceSupplier. "folder" FolderSource)))

(compile 'realtime.p1)
(import 'realtime.p1)

(def myp1
  (.newVertex dag "p1"
    (OneSourceSupplier. "p1" realtime.p1)))
; (def myp2
;   (.newVertex dag "p2"
;     (OneSourceSupplier. "p2" realtime.p1)))

(.edge dag (.to (Edge/from folderSource 0) myp1))
; (.edge dag (.to (Edge/from folderSource 1) myp2))

(def job
  (-> jet
   (.newJob dag config)))

(comment
 (.cancel job)


 (.shutdown jet)

)
