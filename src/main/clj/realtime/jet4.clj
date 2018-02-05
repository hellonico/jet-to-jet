; Simple Full CLJ based vertices
;
(ns realtime.jet4)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

(compile 'realtime.p3)
(compile 'realtime.p2)

(defn dag-4 []
  (let [ dag (DAG.)
       myp3
         (.newVertex dag "p3" (OneSourceSupplier. "p3" realtime.p3))
       myp2
         (.newVertex dag "p2" (OneSourceSupplier. "p2" realtime.p2))]
  (.edge dag (.to (Edge/from myp3 0) myp2))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job
  (-> jet
   (.newJob (dag-4) config)))

(comment
 (.cancel job)
 (.shutdown jet)

)
