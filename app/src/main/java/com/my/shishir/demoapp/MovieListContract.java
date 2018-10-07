package com.my.shishir.demoapp;

import android.view.View;

import com.my.shishir.demoapp.api.RequestManager;
import com.my.shishir.demoapp.model.MoviesData;
import com.my.shishir.demoapp.model.Result;

import java.util.List;

interface MovieListContract {
    interface MainView {
        void showProgress();
        void hideProgress();
        void setAdapter(List<Result> allMoviesList);
        void showMessage(int message);
        void launchActivity(Result result);
        void noData(boolean shouldShow);
        void setToDate(String date);
        void setFromDate(String date);
    }

    interface MainPresenter extends RequestManager.ResponseListener<MoviesData> {
        void onClick(Result result);
        void onClick(View view, String toDate, String fromDate);

    }
}
