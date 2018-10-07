package com.my.shishir.demoapp;

interface MovieDetailContract {
    interface MovieDetailPresenter {
        void setData();
    }

    interface MovieDetailView {
        void setTitle(String title);
        void setDescription(String description);
        void setImage(String url);
        void setReleasedDate(String date);
        void setRating(String rating);
        void setVoteCount(String vote);
        void setMovieId(String id);
        void setPopularity(String polularity);
    }

}
