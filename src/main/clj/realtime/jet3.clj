; 1/2 Java Based vertices
; 1/2 CLJ based vertices
;

(ns realtime.jet3)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import 'FolderSource)
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])


(compile 'realtime.p1)

(defn dag-1 []
 (let [ dag (DAG.)
        folderSource
          (.newVertex dag "folder source" (OneSourceSupplier. "folder" FolderSource))
        myp1
          (.newVertex dag "p1" (OneSourceSupplier. "p1" realtime.p1))]
   (.edge dag (.to (Edge/from folderSource 0) myp1))))

(compile 'realtime.p2)

(defn dag-2 []
  (let [ dag (DAG.)
       folderSource
         (.newVertex dag "folder source" (OneSourceSupplier. "folder" FolderSource))
       myp1
         (.newVertex dag "p2" (OneSourceSupplier. "p2" realtime.p2))]
  (.edge dag (.to (Edge/from folderSource 0) myp1))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job
  (-> jet
   (.newJob (dag-3) config)))

(comment
 (.cancel job)
 (.shutdown jet)

)
