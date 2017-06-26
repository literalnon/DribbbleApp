package com.example.donald.testapplication.Activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.agilie.dribbblesdk.domain.Shot;
import com.agilie.dribbblesdk.service.auth.AuthCredentials;
import com.agilie.dribbblesdk.service.auth.DribbbleAuthHelper;
import com.agilie.dribbblesdk.service.retrofit.DribbbleServiceGenerator;
import com.agilie.dribbblesdk.service.retrofit.services.DribbbleShotsService;
import com.example.donald.testapplication.Data.CustomImage;
import com.example.donald.testapplication.DB.DBCustomViews;
import com.example.donald.testapplication.Fragment.ImagesListFragment;
import com.example.donald.testapplication.R;
import com.google.api.client.auth.oauth2.Credential;

import java.util.List;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String DRIBBBLE_CLIENT_ID = "43fcf3e1bd1e06e5b7ee3795131939b2490435516363294023ba2ad3df188986";
    private static final String DRIBBBLE_CLIENT_SECRET = "c5ad03eae740e0c7ed5c2545e48e432246d1c40e3a972968f4ffdc076742df85";
    private static final String DRIBBBLE_CLIENT_ACCESS_TOKEN = "d8c102c940f35cf8eba2e9dba8f6644485f6d82e6ca9ba58909e4110f7b8472a";
    private static final String DRIBBBLE_CLIENT_REDIRECT_URL = "https://dribbble.com/DonaldDuck";
    public static final int DEFAULT_IMAGES_NUMBER = 51;
    public static Vector<CustomImage> customImages;

    private Context context;
    private ProgressBar progressBarListLoad;
    private RelativeLayout mainLayout;

    private String token;
    public static int pageNumber = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        progressBarListLoad = (ProgressBar)findViewById(R.id.progressBar);
        mainLayout  = (RelativeLayout) findViewById(R.id.main_layout);

        customImages = new Vector<>();

        AuthCredentials credentials = AuthCredentials.newBuilder(
                DRIBBBLE_CLIENT_ID,
                DRIBBBLE_CLIENT_SECRET,
                DRIBBBLE_CLIENT_ACCESS_TOKEN,
                DRIBBBLE_CLIENT_REDIRECT_URL)
                .build();

        final SQLiteDatabase imagesBase = new DBCustomViews(context).getWritableDatabase();

        final Callback<List<Shot>> callback = new Callback<List<Shot>>() {
            @Override
            public void onResponse(Call<List<Shot>> call, Response<List<Shot>> response) {
                //get vector of custom_image and save in db
                for(int i = 0; i < response.body().size() && customImages.size() < DEFAULT_IMAGES_NUMBER; ++i){
                    if(!response.body().get(i).getAnimated()) {

                        CustomImage cImage = new CustomImage(
                                response.body().get(i).getTitle(),
                                response.body().get(i).getDescription(),
                                response.body().get(i).getImages());

                        customImages.add(cImage);

                        imagesBase.insert(DBCustomViews.NAME_TABLE, null, cImage.getContentValues());
                        Log.d("insert " + i, cImage.getNormalUrl());
                    }
                }

                startFragment();
            }

            @Override
            public void onFailure(Call<List<Shot>> call, Throwable t) {
                Log.e("FailureCallback", t.getMessage());
            }
        };

        DribbbleAuthHelper.startOauthDialog(this, credentials, new DribbbleAuthHelper.AuthListener() {

            @Override
            public void onSuccess(final Credential credential) {
                token = credential.getAccessToken();//get token

                Cursor cursor = imagesBase.query(DBCustomViews.NAME_TABLE, null, null, null, null, null, null);

                //if count db info less than DEFAULT_IMAGES_NUMBER - load data
                if (cursor.getCount() < DEFAULT_IMAGES_NUMBER) {
                    final DribbbleShotsService dribbbleShotsService = DribbbleServiceGenerator.getDribbbleShotService(token);
                    Call<List<Shot>> shotsCall = dribbbleShotsService.fetchShots(pageNumber, DEFAULT_IMAGES_NUMBER * 2);
                    shotsCall.enqueue(callback);
                    cursor.close();
                } else {
                    while (cursor.moveToNext()) {
                        customImages.add(new CustomImage(cursor));
                    }
                    startFragment();
                    cursor.close();
                }
                // Handle success login event
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                Toast.makeText(context, "Error: try authorize later", Toast.LENGTH_LONG).show();
                // Handle error here
            }
        });
        //  DribbbleAuthHelper.logout(this, credentials);
    }

    private void startFragment(){
        try {
            mainLayout.removeViewInLayout(progressBarListLoad);
        }catch (Exception e){
            Log.e("Remove View", e.getMessage());
            progressBarListLoad.setAlpha(0);
        }

        getFragmentManager().beginTransaction().add(R.id.frame_image_list, ImagesListFragment.newInstance(token)).commit();
    }

}
