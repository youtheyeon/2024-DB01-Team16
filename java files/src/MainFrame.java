import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private UserSession userSession;
    private LibraryManager libraryManager;

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
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(myPageButton, BorderLayout.EAST);
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
        String[] categories = {"소설", "에세이", "인문", "과학"};
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
        DefaultTableModel tableModel = new DefaultTableModel(new Object[]{"Title", "Author Name", "Publisher", "Is Borrowed"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        bookListPanel.add(scrollPane, BorderLayout.CENTER);
        add(bookListPanel, BorderLayout.CENTER);

        // 책 목록 조회
        displayBooks(tableModel);

        // 마이페이지 버튼 클릭 시 마이페이지로 이동
        myPageButton.addActionListener(e -> {
            UserFrame userFrame = new UserFrame(userSession, this);
            userFrame.setVisible(true);
            setVisible(false);
        });
    }

    private void displayBooks(DefaultTableModel tableModel) {
        List<Book> books = libraryManager.getBooks();
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getTitle(),
                book.getAuthorName(),
                book.getPublisher(),
                book.isBorrowed() ? "Yes" : "No"
            });
        }
    }
}
