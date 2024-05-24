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
        tableModel = new DefaultTableModel(new Object[]{"ID", "Category", "Title", "Author Name", "Publisher", "Borrow Date", "Return Date", "User Name"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        add(scrollPane, BorderLayout.CENTER);

        // 책 목록 조회
        loadBookList();

        // 하단 패널: 추가 & 삭제 버튼
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton addButton = new JButton("Add Book");
        JButton deleteButton = new JButton("Delete Book");
        JButton modifyButton = new JButton("Modify Book");
        JButton backButton = new JButton("Back");
        bottomPanel.add(addButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(modifyButton);
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // 추가 버튼 클릭 시 책 추가 다이얼로그 표시
        addButton.addActionListener(e -> {
            JTextField categoryField = new JTextField(20);
            JTextField titleField = new JTextField(20);
            JTextField authorField = new JTextField(20);
            JTextField publisherField = new JTextField(20);

            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Category ID (e.g., 1 for Novel, 2 for Essay):"));
            panel.add(categoryField);
            panel.add(new JLabel("Title:"));
            panel.add(titleField);
            panel.add(new JLabel("Author Name:"));
            panel.add(authorField);
            panel.add(new JLabel("Publisher:"));
            panel.add(publisherField);

            String[] options = {"Add", "Cancel"};
            int result = JOptionPane.showOptionDialog(this, panel, "Add Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int categoryId = Integer.parseInt(categoryField.getText());
                    String title = titleField.getText();
                    String author = authorField.getText();
                    String publisher = publisherField.getText();
                    if (!title.isEmpty() && !author.isEmpty() && !publisher.isEmpty()) {
                        libraryManager.addBook(new Book(0, categoryId, title, author, publisher, false));
                        loadBookList();
                    } else {
                        JOptionPane.showMessageDialog(this, "Enter every field", "Input Error", JOptionPane.PLAIN_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter a valid Category ID", "Input Error", JOptionPane.PLAIN_MESSAGE);
                }
            }
        });

        // 삭제 버튼 클릭 시 책 ID 입력 받아서 삭제
        deleteButton.addActionListener(e -> {
            String bookIdStr = JOptionPane.showInputDialog(this, "Enter the ID of the book to delete:", "Delete Book", JOptionPane.PLAIN_MESSAGE);
            if (bookIdStr != null) {
                try {
                    int bookId = Integer.parseInt(bookIdStr);
                    boolean removed = libraryManager.removeBook(bookId);
                    if (removed) {
                        loadBookList();
                        JOptionPane.showMessageDialog(this, "Book deleted successfully.", "Success", JOptionPane.PLAIN_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Book ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter a valid book ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 수정 버튼 클릭 시 책 정보 수정 다이얼로그 표시
        modifyButton.addActionListener(e -> {
            String bookIdStr = JOptionPane.showInputDialog(this, "Enter the ID of the book to modify:", "Modify Book", JOptionPane.PLAIN_MESSAGE);
            if (bookIdStr != null) {
                try {
                    int bookId = Integer.parseInt(bookIdStr);
                    Book book = libraryManager.getBookById(bookId);
                    if (book != null) {
                        JTextField categoryField = new JTextField(String.valueOf(book.getCategoryId()), 20);
                        JTextField titleField = new JTextField(book.getTitle(), 20);
                        JTextField authorField = new JTextField(book.getAuthorName(), 20);
                        JTextField publisherField = new JTextField(book.getPublisher(), 20);

                        JPanel panel = new JPanel(new GridLayout(0, 1));
                        panel.add(new JLabel("Category ID (e.g., 1 for Fiction, 2 for Non-fiction):"));
                        panel.add(categoryField);
                        panel.add(new JLabel("Title:"));
                        panel.add(titleField);
                        panel.add(new JLabel("Author Name:"));
                        panel.add(authorField);
                        panel.add(new JLabel("Publisher:"));
                        panel.add(publisherField);

                        String[] options = {"Modify", "Cancel"};
                        int result = JOptionPane.showOptionDialog(this, panel, "Modify Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                        if (result == JOptionPane.OK_OPTION) {
                            try {
                                int categoryId = Integer.parseInt(categoryField.getText());
                                String title = titleField.getText();
                                String author = authorField.getText();
                                String publisher = publisherField.getText();
                                if (!title.isEmpty() && !author.isEmpty() && !publisher.isEmpty()) {
                                    book.setCategoryId(categoryId);
                                    book.setTitle(title);
                                    book.setAuthorName(author);
                                    book.setPublisher(publisher);
                                    libraryManager.updateBook(book);
                                    loadBookList();
                                } else {
                                    JOptionPane.showMessageDialog(this, "Enter every field", "Input Error", JOptionPane.PLAIN_MESSAGE);
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "Enter a valid Category ID", "Input Error", JOptionPane.PLAIN_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Book ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Enter a valid book ID.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // 뒤로가기 버튼 클릭 시 메인 페이지로 이동
        backButton.addActionListener(e -> {
            mainFrame.setVisible(true);
            setVisible(false);
        });
    }

    private void loadBookList() {
        tableModel.setRowCount(0); // Clear existing rows
        List<BookDetails> books = libraryManager.getBookDetails();
        for (BookDetails book : books) {
            tableModel.addRow(new Object[]{
                book.getBookId(),
                book.getCategory(),
                book.getTitle(),
                book.getAuthorName(),
                book.getPublisher(),
                book.getBorrowDate(),
                book.getReturnDate(),
                book.getUserName(),
            });
        }
    }
}