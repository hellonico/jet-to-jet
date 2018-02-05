(ns realtime.jet6
  " Simple Full CLJ based vertices
    Reading from folder and converting image to black and white")

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

(compile 'realtime.p5)
(compile 'realtime.p6)

(defn dag-6 []
  (let [ dag (DAG.)
       myp5
         (.newVertex dag "p5" (OneSourceSupplier. "p5" realtime.p5))
       myp6
         (.newVertex dag "p6" (OneSourceSupplier. "p6" realtime.p6))]
  (.edge dag (Edge/between myp5 myp6))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job
  (-> jet
   (.newJob (dag-6) config)))

(comment
 (.cancel job)
 (.shutdown jet)

)
