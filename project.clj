(defproject realtime-image-recognition "1.0-SNAPSHOT"
  :injections [
  (clojure.lang.RT/loadLibrary org.opencv.core.Core/NATIVE_LIBRARY_NAME)
  ]
  :main jet.client
  :source-paths ["src/main/clj"]
  :aot [pipes.touppercase]
  :java-source-paths ["src/main/java"]
  :resource-paths ["src/main/resources"]
  :repositories {
      "vendredi"
         "https://repository.hellonico.info/repository/hellonico/"
      "snapshot-repository"
          "https://oss.sonatype.org/content/repositories/snapshots"}
  :dependencies [
    [org.clojure/clojure "1.9.0"]
    [org.slf4j/slf4j-simple "1.7.21"]
    [origami "0.1.10"]

    ; NOT NEEDED ANYMORE
    [org.boofcv/boofcv-core "0.28"]
    [org.boofcv/boofcv-WebcamCapture "0.28"]

    [com.hazelcast.jet/hazelcast-jet "0.6-SNAPSHOT"]

    [chazel "0.1.17-SNAPSHOT"]
    [camel-snake-kebab "0.4.0"]
    ])
