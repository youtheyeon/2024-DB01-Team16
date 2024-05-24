public class BookDetails {
    private int bookId;
    private String title;
    private String authorName;
    private String publisher;
    private String category;
    private String borrowDate;
    private String returnDate;
    private String userName;

    public BookDetails(int bookId, String title, String authorName, String publisher, String category, String borrowDate, String returnDate, String userName) {
        this.bookId = bookId;
        this.title = title;
        this.authorName = authorName;
        this.publisher = publisher;
        this.category = category;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.userName = userName;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getCategory() {
        return category;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public String getUserName() {
        return userName;
    }
}
