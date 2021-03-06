(ns pipes.j4
    "Creating an API"
  (:import
    [com.hazelcast.jet.aggregate AggregateOperations]
    [com.hazelcast.jet.function DistributedFunctions])
  (:require
    [chazel.core :refer :all]
    [jet.core :as jet]))

; new jet
(jet/new-instance)

(-> (hz-list "text")
    (add-all! ["hello world hello hello world"]))

(jet/pipeline [
    [:draw-from  (jet/source :list "text")]
    [:flat-map   (jet/new-fn (quote pipes.myfun))]
    [:group-by   (DistributedFunctions/wholeItem) (AggregateOperations/counting) ]
    [:drain-to   (jet/sink :map "counts")]])

(clojure.pprint/pprint
  (hz-map "counts"))
