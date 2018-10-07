package com.my.shishir.demoapp.api;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.my.shishir.demoapp.listener.TaskListener;
import com.my.shishir.demoapp.model.MoviesData;
import com.my.shishir.demoapp.utility.Utility;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.my.shishir.demoapp.api.ArrayOrEmptyStringTypeAdapterFactory.getArrayOrEmptyStringTypeAdapterFactory;

/**
 * This class has responsibility to make request and return response
 * after parsing to presenter
 */
public class RequestManager implements TaskListener<List<MoviesData>> {

    private final TaskListener<List<MoviesData>> taskListener;
    private final ResponseListener<MoviesData> responseListener;

    public RequestManager(ResponseListener<MoviesData> responseListener) {
        taskListener = this;
        this.responseListener = responseListener;
    }

    public void callApi(int page) {
        try {
            RequestExecutor requestExecutor
                    = new RequestExecutor(taskListener,
                    getRetroClient(), page, Utility.isNetworkConnectionAvailable());
            requestExecutor.execute();
        } catch (Exception e) {
            Log.i(getClass().getName(), e.toString());
            onFailure(new ArrayList<MoviesData>(), Utility.OTHER_ERROR_CODE);
        }
    }


    public void dateFilter(String toDate, String fromData) {
        try {
            RequestExecutor requestExecutor
                    = new RequestExecutor(taskListener,
                    getRetroClient(), 0, Utility.isNetworkConnectionAvailable());

            requestExecutor.dateFilter(toDate, fromData);
        } catch (Exception e) {
            Log.i(getClass().getName(), e.toString());
            onFailure(new ArrayList<MoviesData>(), Utility.OTHER_ERROR_CODE);
        }
    }

    private Retrofit getRetroClient() {
        // For logging or tracking request
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // To set custom timeout
        final OkHttpClient.Builder okHttpClient = new okhttp3.OkHttpClient().newBuilder();
        okHttpClient.readTimeout(60, TimeUnit.SECONDS);
        okHttpClient.writeTimeout(60, TimeUnit.SECONDS);
        okHttpClient.connectTimeout(60, TimeUnit.SECONDS);

        okHttpClient.addInterceptor(interceptor);

        return new Retrofit.Builder()
                .baseUrl(Utility.BASE_URL)

                // To handle parameter expected as list but may be empty in some cases
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .registerTypeAdapterFactory(getArrayOrEmptyStringTypeAdapterFactory())
                        .create()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient.build())
                .build();
    }

    @Override
    public void onSuccess(@NonNull List<MoviesData> result) {
        responseListener.onResponse(result, true, Utility.NO_ERROR_CODE);
    }

    @Override
    public void onFailure(@NonNull List<MoviesData> moviesDataList, int errorType) {
        responseListener.onResponse(moviesDataList, false, errorType);
    }

    // Data type is fixed here as of now, we do not have any other server request
    // in the application
    // here reason is used for error occurred due to network or any other
    // since api doesn't support any error message on failure we are using error code '0'
    // and failure due to network is '1'
    public interface ResponseListener<T> {
        void onResponse(@NonNull List<MoviesData> moviesDataList, boolean success, int reason);
    }
}
