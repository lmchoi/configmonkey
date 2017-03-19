(ns com.lmc.monkey.config.chef-recipe-generator-test
  (:use midje.sweet)
  (:require [com.lmc.monkey.config.chef-recipe-generator :as chef]))

(facts "about generating attributes"
       (fact ""
             (chef/generate-attributes
              {:template {:erb {:host "<%= node['local']['host'] %>",
                                :port "<%= node['local']['port'] %>"}}
               :values   {:dev {:host "munchy.dev"
                                :port 1337}
                          :qa  {:host "munchy.qa"
                                :port 9001}}})
             => {:dev {"override['local']['host']" "munchy.dev"
                       "override['local']['port']" 1337}
                 :qa  {"override['local']['host']" "munchy.qa"
                       "override['local']['port']" 9001}}))


