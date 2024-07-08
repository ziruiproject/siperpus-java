package siperpus.dao;

import siperpus.database.Database;
import siperpus.model.ReturnTransaction;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReturnTransactionDAO {
    private final Database database;

    public ReturnTransactionDAO(Database database) {
        this.database = database;
    }

    public void addReturnTransaction(ReturnTransaction transaction) {
        String sql = "INSERT INTO ReturnTransactions (borrow_transaction_id, return_date) VALUES (?, ?)";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getBorrowTransactionId());
            pstmt.setDate(2, transaction.getActualReturnDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ReturnTransaction> getAllReturnTransactions() {
        List<ReturnTransaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM ReturnTransactions";
        try (Connection conn = database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                ReturnTransaction transaction = new ReturnTransaction();
                transaction.setId(rs.getInt("id"));
                transaction.setBorrowTransactionId(rs.getInt("borrow_transaction_id"));
                transaction.setActualReturnDate(rs.getDate("return_date"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public void updateReturnTransaction(ReturnTransaction transaction) {
        String sql = "UPDATE ReturnTransactions SET borrow_transaction_id = ?, return_date = ? WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, transaction.getBorrowTransactionId());
            pstmt.setDate(2, transaction.getActualReturnDate());
            pstmt.setInt(3, transaction.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteReturnTransaction(int id) {
        String sql = "DELETE FROM ReturnTransactions WHERE id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReturnTransaction getReturnTransactionByBorrowTransactionId(int borrowTransactionId) {
        String sql = "SELECT * FROM ReturnTransactions WHERE borrow_transaction_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, borrowTransactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ReturnTransaction transaction = new ReturnTransaction();
                    transaction.setId(rs.getInt("id"));
                    transaction.setBorrowTransactionId(rs.getInt("borrow_transaction_id"));
                    transaction.setActualReturnDate(rs.getDate("actual_return_date"));
                    return transaction;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
