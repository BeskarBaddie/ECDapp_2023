package com.honours.ecd_2023;

import android.net.Uri;

public class Video {

    private String title;
    private String file;
    private String search;

    private String topics;

    private String tag;

    public String getTopics() {
        return topics;
    }

    public void setTopics(String topics) {
        this.topics = topics;
    }

    public String getTags() {
        return tag;
    }

    public void setTags(String tag) {
        this.tag = tag;
    }

    public Video(String title, String  file, String tag, String topics) {
        this.title = title;
        this.file = file;
        this.tag = tag;
        this.topics = topics;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String  getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
