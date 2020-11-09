package com.josystems.ethiopian.ortodox.model;

import java.util.List;

public class Content {
    private List<Chapter> contents;

    public Content(List<Chapter> contents) {
        this.contents = contents;
    }
    public Content(){
    }

    public Chapter getChapter(int chapter) {
        return contents.get(chapter);
    }

    public int size() {
        return contents.size();
    }

    public List<Chapter> getContents() {
        return contents;
    }

    public void setContents(List<Chapter> contents) {
        this.contents = contents;
    }
}
