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

public class AddPhotoActivity extends AppCompatActivity {

    private ImageView photoSource;
    private EditText addTitleEditText, addDescriptionEditText;
    private Button addButton;
    private Bitmap selectedImage;
    private Bitmap scaledImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        getSupportActionBar().setTitle("Add Photo");

        photoSource = findViewById(R.id.add_photo_source);
        addTitleEditText = findViewById(R.id.add_photo_title);
        addDescriptionEditText = findViewById(R.id.add_photo_description);
        addButton = findViewById(R.id.add_photo_button);

        photoSource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddPhotoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(AddPhotoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                }else{
                    Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(photoIntent, 2);
                }

            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedImage == null){
                    Toast.makeText(AddPhotoActivity.this, "Please select a photo", Toast.LENGTH_SHORT);
                }else {
                    String photoTitle = addTitleEditText.getText().toString();
                    String photoDescription = addDescriptionEditText.getText().toString();
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    scaledImage = makeSmall(selectedImage, 300);
                    scaledImage.compress(Bitmap.CompressFormat.PNG, 50, outputStream);
                    byte[] image = outputStream.toByteArray();

                    Intent i = new Intent();
                    i.putExtra("photoTitle", photoTitle);
                    i.putExtra("photoDescription", photoDescription);
                    i.putExtra("image", image);
                    setResult(RESULT_OK, i);
                    finish();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode ==1 && grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(photoIntent, 1);
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == 2 && resultCode ==RESULT_OK && data != null){
            try {
                if (Build.VERSION.SDK_INT >=28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(), data.getData());
                    selectedImage = ImageDecoder.decodeBitmap(source);
                }else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                }
                photoSource.setImageBitmap(selectedImage);
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
}