(ns com.lmc.monkey.config.selector
  (:refer-clojure :exclude [load]))

(declare select-attributes)

(defn- find-nested-attribute
  [data [top-level-fieldname required-children]]
  (let [[fieldname nested-data] (find data top-level-fieldname)]
    (when nested-data
      [fieldname (select-attributes (into {} nested-data) required-children)])))

(defn- find-attribute
  [data [fieldname value :as attr]]
  (if (coll? value)
    (find-nested-attribute data attr)
    (find data fieldname)))

(defn- select-attributes
  [data required-attributes]
  (loop [ret {}
         attributes (seq required-attributes)]
    (if attributes
      (let [attr (first attributes)
            entry (find-attribute data attr)]
        (recur
         (if entry
           (conj ret entry)
           ret)
         (next attributes)))
      (with-meta ret (meta data)))))

(defn select-entries
  [all-entries required-attributes]
  (-> all-entries
      (select-attributes required-attributes)
      clojure.walk/keywordize-keys))

