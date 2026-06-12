package com.gspadaro.blogapi.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class Comment implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String text;
    private LocalDate date;

    private User author;

    public Comment() {
    }

    public Comment(String text, LocalDate date, User author) {
        this.text = text;
        this.date = date;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}
