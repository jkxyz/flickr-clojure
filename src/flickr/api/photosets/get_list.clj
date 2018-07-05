(ns flickr.api.photosets.get-list
  (:require [clojure.spec.alpha :as s]
            [flickr.api.core :as api]))

(s/def ::id string?)
(s/def ::title string?)
(s/def ::description string?)
(s/def ::photos int?)

(s/def ::single-photoset (s/keys :req-un [::id
                                          ::title
                                          ::description
                                          ::photos]))

(s/def ::photoset (s/coll-of ::single-photoset))

(s/def ::photosets (s/keys :req-un [::photoset]))

(s/def ::response (s/merge ::api/response
                           (s/keys :req-un [::photosets])))

(def ^:private method "flickr.photosets.getList")

(defn request 
  "Gets photosets for user-id."
  [api-info user-id]
  (let [response (api/request api-info method {"user_id" user-id})
        parsed (s/conform ::response response)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid response" (s/explain-data ::response response)))
      parsed)))
