package io.github.mellamopablo.youtuberssbrowser.model;

import android.graphics.Bitmap;

public class Video {
    private String id;
    private String title;
    private String description;
    private Bitmap thumbnail;

    public Video(String id, String title, String description, Bitmap thumbnail) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.thumbnail = thumbnail;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }
}
