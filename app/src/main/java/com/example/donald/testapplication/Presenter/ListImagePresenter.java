package com.example.donald.testapplication.Presenter;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.agilie.dribbblesdk.domain.Shot;
import com.agilie.dribbblesdk.service.auth.AuthCredentials;
import com.agilie.dribbblesdk.service.retrofit.DribbbleServiceGenerator;
import com.agilie.dribbblesdk.service.retrofit.services.DribbbleShotsService;
import com.example.donald.testapplication.Data.CustomImage;
import com.example.donald.testapplication.MainView;
import com.example.donald.testapplication.Model;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by bloold on 06.07.17.
 */

public class ListImagePresenter extends BasePresenter<Model, MainView> implements Parcelable{

    FragmentActivity activity;

    public ListImagePresenter(FragmentActivity activity){
        this.activity = activity;
        model = new Model(activity);
    }

    protected ListImagePresenter(Parcel in) {
        model = (Model)in.readValue(Model.class.getClassLoader());
        view = (MainView) in.readValue(MainView.class.getClassLoader());
        activity = (FragmentActivity)in.readValue(FragmentActivity.class.getClassLoader());

    }

    public static final Creator<ListImagePresenter> CREATOR = new Creator<ListImagePresenter>() {
        @Override
        public ListImagePresenter createFromParcel(Parcel in) {
            return new ListImagePresenter(in);
        }

        @Override
        public ListImagePresenter[] newArray(int size) {
            return new ListImagePresenter[size];
        }
    };

    public void getShots(final String token){
        model.setToken(token);

        final Callback<List<Shot>> callback = new Callback<List<Shot>>() {
            @Override
            public void onResponse(Call<List<Shot>> call, Response<List<Shot>> response) {
                //get vector of custom_image and save in db
                for(int i = 0; i < response.body().size() && model.getImagesSize() < model.getDefaultImagesNumber(); ++i){
                    //if(!response.body().get(i).getAnimated()) {

                        CustomImage cImage = new CustomImage(
                                response.body().get(i).getTitle(),
                                response.body().get(i).getDescription(),
                                response.body().get(i).getImages());

                        model.addImage(cImage);

                        model.imageBaseInsert(cImage.getContentValues());
                        Log.d("downloading " + i, cImage.getNormalUrl());
                    //}
                }

                view.startFragment();
            }

            @Override
            public void onFailure(Call<List<Shot>> call, Throwable t) {
                Log.e("FailureCallback", t.getMessage());
            }
        };

        Cursor cursor = model.getTableData();

        //if count db info less than DEFAULT_IMAGES_NUMBER - load data
        if (cursor.getCount() < model.getDefaultImagesNumber()) {
            final DribbbleShotsService dribbbleShotsService = DribbbleServiceGenerator.getDribbbleShotService(token);
            Call<List<Shot>> shotsCall = dribbbleShotsService.fetchShots(model.getPageNumber(), model.getDefaultImagesNumber());
            shotsCall.enqueue(callback);
            cursor.close();
        } else if (!model.isFullImages()){
            int i = 0;
            while (cursor.moveToNext()) {
                model.addImage(new CustomImage(cursor));
                Log.d("insert ", ++i + "");
            }
            view.startFragment();
            cursor.close();
        }else{
            view.startFragment();
            cursor.close();
        }

    }

    public AuthCredentials getCredentials(){
        return model.getCredentials();
    }

    public String getTeaserUrl(int position){
        return model.getTeaserUrl(position);
    }

    public int getImagesSize(){
        return model.getImagesSize();
    }

    public int getDefaultImageNumber(){
        return model.getDefaultImagesNumber();
    }

    public void addImage(CustomImage image){
        model.addImage(image);
    }

    public int getPageNumber(){
        return model.getPageNumber();
    }

    @Override
    public void bindView(MainView view) {
        this.view = view;
    }

    public void hideListFragment(Fragment fragment){
        view.hideListstartNormalFragment(fragment);
    }

    public void stopProgressBar() {
        view.stopProgressBar();
    }

    public void startProgressBar() {
        view.startProgressBar();
    }

    public String getNormalUrl(int position){
        return model.getNormalUrl(position);
    }

    public String getTitle(int position){
        return model.getTitle(position);
    }

    public String getDescription(int position){
        return model.getDescription(position);
    }

    public void clearImages(){
        model.clearImages();
    }

    public void incPageNumber(){
        model.incPageNumber();
    }

    public String getToken(){
        return model.getToken();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(model);
        parcel.writeValue(view);
        parcel.writeValue(activity);
    }
}
