public class BorrowedBook {
    private String title;
    private String borrowDate;
    private String returnDate;

    public BorrowedBook(String title, String borrowDate, String returnDate) {
        this.title = title;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public String getTitle() {
        return title;
    }

    public String getBorrowDate() {
        return borrowDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
}