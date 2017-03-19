(ns com.lmc.monkey.config.chef-recipe-generator)

(defn- parse-placeholders
  [placeholders]
  (str "override" (second (re-matches #"<%=\s*node(\[.+\])\s*%>" placeholders))))

(defn- per-attribute
  [values [fieldname placeholders]]
  {(parse-placeholders placeholders) (get values fieldname)})

(defn- per-env
  [[env values] required-attributes]
  {env (into {} (map #(per-attribute values %) required-attributes))})

(defn generate-attributes [{:keys [template values]}]
  (into {} (map #(per-env % (:erb template)) values)))
