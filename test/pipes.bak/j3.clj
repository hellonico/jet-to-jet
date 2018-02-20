(ns pipes.j3
    "Creating an API"
  (:import
    [com.hazelcast.jet.aggregate AggregateOperations]
    [com.hazelcast.jet.function DistributedFunctions])
  (:require
    [chazel.core :refer :all]
    [jet.core :as jet]))

; new jet instance
(jet/new-instance)

(.clear   (hz-list "text"))
(add-all!
  (hz-list "text")
  ["hello world hello hello world i want more hello"])

(-> (jet/hz-pipeline)
    (.drawFrom  (jet/source :list "text" ))
    (.flatMap   (jet/new-fn (quote pipes.myfun)))
    (.groupBy   (DistributedFunctions/wholeItem) (AggregateOperations/counting) )
    (.drainTo   (jet/sink :map "counts"))
    (.getPipeline)
    (jet/new-job)
    (.join))

(clojure.pprint/pprint
  (hz-map "counts"))

; apply map to list
(-> (jet/hz-pipeline)
    (.drawFrom  (jet/source :list "text" ))
    (.flatMap   (jet/new-fn 'pipes.myfun))
    (.drainTo   (jet/sink :list "mapped"))
    (.getPipeline)
    (jet/new-job)
    (.join))

(clojure.pprint/pprint
  (hz-list "mapped"))
