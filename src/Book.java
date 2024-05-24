public class Book {
    private int bookId;
    private int categoryId;
    private String category;
    private String title;
    private String authorName;
    private String publisher;
    private boolean isBorrowed;

    public Book(int bookId, int categoryId, String category, String title, String authorName, String publisher, boolean isBorrowed) {
        this.bookId = bookId;
        this.categoryId = categoryId;
        this.category = category;
        this.title = title;
        this.authorName = authorName;
        this.publisher = publisher;
        this.isBorrowed = isBorrowed;
    }

    public int getBookId() {
        return bookId;
    }

    public int getCategoryId() {
        return categoryId;
    }
    
    public String getCategory() {
    	return category;
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

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }
}