package com.ljt.latestnews.news.model;

/**
 * Created by ${JT.L} on 2017/11/20.
 */

public interface NewsModel {
    void loadNews(String url,int type,OnLoadNewsListListener listener);
    void loadNewsDetail(String url,OnLoadNewsDetailListener listener);
}
