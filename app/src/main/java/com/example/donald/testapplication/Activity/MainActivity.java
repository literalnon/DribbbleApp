package com.example.donald.testapplication.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agilie.dribbblesdk.service.auth.DribbbleAuthHelper;
import com.example.donald.testapplication.Fragment.ListImageFragment;
import com.example.donald.testapplication.Fragment.NormalImageFragment;
import com.example.donald.testapplication.Presenter.BasePresenter;
import com.example.donald.testapplication.Presenter.ListImagePresenter;
import com.example.donald.testapplication.MainView;
import com.example.donald.testapplication.R;
import com.google.api.client.auth.oauth2.Credential;


public class MainActivity extends AppCompatActivity implements MainView{

    private static final String PRESENTER_KEY = "presenter";
    private static final String LIST_IMAGE_FRAGMENT_KEY = "listImage";
    private static final String NORMAL_IMAGE_FRAGMENT_KEY = "normalImage";

    private ProgressBar progressBarLoad;

    private ListImagePresenter presenter;
    private ListImageFragment listImgFragment;

    public Context context;
    private NormalImageFragment normalFragment;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        progressBarLoad = (ProgressBar)findViewById(R.id.progressBar);

        if(savedInstanceState != null){
            presenter = savedInstanceState.getParcelable(PRESENTER_KEY);
        }else {
            presenter = new ListImagePresenter(this);
        }
            presenter.bindView(this);

        authorize();

        //  DribbbleAuthHelper.logout(this, credentials);
    }

    private void authorize(){

        DribbbleAuthHelper.startOauthDialog(this, presenter.getCredentials(), new DribbbleAuthHelper.AuthListener()
        {

            @Override
            public void onSuccess(final Credential credential) {
                presenter.getShots(credential.getAccessToken());
                // Handle success login event
            }

            @Override
            public void onError(Exception ex) {
                ex.printStackTrace();
                //Toast.makeText(context, "Error: try authorize later", Toast.LENGTH_LONG).show();
                // Handle error here
            }
        });
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void startFragment() {
        stopProgressBar();
        listImgFragment = ListImageFragment.newInstance(presenter.getToken());
        getFragmentManager().beginTransaction().add(R.id.main_layout, listImgFragment).commit();
    }

    @Override
    public void hideListstartNormalFragment(Fragment fragment){
        //stopProgressBar();
        FragmentManager manager = getFragmentManager();
        normalFragment = (NormalImageFragment)fragment;
        manager.beginTransaction().hide(listImgFragment).add(R.id.main_layout, normalFragment).commit();
    }

    @Override
    public void hideNormalstartListFragment(){
        getFragmentManager().beginTransaction().remove(normalFragment).show(listImgFragment).commit();
    }

    @Override
    public void stopProgressBar() {
        progressBarLoad.setAlpha(0);
    }

    @Override
    public void startProgressBar() {
        progressBarLoad.setAlpha(1);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable(PRESENTER_KEY, presenter);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    @Override
    public void onBackPressed() {
        if(listImgFragment.isHidden()) {
            hideNormalstartListFragment();
        } else {
            super.onBackPressed();
        }
    }
}
