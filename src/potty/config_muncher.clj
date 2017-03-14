(ns potty.config-muncher
  (:require [potty.stripper :as extractor]))

(defn- munch-env
  [filename]
  {(keyword (second (re-matches #".*-(.+)\.yml" filename)))
   (extractor/extract-values filename)}
  )

(defn munch
  [config-files]
  (apply merge (map munch-env config-files)))

