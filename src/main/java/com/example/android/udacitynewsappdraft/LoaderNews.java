package com.example.android.udacitynewsappdraft;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by MP on 3/31/2017.
 */

public class LoaderNews extends AsyncTaskLoader {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private String mURL;

    public LoaderNews(Context context, String url) {
        super(context);
        mURL = url;
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        if (mURL == null) {
            return null;
        }

        Log.d(LOG_TAG, "load in background has returned news");
        return API.fetchNewsData(mURL);

    }

}
