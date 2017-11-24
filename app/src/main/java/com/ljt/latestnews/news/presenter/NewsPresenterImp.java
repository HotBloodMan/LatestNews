package com.ljt.latestnews.news.presenter;

import android.util.Log;

import com.ljt.latestnews.main.bean.NewsBean;
import com.ljt.latestnews.main.commons.Urls;
import com.ljt.latestnews.news.model.NewsModel;
import com.ljt.latestnews.news.model.NewsModelImp;
import com.ljt.latestnews.news.model.OnLoadNewsListListener;
import com.ljt.latestnews.news.view.NewsView;
import com.ljt.latestnews.news.widget.NewsFragment;

import java.util.List;

/**
 * Created by ${JT.L} on 2017/11/20.
 */

public class NewsPresenterImp implements NewsPresenter,OnLoadNewsListListener {

    public static String TAG= NewsPresenterImp.class.getSimpleName();
    private NewsView mNewsView;
    private NewsModel mNewsModel;

    public NewsPresenterImp(NewsView newsView){
        this.mNewsView=newsView;
        this.mNewsModel=new NewsModelImp();
    }

    @Override
    public void loadNews(final int type, final int page) {
        String url = getUrl(type, page);
         Log.d(TAG,TAG+" type"+type+" ----->>>url= "+url.toString());
        //只有第一页时才显示进度条
        if(page==0){
            mNewsView.showProgress();
        }
        mNewsModel.loadNews(url,type,this);
    }
    //根据类别和页面索引创建url
    private String getUrl(int type,int pageIndex){
        StringBuffer sb = new StringBuffer();
        switch (type) {
            case NewsFragment.NEWS_TYPE_TOP:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
            case NewsFragment.NEWS_TYPE_NBA:
                sb.append(Urls.COMMON_URL).append(Urls.NBA_ID);
                break;
            case NewsFragment.NEWS_TYPE_CARS:
                sb.append(Urls.COMMON_URL).append(Urls.CAR_ID);
                break;
            case NewsFragment.NEWS_TYPE_JOKES:
                sb.append(Urls.COMMON_URL).append(Urls.JOKE_ID);
                break;
            default:
                sb.append(Urls.TOP_URL).append(Urls.TOP_ID);
                break;
        }
        sb.append("/").append(pageIndex).append(Urls.END_URL);
        return sb.toString();
    }

    @Override
    public void onSuccess(List<NewsBean> list) {
        mNewsView.hideProgress();
        mNewsView.addNews(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mNewsView.hideProgress();
        mNewsView.showLoadFailMsg();
    }

}
