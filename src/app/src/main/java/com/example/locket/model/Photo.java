package com.example.locket.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Photo {
    private String photoId;
    private String userId;
    private String imageUrl;
    private String caption;
    private String musicUrl;
    private String location;
    private boolean isAd; // Thuộc tính để đánh dấu quảng cáo

    @ServerTimestamp
    private Date createdAt;

    public Photo() {
    }

    public Photo(String photoId, String userId, String imageUrl, String caption, String musicUrl, String location) {
        this.photoId = photoId;
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.caption = caption;
        this.musicUrl = musicUrl;
        this.location = location;
        this.isAd = false; // Mặc định không phải quảng cáo
    }

    // Constructor cho quảng cáo
    public Photo(String imageUrl) {
        this.photoId = "ad_" + System.currentTimeMillis(); // ID duy nhất cho quảng cáo
        this.userId = "";
        this.imageUrl = imageUrl;
        this.caption = "";
        this.musicUrl = "";
        this.location = "";
        this.isAd = true; // Đánh dấu là quảng cáo
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl != null ? imageUrl.replace("http://", "https://") : null;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAd() {
        return isAd;
    }

    public void setAd(boolean isAd) {
        this.isAd = isAd;
    }
}