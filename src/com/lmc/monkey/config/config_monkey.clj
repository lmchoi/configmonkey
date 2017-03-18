(ns com.lmc.monkey.config.config-monkey
  (:require [clojure.java.io :as io]
            [com.lmc.monkey.config.selector :as selector]
            [com.lmc.monkey.config.yaml-reader :as reader]
            [com.lmc.monkey.config.template-reader :as template]))

(defn- read-files [input]
  (-> input
      io/resource
      io/file
      file-seq))

(defn is-template? [input-file]
  (clojure.string/ends-with? (.getName input-file) ".erb"))

(defn select-entries-by-name
  [all-entries required-attributes]
  (let [criteria (fn
                   ([[fieldname _]]
                    (get required-attributes fieldname))
                   ([[fieldname _] rt]
                    (get rt fieldname)))
        children-criteria (fn [entry cc]
                            #(criteria % (cc entry)))]
    (selector/by-criteria all-entries criteria children-criteria)))

(defn- process-env-values
  [input-file [required-attributes]]
  (let [env (second (re-matches #".*-(.+)\.yml" (.getName input-file)))]
    (when env
      {(keyword env) (-> input-file
                         reader/read-file
                         (select-entries-by-name required-attributes))})))

(defn extract
  [{:keys [input]}]
  (let [input-files         (read-files input)
        required-attributes (template/parse-erb-files (filter is-template? input-files))]
    {:template {:erb required-attributes}
     :values   (selector/select-attributes process-env-values input-files required-attributes)}))

(defn munch
  [config]
  (extract config))
