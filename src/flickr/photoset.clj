(ns flickr.photoset
  (:require [clojure.spec.alpha :as s]
            [flickr.api.photosets.get-list :as get-list]))

(defn- make-photoset [user {:keys [id title description photos]}]
  {::id id
   ::title title
   ::description description
   ::photos-count photos
   ::owner user})

(defn- extract-photosets [user {:keys [photosets]}]
  (map (partial make-photoset user)
       (:photoset photosets)))

(defn get-by-user [api-info {:flickr.user/keys [id] :as user}]
  (let [response (get-list/request api-info id)]
    (extract-photosets user response)))
