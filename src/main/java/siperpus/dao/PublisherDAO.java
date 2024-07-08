package siperpus.dao;

import siperpus.database.Database;
import siperpus.model.Publisher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO {
    private Database database;

    public PublisherDAO(Database database) {
        this.database = database;
    }

    // Create
    public void addPublisher(Publisher publisher) {
        String sql = "INSERT INTO Publishers (name) VALUES (?)";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, publisher.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Read all
    public List<Publisher> getAllPublishers() {
        List<Publisher> publishers = new ArrayList<>();
        String sql = "SELECT * FROM Publishers";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Publisher publisher = new Publisher();
                publisher.setId(rs.getInt("id"));
                publisher.setName(rs.getString("name"));
                publisher.setCreatedAt(rs.getTimestamp("created_at"));
                publisher.setUpdatedAt(rs.getTimestamp("updated_at"));
                publishers.add(publisher);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return publishers;
    }

    // Read by ID
    public Publisher getPublisherById(int id) {
        String sql = "SELECT * FROM Publishers WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Publisher publisher = new Publisher();
                    publisher.setId(rs.getInt("id"));
                    publisher.setName(rs.getString("name"));
                    publisher.setCreatedAt(rs.getTimestamp("created_at"));
                    publisher.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return publisher;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Read by Name
    public Publisher getPublisherByName(String name) {
        String sql = "SELECT * FROM Publishers WHERE name = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Publisher publisher = new Publisher();
                    publisher.setId(rs.getInt("id"));
                    publisher.setName(rs.getString("name"));
                    publisher.setCreatedAt(rs.getTimestamp("created_at"));
                    publisher.setUpdatedAt(rs.getTimestamp("updated_at"));
                    return publisher;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    // Update
    public void updatePublisher(Publisher publisher) {
        String sql = "UPDATE Publishers SET name = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, publisher.getName());
            stmt.setInt(2, publisher.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Delete
    public void deletePublisher(int id) {
        String sql = "DELETE FROM Publishers WHERE id = ?";
        try (Connection connection = database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
