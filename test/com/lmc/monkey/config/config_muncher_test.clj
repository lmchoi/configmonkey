(ns com.lmc.monkey.config.config-muncher-test
  (:use midje.sweet)
  (:require [com.lmc.monkey.config.config-monkey :as muncher]))

(facts "aboout munching configs"
       (fact "munch template and values"
             (muncher/extract {:input  "munchy_input"
                             :output "munchy_out"})
             => {:template {:erb {"host" "<%= node['local']['host'] %>",
                                  "port" "<%= node['local']['port'] %>"}}
                 :values   {:dev {:host "munchy.dev"
                                  :port 1337}
                            :qa  {:host "munchy.qa"
                                  :port 9001}}}))
