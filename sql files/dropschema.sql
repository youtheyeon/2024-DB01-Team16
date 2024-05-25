DROP VIEW IF EXISTS BookDetails;
DROP VIEW IF EXISTS FilteredBooks;

DROP INDEX idx_title ON Book;
DROP INDEX idx_user_name ON User;
DROP INDEX idx_category ON Category;

-- First drop the foreign key constraints
ALTER TABLE Borrow DROP FOREIGN KEY borrow_ibfk_1;
ALTER TABLE Borrow DROP FOREIGN KEY borrow_ibfk_2;
-- Drop the indexes
DROP INDEX idx_borrow_user ON Borrow;
DROP INDEX idx_borrow_book ON Borrow;

-- Drop the tables
DROP TABLE IF EXISTS Borrow;
DROP TABLE IF EXISTS User;
DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS Category;
