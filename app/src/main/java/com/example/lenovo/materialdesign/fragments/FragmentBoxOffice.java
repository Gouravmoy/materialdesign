package com.example.lenovo.materialdesign.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.lenovo.materialdesign.R;
import com.example.lenovo.materialdesign.adapters.AdapterBoxOffice;
import com.example.lenovo.materialdesign.extras.MoviesSorter;
import com.example.lenovo.materialdesign.extras.SortListener;
import com.example.lenovo.materialdesign.logging.L;
import com.example.lenovo.materialdesign.network.VolleySingleton;
import com.example.lenovo.materialdesign.pojo.Movie;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentBoxOffice#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentBoxOffice extends Fragment implements SortListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String STATE_MOVIES = "state_movies";
    //JobS

    private static String URL_ROTTEN_TOMATOES_BOX_OFFICE = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Movie> listMovies = new ArrayList<>();
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-DD-mm");
    private RecyclerView listMovieHits;
    private AdapterBoxOffice adapterBoxOffice;
    private TextView onVolleyError;
    private MoviesSorter moviesSorter = new MoviesSorter();

    public FragmentBoxOffice() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBoxOffice.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentBoxOffice newInstance(String param1, String param2) {
        FragmentBoxOffice fragment = new FragmentBoxOffice();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //sendJSONRequest();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, listMovies);
    }



    private void handelVolleyError(VolleyError error) {
        onVolleyError.setVisibility(View.VISIBLE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            onVolleyError.setText(R.string.errorTimeOut);
        } else if (error instanceof AuthFailureError) {
            onVolleyError.setText(R.string.authFailure);
        } else if (error instanceof ServerError) {
            onVolleyError.setText(R.string.serverError);
        } else if (error instanceof NetworkError) {
            onVolleyError.setText(R.string.netConError);
        } else if (error instanceof ParseError) {
            onVolleyError.setText(R.string.parseError);
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_office, container, false);
        onVolleyError = (TextView) view.findViewById(R.id.textVolleyError);
        listMovieHits = (RecyclerView) view.findViewById(R.id.listMovieHits);
        listMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        listMovieHits.setAdapter(adapterBoxOffice);
        if (savedInstanceState != null) {
            listMovies = savedInstanceState.getParcelableArrayList(STATE_MOVIES);
            adapterBoxOffice.setMovieList(listMovies);
        } else {
            //sendJSONRequest();
        }
        return view;
    }

    @Override
    public void onSortByName() {
        L.t(getActivity(), "Box Office Sort By Name");
        //moviesSorter = new MoviesSorter();
        moviesSorter.sortMoviesByName(listMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByDate() {
        L.t(getActivity(), "Box Office Sort By Date");
        moviesSorter.sortMoviesByDate(listMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }

    @Override
    public void onSortByRating() {
        L.t(getActivity(), "Box Office Sort By Ratng");
        moviesSorter.sortMoviesByRating(listMovies);
        adapterBoxOffice.notifyDataSetChanged();
    }
}
