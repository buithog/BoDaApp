package com.example.myapp.model;

import java.io.Serializable;

public class Voice implements Serializable {
    private String id;
    private String name;
    private String thumbnailUrl;
    private String mp3Url;
    private String description;
    private String location;

    public Voice(String name, String thumbnailUrl) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
    }

    public Voice() {}

    public String getMp3Url() {
        return mp3Url;
    }

    public void setMp3Url(String mp3Url) {
        this.mp3Url = mp3Url;
    }

    public Voice(String id, String name, String thumbnailUrl, String mp3Url, String description, String location) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.mp3Url = mp3Url;
        this.description = description;
        this.location = location;
    }

    public Voice(String name, String thumbnailUrl, String description, String location) {
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.location = location;
    }

    public Voice(String id, String name, String thumbnailUrl, String description, String location) {
        this.id = id;
        this.name = name;
        this.thumbnailUrl = thumbnailUrl;
        this.description = description;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
}
