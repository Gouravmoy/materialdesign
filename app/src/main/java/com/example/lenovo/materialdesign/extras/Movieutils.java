package com.example.lenovo.materialdesign.extras;

import com.android.volley.RequestQueue;
import com.example.lenovo.materialdesign.MyApplication;
import com.example.lenovo.materialdesign.json.EndPoints;
import com.example.lenovo.materialdesign.json.Parser;
import com.example.lenovo.materialdesign.json.Requestor;
import com.example.lenovo.materialdesign.pojo.Movie;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lenovo on 1/7/2016.
 */
public class Movieutils {
    public static ArrayList<Movie> loadBoxOfficeMovies(RequestQueue requestQueue){
        JSONObject response = Requestor.sendRequestBoxOfficeMovies(requestQueue, EndPoints.getRequestUrl(30));
        ArrayList<Movie> movieList = Parser.parseJsonResponse(response);
        MyApplication.getWritableDatabase().insertMoviesBoxOffice(movieList,true);
        return movieList;
    }
}
