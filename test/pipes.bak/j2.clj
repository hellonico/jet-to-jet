(ns pipes.j2
    "Translate the first jet example"
  (:import
    [com.hazelcast.jet.aggregate AggregateOperations]
    [com.hazelcast.jet.function DistributedFunctions]
    [com.hazelcast.jet Sources Sinks])
  (:require
    [chazel.core :refer :all]
    [jet.core :as jet]))

; new jet
(jet/new-instance)

(add-all!
  (hz-list "text")
  ["hello world hello hello world"])

(-> (jet/hz-pipeline)
    (.drawFrom  (Sources/list "text"))
    (.flatMap   (jet/new-fn 'pipes.myfun))
    (.groupBy   (DistributedFunctions/wholeItem) (AggregateOperations/counting) )
    (.drainTo   (Sinks/map "counts"))
    (.getPipeline)
    (jet/new-job)
    (.join))

(clojure.pprint/pprint
  (hz-map "counts"))

(comment

  (.clear (hz-map "counts"))
  (.clear (hz-list "text"))

  )
