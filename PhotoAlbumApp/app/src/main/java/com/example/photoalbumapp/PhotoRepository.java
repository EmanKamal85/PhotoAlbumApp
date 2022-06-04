package com.example.photoalbumapp;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PhotoRepository {

    private PhotoDao photoDao;
    private LiveData<List<Photo>> photos;

    public PhotoRepository(Application application) {
        PhotoDatabase database = PhotoDatabase.getInstance(application);
        photoDao = database.photoDao();
        photos = photoDao.getAllPhotos();
    }

    public void insert(Photo photo){
        new InsertPhotoAsync(photoDao).execute(photo);
    }

    public void delete(Photo photo){
        new DeletePhotoAsync(photoDao).execute(photo);
    }

    public void update(Photo photo){
        new UpdateAsync(photoDao).execute(photo);
    }

    public LiveData<List<Photo>> getAllPhotos(){
        return photos;
    }

    private static class InsertPhotoAsync extends AsyncTask<Photo, Void, Void>{

        PhotoDao photoDao;

        private InsertPhotoAsync(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.insert(photos[0]);
            return null;
        }
    }

    private static class DeletePhotoAsync extends AsyncTask<Photo, Void, Void>{

        PhotoDao photoDao;

        public DeletePhotoAsync(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.delete(photos[0]);
            return null;
        }
    }

    private static class UpdateAsync extends AsyncTask<Photo, Void, Void>{

        PhotoDao photoDao;

        public UpdateAsync(PhotoDao photoDao) {
            this.photoDao = photoDao;
        }

        @Override
        protected Void doInBackground(Photo... photos) {
            photoDao.update(photos[0]);
            return null;
        }
    }
}
