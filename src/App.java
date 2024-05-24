import javax.swing.SwingUtilities;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });

         try {
            // 데이터베이스 연결 설정
            String url = "jdbc:mysql://localhost:3306/library_db?serverTimezone=UTC"; // 데이터베이스를 사용
            String username = "root"; // MySQL 사용자 이름
            String password = "root"; // MySQL 사용자 비밀번호

            conn = DriverManager.getConnection(url, username, password);
            System.out.println("데이터베이스에 연결되었습니다.");

            // 테스트를 위한 메서드 호출
            insertBook("책 제목", "저자", "2023-01-01");
            insertUser("사용자 이름", "user@example.com");
            selectBorrowStatus("책 제목");
            selectBooksByCategory("카테고리 이름");
            updateBorrowInfo(1, 1, "2023-12-31");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 책 추가 메뉴
    public static void insertBook(String title, String author, String publicationDate) {
        try {
            String sql = "INSERT INTO book (title, author, publication_date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publicationDate);
            pstmt.executeUpdate();
            System.out.println("책이 성공적으로 추가되었습니다.");
        } catch (SQLException e) {
            System.out.println("책 추가 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 사용자 추가 메뉴
    public static void insertUser(String name, String email) {
        try {
            String sql = "INSERT INTO user (name, email) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.executeUpdate();
            System.out.println("사용자가 성공적으로 추가되었습니다.");
        } catch (SQLException e) {
            System.out.println("사용자 추가 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 책 제목으로 대출 상태 조회
    public static void selectBorrowStatus(String bookTitle) {
        try {
            String sql = "SELECT u.name, CASE WHEN b.book_id IS NULL THEN '대출 가능' ELSE '대출 중' END AS borrow_status " +
                         "FROM user u LEFT JOIN borrow b ON u.user_id = b.user_id " +
                         "INNER JOIN book bk ON b.book_id = bk.book_id " +
                         "WHERE bk.title = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookTitle);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("사용자: " + rs.getString("name") + ", 대출 상태: " + rs.getString("borrow_status"));
            }
        } catch (SQLException e) {
            System.out.println("조회 중 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    // 카테고리 선택에 따른 책 조회
    public static void selectBooksByCategory(String category) {
        try {
            String sql = "SELECT title FROM book WHERE category = ?";
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

    // 대출 정보 업데이트 메뉴
    public static void updateBorrowInfo(int userId, int bookId, String returnDate) {
        try {
            String sql = "UPDATE borrow SET return_date = ? WHERE user_id = ? AND book_id = ?";
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
