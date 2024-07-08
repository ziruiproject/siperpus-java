package siperpus.dao;

import siperpus.database.Database;
import siperpus.model.AuthorPublisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuthorPublisherDAO {
    private final Database database;

    public AuthorPublisherDAO(Database database) {
        this.database = database;
    }

    // Create
    public void addAuthorPublisher(AuthorPublisher authorPublisher) {
        String sql = "INSERT INTO AuthorPublisher (author_id, publisher_id) VALUES (?, ?)";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, authorPublisher.getAuthorId());
            stmt.setInt(2, authorPublisher.getPublisherId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Read all
    public List<AuthorPublisher> getAllAuthorPublishers() {
        List<AuthorPublisher> authorPublishers = new ArrayList<>();
        String sql = "SELECT * FROM AuthorPublisher";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AuthorPublisher authorPublisher = new AuthorPublisher();
                authorPublisher.setId(rs.getInt("id"));
                authorPublisher.setAuthorId(rs.getInt("author_id"));
                authorPublisher.setPublisherId(rs.getInt("publisher_id"));
                authorPublisher.setCreatedAt(rs.getTimestamp("created_at"));
                authorPublisher.setUpdatedAt(rs.getTimestamp("updated_at"));
                authorPublishers.add(authorPublisher);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorPublishers;
    }

    // Read by ID
    public AuthorPublisher getAuthorPublisherById(int id) {
        String sql = "SELECT * FROM AuthorPublisher WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    AuthorPublisher authorPublisher = new AuthorPublisher();
                    authorPublisher.setId(rs.getInt("id"));
                    authorPublisher.setAuthorId(rs.getInt("author_id"));
                    authorPublisher.setPublisherId(rs.getInt("publisher_id"));
                    authorPublisher.setCreatedAt(rs.getTimestamp("created_at"));
                    authorPublisher.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return authorPublisher;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Update
    public void updateAuthorPublisher(AuthorPublisher authorPublisher) {
        String sql = "UPDATE AuthorPublisher SET author_id = ?, publisher_id = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, authorPublisher.getAuthorId());
            stmt.setInt(2, authorPublisher.getPublisherId());
            stmt.setInt(3, authorPublisher.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete
    public void deleteAuthorPublisher(int id) {
        String sql = "DELETE FROM AuthorPublisher WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
