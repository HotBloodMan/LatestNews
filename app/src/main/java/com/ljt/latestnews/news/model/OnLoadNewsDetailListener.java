package com.ljt.latestnews.news.model;

import com.ljt.latestnews.main.bean.NewsBean;
import com.ljt.latestnews.main.bean.NewsDetailBean;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/20.
 */

public interface OnLoadNewsDetailListener {
    void onSuccess(List<NewsDetailBean> list);
    void onFailure(String msg, Exception e);
}
