package com.my.shishir.demoapp;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.my.shishir.demoapp.api.RequestManager;
import com.my.shishir.demoapp.model.MoviesData;
import com.my.shishir.demoapp.model.Result;
import com.my.shishir.demoapp.utility.Utility;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainPresenterImpl implements MovieListContract.MainPresenter {


    private static final int THRESHOLD = 10;

    private final MovieListContract.MainView mainView;
    private final RequestManager requestManager;

    private boolean isLoading;
    private int page = 1;
    private final List<Result> allMoviesDataList;
    private boolean isAllDataLoaded;

    MainPresenterImpl(MovieListContract.MainView mainView) {
        this.mainView = mainView;
        allMoviesDataList = new ArrayList<>();

        RequestManager.ResponseListener<MoviesData> responseListener = this;
        requestManager = new RequestManager(responseListener);
    }

    private void startProcess(int offset) {
        requestManager.callApi(offset);
    }

    public void initiate() {
        mainView.showProgress();
        startProcess(page);
    }

    public void onRefresh() {
        startProcess(page);
    }

    @Override
    public void onClick(Result result) {
        mainView.launchActivity(result);
    }

    @Override
    public void onClick(View view, String toDate, String fromDate) {

        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        final int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        switch (view.getId()) {
            case R.id.text_from_date:
                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(
                        view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthString = "" + (month + 1);
                        String dayString = "" + dayOfMonth;

                        if (month + 1 < 10) {
                            monthString = "0" + (month + 1);
                        }

                        if (dayOfMonth < 10) {

                            dayString = "0" + dayOfMonth;
                        }

                        mainView.setFromDate(year + "-" + monthString + "-" + dayString);
                    }
                }, mYear, mMonth, mDay);

                fromDatePickerDialog.show();
                break;

            case R.id.text_to_date:
                DatePickerDialog toDatePickerDialogg = new DatePickerDialog(
                        view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String monthString = "" + (month + 1);
                        String dayString = "" + dayOfMonth;

                        if (month + 1 < 10) {
                            monthString = "0" + (month + 1);
                        }

                        if (dayOfMonth < 10) {

                            dayString = "0" + dayOfMonth;
                        }

                        mainView.setToDate(year + "-" + monthString + "-" + dayString);
                    }
                }, mYear, mMonth, mDay);

                toDatePickerDialogg.show();
                break;

            case R.id.search_by_date:
                try {
                    if (!TextUtils.isEmpty(toDate) && !TextUtils.isEmpty(fromDate)) {

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                        Date todate;
                        todate = sdf.parse(toDate);

                        Date fromdate = sdf.parse(fromDate);

                        if (fromdate.before(todate)) {
                            requestManager.dateFilter(toDate, fromDate);
                        } else {
                            mainView.showMessage(R.string.check_to_from_date);
                        }
                    } else {
                        mainView.showMessage(R.string.please_check_date);
                    }
                } catch (ParseException e) {
                    Log.i("searchBydate:error", e.toString());
                }
                break;
        }

    }

    public void onScrolled(RecyclerView recyclerView) {

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

        int visibleItemCount = linearLayoutManager.getChildCount();
        int totalItemCount = linearLayoutManager.getItemCount();
        int pastVisibleItems = linearLayoutManager.findFirstVisibleItemPosition();

        if (!isAllDataLoaded
                && (visibleItemCount + pastVisibleItems + THRESHOLD >= totalItemCount)
                && !isLoading) {
            isLoading = true;
            mainView.showProgress();

            startProcess(page);
        }
    }

    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        // Not in use as of now
    }

    private void combineArrayList(List<MoviesData> moviesDataList) {
        allMoviesDataList.clear();

        if (moviesDataList == null) {
            return;
        }

        for (MoviesData moviesData : moviesDataList) {
            allMoviesDataList.addAll(moviesData.getResults());
        }

        Collections.sort(allMoviesDataList, new Comparator<Result>() {
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            @Override
            public int compare(Result o1, Result o2) {
                try {
                    return (dateFormat.parse(o2.getReleaseDate())).compareTo(dateFormat.parse(o1.getReleaseDate()));
                } catch (ParseException e) {
                    throw new IllegalArgumentException(e);
                }
            }
        });
    }

    @Override
    public void onResponse(@NonNull List<MoviesData> moviesDataList, boolean success,
                           int errorType) {
        mainView.hideProgress();

        mainView.noData(moviesDataList.isEmpty());
        combineArrayList(moviesDataList);
        mainView.setAdapter(allMoviesDataList);


        isLoading = false;

        // In-case of success we update page value
        if (success) {
            if (page <= moviesDataList.size()) {
                page = moviesDataList.size() + 1;
            }

            return;
        }

        switch (errorType) {
            case Utility.ALL_DATA_LOADED:
                isAllDataLoaded = true;
                break;
            case Utility.OTHER_ERROR_CODE:
                mainView.showMessage(R.string.download_failed);
                break;
            case Utility.NETWORK_ERROR_CODE:
                mainView.showMessage(R.string.no_internet);
                break;
        }
    }
}
