(ns potty.core
  (:gen-class)
  (:require [potty.config-muncher :as muncher]))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (prn (muncher/munch {:input "resources/"
                       :output "output/"})))
