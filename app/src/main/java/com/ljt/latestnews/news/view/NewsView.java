package com.ljt.latestnews.news.view;

import com.ljt.latestnews.main.bean.NewsBean;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/20.
 */

public interface NewsView {
    void showProgress();

    void addNews(List<NewsBean> newsList);

    void hideProgress();

    void showLoadFailMsg();
}
