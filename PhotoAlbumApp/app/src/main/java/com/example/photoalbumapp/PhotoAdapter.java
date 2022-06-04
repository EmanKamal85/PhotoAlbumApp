package com.example.photoalbumapp;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private List<Photo> photos = new ArrayList<>();
    private OnPhotoClickListener listener;

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    public Photo getPhoto(int position){
        return photos.get(position);
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_card, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        //holder.photoImageView(photos.get(position).photoSource);
        holder.titleTextView.setText(photos.get(position).getPhotoTitle());
        holder.descriptionTexView.setText(photos.get(position).getPhotoDescription());
        holder.photoImageView.setImageBitmap(BitmapFactory.decodeByteArray(photos.get(position).getPhotoSource(),
                0, photos.get(position).getPhotoSource().length));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                if (listener != null && position!=RecyclerView.NO_POSITION){
                    listener.onPhotoClick(photos.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    public static class PhotoViewHolder extends RecyclerView.ViewHolder{
        ImageView photoImageView;
        TextView titleTextView, descriptionTexView;

    public PhotoViewHolder(@NonNull View itemView) {
        super(itemView);
        photoImageView = itemView.findViewById(R.id.card_photo_source);
        titleTextView = itemView.findViewById(R.id.photo_title);
        descriptionTexView = itemView.findViewById(R.id.photo_description);
    }
}

    public interface OnPhotoClickListener{
         void onPhotoClick(Photo photo);
    }

    public void setListener(OnPhotoClickListener listener) {
        this.listener = listener;
    }

//    public void setOnPhotoClickListener(OnPhotoClickListener listener){
//        this.listener = listener;
//
//    }
}


