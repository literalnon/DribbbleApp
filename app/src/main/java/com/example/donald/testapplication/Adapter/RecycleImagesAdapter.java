package com.example.donald.testapplication.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.donald.testapplication.Activity.ImageLookActivity;
import com.example.donald.testapplication.Activity.MainActivity;
import com.example.donald.testapplication.R;

/**
 * Created by Donald on 23.06.2017.
 */
public class RecycleImagesAdapter extends RecyclerView.Adapter<RecycleImagesAdapter.ViewHolder> {

    public static final String INT_POSITION = "position";
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageTeaser;
        public ViewHolder(View itemView) {
            super(itemView);
            imageTeaser = (ImageView)itemView.findViewById(R.id.img_teaser);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_image_teaser, parent, false);

        ViewHolder viewHolder = new ViewHolder(inflateView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide
                .with(context)
                .load(MainActivity.customImages.get(position).getTeaserUrl())
                .placeholder(R.drawable.loading)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageTeaser);

        holder.imageTeaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageLookActivity.class);
                intent.putExtra(INT_POSITION, position);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return MainActivity.customImages.size();
    }

    public RecycleImagesAdapter(Context context){
        this.context = context;
    }


}
