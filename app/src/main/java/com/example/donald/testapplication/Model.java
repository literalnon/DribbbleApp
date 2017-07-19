package com.example.donald.testapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import com.agilie.dribbblesdk.service.auth.AuthCredentials;

import com.example.donald.testapplication.DB.DBCustomViews;
import com.example.donald.testapplication.Data.CustomImage;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by bloold on 06.07.17.
 */

public class Model implements Parcelable {

    private final String DRIBBBLE_CLIENT_ID = "43fcf3e1bd1e06e5b7ee3795131939b2490435516363294023ba2ad3df188986";
    private final String DRIBBBLE_CLIENT_SECRET = "c5ad03eae740e0c7ed5c2545e48e432246d1c40e3a972968f4ffdc076742df85";
    private final String DRIBBBLE_CLIENT_ACCESS_TOKEN = "d8c102c940f35cf8eba2e9dba8f6644485f6d82e6ca9ba58909e4110f7b8472a";
    private final String DRIBBBLE_CLIENT_REDIRECT_URL = "https://dribbble.com/DonaldDuck";

    private final int DEFAULT_IMAGES_NUMBER = 51;
    private int pageNumber = 1;
    private SQLiteDatabase imagesBase;
    private ArrayList<CustomImage> images;
    private String token;


    public Model(Context context){
        imagesBase = new DBCustomViews(context).getWritableDatabase();
        images = new ArrayList<>();
    }

    protected Model(Parcel in) {
        token = in.readString();
        images = in.readArrayList(CustomImage.class.getClassLoader());
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    public AuthCredentials getCredentials(){
        return AuthCredentials.newBuilder(
                DRIBBBLE_CLIENT_ID,
                DRIBBBLE_CLIENT_SECRET,
                DRIBBBLE_CLIENT_ACCESS_TOKEN,
                DRIBBBLE_CLIENT_REDIRECT_URL)
                .build();
    }

    public int getImagesSize(){
        return images.size();
    }

    public void addImage(CustomImage image){
        images.add(image);
    }

    public void imageBaseInsert(ContentValues values){
        imagesBase.insert(DBCustomViews.NAME_TABLE, null, values);
    }

    public boolean isFullImages(){
        return images.size() >= DEFAULT_IMAGES_NUMBER;
    }

    public int getDefaultImagesNumber(){
        return DEFAULT_IMAGES_NUMBER;
    }

    public Cursor getTableData(){
        return imagesBase.query(DBCustomViews.NAME_TABLE, null, null, null, null, null, null);
    }

    public int getPageNumber(){
        return pageNumber;
    }

    public String getTeaserUrl(int position){
        return images.get(position).getTeaserUrl();
    }

    public void setToken(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }

    public String getNormalUrl(int position){
        return images.get(position).getNormalUrl();
    }

    public String getTitle(int position){
        return images.get(position).getTitle();
    }

    public String getDescription(int position){
        return images.get(position).getDescription();
    }

    public void clearImages(){
        images.clear();
    }

    public void incPageNumber(){
        ++pageNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(images);
        parcel.writeString(token);
    }
}
