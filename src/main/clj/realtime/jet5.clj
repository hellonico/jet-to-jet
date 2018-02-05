; Simple Full CLJ based vertices
; Reading images from folder

(ns realtime.jet5)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

(compile 'realtime.p5)
(compile 'realtime.p2)

(defn dag-5 []
  (let [ dag (DAG.)
       myp3
         (.newVertex dag "p5" (OneSourceSupplier. "p5" realtime.p5))
       myp2
         (.newVertex dag "p2" (OneSourceSupplier. "p2" realtime.p2))]
  (.edge dag (.to (Edge/from myp3 0) myp2))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job
  (-> jet
   (.newJob (dag-5) config)))

(comment
 (.cancel job)
 (.shutdown jet)

)
