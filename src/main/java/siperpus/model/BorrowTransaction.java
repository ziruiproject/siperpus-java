package siperpus.model;

import java.sql.Date;

public class BorrowTransaction {
    private int id;
    private int memberId;
    private int bookId;
    private Date borrowDate;
    private Date scheduledReturnDate;
    private Date actualReturnDate;
    private Date createdAt;
    private Date updatedAt;

    // Constructors, getters, and setters
    // Constructor
    public BorrowTransaction() {
        this.createdAt = new Date(System.currentTimeMillis());
        this.updatedAt = new Date(System.currentTimeMillis());
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getScheduledReturnDate() {
        return scheduledReturnDate;
    }

    public void setScheduledReturnDate(Date scheduledReturnDate) {
        this.scheduledReturnDate = scheduledReturnDate;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "BorrowTransaction{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", bookId=" + bookId +
                ", borrowDate=" + borrowDate +
                ", scheduledReturnDate=" + scheduledReturnDate +
                ", actualReturnDate=" + actualReturnDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
