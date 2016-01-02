package com.example.lenovo.materialdesign.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.lenovo.materialdesign.MyApplication;
import com.example.lenovo.materialdesign.R;
import com.example.lenovo.materialdesign.logging.L;
import com.example.lenovo.materialdesign.network.VolleySingleton;
import com.example.lenovo.materialdesign.pojo.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 1/2/2016.
 */
public class AdapterBoxOffice extends RecyclerView.Adapter<AdapterBoxOffice.ViewHolderBoxOffice> {

    private LayoutInflater inflater;
    private List<Movie> movieList = new ArrayList<>();
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public void setMovieList(ArrayList<Movie> movies) {
        this.movieList = movies;
        notifyItemRangeChanged(0, movieList.size());
    }

    public AdapterBoxOffice(Context context) {
        inflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getinstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_movie_box_office, parent, false);
        ViewHolderBoxOffice viewHolderBoxOffice = new ViewHolderBoxOffice(view);
        return viewHolderBoxOffice;
    }

    @Override
    public void onBindViewHolder(final ViewHolderBoxOffice holder, int position) {
        Movie currentMovie = movieList.get(position);
        holder.movieTitle.setText(currentMovie.getTitle());
        holder.movieReleaseDate.setText(currentMovie.getReleaseDateTheaatre().toString());
        holder.movieAudienceScore.setRating(currentMovie.getAudienceScore() / 20.0F);
        String urlThumbnail = currentMovie.getUrlThumbnail();
        if (urlThumbnail != null) {
            imageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.movieThumbnail.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {
                    L.t(MyApplication.getAppContext(), "Failed to Load Image");
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class ViewHolderBoxOffice extends RecyclerView.ViewHolder {
        ImageView movieThumbnail;
        TextView movieTitle;
        TextView movieReleaseDate;
        RatingBar movieAudienceScore;

        public ViewHolderBoxOffice(View itemView) {
            super(itemView);
            movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnail);
            movieTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDate);
            movieAudienceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScore);
        }
    }

}
