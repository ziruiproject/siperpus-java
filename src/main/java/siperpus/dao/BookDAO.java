package siperpus.dao;

import siperpus.database.Database;
import siperpus.model.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private final Database database;

    public BookDAO(Database database) {
        this.database = database;
    }

    // Create
    public void addBook(Book book) {
        String sql = "INSERT INTO Books (title, call_number, stok, author_publisher_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getCallNumber());
            stmt.setInt(3, book.getStock());
            stmt.setInt(4, book.getAuthorPublisherId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Read all
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT b.*, a.name AS author_name, p.name AS publisher_name " +
                "FROM Books b " +
                "JOIN AuthorPublisher ap ON b.author_publisher_id = ap.id " +
                "JOIN Authors a ON ap.author_id = a.id " +
                "JOIN Publishers p ON ap.publisher_id = p.id " +
                "ORDER BY b.id"; // Added ORDER BY clause to sort by id

        try (Connection connection = database.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Book book = extractBookFromResultSet(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setCallNumber(rs.getString("call_number"));
        book.setStock(rs.getInt("stok"));
        book.setAuthorPublisherId(rs.getInt("author_publisher_id"));
        book.setAuthorName(rs.getString("author_name")); // Set author name
        book.setPublisherName(rs.getString("publisher_name")); // Set publisher name
        book.setCreatedAt(rs.getTimestamp("created_at"));
        book.setUpdatedAt(rs.getTimestamp("updated_at"));
        return book;
    }

    // Read by ID
    public Book getBookById(int id) {
        String sql = "SELECT * FROM Books WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setCallNumber(rs.getString("call_number"));
                    book.setStock(rs.getInt("stok"));
                    book.setAuthorPublisherId(rs.getInt("author_publisher_id"));
                    book.setCreatedAt(rs.getTimestamp("created_at"));
                    book.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return book;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Update
    public void updateBook(Book book) {
        String sql = "UPDATE Books SET title = ?, call_number = ?, stok = ?, author_publisher_id = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getCallNumber());
            stmt.setInt(3, book.getStock());
            stmt.setInt(4, book.getAuthorPublisherId());
            stmt.setInt(5, book.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete
    public void deleteBook(int id) {
        String sql = "DELETE FROM Books WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT Books.*, " +
                "(SELECT GROUP_CONCAT(Authors.name SEPARATOR ', ') " +
                " FROM AuthorPublisher " +
                " JOIN Authors ON AuthorPublisher.author_id = Authors.id " +
                " WHERE AuthorPublisher.id = Books.author_publisher_id) AS author_names, " +
                "(SELECT GROUP_CONCAT(Publishers.name SEPARATOR ', ') " +
                " FROM AuthorPublisher " +
                " JOIN Publishers ON AuthorPublisher.publisher_id = Publishers.id " +
                " WHERE AuthorPublisher.id = Books.author_publisher_id) AS publisher_names " +
                "FROM Books " +
                "WHERE Books.title LIKE ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    book.setCallNumber(rs.getString("call_number"));
                    book.setStock(rs.getInt("stok"));
                    book.setAuthorName(rs.getString("author_names"));
                    book.setPublisherName(rs.getString("publisher_names"));
                    book.setCreatedAt(rs.getTimestamp("created_at"));
                    book.setUpdatedAt(rs.getTimestamp("updated_at"));
                    books.add(book);
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return books;
    }


    public int getAuthorPublisherId(int authorId, int publisherId) {
        int authorPublisherId = -1;
        String sql = "SELECT id FROM author_publisher WHERE author_id = ? AND publisher_id = ?";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, authorId);
            stmt.setInt(2, publisherId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    authorPublisherId = rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorPublisherId;
    }

    public int addAuthorPublisher(int authorId, int publisherId) {
        int authorPublisherId = -1;
        String sql = "INSERT INTO AuthorPublisher (author_id, publisher_id) VALUES (?, ?)";
        try (Connection conn = database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, authorId);
            stmt.setInt(2, publisherId);
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    authorPublisherId = generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authorPublisherId;
    }

    public Book getBookByTitle(String title) {
        String sql = "SELECT * FROM Books WHERE title = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, title);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setTitle(rs.getString("title"));
                    // Set other fields if necessary
                    return book;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Increment book stock
    public void incrementBookStock(int bookId) {
        String sql = "UPDATE Books SET stok = stok + 1 WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Decrement book stock
    public void decrementBookStock(int bookId) {
        String sql = "UPDATE Books SET stok = stok - 1 WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
