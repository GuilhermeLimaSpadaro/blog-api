package com.gspadaro.blogapi.domain;

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
    private String authorId;

    public Post() {
    }

    public Post(String id, Instant date, String title, String body, String authorId) {
        this.id = id;
        this.date = date;
        if (title == null) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        this.title = title;
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
        this.body = body;
        if (authorId == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        this.authorId = authorId;
    }

    public Post(String title, String body, String author) {
        this(null, null, title, body, author);
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

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
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
