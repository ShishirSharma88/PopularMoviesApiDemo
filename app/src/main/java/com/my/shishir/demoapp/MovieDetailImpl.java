package com.my.shishir.demoapp;

import com.my.shishir.demoapp.model.Result;
import com.my.shishir.demoapp.utility.Utility;

public class MovieDetailImpl implements MovieDetailContract.MovieDetailPresenter {

    private final MovieDetailContract.MovieDetailView movieDetailView;
    private final Result result;

    MovieDetailImpl(MovieDetailContract.MovieDetailView movieDetailView, Result result) {
        this.movieDetailView = movieDetailView;
        this.result = result;
    }

    @Override
    public void setData() {
        movieDetailView.setDescription(result.getOverview());
        movieDetailView.setTitle(result.getTitle());
        movieDetailView.setImage(Utility.BASE_URL_IMAGE + result.getPosterPath());
        movieDetailView.setMovieId("Movie Id :" + String.valueOf(result.getId()));
        movieDetailView.setPopularity("Popularity :" + String.valueOf(result.getPopularity()));
        movieDetailView.setRating("Vote Average :" + String.valueOf(result.getVoteAverage()));
        movieDetailView.setVoteCount("Vote Count :" + String.valueOf(result.getVoteCount()));
        movieDetailView.setReleasedDate("Released Date : " + result.getReleaseDate());
    }
}
