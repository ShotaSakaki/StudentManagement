INSERT INTO students(last_name, first_name, last_name_furigana, first_name_furigana, nickname, email, prefecture, age, gender)
VALUES('新庄', '剛志', 'しんじょう', 'つよし', 'BIGBOSS', 'fansareourtreasure@fighters.com', '北海道', 52, '男'),
            ('遠藤', 'さくら', 'えんどう', 'さくら', 'さく', 'sakuraendo_official@nogizaka46.com', '愛知県', 22, '女'),
            ('岡田', '彰布', 'おかだ', 'あきのぶ', 'どんでん', 'aregoeson@tigers.com', '兵庫県', 67, '男'),
            ('松田', '里奈', 'まつだ', 'りな', 'まつり', 'motoginkouin@sakurazaka46.com', '宮崎県', 25, '女'),
            ('吉井', '理人', 'よしい', 'まさと', 'うま味紳士', 'jibuntachiwokoeteike@marines.com', '千葉県', 59, '男');

INSERT INTO students_courses(student_id, course_name, start_date, end_date)
    VALUES(1, 'TOEIC', '2024-04-01 00:00:00', '2024-09-30 23:59:59'),
                (5, 'TOEIC', '2024-04-01 00:00:00', '2024-09-30 23:59:59'),
                (2, 'CAD', '2024-05-01 00:00:00', '2024-08-31 23:59:59'),
                (3, '中国語', '2024-01-01 00:00:00', '2024-12-31 23:59:59'),
                (4, '中国語', '2024-10-01 00:00:00', '2025-09-30 23:59:59');
