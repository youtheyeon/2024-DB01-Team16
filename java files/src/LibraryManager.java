import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LibraryManager {

    public List<FilteredBooks> getBooks() {
        List<FilteredBooks> books = new ArrayList<>();
        String selectSQL = "SELECT fb.book_id, c.category, fb.title, fb.author_name, fb.publisher, fb.is_borrowed " +
                            "FROM FilteredBooks fb " +
                            "JOIN Category c ON fb.category_id = c.category_id";
    
        try (Connection connection = App.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = preparedStatement.executeQuery()) {
    
            while (resultSet.next()) {
                int bookId = resultSet.getInt("book_id");
                String category = resultSet.getString("category");
                String title = resultSet.getString("title");
                String authorName = resultSet.getString("author_name");
                String publisher = resultSet.getString("publisher");
                boolean isBorrowed = resultSet.getBoolean("is_borrowed");
                books.add(new FilteredBooks(bookId, category, title, authorName, publisher, isBorrowed));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }    

    public List<BookDetails> getBookDetails() {
        List<BookDetails> books = new ArrayList<>();
        String query = "SELECT book_id, title, author_name, publisher, category, borrow_date, return_date, user_name FROM BookDetails";

        try (Connection connection = App.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                books.add(new BookDetails(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author_name"),
                    rs.getString("publisher"),
                    rs.getString("category"),
                    rs.getString("borrow_date"),
                    rs.getString("return_date"),
                    rs.getString("user_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<FilteredBooks> getBooksByFilter(String filterText) {
        List<FilteredBooks> books = new ArrayList<>();
        String query = "SELECT fb.book_id, c.category, fb.title, fb.author_name, fb.publisher, fb.is_borrowed " +
                       "FROM FilteredBooks fb " +
                       "JOIN Category c ON fb.category_id = c.category_id " +
                       "WHERE LOWER(fb.title) LIKE ? OR LOWER(fb.author_name) LIKE ? OR LOWER(c.category) LIKE ?";
    
        try (Connection connection = App.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            String searchText = "%" + filterText.toLowerCase() + "%";
            stmt.setString(1, searchText);
            stmt.setString(2, searchText);
            stmt.setString(3, searchText);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new FilteredBooks(
                            rs.getInt("book_id"),
                            rs.getString("category"),
                            rs.getString("title"),
                            rs.getString("author_name"),
                            rs.getString("publisher"),
                            rs.getBoolean("is_borrowed")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    
        return books;
    }    

    public Map<String, Integer> getBooksCountByFilter(String filterText) {
        Map<String, Integer> bookCounts = new HashMap<>();
        String query = "SELECT c.category, COUNT(*) as count " +
                       "FROM Book b JOIN Category c ON b.category_id = c.category_id " +
                       "WHERE LOWER(b.title) LIKE ? OR LOWER(b.author_name) LIKE ? OR LOWER(c.category) LIKE ? " +
                       "GROUP BY c.category";

        try (Connection connection = App.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            String searchText = "%" + filterText.toLowerCase() + "%";
            stmt.setString(1, searchText);
            stmt.setString(2, searchText);
            stmt.setString(3, searchText);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    bookCounts.put(rs.getString("category"), rs.getInt("count"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookCounts;
    }

    public Book getBookById(int bookId) {
        String selectBookSQL = "SELECT * FROM Book WHERE book_id = ?";
        
        try (Connection connection = App.getConnection();
            PreparedStatement selectBookStatement = connection.prepareStatement(selectBookSQL)) {
            
            selectBookStatement.setInt(1, bookId);
            ResultSet resultSet = selectBookStatement.executeQuery();
            
            if (resultSet.next()) {
                int categoryId = resultSet.getInt("category_id");
                String title = resultSet.getString("title");
                String authorName = resultSet.getString("author_name");
                String publisher = resultSet.getString("publisher");
                boolean isBorrowed = resultSet.getBoolean("is_borrowed");
                
                return new Book(bookId, categoryId, title, authorName, publisher, isBorrowed);
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addBook(Book book) {
        String insertSQL = "INSERT INTO Book (category_id, title, author_name, publisher, is_borrowed) VALUES (?, ?, ?, ?, ?)";
    
        try (Connection connection = App.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setInt(1, book.getCategoryId());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setString(3, book.getAuthorName());
            preparedStatement.setString(4, book.getPublisher());
            preparedStatement.setBoolean(5, false); 
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    

    public boolean removeBook(int bookId) {
        String deleteBorrowSQL = "DELETE FROM Borrow WHERE book_id = ?";
        String deleteBookSQL = "DELETE FROM Book WHERE book_id = ?";
    
        try (Connection connection = App.getConnection();
            PreparedStatement deleteBorrowStatement = connection.prepareStatement(deleteBorrowSQL);
            PreparedStatement deleteBookStatement = connection.prepareStatement(deleteBookSQL)) {
            
            deleteBorrowStatement.setInt(1, bookId);
            deleteBorrowStatement.executeUpdate();
    
            deleteBookStatement.setInt(1, bookId);
            deleteBookStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateBook(Book book) {
        String updateBookSQL = "UPDATE Book SET category_id = ?, title = ?, author_name = ?, publisher = ? WHERE book_id = ?";
        
        try (Connection connection = App.getConnection();
            PreparedStatement updateBookStatement = connection.prepareStatement(updateBookSQL)) {
            
            updateBookStatement.setInt(1, book.getCategoryId());
            updateBookStatement.setString(2, book.getTitle());
            updateBookStatement.setString(3, book.getAuthorName());
            updateBookStatement.setString(4, book.getPublisher());
            updateBookStatement.setInt(5, book.getBookId());
            
            int affectedRows = updateBookStatement.executeUpdate();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void borrowBook(int userId, int bookId) {
        String insertBorrowSQL = "INSERT INTO Borrow (borrow_date, return_date, user_id, book_id) VALUES (?, ?, ?, ?)";
        String updateBookSQL = "UPDATE Book SET is_borrowed = true WHERE book_id = ?";

        try (Connection connection = App.getConnection();
            PreparedStatement insertBorrowStatement = connection.prepareStatement(insertBorrowSQL);
            PreparedStatement updateBookStatement = connection.prepareStatement(updateBookSQL)) {

            // 대출일을 현재 시간으로 설정
            LocalDate borrowDate = LocalDate.now();
            // 반납일을 대출일로부터 7일 후로 설정
            LocalDate returnDate = borrowDate.plusDays(7);

            // 대출일과 반납일을 문자열로 변환
            String borrowDateStr = borrowDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String returnDateStr = returnDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // SQL 문에 대출일과 반납일을 설정
            insertBorrowStatement.setString(1, borrowDateStr);
            insertBorrowStatement.setString(2, returnDateStr);
            insertBorrowStatement.setInt(3, userId);
            insertBorrowStatement.setInt(4, bookId);
            insertBorrowStatement.executeUpdate();

            // 책의 대출 상태를 true로 업데이트
            updateBookStatement.setInt(1, bookId);
            updateBookStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void returnBook(int userId, int bookId) {
        String deleteQuery = "DELETE FROM Borrow WHERE user_id = ? AND book_id = ?";
        String updateQuery = "UPDATE Book SET is_borrowed = FALSE WHERE book_id = ?";

        try (Connection connection = App.getConnection();
            PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery);
            PreparedStatement updateStmt = connection.prepareStatement(updateQuery)) {

            deleteStmt.setInt(1, userId);
            deleteStmt.setInt(2, bookId);
            deleteStmt.executeUpdate();

            updateStmt.setInt(1, bookId);
            updateStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<BorrowedBook> getBorrowedBooks(int userId) {
        List<BorrowedBook> borrowedBooks = new ArrayList<>();
        String query = "SELECT b.borrow_id, b.borrow_date, b.return_date, b.user_id, b.book_id, " +
                       "bk.title, bk.author_name, bk.publisher " +
                       "FROM Borrow b " +
                       "JOIN Book bk ON b.book_id = bk.book_id " +
                       "WHERE b.user_id = ?";

        try (Connection connection = App.getConnection();
            PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                BorrowedBook borrowedBook = new BorrowedBook();
                borrowedBook.setBorrowId(rs.getInt("borrow_id"));
                borrowedBook.setBorrowDate(rs.getString("borrow_date"));
                borrowedBook.setReturnDate(rs.getString("return_date"));
                borrowedBook.setUserId(rs.getInt("user_id"));
                borrowedBook.setBookId(rs.getInt("book_id"));
                borrowedBook.setTitle(rs.getString("title"));
                borrowedBook.setAuthorName(rs.getString("author_name"));
                borrowedBook.setPublisher(rs.getString("publisher"));
                borrowedBooks.add(borrowedBook);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowedBooks;
    }
}
