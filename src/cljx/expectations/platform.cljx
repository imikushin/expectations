(ns expectations.platform
  (:refer-clojure :exclude [all-ns bound? format ns-interns])
  #+cljs (:require-macros [expectations.platform.cljs :as cljs])
  (:require #+clj [clojure.pprint :as pprint]
            #+cljs [goog.string]
            #+cljs [goog.string.format]))

(defn all-ns []
  #+clj (clojure.core/all-ns)
  #+cljs (cljs/all-ns*))

(def bound?
  #+clj clojure.core/bound?
  #+cljs (fn [& vars] (every? #(deref %) vars)))

(def format
  #+clj clojure.core/format
  #+cljs goog.string/format)

(defn getenv [var]
  #+clj (System/getenv var)
  #+cljs (aget (.-env js/process) var))

(defn get-message [e] (-> e
                        #+clj .getMessage
                        #+cljs .-message))

(defn nano-time []
  #+clj (System/nanoTime)
  #+cljs (-> js/process .hrtime js->clj
           (#(+ (* 1e9 (% 0)) (% 1)))))

(defn ns-interns [ns]
  #+clj (clojure.core/ns-interns ns)
  #+cljs (cljs/ns-interns* ns))

(defn on-windows? []
  (re-find #"[Ww]in"
    #+clj (System/getProperty "os.name")
    #+cljs (.-platform js/process)))

(def pprint
  #+clj pprint/pprint
  #+cljs println)                                           ;until there's a usable cljs pprint port

(defn print-stack-trace [e]
  (-> e
    #+clj .printStackTrace
    #+cljs .-stack println))

(def reference-types*
  #+clj #{clojure.lang.Agent clojure.lang.Atom clojure.lang.Ref}
  #+cljs #{cljs.core/Atom})