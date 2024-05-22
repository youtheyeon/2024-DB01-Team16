import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UserFrame extends JFrame {
    private UserSession userSession;
    private MainFrame mainFrame;
    private LibraryManager libraryManager;
    private int selectedBookId = -1; // 선택된 책의 ID를 저장

    public UserFrame(UserSession session, MainFrame mainFrame) {
        this.userSession = session;
        this.mainFrame = mainFrame;
        this.libraryManager = new LibraryManager();
        setTitle("My Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel welcomeLabel = new JLabel(userSession.getUserName() + "님의 마이페이지");
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(welcomeLabel, BorderLayout.NORTH);

        // 대출한 책 목록 테이블
        JTable borrowedBookTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Book ID", "Title", "Author Name", "Publisher", "Is Borrowed"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 모든 셀을 수정 불가능하게 설정
            }
        };
        borrowedBookTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(borrowedBookTable);
        add(scrollPane, BorderLayout.CENTER);

        // 대출한 책 목록 조회
        displayBorrowedBooks(tableModel);

        JPanel bottomPanel = new JPanel();
        JButton backButton = new JButton("Back to Main");
        backButton.addActionListener(e -> {
            dispose();
            mainFrame.setVisible(true);
        });
        bottomPanel.add(backButton);

        JButton returnButton = new JButton("Return Book");
        returnButton.setEnabled(false); // 책이 선택되지 않은 상태에서는 비활성화
        returnButton.addActionListener(e -> {
            if (selectedBookId != -1) {
                libraryManager.returnBook(selectedBookId);
                displayBorrowedBooks(tableModel); // 목록 새로 고침
                returnButton.setEnabled(false); // 반납 후 버튼 비활성화
            }
        });
        bottomPanel.add(returnButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 테이블에서 책을 선택하면 반납 버튼 활성화
        borrowedBookTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int selectedRow = borrowedBookTable.getSelectedRow();
                if (selectedRow != -1) {
                    selectedBookId = (int) tableModel.getValueAt(selectedRow, 0);
                    returnButton.setEnabled(true);
                } else {
                    returnButton.setEnabled(false);
                }
            }
        });
    }

    private void displayBorrowedBooks(DefaultTableModel tableModel) {
        tableModel.setRowCount(0); // 기존 데이터 삭제
        List<Book> borrowedBooks = libraryManager.getBorrowedBooks(userSession.getUserId());
        for (Book book : borrowedBooks) {
            tableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthorName(),
                book.getPublisher(),
                book.isBorrowed() ? "Yes" : "No"
            });
        }
    }
}
