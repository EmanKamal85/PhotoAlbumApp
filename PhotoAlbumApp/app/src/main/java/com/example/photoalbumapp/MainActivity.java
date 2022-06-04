package com.example.photoalbumapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private PhotoViewModel photoViewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.photo_recycler_view);
        fab = findViewById(R.id.fab);

        PhotoAdapter adapter = new PhotoAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        photoViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                .create(PhotoViewModel.class);
        photoViewModel.getAllPhotos().observe(this, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
            adapter.setPhotos(photos);

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPhotoActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT){
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                photoViewModel.delete(adapter.getPhoto(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(recyclerView);


        adapter.setListener(new PhotoAdapter.OnPhotoClickListener() {
            @Override
            public void onPhotoClick(Photo photo) {
                Intent intent = new Intent(MainActivity.this, UpdatePhotoActivity.class);
                intent.putExtra("photoId", photo.getId());
                intent.putExtra("photoTitle", photo.getPhotoTitle());
                intent.putExtra("photoDescription", photo.getPhotoDescription());
                intent.putExtra("photoSource", photo.getPhotoSource());
                startActivityForResult(intent, 4);

            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3 && resultCode == RESULT_OK && data != null){
            String  photoTitle = data.getStringExtra("photoTitle");
            String photoDescription = data.getStringExtra("photoDescription");
            byte [] image = data.getByteArrayExtra("image");
            Photo photo = new Photo(image, photoTitle, photoDescription);
            photoViewModel.insert(photo);
        }
        if (requestCode == 4 && resultCode == RESULT_OK && data != null){
            String updatedPhotoTitle = data.getStringExtra("updatedPhotoTitle");
            String updatedPhotoDescription = data.getStringExtra("updatedPhotoDescription");
            int id = data.getIntExtra("photoId", -1);
            byte [] updatedImage = data.getByteArrayExtra("updatedPhotoSource");
            Photo photo = new Photo(updatedImage, updatedPhotoTitle, updatedPhotoDescription);
            photo.setId(id);
            photoViewModel.update(photo);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}