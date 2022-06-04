package com.example.photoalbumapp;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "photo_table")
public class Photo {

    @PrimaryKey(autoGenerate = true)
    int id;
    //int photoSource;
    private byte[] photoSource;
    private String photoTitle;
    private String photoDescription;

    public Photo(byte[] photoSource, String photoTitle, String photoDescription) {
        this.photoSource = photoSource;
        this.photoTitle = photoTitle;
        this.photoDescription = photoDescription;
    }

    public int getId() {
        return id;
    }

    public byte[] getPhotoSource() {
        return photoSource;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setId(int id) {
        this.id = id;
    }
}
