package com.example.donald.testapplication.Presenter;

import android.app.Fragment;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.donald.testapplication.MainView;

/**
 * Created by bloold on 06.07.17.
 */

public abstract class BasePresenter<M, V>{

    protected M model;
    protected V view;

    public abstract void bindView(MainView view);
}
