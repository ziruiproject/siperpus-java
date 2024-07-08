package siperpus.database;

import java.sql.*;

public class Database {
    public Database() {
        Register();
    }

    void Register() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        String jdbcUrl = "jdbc:sqlite:siperpus.db";

        try {
            return DriverManager.getConnection(jdbcUrl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void executeUpdate(String sql) {
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(String sql) {
        try {
            Connection connection = getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            return stmt.executeQuery();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeDatabase() {
        String[] sqlStatements = {
                // Create table statements
                "CREATE TABLE IF NOT EXISTS Authors (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME DEFAULT CURRENT_TIMESTAMP);",
                "CREATE TABLE IF NOT EXISTS Publishers (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME DEFAULT CURRENT_TIMESTAMP);",
                "CREATE TABLE IF NOT EXISTS AuthorPublisher (id INTEGER PRIMARY KEY AUTOINCREMENT, author_id INTEGER NOT NULL, publisher_id INTEGER NOT NULL, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (author_id) REFERENCES Authors(id), FOREIGN KEY (publisher_id) REFERENCES Publishers(id));",
                "CREATE TABLE IF NOT EXISTS Books (id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL, call_number TEXT NOT NULL, author_publisher_id INTEGER NOT NULL, stok INTEGER NOT NULL, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (author_publisher_id) REFERENCES AuthorPublisher(id));",
                "CREATE TABLE IF NOT EXISTS Members (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, email TEXT NOT NULL, phone_number TEXT NOT NULL, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME DEFAULT CURRENT_TIMESTAMP);",
                "CREATE TABLE IF NOT EXISTS BorrowTransactions (id INTEGER PRIMARY KEY AUTOINCREMENT, member_id INTEGER NOT NULL, book_id INTEGER NOT NULL, borrow_date DATE NOT NULL, scheduled_return_date DATETIME NOT NULL, actual_return_date DATETIME, created_at DATETIME DEFAULT CURRENT_TIMESTAMP, updated_at DATETIME DEFAULT CURRENT_TIMESTAMP, FOREIGN KEY (member_id) REFERENCES Members(id), FOREIGN KEY (book_id) REFERENCES Books(id));"
        };
        for (String sql : sqlStatements) {
            executeUpdate(sql);
        }
    }
}
