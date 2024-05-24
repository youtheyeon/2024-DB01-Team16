import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryManager {
    private List<Book> books;
    private int nextBookId;

    public LibraryManager() {
        books = new ArrayList<>();
        // 임시 데이터
        books.add(new Book(1, 1, "Novel",  "Harry Potter", "J.K. Rowling", "Bloomsbury", false));
        books.add(new Book(2, 2, "Novel", "The Hobbit", "J.R.R. Tolkien", "HarperCollins", true));
        books.add(new Book(3, 3, "Novel", "1984", "George Orwell", "Secker & Warburg", false));
        nextBookId = 0; // 초기 bookId 값
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        book = new Book(nextBookId++, book.getCategoryId(), book.getCategory(), book.getTitle(), book.getAuthorName(), book.getPublisher(), false);
        books.add(book);
    }

    public void removeBook(int bookId) {
        books = books.stream().filter(book -> book.getBookId() != bookId).collect(Collectors.toList());
    }

    public void updateBorrowStatus(int bookId, boolean isBorrowed) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                book.setBorrowed(isBorrowed);
                break;
            }
        }
    }

    public List<Book> getBorrowedBooks(int userId) {
        return books.stream().filter(Book::isBorrowed).collect(Collectors.toList());
    }

    public void returnBook(int bookId) {
        updateBorrowStatus(bookId, false);
    }
}