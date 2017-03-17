(ns com.lmc.monkey.config.selector-test
  (:use midje.sweet)
  (:require [clojure.test :refer :all]
            [com.lmc.monkey.config.selector :as selector]))

(facts "select from map"
       (fact "top-level values"
             (let [required-attributes {"port" "<%= some-port %>"
                                        "host" "<%= some-host %>"}]
               (selector/select-entries {"host"            "localhost"
                                         "port"            1337
                                         "some-other-data" irrelevant} required-attributes)
               => {:host "localhost"
                   :port 1337}))

       (fact "nested values"
             (let [required-attributes {"invoice" anything
                                        "bill-to" {"given"  anything
                                                   "family" anything}}
                   original-map        {"invoice" 34843
                                        "bill-to" {"given"  "Chris"
                                                   "family" "Dumars"}}]
               (selector/select-entries original-map required-attributes)
               => {:invoice 34843
                   :bill-to {:given  "Chris"
                             :family "Dumars"}}))

       (fact "multi level nested values"
             (let [required-attributes {"invoice" anything
                                        "bill-to" {"given"   anything
                                                   "address" {"city"   anything
                                                              "postal" anything}
                                                   "family"  anything}}
                   original-map        {"invoice" 34843
                                        "bill-to" {"family"  "Dumars"
                                                   "address" {"city"   "Royal Oak"
                                                              "postal" 48046}}}]
               (selector/select-entries original-map required-attributes)
               => {:invoice 34843
                   :bill-to {:family  "Dumars"
                             :address {:city   "Royal Oak"
                                       :postal 48046}}})))

