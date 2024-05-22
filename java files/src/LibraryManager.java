import java.util.ArrayList;
import java.util.List;

public class LibraryManager {
    private List<Book> books;

    public LibraryManager() {
        books = new ArrayList<>();
        books.add(new Book(1, 1, "Harry Potter", "J.K. Rowling", "Bloomsbury", false));
        books.add(new Book(2, 2, "The Hobbit", "J.R.R. Tolkien", "HarperCollins", true));
        books.add(new Book(3, 3, "1984", "George Orwell", "Secker & Warburg", false));
    }

    // 책 목록을 반환합니다.
    public List<Book> getBooks() {
        return books;
    }

    // 사용자가 대출한 책 목록을 반환합니다.
    public List<Book> getBorrowedBooks(int userId) {
        List<Book> borrowedBooks = new ArrayList<>();
        for (Book book : books) {
            if (book.isBorrowed()) {
                borrowedBooks.add(book);
            }
        }
        return borrowedBooks;
    }

    // 책을 반납합니다.
    public void returnBook(int bookId) {
        for (Book book : books) {
            if (book.getBookId() == bookId) {
                book.setBorrowed(false);
                break;
            }
        }
    }
}
