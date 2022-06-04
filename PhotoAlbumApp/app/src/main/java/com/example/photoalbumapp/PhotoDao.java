package com.example.photoalbumapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PhotoDao {

    @Insert
    void insert(Photo photo);
    @Delete
    void delete(Photo photo);
    @Update
    void update(Photo photo);
    @Query("Select *from photo_table Order by id ASC")
    LiveData<List<Photo>>getAllPhotos();
}
