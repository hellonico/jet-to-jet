(ns pipes.pipe03
    "Wordcount for pros. (о´∀`о)"
  (:import
    [com.hazelcast.jet.aggregate AggregateOperations]
    [com.hazelcast.jet.function DistributedFunctions])
  (:require
    [chazel.core :refer :all]
    [jet.core :as jet]))

; start jet
(jet/new-instance)

; load book into hazelcast list
(def frankenstein
  (hz-list "frankenstein"))
  
(with-open [rdr (clojure.java.io/reader "src/main/resources/frankenstein.txt")]
     (.addAll frankenstein (into [] (line-seq rdr))))

(jet/pipeline [
 [:draw-from  (jet/source :list "frankenstein")]
 [:flat-map   (jet/new-fn (quote pipes.fn.traverse))]
 [:group-by   (DistributedFunctions/wholeItem) (AggregateOperations/counting) ]
 [:drain-to   (jet/sink :map "counts")]])

; print 10 words with highest frequency
(-> (hz-map "counts")
    (select "*" :page-size 10 :order-by #(compare (val %2) (val %1))  :as :map)
    :results
    (clojure.pprint/pprint))

; print 5 words with lowest frequency
(-> (hz-map "counts")
    (select "*" :page-size 5 :order-by #(compare (val %1) (val %2))  :as :map)
    :results
    (clojure.pprint/pprint))
