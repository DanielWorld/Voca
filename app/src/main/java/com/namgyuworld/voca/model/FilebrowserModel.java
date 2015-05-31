package com.namgyuworld.voca.model;

/**
 * Created by danielpark on 5/31/15.
 */
public class FilebrowserModel {


    private String fileName;
    private long modifyDate;
    private long size;
    private boolean isDirectory;
    private String filePath;

    /**
     * @param fileName    the name of file
     * @param modifyDate  the last modified date
     * @param size        the length of file
     * @param isDirectory is this file?
     */
    public FilebrowserModel(String fileName, long modifyDate, long size, boolean isDirectory, String filePath) {
        this.fileName = fileName;
        this.modifyDate = modifyDate;
        this.size = size;
        this.isDirectory = isDirectory;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public boolean isDirectory() {
        return isDirectory;
    }

    public void setDirectory(boolean isDirectory) {
        this.isDirectory = isDirectory;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
