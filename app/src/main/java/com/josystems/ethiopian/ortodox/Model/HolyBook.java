
package com.josystems.ethiopian.ortodox.Model;

import androidx.recyclerview.widget.RecyclerView;

public class HolyBook {
    private String id;
    private String iconUrl;
    private String title;
    private String description;
    private String language;
    private String dataString;
    private String downloadUrl;
    private String tags;

    public HolyBook(String id, String iconUrl, String title, String description, String language, String dataString) {
        this.id = id;
        this.iconUrl = iconUrl;
        this.title = title;
        this.description = description;
        this.language = language;
        this.dataString = dataString;
    }
    public HolyBook(){
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
