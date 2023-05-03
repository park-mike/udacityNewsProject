package com.example.android.udacitynewsappdraft;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by MP on 4/3/2017.
 */

public class NewsAdapter extends ArrayAdapter<News> {
    public NewsAdapter(Context context, ArrayList<News> newsListinAdapter) {
        super(context, 0, newsListinAdapter);
    }

    @Override
    public View getView(int position, View currentView, ViewGroup parent) {

        View newsInfo = currentView;


        if (newsInfo == null) {
            newsInfo = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        News currentNews = getItem(position);

        TextView authors = (TextView) newsInfo.findViewById(R.id.news_author);
        String authorsName = currentNews.getAuthor();
        authors.setText(authorsName);

        TextView title = (TextView) newsInfo.findViewById(R.id.news_title);
        String titleText = currentNews.getTitle();
        title.setText(titleText);

        TextView url = (TextView) newsInfo.findViewById(R.id.news_url);
        String urlText = currentNews.getUrl();
        url.setText(urlText);

        return newsInfo;


        /**

         newsInfo.newsTitle = (TextView) currentView.findViewById(R.id.news_title);
         newsInfo.newsAuthor = (TextView) currentView.findViewById(R.id.news_author);
         newsInfo.newsURL = (TextView) currentView.findViewById(R.id.news_url);

         currentView.setTag(newsInfo);
         } else {
         newsInfo = (ViewHolder) currentView.getTag();
         }

         News currentNews = getItem(position);

         String newsTitle = currentNews.getTitle();
         String newsAuthor = currentNews.getAuthor();
         String newsURL = currentNews.getUrl();

         newsInfo.newsTitle.setText(newsTitle);
         newsInfo.newsAuthor.setText(newsAuthor);
         newsInfo.newsURL.setText(newsURL);

         return currentView;
         }

         private static class ViewHolder {
         TextView newsTitle;
         TextView newsAuthor;
         TextView newsURL;

         **/
    }
}
