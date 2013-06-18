(ns leiningen.watch
  (:require [nio2.watch :refer [watch-seq]]
            [nio2.io :refer [path]]
            [clojure.string :refer [split join]])
  (:import [java.nio.file Path]))

(def events-to-watch [:create :modify :delete])

(def empty-string "")

(defn path-seq
  [p]
  (let [s (split p #"\\")]
    (if (=  (first s) empty-string)
      (cons "/" (rest s))
      s)))

(defn get-path
  [path-object]
  (let [path (-> path-object .toUri .toString (split #"file://") second)]
    ()))

(defn get-file-name
  [p]
  (let [name (->> p .getFileName .toString)
        name-split (split name #"\.")]
    (if (> (count name-split) 1) ;; accounting for the case where there is no extension.
      (join "." (butlast name-split))
      (first name-split))))

(defn watch-folder
  [p f]
  (let [path-object (apply path (path-seq p))]
    (doseq [ev (apply watch-seq path-object events-to-watch)]
      (f [(ev :path) (get-file-name (ev :path))]))))