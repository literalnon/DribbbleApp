package com.example.donald.testapplication.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.donald.testapplication.R;
import com.example.donald.testapplication.Adapter.RecycleImagesAdapter;

public class ImageLookActivity extends AppCompatActivity {

    private ImageView image;
    private TextView title;
    private TextView description;
    private ProgressBar progressLoad;

    private int curPosition;
    private float fromPosition;
    private boolean oneFinger = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_look);

        curPosition = getIntent().getExtras()
                .getInt(RecycleImagesAdapter.INT_POSITION);

        image = (ImageView) findViewById(R.id.ivNormalImage);
        title = (TextView) findViewById(R.id.tvTitle);
        description = (TextView) findViewById(R.id.tvDiscription);
        progressLoad = (ProgressBar) findViewById(R.id.pbLoadong);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        image.setMaxHeight(displayMetrics.heightPixels / 2);

        loadImage(curPosition);

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getActionMasked())
                {
                    case MotionEvent.ACTION_DOWN: // Пользователь нажал на экран, т.е. начало движения
                        oneFinger = true;
                        fromPosition = event.getX();
                        Log.d("Finger first", oneFinger + "");
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN: // MultiTouch, потенциально увеличение изображения)
                        oneFinger = false;
                        Log.d("Finger no first", oneFinger + "");
                        break;
                    case MotionEvent.ACTION_UP: // Пользователь отпустил экран, т.е. окончание движения
                        if(oneFinger) {
                            float toPosition = event.getX();
                            if (fromPosition - toPosition > getResources().getConfiguration().screenWidthDp / 10)
                                loadImage(curPosition = (curPosition + 1) % MainActivity.DEFAULT_IMAGES_NUMBER);
                            else if (toPosition - fromPosition > getResources().getConfiguration().screenWidthDp / 10)
                                loadImage(curPosition = (curPosition - 1) % MainActivity.DEFAULT_IMAGES_NUMBER);
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void loadImage(int position){
        RequestListener r_listener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressLoad.setAlpha(0);
                return false;
            }
        };

        progressLoad.setAlpha(1);

        Glide
                .with(this)
                .load(MainActivity.customImages.get(position).getNormalUrl())
                .listener(r_listener)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(R.anim.animation)
                .into(image);

        title.setText(MainActivity.customImages.get(curPosition).getTitle());
        description.setText(MainActivity.customImages.get(curPosition).getDescription());
    }
}
