package com.my.shishir.demoapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.my.shishir.demoapp.model.MoviesData;
import com.my.shishir.demoapp.model.Result;
import com.my.shishir.demoapp.utility.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class SqliteDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "MovieDetails";
    private static final String TABLE_NAME = "Detail";
    private static final String KEY_ID = "id";
    private static final String KEY_VOTE_COUNT = "voteCount";
    private static final String KEY_VIDEO = "video";
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    private static final String KEY_TITLE = "title";
    private static final String KEY_POPULARITY = "popularity";
    private static final String KEY_POSTER_PATH = "posterPath";
    private static final String KEY_ORIGINAL_TITLE = "original_title";
    private static final String KEY_ORIGINAL_LANGUAGE = "original_language";
    private static final String KEY_GENREIDS = "geneIds";
    private static final String KEY_ADULT = "adult";
    private static final String KEY_OVERVIEW = "overview";
    private static final String KEY_RELEASED_DATE = "released";
    private static final String KEY_PAGE_NO = "page";

    private static final String[] COLUMNS = {KEY_ID,
            KEY_VOTE_COUNT, KEY_VIDEO,
            KEY_VOTE_AVERAGE,
            KEY_TITLE,
            KEY_POPULARITY,
            KEY_POSTER_PATH,
            KEY_ORIGINAL_TITLE,
            KEY_ORIGINAL_LANGUAGE,
            KEY_GENREIDS,
            KEY_ADULT,
            KEY_OVERVIEW,
            KEY_RELEASED_DATE,
            KEY_PAGE_NO
    };

    SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATION_TABLE = "CREATE TABLE " + TABLE_NAME + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY, "
                + KEY_VOTE_COUNT + " INTEGER,"
                + KEY_VIDEO + " INTEGER,"
                + KEY_VOTE_AVERAGE + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_POPULARITY + " TEXT,"
                + KEY_POSTER_PATH + " TEXT,"
                + KEY_ORIGINAL_TITLE + " TEXT,"
                + KEY_ORIGINAL_LANGUAGE + " TEXT,"
                + KEY_GENREIDS + " TEXT,"
                + KEY_ADULT + " INTEGER,"
                + KEY_OVERVIEW + " TEXT,"
                + KEY_RELEASED_DATE + " TEXT,"
                + KEY_PAGE_NO + " INTEGER" + ")";

        db.execSQL(CREATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you can implement here migration process
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    // This is not in use but here in case you need it in future
    public void deleteMovie(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // This is not in use but here in case you need it in future
    public MoviesData getMovie(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections
                new String[]{String.valueOf(id)}, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit

        if (cursor != null)
            cursor.moveToFirst();

        List<Result> movieList = new ArrayList<>();
        MoviesData moviesData = new MoviesData();
        Result result = new Result();

        result.setId(Integer.parseInt(Objects.requireNonNull(cursor).getString(0)));
        result.setVoteCount(cursor.getInt(1));
        result.setVideo(cursor.getInt(2) == 1);
        result.setVoteAverage(cursor.getFloat(3));
        result.setTitle(cursor.getString(4));
        result.setPopularity(cursor.getFloat(5));
        result.setPosterPath(cursor.getString(6));
        result.setOriginalTitle(cursor.getString(7));
        result.setOriginalLanguage(cursor.getString(8));
        result.setGenreIds(Utility.getIntegerArrayFromStringArrayList(
                Arrays.asList(cursor.getString(9).split(","))));
        result.setAdult(cursor.getInt(10) == 1);
        result.setOverview(cursor.getString(11));
        result.setReleaseDate(cursor.getString(12));

        movieList.add(result);

        moviesData.setPage(cursor.getInt(13));
        moviesData.setResults(movieList);

        cursor.close();
        db.close();

        return moviesData;
    }

    public List<MoviesData> getDatedData(String toDate, String fromDate) {
        List<MoviesData> moviesDataList = new LinkedList<>();

        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + "date(" + KEY_RELEASED_DATE + ") BETWEEN" + " \'" + fromDate + "\'" + " AND" + " \'" + toDate + "\'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor1 = db.rawQuery(query, null);

        if (cursor1 != null && cursor1.getCount() > 0) {
            cursor1.moveToFirst();
            List<Result> resultList = new LinkedList<>();

            do {
                Result result = new Result();
                result.setId(Integer.parseInt(Objects.requireNonNull(cursor1).getString(0)));
                result.setVoteCount(cursor1.getInt(1));
                result.setVideo(cursor1.getInt(2) == 1);
                result.setVoteAverage(cursor1.getFloat(3));
                result.setTitle(cursor1.getString(4));
                result.setPopularity(cursor1.getFloat(5));
                result.setPosterPath(cursor1.getString(6));
                result.setOriginalTitle(cursor1.getString(7));
                result.setOriginalLanguage(cursor1.getString(8));
                result.setGenreIds(Utility.getIntegerArrayFromStringArrayList(
                        Arrays.asList(cursor1.getString(9).split(","))));
                result.setAdult(cursor1.getInt(10) == 1);
                result.setOverview(cursor1.getString(11));
                result.setReleaseDate(cursor1.getString(12));

                resultList.add(result);

            } while (cursor1.moveToNext());

            MoviesData moviesData = new MoviesData();
            moviesData.setResults(resultList);

            moviesDataList.add(moviesData);

            cursor1.close();
        }

        return moviesDataList;
    }

    public List<MoviesData> getAllData() {
        List<MoviesData> moviesDataList = new LinkedList<>();

        String query = "SELECT MAX(" + KEY_PAGE_NO + ")  FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int i = cursor.getInt(0);
            cursor.close();
            for (int j = 1; j <= i; j++) {
                List<Result> resultList = new LinkedList<>();

                Cursor cursor1 = db.query(TABLE_NAME, // a. table
                        COLUMNS, // b. column names
                        " page = ?", // c. selections
                        new String[]{String.valueOf(j)}, // d. selections args
                        null, // e. group by
                        null, // f. having
                        null, // g. order by
                        null); // h. limit

                if (cursor1 != null && cursor1.moveToFirst()) {
                    do {
                        Result result = new Result();
                        result.setId(Integer.parseInt(Objects.requireNonNull(cursor1).getString(0)));
                        result.setVoteCount(cursor1.getInt(1));
                        result.setVideo(cursor1.getInt(2) == 1);
                        result.setVoteAverage(cursor1.getFloat(3));
                        result.setTitle(cursor1.getString(4));
                        result.setPopularity(cursor1.getFloat(5));
                        result.setPosterPath(cursor1.getString(6));
                        result.setOriginalTitle(cursor1.getString(7));
                        result.setOriginalLanguage(cursor1.getString(8));
                        result.setGenreIds(Utility.getIntegerArrayFromStringArrayList(
                                Arrays.asList(cursor1.getString(9).split(","))));
                        result.setAdult(cursor1.getInt(10) == 1);
                        result.setOverview(cursor1.getString(11));
                        result.setReleaseDate(cursor1.getString(12));

                        resultList.add(result);

                    } while (cursor1.moveToNext());

                    MoviesData moviesData = new MoviesData();
                    moviesData.setResults(resultList);

                    moviesDataList.add(moviesData);

                    cursor1.close();
                }
            }
        }
        db.close();
        return moviesDataList;
    }

    public void addMovie(MoviesData moviesData) {
        try {

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();

            List<Result> movieDataList = moviesData.getResults();

            for (Result result : movieDataList) {
                values.put(KEY_ID, result.getId());
                values.put(KEY_VOTE_COUNT, result.getVoteCount());
                values.put(KEY_VIDEO, result.getVideo() ? 1 : 0);
                values.put(KEY_VOTE_AVERAGE, result.getVoteAverage());
                values.put(KEY_TITLE, result.getTitle());
                values.put(KEY_POPULARITY, result.getPopularity());
                values.put(KEY_POSTER_PATH, result.getPosterPath());
                values.put(KEY_ORIGINAL_TITLE, result.getOriginalTitle());
                values.put(KEY_ORIGINAL_LANGUAGE, result.getOriginalLanguage());
                values.put(KEY_GENREIDS, Utility.getStringFromArrayList(result.getGenreIds()));
                values.put(KEY_ADULT, result.getAdult() ? 1 : 0);
                values.put(KEY_OVERVIEW, result.getOverview());
                values.put(KEY_RELEASED_DATE, result.getReleaseDate());
                values.put(KEY_PAGE_NO, moviesData.getPage());

                try {
                    // insert
                    db.insertOrThrow(TABLE_NAME, null, values);
                } catch (Exception e) {
                    Log.i("exception", e.toString());
                }
            }

            db.close();
        } catch (Exception e) {
            Log.e("addMovie : error ", e.toString());
        }
    }
}
