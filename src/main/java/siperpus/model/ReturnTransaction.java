package siperpus.model;

import java.sql.Date;

public class ReturnTransaction {
    private int id;
    private int borrowTransactionId;
    private Date actualReturnDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBorrowTransactionId() {
        return borrowTransactionId;
    }

    public void setBorrowTransactionId(int borrowTransactionId) {
        this.borrowTransactionId = borrowTransactionId;
    }

    public Date getActualReturnDate() {
        return actualReturnDate;
    }

    public void setActualReturnDate(Date actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
    }
}
