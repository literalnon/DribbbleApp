package com.example.donald.testapplication.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agilie.dribbblesdk.domain.Shot;
import com.agilie.dribbblesdk.service.retrofit.DribbbleServiceGenerator;
import com.agilie.dribbblesdk.service.retrofit.services.DribbbleShotsService;
import com.example.donald.testapplication.Activity.MainActivity;
import com.example.donald.testapplication.DB.DBCustomViews;
import com.example.donald.testapplication.Data.CustomImage;
import com.example.donald.testapplication.R;
import com.example.donald.testapplication.Adapter.RecycleImagesAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ImagesListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ImagesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ImagesListFragment extends Fragment {
    private static final String TOKEN = "token";
    private static String token;
    private OnFragmentInteractionListener mListener;

    public ImagesListFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param token auth token.
     * @return A new instance of fragment ImagesListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ImagesListFragment newInstance(String token) {
        ImagesListFragment fragment = new ImagesListFragment();
        Bundle args = new Bundle();
        args.putString(TOKEN, token);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            token = getArguments().getString(TOKEN);
        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View mainFragmentView = inflater.inflate(R.layout.fragment_images_list, container, false);
        final RecyclerView rvImagesTeaser = (RecyclerView)mainFragmentView.findViewById(R.id.rv_list_images);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(inflater.getContext(), 3);

        rvImagesTeaser.setLayoutManager(gridLayoutManager);
        rvImagesTeaser.setAdapter(new RecycleImagesAdapter(inflater.getContext()));

        final SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)mainFragmentView.findViewById(R.id.refresh_layout);
        refreshLayout.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.LinkBlue));
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                final Callback<List<Shot>> callback = new Callback<List<Shot>>() {
                    @Override
                    public void onResponse(Call<List<Shot>> call, final Response<List<Shot>> response) {
                        final SQLiteDatabase imagesBase = new DBCustomViews(inflater.getContext()).getWritableDatabase();
                        imagesBase.delete(DBCustomViews.NAME_TABLE, null, null);

                        MainActivity.customImages.clear();
                        for(int i = 0; i < response.body().size() && MainActivity.customImages.size() < MainActivity.DEFAULT_IMAGES_NUMBER; ++i){
                            if(!response.body().get(i).getAnimated()) {

                                CustomImage cImage = new CustomImage(
                                        response.body().get(i).getTitle(),
                                        response.body().get(i).getDescription(),
                                        response.body().get(i).getImages());

                                MainActivity.customImages.add(cImage);

                                imagesBase.insert(DBCustomViews.NAME_TABLE, null, cImage.getContentValues());
                                Log.d("insert " + i, cImage.getNormalUrl());
                            }
                        }

                        GridLayoutManager gridLayoutManager = new GridLayoutManager(inflater.getContext(), 3);
                        rvImagesTeaser.setLayoutManager(gridLayoutManager);

                        rvImagesTeaser.setAdapter(new RecycleImagesAdapter(inflater.getContext()));

                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<Shot>> call, Throwable t) {
                        Log.e("Failure in callback", t.getMessage());
                    }
                };

                DribbbleShotsService dribbbleShotsService = DribbbleServiceGenerator.getDribbbleShotService(token);
                Call<List<Shot>> shotsCall = dribbbleShotsService.fetchShots(MainActivity.pageNumber, MainActivity.DEFAULT_IMAGES_NUMBER * 2);
                shotsCall.enqueue(callback);
            }
        });

        return mainFragmentView;
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
