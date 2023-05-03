package com.example.android.udacitynewsappdraft;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by MP on 3/31/2017.
 */

public class API {

    private static final String LOG_TAG = API.class.getSimpleName();
    private static final String APIurl = "http://content.guardianapis.com/search?q=";

    public static String webTitle;
    public static String webUrl;
    public static String authorName;
    private static String stringUrl;

    private API() {
    }

    public static ArrayList<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
        }
        ArrayList<News> newsList = extractFeatureFromJson(jsonResponse);
        Log.v("my_tag", "onLoadFinished in API fetchNewsData newsList is:" + newsList.size());
        return newsList;
    }

    private static URL createUrl(String stringUrl) {
        API.stringUrl = stringUrl;
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {

        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(20000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromSteam(inputStream);
            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;

    }

    private static String readFromSteam(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }


    public static ArrayList<News> extractFeatureFromJson(String jsonResponse) {

        ArrayList<News> newsList = new ArrayList<>();

        try {
            JSONObject currentNews = new JSONObject(jsonResponse);
            if (currentNews.has("response")) {
                JSONObject response = currentNews.getJSONObject("response");
                if (response.has("results")) {
                    JSONArray resultsArray = response.getJSONArray("results");

                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject newsJSONInfo = resultsArray.getJSONObject(i);
                        if (newsJSONInfo.has("webTitle")) {
                            webTitle = newsJSONInfo.getString("webTitle");
                        }
                        if (newsJSONInfo.has("webUrl")) {
                            webUrl = newsJSONInfo.getString("webUrl");
                        }
                        if (newsJSONInfo.has("tags")) {
                            JSONArray tagsArray = newsJSONInfo.getJSONArray("tags");
                            for (int j = 0; j < tagsArray.length(); j++) {
                                JSONObject tagInfo = tagsArray.getJSONObject(j);
                                if (tagInfo.has("webTitle")) {
                                    authorName = tagInfo.getString("webTitle");
                                }
                            }
                        }
                        newsList.add(new News(webTitle, authorName, webUrl));
                    }

                }

            }
        } catch (JSONException e) {
        }
        Log.v("my_tag", "onLoadFinished in API newsList is:" + newsList.size());
        return newsList;

    }


}


