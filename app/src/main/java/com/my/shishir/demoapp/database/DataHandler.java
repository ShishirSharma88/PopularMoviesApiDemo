package com.my.shishir.demoapp.database;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.my.shishir.demoapp.AppContext;
import com.my.shishir.demoapp.listener.TaskListener;
import com.my.shishir.demoapp.model.MoviesData;

import java.util.List;

/**
 * This class is responsible for inserting and getting data into the Database
 */
public class DataHandler extends AsyncTask<Void, Void, Boolean> {

    private final MoviesData moviesData;
    private final TaskListener<List<MoviesData>> taskListener;
    private List<MoviesData> allMoviesList;
    private boolean isSuccessful = false;
    private final int errorType;
    private final String toDate;
    private final String fromDate;

    public DataHandler(MoviesData moviesData,
                       TaskListener<List<MoviesData>> taskListener, int errorType, String toDate, String fromdate) {
        this.moviesData = moviesData;
        this.taskListener = taskListener;
        this.errorType = errorType;
        this.toDate = toDate;
        this.fromDate = fromdate;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        try {
            SqliteDatabase sqliteDatabase = new SqliteDatabase(AppContext.getContext());
            if (moviesData != null && !moviesData.getResults().isEmpty()) {
                sqliteDatabase.addMovie(moviesData);

                isSuccessful = true;
            }

            if (!TextUtils.isEmpty(toDate) && !TextUtils.isEmpty(fromDate)) {
                allMoviesList = sqliteDatabase.getDatedData(toDate, fromDate);
            } else {
                allMoviesList = sqliteDatabase.getAllData();
            }
        } catch (Exception e) {
            Log.e("DataHandler:", e.toString());
        }

        return isSuccessful;
    }

    @Override
    protected void onPostExecute(Boolean isSuccessful) {
        super.onPostExecute(isSuccessful);

        if (isSuccessful) {
            taskListener.onSuccess(allMoviesList);
        } else {
            taskListener.onFailure(allMoviesList, errorType);
        }
    }
}
