package com.example.lenovo.materialdesign.tasks;

import android.os.AsyncTask;

import com.android.volley.RequestQueue;
import com.example.lenovo.materialdesign.callbacks.BoxOfficeLoadedListner;
import com.example.lenovo.materialdesign.extras.Movieutils;
import com.example.lenovo.materialdesign.network.VolleySingleton;
import com.example.lenovo.materialdesign.pojo.Movie;

import java.util.ArrayList;

import me.tatarka.support.job.JobParameters;

/**
 * Created by lenovo on 1/6/2016.
 */
public class TaskLoadMoviesBoxOffice extends AsyncTask<Void, Void, ArrayList<Movie>> {
    private BoxOfficeLoadedListner myComponent;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    public TaskLoadMoviesBoxOffice(BoxOfficeLoadedListner myComponent) {
        this.myComponent = myComponent;
        volleySingleton = VolleySingleton.getinstance();
        requestQueue = volleySingleton.getRequestQueue();
    }

    @Override
    protected ArrayList<Movie> doInBackground(Void... params) {
        ArrayList<Movie> movieArrayList = Movieutils.loadBoxOfficeMovies(requestQueue);
        return movieArrayList;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<Movie> movies) {
        if (myComponent != null) {
            myComponent.onBoxOfficeMoviesLoaded(movies);
        }
    }
}
