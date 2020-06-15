package com.example.exoplayerexample.model;

import android.support.v4.media.MediaMetadataCompat;

import com.example.exoplayerexample.R;

import java.util.Date;

public class MediaDocument {

    private String fieldMediaId;
    private String fieldArtist;
    private String fieldTitle;
    private String fieldMediaUrl;
    private String fieldDescription;
    private Date fieldDateAdded;
    private String fieldArtistId;
    private String fieldMediaImage;
    private long fieldMediaDuration;


    public String getFieldMediaId() {
        return fieldMediaId;
    }

    public void setFieldMediaId(String fieldMediaId) {
        this.fieldMediaId = fieldMediaId;
    }

    public String getFieldArtist() {
        return fieldArtist;
    }

    public void setFieldArtist(String fieldArtist) {
        this.fieldArtist = fieldArtist;
    }

    public String getFieldTitle() {
        return fieldTitle;
    }

    public void setFieldTitle(String fieldTitle) {
        this.fieldTitle = fieldTitle;
    }

    public String getFieldMediaUrl() {
        return fieldMediaUrl;
    }

    public void setFieldMediaUrl(String fieldMediaUrl) {
        this.fieldMediaUrl = fieldMediaUrl;
    }

    public String getFieldDescription() {
        return fieldDescription;
    }

    public void setFieldDescription(String fieldDescription) {
        this.fieldDescription = fieldDescription;
    }

    public Date getFieldDateAdded() {
        return fieldDateAdded;
    }

    public void setFieldDateAdded(Date fieldDateAdded) {
        this.fieldDateAdded = fieldDateAdded;
    }

    public String getFieldArtistId() {
        return fieldArtistId;
    }

    public String getFieldMediaImage() {
        return fieldMediaImage;
    }

    public void setFieldMediaImage(String fieldMediaImage) {
        this.fieldMediaImage = fieldMediaImage;
    }

    public void setFieldArtistId(String fieldArtistId) {
        this.fieldArtistId = fieldArtistId;
    }

    public long getFieldMediaDuration() {
        return fieldMediaDuration;
    }

    public void setFieldMediaDuration(long fieldMediaDuration) {
        this.fieldMediaDuration = fieldMediaDuration;
    }
}
