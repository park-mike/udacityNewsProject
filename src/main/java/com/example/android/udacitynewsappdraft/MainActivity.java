package com.example.android.udacitynewsappdraft;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String APIurl = "http://content.guardianapis.com/search?q=";
    private static final int NEWS_LOADER_NUMBER = 1;
    public ArrayList<News> news;
    ListView newsListView;
    private String totalSearch = "";
    private TextView mEmptyTextView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = (ListView) findViewById(R.id.newsList);
        newsAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(newsAdapter);
        mEmptyTextView = (TextView) findViewById(R.id.waiting_for_input);
        newsListView.setEmptyView(mEmptyTextView);

        final EditText userSearch = (EditText) findViewById(R.id.search_field);
        Button searchButton = (Button) findViewById(R.id.search);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userSearched = userSearch.getText().toString();

                String searchedFor;

                try {
                    searchedFor = java.net.URLEncoder.encode(userSearched, "UTF-8");
                    totalSearch = APIurl + searchedFor + "&show-tags=contributor&&api-key=test";
                    Log.e(LOG_TAG, totalSearch);
                    Log.d(LOG_TAG, "user has searched");
                    getData();

                } catch (UnsupportedEncodingException e) {
                    Log.e(LOG_TAG, "Error with creating URL with URLEncoder", e);

                }
            }
        });
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_NUMBER, null, this);
    }


    private void setErrorView() {
        View nowLoading = findViewById(R.id.waiting_for_input);
        nowLoading.setVisibility(View.GONE);

        Context context = getApplicationContext();
        String text = "Can not connect to network";
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }


    /**
     * @Override public boolean onCreateOptionsMenu(Menu menu) {
     * getMenuInflater().inflate(R.menu.main, menu);
     * return true;
     * }
     * @Override public boolean onOptionsItemSelected(MenuItem item) {
     * int id = item.getItemId();
     * if (id == R.id.action_settings) {
     * Intent settingsIntent = new Intent(this, SettingsActivity.class);
     * startActivity(settingsIntent);
     * return true;
     * }
     * return super.onOptionsItemSelected(item);
     * }
     **/

    public void getData() {


        ConnectivityManager connect = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = connect.getActiveNetworkInfo();

        ListView news = (ListView) findViewById(R.id.newsList);
        if (network != null && network.isConnected()) {
            newsAdapter = new NewsAdapter(this, new ArrayList<News>());

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.restartLoader(NEWS_LOADER_NUMBER, null, this);

            news.setAdapter(newsAdapter);

            news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    News currentNews = newsAdapter.getItem(position);
                    Uri newsURL = Uri.parse(currentNews.getUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsURL);
                    startActivity(websiteIntent);
                }
            });

        } else {
            Log.e(LOG_TAG, "error in get data method");
            setErrorView();
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new LoaderNews(this, totalSearch);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsList) {
        View waitingForInput = findViewById(R.id.waiting_for_input);
        waitingForInput.setVisibility(View.GONE);

        if (newsList != null && !newsList.isEmpty()) {
            newsAdapter.addAll(newsList);
        } else {
            View nowLoading = findViewById(R.id.waiting_for_input);
            nowLoading.setVisibility(View.GONE);
            newsAdapter.clear();
        }
        Log.v("my_tag", "onLoadFinished newsList is:" + newsList.size());
    }

    @Override
    public void onBackPressed() {
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(totalSearch, totalSearch);
        savedInstanceState.putParcelableArrayList("newsList", news);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        totalSearch = savedInstanceState.getString(totalSearch);
        if (savedInstanceState != null) {
            news = savedInstanceState.getParcelableArrayList("newsList");
        } else {
            news = new ArrayList<>();
        }
    }


    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
        getSupportLoaderManager().destroyLoader(NEWS_LOADER_NUMBER);
    }

}
