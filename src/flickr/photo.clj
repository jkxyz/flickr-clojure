(ns flickr.photo
  (:require [clojure.spec.alpha :as s]
            [flickr.api.photosets.get-photos :as photosets-get-photos]
            [flickr.api.photos.get-info :as get-info]
            [flickr.api.photos.get-sizes :as get-sizes]))

(defn- make-photo [{:keys [photo]}]
  {::id (:id photo)
   ::title (:title photo)
   ::description (:description photo)
   ::page-url (first (get-in photo [:urls :url]))})

(defn find-by-id [api-info id]
  (let [response (get-info/request api-info id)]
    (make-photo response)))

(defn get-by-photoset [api-info {:flickr.photoset/keys [id] :as photoset}]
  (let [user-id (get-in photoset [:flickr.photoset/owner :flickr.user/id])
        response (photosets-get-photos/request api-info id user-id)
        photo-ids (map :id (get-in response [:photoset :photo]))]
    (map (partial find-by-id api-info) 
         photo-ids)))

(defn- extract-sizes [{:keys [sizes]}]
  (let [sizes (zipmap (map :label (:size sizes)) (map :source (:size sizes)))]
    {:large (get sizes "Large")
     :large-square (get sizes "Large Square")
     :medium (get sizes "Medium")
     :medium-640 (get sizes "Medium 640")
     :medium-800 (get sizes "Medium 800")
     :original (get sizes "Original")
     :small (get sizes "Small")
     :small-320 (get sizes "Small 320")
     :square (get sizes "Square")
     :thumbnail (get sizes "Thumbnail")}))

(defn get-sizes [api-info {::keys [id]}]
  (let [response (get-sizes/request api-info id)]
    (extract-sizes response)))
