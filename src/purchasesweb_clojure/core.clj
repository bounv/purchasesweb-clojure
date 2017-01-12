(ns purchasesweb-clojure.core
  (:require [clojure.string :as str]
            [compojure.core :as c]
            [ring.adapter.jetty :as j]
            [hiccup.core :as h])
  (:gen-class))

(defn read-purchases []
  ;;(println "Type a category name:")
  (let [purchases (slurp "purchases.csv")
        purchases (str/split-lines purchases)
        purchases (map (fn [line]
                        (str/split line #","))
                    purchases)
        header (first purchases)
        purchases (rest purchases)
        purchases (map (fn [line]
                         (zipmap header line))
                    purchases)]
    purchases))
        ;;category-name (read-line)]))
        ;;purchases (filter (fn [line]))]))
                            ;;(= (get line "category") category-name)))]))
                    ;;purchases)]))
        ;;file-text (pr-str purchases)]))
    ;;(spit "filtered_purchases.edn" file-text)))


(defn purchases-html [category]
  (let [purchases (read-purchases)
        purchases (if (= 0 (count category))
                    purchases
                    (filter (fn [purchase]
                              (= (get purchase "category") category))
                      purchases))] 
                         
    [:ol
     (map (fn [purchase]
            [:li (str (get purchase "category") " " (get purchase "date") " " (get purchase "credit_card") " " (get purchase "cvv") " " (get purchase "customer_id"))])
       purchases)]))


(c/defroutes app
  (c/GET "/:category{.*}" [category]
    (h/html [:html
             [:body
              [:a {:href "Alcohol"} "Alcohol"] "\n"
              [:a {:href "Food"} "Food"] "\n"
              [:a {:href "Furniture"} "Furniture"] "\n"
              [:a {:href "Jewelry"} "Jewelry"] "\n"
              [:a {:href "Shoes"} "Shoes"] "\n"
              [:a {:href "Toiletries"} "Toiletries"]
              (purchases-html category)]])))


(defonce server (atom nil))


(defn -main []
  (when @server
    (.stop @server))
  (reset! server (j/run-jetty app {:port 3000 :join? false})))
