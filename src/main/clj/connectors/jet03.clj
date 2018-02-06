(ns connectors.jet03
  "insert random elements in a hazelcast map. The map is retrived
  from the hazelcast instance of the jet instance.
  This is using chazel as the hazelcast client and is almost independant of Jet itself."
  (:require [chazel.core :refer :all])
  (:import [com.hazelcast.jet Jet]))

;https://github.com/tolitius/chazel#connecting-as-a-client

(defn -main[& args]
  (let [
    jet (Jet/newJetInstance)
    appl_ (hz-map (or (first args) :appl2))]
     (dotimes [_ 100] (put! appl_  (str "lemon" (rand-int 100)) (rand-int 10)))
     (.shutdown jet)))
