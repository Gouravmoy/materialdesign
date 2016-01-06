package com.example.lenovo.materialdesign.json;

import com.example.lenovo.materialdesign.pojo.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.example.lenovo.materialdesign.extras.Constants.NA;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_AUDIENCE_SCORE;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_CAST;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_ID;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_LINKS;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_MOVIES;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_POSTERS;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_RATINGS;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_RELEASE_DATES;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_REVIEWS;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_SELF;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_SIMILAR;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_SYNOPSIS;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_THEATER;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_THUMBNAIL;
import static com.example.lenovo.materialdesign.extras.Keys.EndpointBoxOffice.KEY_TITLE;
import static com.example.lenovo.materialdesign.json.Utils.contains;

/**
 * Created by lenovo on 1/7/2016.
 */
public class Parser {
    public static ArrayList<Movie> parseJsonResponse(JSONObject response) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-DD-mm");
        ArrayList<Movie> movieArrayList = new ArrayList<>();
        if (response != null) {
            if (response.length() > 0) {
                try {
                    JSONArray listJSONMovies = response.getJSONArray(KEY_MOVIES);
                    for (int i = 0; i < listJSONMovies.length(); i++) {
                        long id = -1;
                        String title = NA;
                        String releaseDate = NA;
                        int audienceScore = -1;
                        String synopsis = NA;
                        String thumbnailURL = NA;
                        String urlSelf = NA;
                        String urlCast = NA;
                        String urlReview = NA;
                        String urlSimilar = NA;

                        JSONObject movie = listJSONMovies.getJSONObject(i);
                        if (contains(movie, KEY_ID)) {
                            id = movie.getLong(KEY_ID);
                        }
                        if (contains(movie, KEY_TITLE)) {
                            title = movie.getString(KEY_TITLE);
                        }
                        JSONObject releaseDtObj = movie.getJSONObject(KEY_RELEASE_DATES);
                        releaseDate = null;
                        if (contains(releaseDtObj, KEY_THEATER)) {
                            releaseDate = releaseDtObj.getString(KEY_THEATER);
                        }
                        JSONObject ratingObj = movie.getJSONObject(KEY_RATINGS);
                        if (contains(ratingObj, KEY_AUDIENCE_SCORE)) {
                            audienceScore = ratingObj.getInt(KEY_AUDIENCE_SCORE);
                        }
                        if (contains(movie, KEY_SYNOPSIS)) {
                            synopsis = movie.getString(KEY_SYNOPSIS);
                        }
                        JSONObject posterObj = movie.getJSONObject(KEY_POSTERS);
                        if (contains(posterObj, KEY_THUMBNAIL)) {
                            thumbnailURL = posterObj.getString(KEY_THUMBNAIL);
                        }
                        JSONObject urlsObj = movie.getJSONObject(KEY_LINKS);
                        if (contains(urlsObj, KEY_SELF)) {
                            urlSelf = urlsObj.getString(KEY_SELF);
                        }
                        if (contains(urlsObj, KEY_CAST)) {
                            urlCast = urlsObj.getString(KEY_CAST);
                        }
                        if (contains(urlsObj, KEY_REVIEWS)) {
                            urlReview = urlsObj.getString(KEY_REVIEWS);
                        }
                        if (contains(urlsObj, KEY_SIMILAR)) {
                            urlSimilar = urlsObj.getString(KEY_SIMILAR);
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
                        currentMovie.setUrlCast(urlCast);
                        currentMovie.setUrlSelf(urlSelf);
                        currentMovie.setUrlSimmilar(urlSimilar);
                        currentMovie.setUrlReviews(urlReview);
                        if (id != -1 && !title.equals(NA)) {
                            movieArrayList.add(currentMovie);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return movieArrayList;
    }
}
