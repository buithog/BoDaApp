package com.example.myapp.model;


public class Article {

    private String title;
    private String author;
    private String thumbnailUrl;
    private String content;

    public Article(String title, String author, String thumbnailUrl, String content) {
        this.title = title;
        this.author = author;
        this.thumbnailUrl = thumbnailUrl;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Article(String title, String author, String thumbnailUrl) {
        this.title = title;
        this.author = author;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}