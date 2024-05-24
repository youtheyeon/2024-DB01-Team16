import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private UserSession userSession;
    private LibraryManager libraryManager;
    private JButton borrowButton;
    private DefaultTableModel tableModel;

    public MainFrame(UserSession session) {
        this.userSession = session;
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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Category", "Title", "Author Name", "Publisher", "Is Borrowed"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable.setModel(tableModel);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookListPanel.add(scrollPane, BorderLayout.CENTER);
        add(bookListPanel, BorderLayout.CENTER);

        // 오른쪽 아래에 대출하기 버튼 추가
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        borrowButton = new JButton("Borrow");
        borrowButton.setVisible(false); // 초기에는 버튼을 숨김
        borrowButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) bookTable.getValueAt(selectedRow, 0);
                libraryManager.borrowBook(userSession.getUserId(), bookId); // 대출하기 메서드 호출
            }
        });
        buttonPanel.add(borrowButton);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        rightPanel.add(buttonPanel);

        // 테이블 행 선택 리스너 추가
        bookTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                borrowButton.setVisible(true); // 대출하기 버튼 표시
            } else {
                borrowButton.setVisible(false); // 대출하기 버튼 숨김
            }
        });

        // 마이페이지 버튼 클릭 시 마이페이지로 이동
        myPageButton.addActionListener(e -> {
            UserFrame userFrame = new UserFrame(libraryManager, userSession, this);
            userFrame.setVisible(true);
            setVisible(false);
        });

        // 도서 관리 페이지 버튼 클릭 시 도서 관리 페이지로 이동
        bookManagementButton.addActionListener(e -> {
            BookManagementFrame bookManagementFrame = new BookManagementFrame(libraryManager, this);
            bookManagementFrame.setVisible(true);
            setVisible(false);
        });

        // 책 목록 불러오기
        loadBookList();
    }

    private void loadBookList() {
        tableModel.setRowCount(0);
        
        List<Book> books = libraryManager.getBooks();
        for (Book book : books) {
            String category = getCategoryName(book.getCategoryId());
            tableModel.addRow(new Object[]{book.getBookId(), category, book.getTitle(), book.getAuthorName(), book.getPublisher(), book.isBorrowed() ? "Yes" : "No"});
        }
    }
    

    private String getCategoryName(int categoryId) {
        switch (categoryId) {
            case 1: return "Novel";
            case 2: return "Essay";
            case 3: return "Literature";
            case 4: return "Science";
            default: return "Unknown";
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            loadBookList();
        }
    }
}
