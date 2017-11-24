package com.ljt.latestnews.news.model;

import android.util.Log;

import com.ljt.latestnews.main.bean.NewsBean;
import com.ljt.latestnews.main.commons.Urls;
import com.ljt.latestnews.main.utils.NewsJsonUtils;
import com.ljt.latestnews.main.utils.OkHttpUtils;
import com.ljt.latestnews.news.widget.NewsFragment;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/20.
 */

public class NewsModelImp implements NewsModel{
    public static String TAG= NewsModelImp.class.getSimpleName();

    @Override
    public void loadNews(String url, final int type, final OnLoadNewsListListener listener) {
        OkHttpUtils.ResultCallback<String> loadNewsCallback=new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG,TAG+" type="+ type+" ---loadNews-->>> response= "+response);
                List<NewsBean> newsBeanList = NewsJsonUtils.readJsonNewsBeans(response, getID(type));
                listener.onSuccess(newsBeanList);
            }

            @Override
            public void onFailure(Exception e) {
                Log.d(TAG,TAG+" ---loadNews-->>>e= "+e.toString());
                listener.onFailure("load news list failure.", e);
            }
        };
        OkHttpUtils.get(url,loadNewsCallback);
    }

    @Override
    public void loadNewsDetail(String url, OnLoadNewsDetailListener listener) {

    }

    /**
     * 获取ID
     * @param type
     * @return
     */
    private String getID(int type) {
        String id;
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                id = Urls.TOP_ID;
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                id = Urls.NBA_ID;
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                id = Urls.CAR_ID;
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                id = Urls.JOKE_ID;
                break;
            default:
                id = Urls.TOP_ID;
                break;
        }
        return id;
    }
}
