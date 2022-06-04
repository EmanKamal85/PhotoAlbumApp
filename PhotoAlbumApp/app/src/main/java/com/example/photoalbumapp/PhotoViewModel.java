package com.example.photoalbumapp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoViewModel extends AndroidViewModel {
    private PhotoRepository photoRepository;
    private LiveData<List<Photo>> photos;
    public PhotoViewModel(@NonNull Application application) {
        super(application);
        photoRepository = new PhotoRepository(application);
        photos = photoRepository.getAllPhotos();
    }

    public void insert(Photo photo){
        photoRepository.insert(photo);
    }

    public void delete(Photo photo){
        photoRepository.delete(photo);
    }

    public void update(Photo photo){
        photoRepository.update(photo);
    }

    public LiveData<List<Photo>> getAllPhotos(){
        return photos;
    }
}
