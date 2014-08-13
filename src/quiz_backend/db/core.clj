(ns quiz-backend.db.core
  (:use korma.core
        [korma.db :only (defdb)])
  (:require [quiz-backend.db.schema :as schema]))

(defdb db schema/db-spec)

(defentity answers)

(defentity questions
  (has-many answers {:fk :questionid}))

(defentity quizzes
  (has-many questions {:fk :quizid}))

(defn get-quiz [id]
  (first (select quizzes
                 (with questions)
                 (where {:id id})
                 (limit 1))))

(defn save-quiz [name]
  (insert quizzes
  (set-fields {:name name
               :timestamp (new java.util.Date)})))

(defn get-quizzes []
  (select quizzes))

(defn get-question [id]
  (first (select questions
                 (with answers)
                 (where {:id id})
                 (limit 1))))

(defn save-question [quiz-id question]
  (insert questions
          (set-fields {:quizid quiz-id
                       :question question
                       :timestamp (new java.util.Date)})))

(defn save-answer [question-id answer correct]
  (insert answers
          (set-fields {:questionid question-id
                       :answer answer
                       :correct correct
                       :timestamp (new java.util.Date)})))
