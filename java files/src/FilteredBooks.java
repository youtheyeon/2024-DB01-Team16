public class FilteredBooks {
    private int bookId;
    private String category;
    private String title;
    private String authorName;
    private String publisher;
    private boolean isBorrowed;

    public FilteredBooks(int bookId, String category, String title, String authorName, String publisher, boolean isBorrowed) {
        this.bookId = bookId;
        this.category = category;
        this.title = title;
        this.authorName = authorName;
        this.publisher = publisher;
        this.isBorrowed = isBorrowed;
    }

    public int getBookId() {
        return bookId;
    }

    public String getCategory() {
        return category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }
}
