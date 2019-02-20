package com.example.admin.youtubev3.Model;

public class ChannelYoutube {
    private String id, title, description, publicAt, thumnail, subscriberCount, videocount;

    public ChannelYoutube() {
    }

    public ChannelYoutube(String id, String title, String description, String publicAt, String thumnail, String subscriberCount, String videocount) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publicAt = publicAt;
        this.thumnail = thumnail;
        this.subscriberCount = subscriberCount;
        this.videocount = videocount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPublicAt() {
        return publicAt;
    }

    public void setPublicAt(String publicAt) {
        this.publicAt = publicAt;
    }

    public String getThumnail() {
        return thumnail;
    }

    public void setThumnail(String thumnail) {
        this.thumnail = thumnail;
    }

    public String getSubscriberCount() {
        return subscriberCount;
    }

    public void setSubscriberCount(String subscriberCount) {
        this.subscriberCount = subscriberCount;
    }

    public String getVideocount() {
        return videocount;
    }

    public void setVideocount(String videocount) {
        this.videocount = videocount;
    }
}
