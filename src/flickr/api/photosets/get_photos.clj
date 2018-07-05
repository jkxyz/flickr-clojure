(ns flickr.api.photosets.get-photos
  (:require [clojure.spec.alpha :as s]
            [flickr.api.core :as api]))

(s/def ::id string?)
(s/def ::single-photo (s/keys :req-un [::id]))

(s/def ::photo (s/coll-of ::single-photo))

(s/def ::photoset (s/keys :req-un [::photo]))

(s/def ::response (s/merge ::api/response
                           (s/keys :req-un [::photoset])))

(def ^:private method "flickr.photosets.getPhotos")

(defn request 
  "Gets photos for photoset-id and user-id."
  [api-info photoset-id user-id]
  (let [response (api/request api-info method {"photoset_id" photoset-id
                                               "user_id" user-id})
        parsed (s/conform ::response response)]
    (if (= parsed ::s/invalid)
      (throw (ex-info "Invalid response" (s/explain-data ::response response)))
      parsed)))
