(ns com.lmc.monkey.config.config-monkey
  (:require [com.lmc.monkey.config.selector :as extractor]
            [clojure.java.io :as io]
            [com.lmc.monkey.config.required-attributes-finder :as finder]
            [com.lmc.monkey.config.yaml-reader :as reader]))

(defn- read-files [input]
  (-> input
      io/resource
      io/file
      file-seq))

(defn is-template? [input-file]
  (clojure.string/ends-with? (.getName input-file) ".erb"))

; array within an array??
(defn- process-template [input-files]
  (first (map
           #(finder/select-entries-by-criteria
             (reader/read-file %)
             (fn [a] (re-matches #".*<%=.*%>.*" a)))
           (filter is-template? input-files))))

(defn- process-env-values [required-attributes input-file]
  (let [env (second (re-matches #".*-(.+)\.yml" (.getName input-file)))]
    (when env
      {(keyword env)
       (clojure.walk/keywordize-keys (extractor/select-entries (reader/read-file input-file) required-attributes))})))

(defn- process-values [required-attributes in-files]
  (loop [ret {}
         input-files in-files]
    (if input-files
      (let [in (process-env-values required-attributes (first input-files))]
        (recur
          (if in
            (merge ret in)
            ret)
          (next input-files)))
      ret)))

(defn extract [{:keys [input]}]
  (let [input-files         (read-files input)
        required-attributes (process-template input-files)]
    {:template {:erb required-attributes}
     :values   (process-values required-attributes input-files)}))
