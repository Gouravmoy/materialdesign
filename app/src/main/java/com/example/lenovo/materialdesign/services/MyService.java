package com.example.lenovo.materialdesign.services;

import android.os.AsyncTask;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.example.lenovo.materialdesign.MyApplication;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

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

/**
 * Created by lenovo on 1/5/2016.
 */
public class MyService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {
        new MyTask(this).execute(params);
        int i = 0;
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    private static class MyTask extends AsyncTask<JobParameters, Void, JobParameters> {
        MyService myService;
        private DateFormat dateFormat = new SimpleDateFormat("yyyy-DD-mm");
        private VolleySingleton volleySingleton;
        private RequestQueue requestQueue;

        MyTask(MyService myService) {
            this.myService = myService;
            volleySingleton = VolleySingleton.getinstance();
            requestQueue = volleySingleton.getRequestQueue();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JobParameters doInBackground(JobParameters... params) {
            JSONObject response = sendJSONRequest();
            ArrayList<Movie> movieList = parseJsonResponse(response);
            MyApplication.getWritableDatabase().insertMoviesBoxOffice(movieList, true);
            return params[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            myService.jobFinished(jobParameters, false);
        }

        public static String getRequestUrl(int limit) {
            return UrlEndpoints.URL_BOX_OFFICE + UrlEndpoints.URL_CHAR_QUESTION + UrlEndpoints.URL_PARAM_API_KEY + MyApplication.API_KEY_ROTTEN_TOMATOES + UrlEndpoints.URL_CHAR_AMEPERSAND + UrlEndpoints.URL_PARAM_LIMIT + limit;
        }

        private JSONObject sendJSONRequest() {
            JSONObject response = null;
            RequestFuture<JSONObject> requestFuture = RequestFuture.newFuture();
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                    getRequestUrl(30), requestFuture, requestFuture);
            requestQueue.add(request);
            try {
                response = requestFuture.get(30000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                L.m(e.getMessage() + "");
            } catch (ExecutionException e) {
                L.m(e.getMessage() + "");
            } catch (TimeoutException e) {
                L.m(e.getMessage() + "");
            }
            return response;
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
            return movieArrayList;
        }

        public boolean contains(JSONObject jsonObject, String key) {
            return jsonObject != null && jsonObject.has(key) && !jsonObject.isNull(key) ? true : false;
        }
    }
}
