package com.example.lenovo.materialdesign.extras;

import com.example.lenovo.materialdesign.pojo.Movie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by lenovo on 1/4/2016.
 */
public class MoviesSorter {
    public void sortMoviesByName(ArrayList<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                return lhs.getTitle().compareTo(rhs.getTitle());
            }
        });
    }

    public void sortMoviesByDate(ArrayList<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                int returnVal = 0;
                if (lhs.getReleaseDateTheaatre().before(rhs.getReleaseDateTheaatre()))
                    return 1;
                if (lhs.getReleaseDateTheaatre().after(rhs.getReleaseDateTheaatre()))
                    return -1;
                else
                    return 0;
            }
        });
    }

    public void sortMoviesByRating(ArrayList<Movie> movies) {
        Collections.sort(movies, new Comparator<Movie>() {
            @Override
            public int compare(Movie lhs, Movie rhs) {
                if (lhs.getAudienceScore() < rhs.getAudienceScore())
                    return 1;
                if (lhs.getAudienceScore() > rhs.getAudienceScore())
                    return -1;
                else
                    return 0;
            }
        });
    }
}
