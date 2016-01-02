package com.example.lenovo.materialdesign.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.lenovo.materialdesign.MyApplication;
import com.example.lenovo.materialdesign.R;
import com.example.lenovo.materialdesign.adapters.AdapterBoxOffice;
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
public class FragmentBoxOffice extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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

    private void sendJSONRequest() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestUrl(10), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listMovies = parseJsonResponse(response);
                adapterBoxOffice.setMovieList(listMovies);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                L.t(getActivity(), "Failed");
            }
        });
        requestQueue.add(request);
    }

    private ArrayList<Movie> parseJsonResponse(JSONObject response) {

        ArrayList<Movie> movieArrayList = new ArrayList<>();
        if (response != null || response.length() > 0)
            try {
                JSONArray listJSONMovies = response.getJSONArray(KEY_MOVIES);
                for (int i = 0; i < listJSONMovies.length(); i++) {
                    JSONObject movie = listJSONMovies.getJSONObject(i);
                    long id = movie.getLong(KEY_ID);
                    String title = movie.getString(KEY_TITLE);
                    JSONObject releaseDtObj = movie.getJSONObject(KEY_RELEASE_DATES);
                    String releaseDate = null;
                    if (releaseDtObj.has(KEY_THEATER)) {
                        releaseDate = releaseDtObj.getString(KEY_THEATER);
                    } else {
                        releaseDate = "NA";
                    }
                    JSONObject ratingObj = movie.getJSONObject(KEY_RATINGS);
                    int ratings = -1;
                    if (ratingObj.has(KEY_AUDIENCE_SCORE)) {
                        //String stringRatings = ratingObj.getString(KEY_AUDIENCE_SCORE);
                        ratings = ratingObj.getInt(KEY_AUDIENCE_SCORE);
                    }
                    String synopsis = movie.getString(KEY_SYNOPSIS);
                    JSONObject posterObj = movie.getJSONObject(KEY_POSTERS);
                    String thumbnailURL = "";
                    if (posterObj.has(KEY_THUMBNAIL)) {
                        thumbnailURL = posterObj.getString(KEY_THUMBNAIL);
                    }
                    Movie currentMovie = new Movie();
                    currentMovie.setId(id);
                    currentMovie.setTitle(title);
                    currentMovie.setSynopsis(synopsis);
                    currentMovie.setAudienceScore(ratings);
                    currentMovie.setReleaseDateTheaatre(dateFormat.parse(releaseDate));
                    currentMovie.setUrlThumbnail(thumbnailURL);
                    movieArrayList.add(currentMovie);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        return movieArrayList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_box_office, container, false);
        listMovieHits = (RecyclerView) view.findViewById(R.id.listMovieHits);
        listMovieHits.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapterBoxOffice = new AdapterBoxOffice(getActivity());
        listMovieHits.setAdapter(adapterBoxOffice);
        sendJSONRequest();
        return view;
    }

}
