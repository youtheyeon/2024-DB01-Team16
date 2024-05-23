import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private UserSession userSession;
    private LibraryManager libraryManager;
    private JButton borrowButton;

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
        String[] categories = {"전체", "소설", "에세이", "인문", "과학"};
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

        // 오른쪽 아래에 대출하기 버튼 추가
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        borrowButton = new JButton("Borrow");
        borrowButton.setVisible(false); // 초기에는 버튼을 숨김
        borrowButton.addActionListener(e -> borrowBook(bookTable));
        buttonPanel.add(borrowButton);

        rightPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        rightPanel.add(buttonPanel);
        
        // 책 목록 조회
        displayBooks(tableModel);

        // 마이페이지 버튼 클릭 시 마이페이지로 이동
        myPageButton.addActionListener(e -> {
            UserFrame userFrame = new UserFrame(userSession, this);
            userFrame.setVisible(true);
            setVisible(false);
        });

        // 테이블 행 선택 리스너 추가
        bookTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && bookTable.getSelectedRow() != -1) {
                borrowButton.setVisible(true); // 책이 선택되면 버튼을 보이게 함
            } else {
                borrowButton.setVisible(false); // 선택이 해제되면 버튼을 숨김
            }
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

    private void borrowBook(JTable bookTable) {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow != -1) {
            String bookTitle = (String) bookTable.getValueAt(selectedRow, 0);
            String isBorrowed = (String) bookTable.getValueAt(selectedRow, 3);
            if ("Yes".equals(isBorrowed)) {
                JOptionPane.showMessageDialog(this, bookTitle + "는(은) 대출이 불가능합니다:(", "도서 대출 불가능", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, bookTitle + "를(을) 대출했습니다!", "도서 대출 완료", JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
}
