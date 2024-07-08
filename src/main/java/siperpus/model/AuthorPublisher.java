package siperpus.model;

import java.util.Date;

public class AuthorPublisher {
    private int id;
    private int authorId;
    private int publisherId;
    private Date createdAt;
    private Date updatedAt;

    // Constructors, getters, and setters
    public AuthorPublisher() {}

    public AuthorPublisher(int id, int authorId, int publisherId, Date createdAt, Date updatedAt) {
        this.id = id;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
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
}
