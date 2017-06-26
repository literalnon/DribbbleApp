package com.example.donald.testapplication.Data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.agilie.dribbblesdk.domain.Images;
import com.example.donald.testapplication.DB.DBCustomViews;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Donald on 24.06.2017.
 */
public class CustomImage {

    String title;
    String description;
    String normal;
    String teaser;

    public CustomImage(String title, String description, Images images){
        this.title = title;
        createDescription(description);
        this.normal = images.getHidpi() == null ? images.getNormal() : images.getHidpi();
        this.teaser = images.getTeaser();
    }

    public CustomImage(Cursor cursor){
        this.title = cursor.getString(cursor.getColumnIndex(DBCustomViews.NAME_COLLUMN_TITLE));
        createDescription(cursor.getString(cursor.getColumnIndex(DBCustomViews.NAME_COLLUMN_DESCRIPTION)));
        this.normal = cursor.getString(cursor.getColumnIndex(DBCustomViews.NAME_COLLUMN_NORMAL));
        this.teaser = cursor.getString(cursor.getColumnIndex(DBCustomViews.NAME_COLLUMN_TEASER));
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public String getTeaserUrl(){
        return teaser;
    }

    public String getNormalUrl(){
        return normal;
    }

    public ContentValues getContentValues(){
        ContentValues values = new ContentValues();

        values.put(DBCustomViews.NAME_COLLUMN_TITLE, title);
        values.put(DBCustomViews.NAME_COLLUMN_DESCRIPTION, description);
        values.put(DBCustomViews.NAME_COLLUMN_NORMAL, normal);
        values.put(DBCustomViews.NAME_COLLUMN_TEASER, teaser);

        return values;
    }

    private void createDescription(String des){
        String resString = "";
        boolean is = false;
        if(des != null) {
            char[] a = des.toCharArray();
            for (int i = 0; i < a.length; ++i) {
                if (a[i] == '>') {
                    is = true;
                } else if (a[i] == '<') {
                    is = false;
                } else if (is) {
                    resString += a[i];
                }
            }
        }
        this.description = resString;
    }
}
