CREATE TABLE IF NOT EXISTS students
    (
      id int PRIMARY KEY AUTO_INCREMENT,
      last_name VARCHAR(50) NOT NULL,
      first_name VARCHAR(50) NOT NULL,
      last_name_furigana VARCHAR(50) NOT NULL,
      first_name_furigana VARCHAR(50) NOT NULL,
      nickname VARCHAR(50),
      email VARCHAR(50) NOT NULL,
      prefecture VARCHAR(50),
      age int,
      gender VARCHAR(10),
      remark VARCHAR(100),
      is_deleted boolean
    );

CREATE TABLE IF NOT EXISTS students_courses
     (
       id int PRIMARY KEY AUTO_INCREMENT,
       student_id int NOT NULL,
       course_name VARCHAR(50) NOT NULL,
       start_date TIMESTAMP,
       end_date TIMESTAMP
     );
