; Distributed Jet Test
; All messages sent to all participants
;
(ns realtime.jet09)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

; TO READ
; (import '[com.hazelcast.jet.core.processor Processors Processors$NoopP])

(compile 'realtime.p1)
(compile 'realtime.p3)

(defn dag-09 []
  (let [ dag (DAG.)
       myp3
         (.newVertex dag "p3" (OneSourceSupplier. "p3" realtime.p3))
       myp1
         (.newVertex dag "p1" (ManySourceSupplier. "p1" realtime.p1))]
  (.edge dag (.distributed  (Edge/between myp3 myp1)))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job (-> jet (.newJob (dag-09) config)) )

(comment
 (.cancel job)
 (.shutdown jet)

)
