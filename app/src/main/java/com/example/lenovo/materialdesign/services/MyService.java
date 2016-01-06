package com.example.lenovo.materialdesign.services;

import com.example.lenovo.materialdesign.callbacks.BoxOfficeLoadedListner;
import com.example.lenovo.materialdesign.logging.L;
import com.example.lenovo.materialdesign.pojo.Movie;
import com.example.lenovo.materialdesign.tasks.TaskLoadMoviesBoxOffice;

import java.util.ArrayList;

import me.tatarka.support.job.JobParameters;
import me.tatarka.support.job.JobService;

/**
 * Created by lenovo on 1/5/2016.
 */
public class MyService extends JobService implements BoxOfficeLoadedListner {
    private JobParameters jobParameters;

    @Override
    public boolean onStartJob(JobParameters params) {
        L.m("Start Job");
        this.jobParameters = params;
        new TaskLoadMoviesBoxOffice(this).execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        L.m("Stop Job");
        return false;
    }

    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<Movie> movies) {
        L.m("onBoxOfficeMoviesLoaded");
        jobFinished(jobParameters, false);
    }
}
