(ns client.simple
  "Simply Start a new Jet Instance. This does not do anything but start a jet instance"
  (:import [com.hazelcast.jet Jet])
  (:gen-class))

; eventually  all your code dependencies are in and so you can start a new 
; jet instance to join the show by calling this namespace.

(defn -main [& args]
  (Jet/newJetInstance))
