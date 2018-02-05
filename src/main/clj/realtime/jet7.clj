(ns realtime.jet7
  " Simple Full CLJ based vertices
    Reading from folder and converting image to black and white
    This second version uses origami to load the pictures from the
    source folder")

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

(compile 'realtime.p5b)
(compile 'realtime.p6)

(defn dag-7 []
  (let [ dag  (DAG.)
         myp5 (.newVertex dag "p5b" (OneSourceSupplier. "p5b" realtime.p5b))
         myp6 (.newVertex dag "p6"  (OneSourceSupplier. "p6" realtime.p6))]
  (.edge dag (Edge/between myp5 myp6))))

(def config (JobConfig.))
(def jet    (Jet/newJetInstance))
(def job    (.newJob jet (dag-7) config)))


(comment
 (.cancel job)
 (.shutdown jet)

)
