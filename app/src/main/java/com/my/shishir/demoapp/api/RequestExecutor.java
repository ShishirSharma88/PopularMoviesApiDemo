package com.my.shishir.demoapp.api;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.my.shishir.demoapp.database.DataHandler;
import com.my.shishir.demoapp.listener.TaskListener;
import com.my.shishir.demoapp.model.MoviesData;
import com.my.shishir.demoapp.utility.Utility;

import java.util.List;

import retrofit2.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * This class is responsible for server communication
 */
class RequestExecutor {

    private final String TAG = getClass().getName();
    private final TaskListener<List<MoviesData>> taskListener;
    private final Retrofit retrofit;
    private final int page;
    private final boolean isNetworkAvailable;

    RequestExecutor(@NonNull TaskListener<List<MoviesData>> taskListener,
                    @NonNull Retrofit retrofit, int page, boolean isNetworkAvailable) {
        this.taskListener = taskListener;
        this.retrofit = retrofit;
        this.page = page;
        this.isNetworkAvailable = isNetworkAvailable;
    }

    void dateFilter(String toDate, String fromDate) {
        new DataHandler(null, taskListener, Utility.ALL_DATA_LOADED, toDate, fromDate)
                .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    void execute() {
        if (!isNetworkAvailable) {
            new DataHandler(null, taskListener, Utility.NETWORK_ERROR_CODE, null, null)
                    .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            return;
        }

        RetroInterface retroInterface = retrofit.create(RetroInterface.class);

        Observable<MoviesData> observable = retroInterface.getData(Utility.API_KEY, Utility.LANGUAGE, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Observer<MoviesData>() {
            @Override
            public void onCompleted() {
                Log.i(TAG, "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError : " + e.toString());
                new DataHandler(null, taskListener, Utility.OTHER_ERROR_CODE, null, null)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }

            @Override
            public void onNext(MoviesData moviesData) {
                Log.i(TAG, "onNext : ");
                new DataHandler(moviesData, taskListener,
                        (moviesData.getResults().isEmpty() ? Utility.ALL_DATA_LOADED : Utility.NO_ERROR_CODE), null, null)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        });
    }
}
