package com.example.donald.testapplication.Adapter;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.prefill.PreFillType;
import com.example.donald.testapplication.Fragment.NormalImageFragment;
import com.example.donald.testapplication.ImagesViewHolder;
import com.example.donald.testapplication.Presenter.ListImagePresenter;
import com.example.donald.testapplication.R;

/**
 * Created by Donald on 23.06.2017.
 */
public class RecycleImagesAdapter extends RecyclerView.Adapter<ImagesViewHolder> {

    public static final String INT_POSITION = "position";
    private final ListImagePresenter presenter;
    private Context context;

    @Override
    public ImagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflateView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_image_teaser, parent, false);

        ImagesViewHolder viewHolder = new ImagesViewHolder<>(inflateView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImagesViewHolder holder, final int position) {

        Glide
                .with(context)
                .load(presenter.getTeaserUrl(position))
                .placeholder(R.drawable.loading)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageTeaser);

        holder.imageTeaser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new NormalImageFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(INT_POSITION, position);
                fragment.setArguments(bundle);
                presenter.startFragment(fragment);
            }
        });
    }

    @Override
    public int getItemCount() {
        return presenter.getImagesSize();
    }

    public RecycleImagesAdapter(Context context, ListImagePresenter presenter){
        this.context = context;
        this.presenter = presenter;
    }


}
