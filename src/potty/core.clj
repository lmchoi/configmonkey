(ns potty.core
  (:gen-class)
  (:require [potty.required-attributes-finder :as finder]
            [potty.stripper :as extractor]))

(defn -main
  "I don't do a whole lot ... yet."
  [filename & args]
  (prn  (-> (finder/find-attributes-deprecated (str "resources/" filename ".erb"))
            (extractor/extract-values-deprecated (str "resources/" filename)))))
