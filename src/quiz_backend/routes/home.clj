(ns quiz-backend.routes.home
  (:require [compojure.core :refer :all]
            [noir.response :as response]
            [quiz-backend.layout :as layout]
            [quiz-backend.db.core :as db]
            [quiz-backend.util :as util]))

(defn list-quizzes []
  "list all quizzes in the db, because #yolo"
  (db/get-quizzes))

(defn add-quiz [name]
  "adds quiz and returns the id"
  {:success true :id (db/save-quiz name)})

(defn add-question [quiz-id question]
  "adds a question to the db, associated to a quiz"
  {:success true :id (db/save-question quiz-id question)})

(defn get-quiz [id]
  "get quiz information by id"
  (db/get-quiz id))

(defn get-question [id]
  "get question information by id"
  (db/get-question))

(defn add-answer [question-id answer correct]
  "add an answer to the db"
  {:success true :id (db/save-answer question-id answer correct)})

(defroutes home-routes
  (GET "/quizzes" [] (response/json (list-quizzes)))
  (GET "/quiz/:id" [id] (response/json (get-quiz id)))
  (POST "/quiz" [name] (response/json (add-quiz name)))
  (GET "/question/:id" [id] (response/json (get-question id)))
  (POST "/question" [quiz-id question]
        (response/json (add-question quiz-id question)))
  (POST "/answer" [question-id answer correct]
        (response/json (add-answer question-id answer correct))))
