package siperpus.model;

import java.sql.Timestamp;

public class Book {
    private int id;
    private String title;
    private String callNumber;
    private int stock;
    private int authorPublisherId;
    private String authorName;      // New field
    private String publisherName;   // New field
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // Getters and setters for all fields including the new ones
    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }
    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public int getAuthorPublisherId() {
        return authorPublisherId;
    }

    public void setAuthorPublisherId(int authorPublisherId) {
        this.authorPublisherId = authorPublisherId;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getStock() {
        return stock;
    }
}
