-- Category 테이블 생성
CREATE TABLE Category (
  category_id INT AUTO_INCREMENT PRIMARY KEY,
  category VARCHAR(30)
);

-- Book 테이블 생성
CREATE TABLE Book (
  book_id INT AUTO_INCREMENT PRIMARY KEY,
  category_id INT,
  title VARCHAR(50),
  author_name VARCHAR(20),
  publisher VARCHAR(50),
  is_borrowed BOOLEAN DEFAULT FALSE,
  FOREIGN KEY (category_id) REFERENCES Category(category_id)
);

-- User 테이블 생성
CREATE TABLE User (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(20),
  phone_num INT
);

-- Borrow 테이블 생성
CREATE TABLE Borrow (
  borrow_id INT AUTO_INCREMENT PRIMARY KEY,
  borrow_date VARCHAR(20),
  return_date VARCHAR(20),
  user_id INT,
  book_id INT,
  FOREIGN KEY (user_id) REFERENCES User(user_id),
  FOREIGN KEY (book_id) REFERENCES Book(book_id)
);

-- BookDetails 뷰 생성
CREATE VIEW BookDetails AS
SELECT 
  b.book_id, b.title, b.author_name, b.publisher, c.category, 
  br.borrow_date, br.return_date, u.user_name
FROM 
  Book b
LEFT JOIN 
  Borrow br ON b.book_id = br.book_id
LEFT JOIN 
  User u ON br.user_id = u.user_id
LEFT JOIN 
  Category c ON b.category_id = c.category_id;

-- 인덱스 추가
CREATE INDEX idx_title ON Book(title);
CREATE INDEX idx_user_name ON User(user_name);
CREATE INDEX idx_borrow_user ON Borrow(user_id);
CREATE INDEX idx_borrow_book ON Borrow(book_id);
CREATE INDEX idx_category ON Category(category);