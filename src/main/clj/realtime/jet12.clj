; Distributed Jet Test
; Simple Aggregation Function, counting written in Java
;
(ns realtime.jet12)

(import '[com.hazelcast.jet.core DAG])
(import '[com.hazelcast.jet.core Edge])
(import '[com.hazelcast.jet Jet])
(import '[com.hazelcast.jet.config JobConfig])
(import '[com.hazelcast.jet.core WindowDefinition])

(compile 'realtime.p1)
(compile 'realtime.p3)

; used by piped processors
; (def tumbling (WindowDefinition/tumblingWindowDef 1))
(def tumbling (WindowDefinition/tumblingWindowDef 10))
(def cc (Helper/myCounting tumbling))

(defn dag-12 []
 (let [ dag (DAG.)
        myp3
         (.newVertex dag "p3" (OneSourceSupplier. "p3" realtime.p3))
        myp1
         (.newVertex dag "p1"  (OneSourceSupplier. "p1" realtime.p1))
        counting (.newVertex dag "counting" cc)]
        (->> counting  (Edge/between myp3) (.allToOne) (.edge dag))
        (->> myp1      (Edge/between counting) (.edge dag))))

(def config (JobConfig.))
(def jet (Jet/newJetInstance))
(def job (-> jet (.newJob (dag-12) config)) )

; total should be 240 (p3 currently sends 2 messages each time)

(comment
 (.cancel job)
 ; cancel all
 (doseq [j (.getJobs jet)] (.cancel j))
 (.shutdown jet)

)
