package com.honours.ecd_2023;

public class Video {

    private String Title;
    private String FileURL;
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

    public Video(){


    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getFileURL() {
        return FileURL;
    }

    public void setFileURL(String FileURL) {
        this.FileURL = FileURL;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
