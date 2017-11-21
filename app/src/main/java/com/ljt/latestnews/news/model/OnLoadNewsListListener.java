package com.ljt.latestnews.news.model;

import com.ljt.latestnews.main.bean.NewsBean;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/20.
 */

public interface OnLoadNewsListListener {
    void onSuccess(List<NewsBean> list);
    void onFailure(String msg,Exception e);
}
