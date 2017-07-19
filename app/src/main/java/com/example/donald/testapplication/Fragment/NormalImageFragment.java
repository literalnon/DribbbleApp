package com.example.donald.testapplication.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.donald.testapplication.Adapter.RecycleImagesAdapter;
import com.example.donald.testapplication.MainView;
import com.example.donald.testapplication.Presenter.BasePresenter;
import com.example.donald.testapplication.Presenter.ListImagePresenter;
import com.example.donald.testapplication.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NormalImageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NormalImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NormalImageFragment extends Fragment{

    private static final String PRESENTER_KEY = "presenter";

    private static int curPosition;

    private OnFragmentInteractionListener mListener;
    private ListImagePresenter presenter;

    private TextView title;
    private ImageView image;
    private TextView description;
    private View mainView;

    public NormalImageFragment() {

        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NormalImageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NormalImageFragment newInstance() {
        NormalImageFragment fragment = new NormalImageFragment();
        Bundle args = new Bundle();
        args.putInt(RecycleImagesAdapter.INT_POSITION, curPosition);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            curPosition = getArguments().getInt(RecycleImagesAdapter.INT_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(mainView == null){
            mainView = inflater.inflate(R.layout.fragment_normal_image, container, false);
        }

        if(presenter == null && savedInstanceState != null){
            presenter = savedInstanceState.getParcelable(PRESENTER_KEY);
        }

        image = (ImageView) mainView.findViewById(R.id.ivNormalImage);
        title = (TextView) mainView.findViewById(R.id.tvTitle);
        description = (TextView) mainView.findViewById(R.id.tvDiscription);

        image.setMaxHeight(getResources().getDisplayMetrics().heightPixels / 2);
        loadImage(curPosition);

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //slide

                return true;
            }
        });

        return mainView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            MainView view = (MainView)context;
            presenter = (ListImagePresenter)view.getPresenter();
        }catch (ClassCastException e){
            throw new ClassCastException("MainView cast: " + e.getMessage());
        }
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PRESENTER_KEY, presenter);
    }

    private void loadImage(int position){
        RequestListener r_listener = new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                presenter.stopProgressBar();
                return false;
            }
        };

        presenter.startProgressBar();

        Glide
                .with(this)
                .load(presenter.getNormalUrl(position))
                .listener(r_listener)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .animate(R.anim.animation)
                .into(image);

        title.setText(presenter.getTitle(position));
        description.setText(presenter.getDescription(position));
    }
}
