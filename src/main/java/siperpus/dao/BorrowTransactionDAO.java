package siperpus.dao;

import siperpus.database.Database;
import siperpus.model.BorrowTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowTransactionDAO {
    private final Database database;

    public BorrowTransactionDAO(Database database) {
        this.database = database;
    }

    public List<BorrowTransaction> getAllBorrowTransactions() {
        List<BorrowTransaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM BorrowTransactions";

        try (Connection connection = database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                BorrowTransaction transaction = extractTransactionFromResultSet(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void addBorrowTransaction(BorrowTransaction transaction) {
        String query = "INSERT INTO BorrowTransactions " +
                "(member_id, book_id, borrow_date, scheduled_return_date, actual_return_date, created_at, updated_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, transaction.getMemberId());
            pstmt.setInt(2, transaction.getBookId());
            pstmt.setDate(3, transaction.getBorrowDate());
            pstmt.setDate(4, transaction.getScheduledReturnDate());
            pstmt.setDate(5, transaction.getActualReturnDate());
            pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            pstmt.setTimestamp(7, new Timestamp(System.currentTimeMillis()));

            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                transaction.setId(generatedKeys.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBorrowTransaction(BorrowTransaction transaction) {
        String query = "UPDATE BorrowTransactions " +
                "SET member_id = ?, book_id = ?, borrow_date = ?, " +
                "scheduled_return_date = ?, actual_return_date = ?, updated_at = ? " +
                "WHERE id = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, transaction.getMemberId());
            pstmt.setInt(2, transaction.getBookId());
            pstmt.setDate(3, transaction.getBorrowDate());
            pstmt.setDate(4, transaction.getScheduledReturnDate());
            pstmt.setDate(5, transaction.getActualReturnDate());
            pstmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(7, transaction.getId());

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBorrowTransaction(int transactionId) {
        String query = "DELETE FROM BorrowTransactions WHERE id = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, transactionId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BorrowTransaction getBorrowTransactionById(int transactionId) {
        BorrowTransaction transaction = null;
        String query = "SELECT * FROM BorrowTransactions WHERE id = ?";

        try (Connection connection = database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                transaction = extractTransactionFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    private BorrowTransaction extractTransactionFromResultSet(ResultSet rs) throws SQLException {
        BorrowTransaction transaction = new BorrowTransaction();
        System.out.println(rs.getTimestamp("scheduled_return_date"));
        transaction.setId(rs.getInt("id"));
        transaction.setMemberId(rs.getInt("member_id"));
        transaction.setBookId(rs.getInt("book_id"));
        transaction.setActualReturnDate(rs.getDate("actual_return_date"));
        transaction.setScheduledReturnDate(rs.getDate("scheduled_return_date"));
        transaction.setBorrowDate(rs.getDate("borrow_date"));
        return transaction;
    }

    public List<BorrowTransaction> searchBorrowTransactions(String keyword) {
        List<BorrowTransaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM BorrowTransactions WHERE member_id IN (SELECT id FROM Members WHERE name LIKE ?) " +
                "OR book_id IN (SELECT id FROM Books WHERE title LIKE ?)";

        try (Connection connection = database.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BorrowTransaction transaction = extractTransactionFromResultSet(rs);
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

}
