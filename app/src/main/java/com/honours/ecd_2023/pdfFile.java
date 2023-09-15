package com.honours.ecd_2023;

/**
 * Represents a PDF file with a file name and a URL.
 */
public class pdfFile {

    private String fileName;
    private String url;

    /**
     * Constructs a new pdfFile instance with the given file name and URL.
     *
     * @param fileName The name of the PDF file.
     * @param url      The URL where the PDF file is located.
     */
    public pdfFile(String fileName, String url) {
        this.fileName = fileName;
        this.url = url;
    }

    /**
     * Default constructor required for Firebase deserialization.
     */
    public pdfFile() {
        // Default constructor required for Firebase deserialization
    }

    /**
     * Gets the file name of the PDF.
     *
     * @return The file name of the PDF.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Sets the file name of the PDF.
     *
     * @param fileName The file name of the PDF.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Gets the URL where the PDF file is located.
     *
     * @return The URL of the PDF file.
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets the URL where the PDF file is located.
     *
     * @param url The URL of the PDF file.
     */
    public void setUrl(String url) {
        this.url = url;
    }
}
