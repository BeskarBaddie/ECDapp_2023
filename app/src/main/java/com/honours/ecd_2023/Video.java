package com.honours.ecd_2023;

/**
 * Represents a video entity with various attributes.
 */
public class Video {

    private String title;
    private String file;
    private String search;
    private String topics;
    private String tag;
    private String language;

    /**
     * Constructs a new Video object with the given attributes.
     *
     * @param title    The title of the video.
     * @param file     The file associated with the video.
     * @param tag      The tag or type of the video (e.g., "video" or "pdf").
     * @param topics   The topics or categories associated with the video.
     * @param language The language of the video.
     */
    public Video(String title, String file, String tag, String topics, String language) {
        this.title = title;
        this.file = file;
        this.tag = tag;
        this.topics = topics;
        this.language = language;
    }

    /**
     * Gets the title of the video.
     *
     * @return The title of the video.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the video.
     *
     * @param title The title to set for the video.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the file associated with the video.
     *
     * @return The file associated with the video.
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets the file associated with the video.
     *
     * @param file The file to set for the video.
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Gets the search attribute of the video.
     *
     * @return The search attribute of the video.
     */
    public String getSearch() {
        return search;
    }

    /**
     * Sets the search attribute of the video.
     *
     * @param search The search attribute to set for the video.
     */
    public void setSearch(String search) {
        this.search = search;
    }

    /**
     * Gets the topics or categories associated with the video.
     *
     * @return The topics or categories associated with the video.
     */
    public String getTopics() {
        return topics;
    }

    /**
     * Sets the topics or categories associated with the video.
     *
     * @param topics The topics to set for the video.
     */
    public void setTopics(String topics) {
        this.topics = topics;
    }

    /**
     * Gets the tag or type of the video.
     *
     * @return The tag or type of the video.
     */
    public String getTags() {
        return tag;
    }

    /**
     * Sets the tag or type of the video.
     *
     * @param tag The tag to set for the video.
     */
    public void setTags(String tag) {
        this.tag = tag;
    }

    /**
     * Gets the language of the video.
     *
     * @return The language of the video.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the language of the video.
     *
     * @param language The language to set for the video.
     */
    public void setLanguage(String language) {
        this.language = language;
    }
}
