(ns quiz-backend.db.schema
  (:require [clojure.java.jdbc :as sql]
            [noir.io :as io]))

(def db-store "site.db")

(def db-spec {:classname "org.h2.Driver"
              :subprotocol "h2"
              :subname (str (io/resource-path) db-store)
              :user "sa"
              :password ""
              :make-pool? true
              :naming {:keys clojure.string/lower-case
                       :fields clojure.string/upper-case}})
(defn initialized?
  "checks to see if the database schema is present"
  []
  (.exists (new java.io.File (str (io/resource-path) db-store ".mv.db"))))

(defn create-quizzes-table
  []
  (sql/db-do-commands
    db-spec
    (sql/create-table-ddl
      :quizzes
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:timestamp :timestamp]
      [:name "varchar(30)"]))
   (sql/db-do-prepared db-spec
       "CREATE INDEX quiz_timestamp_index ON quizzes (timestamp)"))

(defn create-questions-table
  []
  (sql/db-do-commands
    db-spec
    (sql/create-table-ddl
      :questions
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:quizid :integer]
      [:timestamp :timestamp]
      [:question "varchar(256)"]))
   (sql/db-do-prepared db-spec
       "CREATE INDEX question_timestamp_index ON questions (timestamp)"))

(defn create-answers-table
  []
  (sql/db-do-commands
    db-spec
    (sql/create-table-ddl
      :answers
      [:id "INTEGER PRIMARY KEY AUTO_INCREMENT"]
      [:questionid :integer]
      [:timestamp :timestamp]
      [:answer "varchar(256)"]
      [:correct :boolean]))
   (sql/db-do-prepared db-spec
       "CREATE INDEX answer_timestamp_index ON answers (timestamp)"))

(defn create-tables
  "creates the database tables used by the application"
  []
  (create-quizzes-table)
  (create-questions-table)
  (create-answers-table))
