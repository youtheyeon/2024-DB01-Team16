import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private UserSession userSession;
    private LibraryManager libraryManager;
    private JButton borrowButton;

    public MainFrame(UserSession session, Connection conn) { // Connection conn 추가
        
        this.userSession = session;
        this.libraryManager = new LibraryManager(conn); //db 연동 전달
        
        this.libraryManager = new LibraryManager();
        setTitle("Library Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 상단 패널: 제목 & 마이페이지 버튼
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        JLabel titleLabel = new JLabel("Library Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        JButton myPageButton = new JButton("MyPage");
        JButton bookManagementButton = new JButton("Book Management");
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(myPageButton, BorderLayout.EAST);
        topPanel.add(bookManagementButton, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // 오른쪽 패널
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        // 검색창 패널
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20)); 
        JTextField searchField = new JTextField(20);
        searchField.setMaximumSize(new Dimension(300, 30));
        searchPanel.add(searchField);

        // 카테고리 드롭다운 패널
        String[] categories = {"All", "Novel", "Essay", "Literature", "Science"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setMaximumSize(new Dimension(300, 30)); 
        searchPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        searchPanel.add(categoryComboBox);

        rightPanel.add(searchPanel);
        add(rightPanel, BorderLayout.EAST);

        // 책 목록 테이블
        JPanel bookListPanel = new JPanel(new BorderLayout());
        bookListPanel.setBorder(BorderFactory.createEmptyBorder(4, 20, 20, 10));

        JTable bookTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Category", "Title", "Author Name", "Publisher", "Is Borrowed"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookListPanel.add(scrollPane, BorderLayout.CENTER);
        add(bookListPanel, BorderLayout.CENTER);

        // 테이블 행 선택 리스너 추가
        bookTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                borrowBook(bookTable);
            }
        });

        // 마이페이지 버튼 클릭 시 마이페이지로 이동
        myPageButton.addActionListener(e -> {
            UserFrame userFrame = new UserFrame(userSession, this);
            userFrame.setVisible(true);
            setVisible(false);
        });

        // 도서 관리 페이지 버튼 클릭 시 도서 관리 페이지로 이동
        bookManagementButton.addActionListener(e -> {
            BookManagementFrame bookManagementFrame = new BookManagementFrame(libraryManager, this);
            bookManagementFrame.setVisible(true);
            setVisible(false);
        });
    }

    private void borrowBook(JTable bookTable) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            String bookTitle = (String) bookTable.getValueAt(selectedRow, 0);
            String isBorrowed = (String) bookTable.getValueAt(selectedRow, 4);
            if ("Yes".equals(isBorrowed)) {
                JOptionPane.showMessageDialog(this, bookTitle + "cannot be borrowed! :(", "Book cannot be borrowed", JOptionPane.PLAIN_MESSAGE);
            } else {
                // 대출일과 반납일 입력 받기
                JTextField borrowDateField = new JTextField();
                JTextField returnDateField = new JTextField();
                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Enter borrow date (YYYY-MM-DD):"));
                panel.add(borrowDateField);
                panel.add(new JLabel("Enter return date (YYYY-MM-DD):"));
                panel.add(returnDateField);
                int result = JOptionPane.showConfirmDialog(null, panel, "Set Dates for " + bookTitle, JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String borrowDate = borrowDateField.getText();
                    String returnDate = returnDateField.getText();
                    // 대출일과 반납일 사용하여 처리
                    JOptionPane.showMessageDialog(this, "Dates set for " + bookTitle, "Dates set", JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }

    public LibraryManager getLibraryManager() {
        return libraryManager;
    }
}
