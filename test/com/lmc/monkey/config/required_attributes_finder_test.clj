(ns com.lmc.monkey.config.required-attributes-finder-test
  (:use midje.sweet)
  (:require  [com.lmc.monkey.config.required-attributes-finder :refer :all]))

(facts "about finding required attributes based on erb subs"
       (fact "top-level values"
             (find-attributes-deprecated "resources/simple-config.yml.erb")
             => {"host" "<%= node['some']['host'] %>"
                 "port" "<%= node['some']['port'] %>"})

       (fact "nested values"
             (find-attributes-deprecated "resources/sample-invoice.yml.erb")
             => {"invoice" "<%= id %>"
                 "bill-to" {"given" "<%= firstname %>"}
                 "ship-to" {"given" "<%= firstname %>"}}))
