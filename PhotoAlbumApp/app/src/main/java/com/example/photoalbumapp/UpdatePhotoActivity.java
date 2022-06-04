package com.example.photoalbumapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class UpdatePhotoActivity extends AppCompatActivity {

    private ImageView updatePhotoSourceImageView;
    private EditText updateTitleEditText, updateDescriptionEditText;
    private Button updateButton;
    private Bitmap updatedSelectedImage;
    private Bitmap updatedScaledImage;
    int photoId;
    byte[] updatedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_photo);
        getSupportActionBar().setTitle("Update Photo");

        updatePhotoSourceImageView = findViewById(R.id.update_photo_source);
        updateTitleEditText = findViewById(R.id.update_photo_title);
        updateDescriptionEditText = findViewById(R.id.update_photo_description);
        updateButton = findViewById(R.id.update_photo_button);

        getData();


        updatePhotoSourceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(UpdatePhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                        != PackageManager.PERMISSION_GRANTED){
//                    ActivityCompat.requestPermissions(UpdatePhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//                }else{
                    Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(photoIntent, 5);
//                }


            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (photoId == -1){
                    Toast.makeText(UpdatePhotoActivity.this, "There is a problem!", Toast.LENGTH_SHORT);
                }else{
                    String photoTitle = updateTitleEditText.getText().toString();
                    String photoDescription = updateDescriptionEditText.getText().toString();
                    Intent i = new Intent();
                    i.putExtra("updatedPhotoTitle", photoTitle);
                    i.putExtra("updatedPhotoDescription", photoDescription);
                    i.putExtra("photoId", photoId);

                    if (updatedSelectedImage == null){
                        i.putExtra("updatedPhotoSource", updatedImage);
                    }else {

                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        updatedScaledImage = makeSmall(updatedSelectedImage, 300);
                        updatedScaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                        byte[] image = outputStream.toByteArray();
                        i.putExtra("updatedPhotoSource", image);
                    }
                    setResult(RESULT_OK, i);
                    finish();

                }


            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 5 && resultCode ==RESULT_OK && data != null){
            try {
                if (Build.VERSION.SDK_INT >=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), data.getData());
                    updatedSelectedImage = ImageDecoder.decodeBitmap(source);
                }else {
                    updatedSelectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                }
                updatePhotoSourceImageView.setImageBitmap(updatedSelectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public Bitmap makeSmall(Bitmap image, int maxSize){
        int width = image.getWidth();
        int height = image.getHeight();

        float ratio = (float)width / (float)height;
        if (ratio>1){
            width = maxSize;
            height = (int)(width/ratio);
        }else {
            height = maxSize;
            width = (int)(height * ratio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void getData(){
    Intent i = getIntent();
    photoId = i.getIntExtra("photoId", -1);
    String updatedPhotoTitle = i.getStringExtra("photoTitle");
    String updatedPhotoDescription = i.getStringExtra("photoDescription");
     updatedImage = i.getByteArrayExtra("photoSource");

    updateTitleEditText.setText(updatedPhotoTitle);
    updateDescriptionEditText.setText(updatedPhotoDescription);
    updatePhotoSourceImageView.setImageBitmap(BitmapFactory.decodeByteArray(updatedImage, 0, updatedImage.length));

    }
}