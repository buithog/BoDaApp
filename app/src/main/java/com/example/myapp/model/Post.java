package com.example.myapp.model;

import java.util.List;

public class Post {
    private int id;
    private String author;
    private String title;
    private String thumnailPath;
    private List<String> content;
    private List<String> imagePath;
    private List<Integer> positionImg;

    // Getter và Setter cho các trường
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getThumnailPath() { return thumnailPath; }
    public void setThumnailPath(String thumnailPath) { this.thumnailPath = thumnailPath; }

    public List<String> getContent() { return content; }
    public void setContent(List<String> content) { this.content = content; }

    public List<String> getImagePath() { return imagePath; }
    public void setImagePath(List<String> imagePath) { this.imagePath = imagePath; }

    public List<Integer> getPositionImg() { return positionImg; }
    public void setPositionImg(List<Integer> positionImg) { this.positionImg = positionImg; }
}
