package com.example.donald.testapplication;

import android.app.Fragment;
import android.os.Parcelable;

import com.example.donald.testapplication.Presenter.BasePresenter;

/**
 * Created by bloold on 06.07.17.
 */

public interface MainView extends Parcelable{
    BasePresenter getPresenter();
    void startFragment();
    void hideNormalstartListFragment();
    void hideListstartNormalFragment(Fragment fragment);
    void stopProgressBar();
    void startProgressBar();
}
