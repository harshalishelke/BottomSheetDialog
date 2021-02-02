package com.balance.bottomsheetdialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter  extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    List<String> data = new ArrayList<>();

    public ImageAdapter(){

    }


    @NonNull
    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image,null) );
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.MyViewHolder holder, int position) {


//        File imgFile = new File(data.get(position));
//        if (imgFile.exists()){
//            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//
//            holder.imageView.setImageBitmap(bitmap);
//        }

        Glide.with(holder.itemView.getContext())
                .applyDefaultRequestOptions(new RequestOptions().override(150,150))
                .load(data.get(position))
                .centerCrop()
                .into(holder.imageView);
    }


    public void setData(List<String> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {

        return data!=null ? data.size() : 0 ;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView imageView;
        TextView title;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_view);
            title = itemView.findViewById(R.id.title);
        }
    }
}
