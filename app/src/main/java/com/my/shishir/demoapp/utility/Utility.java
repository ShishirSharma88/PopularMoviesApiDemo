package com.my.shishir.demoapp.utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;

import com.my.shishir.demoapp.AppContext;
import com.my.shishir.demoapp.MovieListAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This is the class to hold static data used in app mostly for urls, Constants, Keys etc
 */
public class Utility {

    public final static int NETWORK_ERROR_CODE = 1;
    public final static int OTHER_ERROR_CODE = 0;
    public final static int NO_ERROR_CODE = -1;

    public final static String KEY_MOVIE_DETAIL = "movie_detail";

    public static final int ALL_DATA_LOADED = 2;

    public static final String API_KEY = "";

    public static final String BASE_URL = "https://api.themoviedb.org/";

    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w185";

    public static final String LANGUAGE = "en-US";

    public static boolean isNetworkConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager) AppContext.getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo info = cm != null ? cm.getActiveNetworkInfo() : null;
        return info != null &&
                (info.getState() == NetworkInfo.State.CONNECTED
                        || info.getState() == NetworkInfo.State.CONNECTING);

    }

    public static void imageRounder(final Bitmap bitmap,
                                    final Resources resource,
                                    @NonNull final MovieListAdapter.ProcessedBitmapListener processedBitmapListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                RoundedBitmapDrawable imageDrawable
                        = RoundedBitmapDrawableFactory.create(resource, bitmap);
                imageDrawable.setCircular(true);
                processedBitmapListener.onProcessDone(imageDrawable);
            }
        }).start();
    }

    public static ArrayList<Integer> getIntegerArrayFromStringArrayList(List<String> stringArray) {
        ArrayList<Integer> result = new ArrayList<>();
        for (String stringValue : stringArray) {
            try {
                //Convert String to Integer, and store it into integer array list.
                result.add(Integer.parseInt(stringValue.trim()));
            } catch (NumberFormatException nfe) {
                //System.out.println("Could not parse " + nfe);
                Log.w("NumberFormat", "Parsing failed! " + stringValue + " can not be an integer");
            }
        }
        return result;
    }

    public static String getStringFromArrayList(List<Integer> list) {
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Integer> iter = list.iterator();
        while (iter.hasNext()) {
            stringBuilder.append(iter.next());
            if (iter.hasNext()) {
                stringBuilder.append(",");
            }
        }
        return stringBuilder.toString();
    }
}
