; Distributed Jet Test
; All messages sent to one participants, but ... does not work :/
;
(ns realtime.jet10)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])

(import '[com.hazelcast.jet.function DistributedFunction])

; TO READ
; (import '[com.hazelcast.jet.core.processor Processors Processors$NoopP])

(compile 'realtime.p1)
(compile 'realtime.p3)
(compile 'realtime.f01)

(defn dag-10 []
  (let [ dag (DAG.)
       myp3
         (.newVertex dag "p3" (OneSourceSupplier. "p3" realtime.p3))
       myp1
         (.newVertex dag "p1" (NotOwnerSourceSupplier. "p1" realtime.p1))]
  (-> (Edge/between myp3 myp1)
   (.partitioned  (new realtime.f01))
   (.distributed)
   ((fn[x] (.edge dag x ))))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job (-> jet (.newJob (dag-10) config)) )

(comment
 (.cancel job)
 (.shutdown jet)

)
