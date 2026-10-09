package br.com.gspadaro.blogapi.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

@Document(collection = "post")
public class Post implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private Instant date = Instant.now();
    private String title;
    private String body;
    private String userId;

    public Post() {
    }

    public Post(Instant date, String title, String body, String userId) {
        this.date = date;
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        this.title = title;
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
        this.body = body;
        if (userId == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        this.userId = userId;
    }

    public Post(String id, Instant date, String title, String body, String userId) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.body = body;
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(id, post.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
