package com.example.donald.testapplication;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.donald.testapplication.Presenter.BasePresenter;

/**
 * Created by bloold on 06.07.17.
 */

public class ImagesViewHolder<Presenter extends BasePresenter> extends RecyclerView.ViewHolder {

    protected Presenter presenter;

    public ImageView imageTeaser;

    public ImagesViewHolder(View itemView) {
        super(itemView);
        imageTeaser = (ImageView)itemView.findViewById(R.id.img_teaser);
    }

}
