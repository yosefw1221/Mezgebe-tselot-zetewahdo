package com.josystems.ethiopian.ortodox.model;

import java.io.Serializable;

public class Chapter implements Serializable {
    private String chapterName;
    private String chapterData;

    public Chapter(String chapterName, String chapterData) {
        this.chapterName = chapterName;
        this.chapterData = chapterData;
    }
    public Chapter(){

    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getChapterData() {
        return chapterData;
    }

    public void setChapterData(String chapterData) {
        this.chapterData = chapterData;
    }
}
