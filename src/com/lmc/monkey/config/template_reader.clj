(ns com.lmc.monkey.config.template-reader
  (:require [com.lmc.monkey.config.selector :as selector]
            [com.lmc.monkey.config.yaml-reader :as reader]))

(defn select-entries-with-erb-placeholders
  [all-entries]
  (let [criteria #(and (string? (val %))
                       (re-matches #".*<%=.*%>.*" (val %)))
        children-criteria (fn [_ criteria]
                            criteria)]
    (selector/by-criteria all-entries criteria children-criteria)))

; array within an array??
(defn parse-erb-files [input-files]
  (first (map
           #(select-entries-with-erb-placeholders
             (reader/read-file %))
           input-files)))