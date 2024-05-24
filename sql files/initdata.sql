-- Category 테이블 초기 데이터 삽입
INSERT INTO Category (category) VALUES
('Fiction'),
('Non-fiction'),
('Science'),
('History'),
('Biography');

-- Book 테이블 초기 데이터 삽입
INSERT INTO Book (category_id, title, author_name, publisher, is_borrowed) VALUES
(1, 'To Kill a Mockingbird', 'Harper Lee', 'J.B. Lippincott & Co.', FALSE),
(2, '1984', 'George Orwell', 'Secker & Warburg', FALSE),
(3, 'Brief History of Time', 'Stephen Hawking', 'Bantam Books', FALSE),
(4, 'Sapiens', 'Yuval Noah Harari', 'Harper', FALSE),
(5, 'The Diary of a Young Girl', 'Anne Frank', 'Contact Publishing', FALSE);

-- User 테이블 초기 데이터 삽입
INSERT INTO User (user_name, phone_num) VALUES
('Alice Johnson', 12340123),
('Bob Smith', 23450234),
('Charlie Brown', 34560345),
('David Williams', 45670456),
('Eva Green', 56780567);

-- Borrow 테이블 초기 데이터 삽입
INSERT INTO Borrow (borrow_date, return_date, user_id, book_id) VALUES
('2024-05-01', '2024-05-15', 1, 1),
('2024-05-05', NULL, 2, 2),
('2024-05-10', NULL, 3, 3),
('2024-05-20', '2024-05-24', 4, 4),
('2024-05-22', NULL, 5, 5);

