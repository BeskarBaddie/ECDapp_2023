package com.honours.ecd_2023;

import android.net.Uri;

public class Video {

    private String Title;
    private byte[]  FileURL;
    private String search;

    private String Topics;

    private String Tags;

    public String getTopics() {
        return Topics;
    }

    public void setTopics(String Topics) {
        this.Topics = Topics;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String Tags) {
        this.Tags = Tags;
    }

    public Video(String title, byte[]  fileURL, String tags, String topics) {
        this.Title = title;
        this.FileURL = fileURL;
        this.Tags = tags;
        this.Topics = topics;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public byte[]  getFileURL() {
        return FileURL;
    }

    public void setFileURL(byte[] FileURL) {
        this.FileURL = FileURL;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
