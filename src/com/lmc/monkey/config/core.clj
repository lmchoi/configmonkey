(ns com.lmc.monkey.config.core
  (:gen-class)
  (:require [com.lmc.monkey.config.config-monkey :as monkey]))

(defn -main
  "Extract configurable properties from configuration files"
  [& args]
  (-> (monkey/extract {:input  "resources/"
                       :output "output/"})
      (prn)))
