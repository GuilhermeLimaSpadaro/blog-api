package com.gspadaro.blogapi.domain;

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
        if (text == null) {
            throw new IllegalArgumentException("Text cannot be null");
        }
        this.text = text;
        if (date == null){
            throw new IllegalArgumentException("Date cannot be null");
        }
        this.date = date;
        if (author == null){
            throw new IllegalArgumentException("Author cannot be null");
        }
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
