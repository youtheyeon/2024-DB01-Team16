-- BookDetails 뷰 삭제
DROP VIEW IF EXISTS BookDetails;
DROP VIEW IF EXISTS FilteredBooks;

-- 인덱스 삭제
DROP INDEX IF EXISTS idx_title ON Book;
DROP INDEX IF EXISTS idx_user_name ON User;
DROP INDEX IF EXISTS idx_borrow_user ON Borrow;
DROP INDEX IF EXISTS idx_borrow_book ON Borrow;
DROP INDEX IF EXISTS idx_category ON Category;

-- 테이블 삭제
DROP TABLE IF EXISTS Borrow;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS Category;
