(ns realtime.jet8
  " Full CLJ based vertices
    Reading from folder and sorting files
    by finding if a cat is inside or not.")

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

(compile 'realtime.p5b)
(compile 'realtime.p7)

(defn dag-8 []
  (let [ dag  (DAG.)
         myp5 (.newVertex dag "p5b" (OneSourceSupplier. "p5b" realtime.p5b))
         myp7 (.newVertex dag "p7"  (OneSourceSupplier. "p7"  realtime.p7))]
  (.edge dag (Edge/between myp5 myp7))))

(def config (JobConfig.))
(def jet    (Jet/newJetInstance))
(def job    (.newJob jet (dag-8) config))

(defn -main[& args]
  (.join job)
  )

(comment
 (.cancel job)
 (.shutdown jet)

)
