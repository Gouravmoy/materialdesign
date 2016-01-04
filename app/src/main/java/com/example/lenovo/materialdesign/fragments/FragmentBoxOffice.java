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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lenovo.materialdesign.MyApplication;
import com.example.lenovo.materialdesign.R;
import com.example.lenovo.materialdesign.adapters.AdapterBoxOffice;
import com.example.lenovo.materialdesign.extras.MoviesSorter;
import com.example.lenovo.materialdesign.extras.SortListener;
import com.example.lenovo.materialdesign.extras.UrlEndpoints;
import com.example.lenovo.materialdesign.logging.L;
import com.example.lenovo.materialdesign.network.VolleySingleton;
import com.example.lenovo.materialdesign.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.lenovo.materialdesign.extras.Constants.*;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_AUDIENCE_SCORE;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_ID;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_MOVIES;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_POSTERS;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_RATINGS;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_RELEASE_DATES;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_SYNOPSIS;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_THEATER;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_THUMBNAIL;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_TITLE;

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

    private static String URL_ROTTEN_TOMATOES_BOX_OFFICE = "http://api.rottentomatoes.com/api/public/v1.0/lists/movies/box_office.json";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
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

    public static String getRequestUrl(int limit) {
        return UrlEndpoints.URL_BOX_OFFICE + UrlEndpoints.URL_CHAR_QUESTION + UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES + UrlEndpoints.URL_CHAR_AMEPERSAND + UrlEndpoints.URL_PARAM_LIMIT + limit;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        volleySingleton = VolleySingleton.getinstance();
        requestQueue = volleySingleton.getRequestQueue();
        //sendJSONRequest();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_MOVIES, listMovies);
    }

    private void sendJSONRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl(30), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                onVolleyError.setVisibility(View.GONE);
                listMovies = parseJsonResponse(response);
                adapterBoxOffice.setMovieList(listMovies);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                handelVolleyError(error);
            }
        });
        requestQueue.add(request);
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

    private ArrayList<Movie> parseJsonResponse(JSONObject response) {
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        if (response != null || response.length() > 0) {

            try {
                JSONArray listJSONMovies = response.getJSONArray(KEY_MOVIES);
                for (int i = 0; i < listJSONMovies.length(); i++) {
                    long id = -1;
                    String title = NA;
                    String releaseDate = NA;
                    int audienceScore = -1;
                    String synopsis = NA;
                    String thumbnailURL = NA;
                    JSONObject movie = listJSONMovies.getJSONObject(i);
                    if (movie.has(KEY_ID) && !movie.isNull(KEY_ID)) {
                        id = movie.getLong(KEY_ID);
                    }
                    if (movie.has(KEY_TITLE) && !movie.isNull(KEY_TITLE)) {
                        title = movie.getString(KEY_TITLE);
                    }
                    JSONObject releaseDtObj = movie.getJSONObject(KEY_RELEASE_DATES);
                    releaseDate = null;
                    if (releaseDtObj.has(KEY_THEATER) && !releaseDtObj.isNull(KEY_THEATER)) {
                        releaseDate = releaseDtObj.getString(KEY_THEATER);
                    }
                    JSONObject ratingObj = movie.getJSONObject(KEY_RATINGS);
                    if (ratingObj.has(KEY_AUDIENCE_SCORE) && !ratingObj.isNull(KEY_AUDIENCE_SCORE)) {
                        audienceScore = ratingObj.getInt(KEY_AUDIENCE_SCORE);
                    }
                    if (movie.has(KEY_SYNOPSIS) && !movie.isNull(KEY_SYNOPSIS)) {
                        synopsis = movie.getString(KEY_SYNOPSIS);
                    }
                    JSONObject posterObj = movie.getJSONObject(KEY_POSTERS);
                    thumbnailURL = "";
                    if (posterObj.has(KEY_THUMBNAIL) && !posterObj.isNull(KEY_THUMBNAIL)) {
                        thumbnailURL = posterObj.getString(KEY_THUMBNAIL);
                    }
                    Movie currentMovie = new Movie();
                    currentMovie.setId(id);
                    currentMovie.setTitle(title);
                    currentMovie.setSynopsis(synopsis);
                    currentMovie.setAudienceScore(audienceScore);
                    Date date = null;
                    try {
                        date = dateFormat.parse(releaseDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    currentMovie.setReleaseDateTheaatre(date);
                    currentMovie.setUrlThumbnail(thumbnailURL);
                    if (id != -1 && !title.equals(NA)) {
                        movieArrayList.add(currentMovie);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return movieArrayList;
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
            sendJSONRequest();
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
