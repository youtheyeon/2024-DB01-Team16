import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {


    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private Connection conn;

    public DatabaseManager(String url, String username, String password) throws SQLException {
        conn = DriverManager.getConnection(url, username, password);
        System.out.println("데이터베이스에 연결되었습니다.");
        initDB();
    }

    public void initDB() {
        try (Statement stmt = conn.createStatement()) {
            // Category 테이블 생성
            String createCategoryTable = "CREATE TABLE IF NOT EXISTS Category (" +
                    "category_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "category VARCHAR(30)" +
                    ")";
            stmt.execute(createCategoryTable);

            // Book 테이블 생성
            String createBookTable = "CREATE TABLE IF NOT EXISTS Book (" +
                    "book_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "category_id INT," +
                    "title VARCHAR(50)," +
                    "author_name VARCHAR(20)," +
                    "publisher VARCHAR(20)," +
                    "FOREIGN KEY (category_id) REFERENCES Category(category_id)" +
                    ")";
            stmt.execute(createBookTable);

            // User 테이블 생성
            String createUserTable = "CREATE TABLE IF NOT EXISTS User (" +
                    "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "user_name VARCHAR(20)," +
                    "phone_num INT" +
                    ")";
            stmt.execute(createUserTable);

            // Borrow 테이블 생성
            String createBorrowTable = "CREATE TABLE IF NOT EXISTS Borrow (" +
                    "borrow_id INT AUTO_INCREMENT PRIMARY KEY," +
                    "borrow_date DATE," +
                    "return_date DATE," +
                    "user_id INT," +
                    "book_id INT," +
                    "FOREIGN KEY (user_id) REFERENCES User(user_id)," +
                    "FOREIGN KEY (book_id) REFERENCES Book(book_id)" +
                    ")";
            stmt.execute(createBorrowTable);

            // BookDetails 뷰 생성
            String createBookDetailsView = "CREATE VIEW IF NOT EXISTS BookDetails AS " +
                    "SELECT b.book_id, b.title, b.author_name, b.publisher, c.category, " +
                    "br.borrow_date, br.return_date, u.user_name " +
                    "FROM Book b " +
                    "LEFT JOIN Borrow br ON b.book_id = br.book_id " +
                    "LEFT JOIN User u ON br.user_id = u.user_id " +
                    "LEFT JOIN Category c ON b.category_id = c.category_id";
            stmt.execute(createBookDetailsView);

            // 인덱스 추가
            String createIndexes = "CREATE INDEX IF NOT EXISTS idx_title ON Book(title); " +
                    "CREATE INDEX IF NOT EXISTS idx_user_name ON User(user_name); " +
                    "CREATE INDEX IF NOT EXISTS idx_borrow_user ON Borrow(user_id); " +
                    "CREATE INDEX IF NOT EXISTS idx_borrow_book ON Borrow(book_id); " +
                    "CREATE INDEX IF NOT EXISTS idx_category ON Category(category)";
            stmt.execute(createIndexes);

            // 초기 데이터 삽입
            String insertCategories = "INSERT INTO Category (category) VALUES " +
                    "('Fiction')," +
                    "('Non-fiction')," +
                    "('Science')," +
                    "('History')," +
                    "('Biography')";
            stmt.execute(insertCategories);

            String insertBooks = "INSERT INTO Book (category_id, title, author_name, publisher) VALUES " +
                    "(1, 'To Kill a Mockingbird', 'Harper Lee', 'J.B. Lippincott & Co.')," +
                    "(2, '1984', 'George Orwell', 'Secker & Warburg')," +
                    "(3, 'Brief History of Time', 'Stephen Hawking', 'Bantam Books')," +
                    "(4, 'Sapiens', 'Yuval Noah Harari', 'Harper')," +
                    "(5, 'The Diary of a Young Girl', 'Anne Frank', 'Contact Publishing')";
            stmt.execute(insertBooks);

            String insertUsers = "INSERT INTO User (user_name, phone_num) VALUES " +
                    "('Alice Johnson', 12340123)," +
                    "('Bob Smith', 23450234)," +
                    "('Charlie Brown', 34560345)," +
                    "('David Williams', 45670456)," +
                    "('Eva Green', 56780567)";
            stmt.execute(insertUsers);

            String insertBorrows = "INSERT INTO Borrow (borrow_date, return_date, user_id, book_id) VALUES " +
                    "('2024-05-01', '2024-05-15', 1, 1)," +
                    "('2024-05-05', NULL, 2, 2)," +
                    "('2024-05-10', NULL, 3, 3)," +
                    "('2024-05-20', '2024-05-24', 4, 4)," +
                    "('2024-05-22', NULL, 5, 5)";
            stmt.execute(insertBorrows);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 책 추가
    public void insertBook(String title, String author, String publicationDate, String publisher, int categoryId) {
        try {
            String sql = "INSERT INTO Book (title, author_name, publisher, publication_date, category_id) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publisher);
            pstmt.setString(4, publicationDate);
            pstmt.setInt(5, categoryId);
            pstmt.executeUpdate();
            System.out.println("책이 성공적으로 추가되었습니다.");
        } catch (SQLException e) {
            System.out.println("책 추가 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 사용자 추가
    public void insertUser(String name, int phoneNum) {
        try {
            String sql = "INSERT INTO User (user_name, phone_num) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setInt(2, phoneNum);
            pstmt.executeUpdate();
            System.out.println("사용자가 성공적으로 추가되었습니다.");
        } catch (SQLException e) {
            System.out.println("사용자 추가 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 책 제목으로 대출 상태 조회
    public void selectBorrowStatus(String bookTitle) {
        try {
            String sql = "SELECT u.user_name, CASE WHEN b.book_id IS NULL THEN '대출 가능' ELSE '대출 중' END AS borrow_status " +
                         "FROM User u LEFT JOIN Borrow b ON u.user_id = b.user_id " +
                         "INNER JOIN Book bk ON b.book_id = bk.book_id " +
                         "WHERE bk.title = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookTitle);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("사용자: " + rs.getString("user_name") + ", 대출 상태: " + rs.getString("borrow_status"));
            }
        } catch (SQLException e) {
            System.out.println("조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 카테고리 선택에 따른 책 조회
    public void selectBooksByCategory(String category) {
        try {
            String sql = "SELECT title FROM Book WHERE category_id = (SELECT category_id FROM Category WHERE category = ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            System.out.println(category + " 카테고리의 책 목록:");
            while (rs.next()) {
                System.out.println(rs.getString("title"));
            }
        } catch (SQLException e) {
            System.out.println("조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 대출 정보 업데이트
    public void updateBorrowInfo(int userId, int bookId, String returnDate) {
        try {
            String sql = "UPDATE Borrow SET return_date = ? WHERE user_id = ? AND book_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, returnDate);
            pstmt.setInt(2, userId);
            pstmt.setInt(3, bookId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("대출 정보가 성공적으로 업데이트되었습니다.");
            } else {
                System.out.println("해당 대출 정보가 없습니다.");
            }
        } catch (SQLException e) {
            System.out.println("업데이트 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }
}
