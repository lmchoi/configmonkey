(ns com.lmc.monkey.config.selector)

(declare select-attributes)
(declare find-attributes)

(defn- find-nested-attributes
  [[top-level-fieldname children :as entry] criteria children-criteria]
  (let [children-attributes (select-attributes
                             find-attributes
                             (into {} children)
                             (children-criteria entry criteria)
                             children-criteria)]
    (when (not (empty? children-attributes))
      [top-level-fieldname children-attributes])))

(defn- find-attributes
  [[_ value :as entry] [criteria children-criteria]]
  (cond
    (coll? value)
    (find-nested-attributes entry criteria children-criteria)

    (criteria entry)
    entry))

(defn select-attributes
  [func data & args]
  (loop [ret {}
         entries data]
    (if entries
      (let [entry (first entries)
            criteria-met (func entry args)]
        (recur
         (if criteria-met
           (conj ret criteria-met)
           ret)
         (next entries)))
      (with-meta ret (meta data)))))

(defn by-criteria
  [all-entries criteria children-criteria]
  (select-attributes find-attributes all-entries criteria children-criteria))
