(ns flickr.core
  (:require [clojure.spec.alpha :as s]))

(s/def ::api-key string?)
(s/def ::api-info (s/keys :req [::api-key]))

(defn api-info
  "Creates a map for connecting to the Flickr API."
  [api-key]
  {:pre [(s/valid? ::api-key api-key)]}
  {::api-key api-key})
