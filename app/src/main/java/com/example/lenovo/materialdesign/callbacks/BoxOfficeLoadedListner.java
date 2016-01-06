package com.example.lenovo.materialdesign.callbacks;

import com.example.lenovo.materialdesign.pojo.Movie;

import java.util.ArrayList;

/**
 * Created by lenovo on 1/7/2016.
 */
public interface BoxOfficeLoadedListner {
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> movies);
}
