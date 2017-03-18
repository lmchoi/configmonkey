(ns com.lmc.monkey.config.config-monkey
  (:require [clojure.java.io :as io]
            [com.lmc.monkey.config.selector :as selector]
            [com.lmc.monkey.config.yaml-reader :as reader]))

(defn- read-files [input]
  (-> input
      io/resource
      io/file
      file-seq))

(defn is-template? [input-file]
  (clojure.string/ends-with? (.getName input-file) ".erb"))

(defn select-entries-with-erb-placeholders
  [all-entries]
  (let [criteria #(and (string? (val %))
                       (re-matches #".*<%=.*%>.*" (val %)))
        children-criteria (fn [_ criteria]
                            criteria)]
    (selector/by-criteria all-entries criteria children-criteria)))

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

; array within an array??
(defn- process-template [input-files]
  (first (map
           #(select-entries-with-erb-placeholders
             (reader/read-file %))
           (filter is-template? input-files))))

(defn- process-env-values
  [input-file [required-attributes]]
  (let [env (second (re-matches #".*-(.+)\.yml" (.getName input-file)))]
    (when env
      {(keyword env)
       (-> input-file
           reader/read-file
           (select-entries-by-name required-attributes)
           clojure.walk/keywordize-keys)})))

(defn extract [{:keys [input]}]
  (let [input-files         (read-files input)
        required-attributes (process-template input-files)]
    {:template {:erb required-attributes}
     :values   (selector/select-attributes process-env-values input-files required-attributes)}))
