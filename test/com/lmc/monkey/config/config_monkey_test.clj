(ns com.lmc.monkey.config.config-monkey-test
  (:use midje.sweet)
  (:require [com.lmc.monkey.config.config-monkey :as monkey]))

(facts "about finding required attributes based on erb subs"
       (fact "top-level values"
             (let [all-entries {"host"    "<%= node['some']['host'] %>"
                                "port"    "<%= node['some']['port'] %>"
                                "useless" irrelevant}]
               (monkey/select-entries-with-erb-placeholders all-entries)
               => {"host" "<%= node['some']['host'] %>"
                   "port" "<%= node['some']['port'] %>"}))

       (fact "nested values"
             (let [all-entries {"invoice" "<%= id %>"
                                "bill-to" {"given" "<%= firstname %>"
                                           "useless2" irrelevant}
                                "ship-to" {"given" "<%= firstname %>"}
                                "useless" irrelevant}]
                     (monkey/select-entries-with-erb-placeholders all-entries)
                     => {"invoice" "<%= id %>"
                         "bill-to" {"given" "<%= firstname %>"}
                         "ship-to" {"given" "<%= firstname %>"}})))

(facts "select from map"
       (fact "top-level values"
             (let [required-attributes {"port" "<%= some-port %>"
                                        "host" "<%= some-host %>"}]
               (monkey/select-entries-by-name {"host"            "localhost"
                                               "port"            1337
                                               "some-other-data" irrelevant} required-attributes)
               => {"host" "localhost"
                   "port" 1337}))

       (fact "nested values"
             (let [required-attributes {"invoice" anything
                                        "bill-to" {"given"  anything
                                                   "family" anything}}
                   original-map        {"invoice" 34843
                                        "bill-to" {"given"  "Chris"
                                                   "family" "Dumars"}
                                        "some-other-data" irrelevant}]
               (monkey/select-entries-by-name original-map required-attributes)
               => {"invoice"  34843
                   "bill-to" {"given"  "Chris"
                              "family" "Dumars"}}))

       (fact "multi level nested values"
             (let [required-attributes {"invoice" anything
                                        "bill-to" {"address" {"city"   anything
                                                              "postal" anything}
                                                   "family"  anything}}
                   original-map        {"invoice" 34843
                                        "bill-to" {"given"   irrelevant
                                                   "family"  "Dumars"
                                                   "address" {"city"   "Royal Oak"
                                                              "postal" 48046}}}]
               (monkey/select-entries-by-name original-map required-attributes)
               => {"invoice" 34843
                   "bill-to" {"family"  "Dumars"
                              "address" {"city"   "Royal Oak"
                                         "postal" 48046}}})))

(facts "aboout munching configs"
       #_(fact "munch template and values"
             (monkey/munch {:input  "munchy_input"
                            :output "munchy_out"})
             => {:dev {"override['local']['host']" "munchy.dev"
                       "override['local']['port']" "1337"}
                 :qa  {"override['local']['host']" "munchy.qa"
                       "override['local']['port']" "9001"}})


       (fact "munch template and values"
             (monkey/extract {:input "munchy_input"
                              :output "munchy_out"})
             => {:template {:erb {"host" "<%= node['local']['host'] %>",
                                  "port" "<%= node['local']['port'] %>"}}
                 :values   {:dev {:host "munchy.dev"
                                  :port 1337}
                            :qa  {:host "munchy.qa"
                                  :port 9001}}}))
