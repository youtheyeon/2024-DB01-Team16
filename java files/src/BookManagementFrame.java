import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BookManagementFrame extends JFrame {
    private LibraryManager libraryManager;
    private MainFrame mainFrame;
    private DefaultTableModel tableModel;

    public BookManagementFrame(LibraryManager libraryManager, MainFrame mainFrame) {
        this.libraryManager = libraryManager;
        this.mainFrame = mainFrame;
        setTitle("Book Management Page");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // 책 목록 테이블
        JTable bookTable = new JTable();
        tableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Author Name", "Publisher", "Is Borrowed"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // 책 목록 조회
        displayBooks();

        // 하단 패널: 추가 & 삭제 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton backButton = new JButton("Back");
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 추가 버튼 클릭 시 책 추가 다이얼로그 표시
        addButton.addActionListener(e -> {
            JTextField titleField = new JTextField(20);
            JTextField authorField = new JTextField(20);
            JTextField publisherField = new JTextField(20);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Title:"));
            panel.add(titleField);
            panel.add(new JLabel("Author Name:"));
            panel.add(authorField);
            panel.add(new JLabel("Publisher:"));
            panel.add(publisherField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String title = titleField.getText();
                String author = authorField.getText();
                String publisher = publisherField.getText();
                if (!title.isEmpty() && !author.isEmpty() && !publisher.isEmpty()) {
                    libraryManager.addBook(new Book(0, 0, title, author, publisher, false));
                    displayBooks();
                } else {
                    JOptionPane.showMessageDialog(this, "모든 필드를 입력하세요.", "입력 오류", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // 삭제 버튼 클릭 시 선택된 책 삭제
        deleteButton.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow != -1) {
                int bookId = (int) bookTable.getValueAt(selectedRow, 0);
                libraryManager.removeBook(bookId);
                displayBooks();
            } else {
                JOptionPane.showMessageDialog(this, "삭제할 책을 선택하세요.", "선택 오류", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // 뒤로가기 버튼 클릭 시 메인 페이지로 이동
        backButton.addActionListener(e -> {
            mainFrame.setVisible(true);
            setVisible(false);
        });
    }

    private void displayBooks() {
        tableModel.setRowCount(0); // Clear existing rows
        List<Book> books = libraryManager.getBooks();
        for (Book book : books) {
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
