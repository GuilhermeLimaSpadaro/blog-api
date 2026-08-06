package com.gspadaro.blogapi.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Document(collection = "post")
public class Post implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    private String id;
    private LocalDateTime date;
    private String title;
    private String body;
    @DBRef
    private User author;
    private List<Comment> comments = new ArrayList<>();

    public Post() {
    }

    public Post(String id, LocalDateTime date, String title, String body, User author) {
        this.id = id;
        if (date == null) {
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
        if (title == null ) {
            throw new IllegalArgumentException("Title cannot be null");
        }
        this.title = title;
        if (body == null) {
            throw new IllegalArgumentException("Body cannot be null");
        }
        this.body = body;
        if (author == null) {
            throw new IllegalArgumentException("Author cannot be null");
        }
        this.author = author;
    }

    public Post(String title, String body, User author) {
        this(null, null, title, body, author);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
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
