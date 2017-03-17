(ns com.lmc.monkey.config.required-attributes-finder-test
  (:use midje.sweet)
  (:require  [com.lmc.monkey.config.required-attributes-finder :refer :all]))

(facts "about finding required attributes based on erb subs"
       (fact "top-level values"
             (let [all-entries {"host"    "<%= node['some']['host'] %>"
                                "port"    "<%= node['some']['port'] %>"
                                "useless" irrelevant}
                   criteria    #(re-matches #".*<%=.*%>.*" %)]
               (select-entries-by-criteria all-entries criteria)
               => {"host" "<%= node['some']['host'] %>"
                   "port" "<%= node['some']['port'] %>"}))

       (fact "nested values"
             (let [all-entries {"invoice" "<%= id %>"
                                "bill-to" {"given" "<%= firstname %>"
                                           "useless2" irrelevant}
                                "ship-to" {"given" "<%= firstname %>"}
                                "useless" irrelevant}
                   criteria    #(re-matches #".*<%=.*%>.*" %)]
                     (select-entries-by-criteria all-entries criteria)
                     => {"invoice" "<%= id %>"
                         "bill-to" {"given" "<%= firstname %>"}
                         "ship-to" {"given" "<%= firstname %>"}})))
