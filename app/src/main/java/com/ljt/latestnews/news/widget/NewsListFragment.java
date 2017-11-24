package com.ljt.latestnews.news.widget;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ljt.latestnews.R;
import com.ljt.latestnews.main.bean.NewsBean;
import com.ljt.latestnews.main.commons.Urls;
import com.ljt.latestnews.news.adapter.NewsAdapter;
import com.ljt.latestnews.news.presenter.NewsPresenter;
import com.ljt.latestnews.news.presenter.NewsPresenterImp;
import com.ljt.latestnews.news.view.NewsView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsListFragment extends Fragment implements NewsView,SwipeRefreshLayout.OnRefreshListener{

    public static String TAG= NewsListFragment.class.getSimpleName();

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<NewsBean> mData;
    private NewsPresenter mNewsPresenter;
    private NewsAdapter mAdapter;

    private int mType = NewsFragment.NEWS_TYPE_TOP;
    private int pageIndex = 0;


    public NewsListFragment() {
        // Required empty public constructor
    }
    public static NewsListFragment newInstance(int type){
         Log.d(TAG,TAG+" newInstance----->>>type=  "+type);
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsPresenter=new NewsPresenterImp(this);
        mType=getArguments().getInt("type");
         Log.d(TAG,TAG+" ----->>>mType= "+mType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_list, container, false);
        mSwipeRefreshWidget=(SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary,
                R.color.primary_dark,R.color.primary_light,
                R.color.accent);

        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter=new NewsAdapter(getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(mOntemClickListener);
        mRecyclerView.addOnScrollListener(mOnScrollListener);
        mRecyclerView.setAdapter(mAdapter);
        onRefresh();

        return view;
    }
    private RecyclerView.OnScrollListener mOnScrollListener=new RecyclerView.OnScrollListener() {

       private int lastVisibleItem;
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
             Log.d(TAG,TAG+" ----->>>onScrollStateChanged( ");
             if(newState==RecyclerView.SCROLL_STATE_IDLE
                     && lastVisibleItem+1==mAdapter.getItemCount()
                     && mAdapter.isShowFooter()){
              //加载更多
                  Log.d(TAG,TAG+" ----->>>loading more data ");
                 mNewsPresenter.loadNews(mType,pageIndex+ Urls.PAZE_SIZE);
             }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            Log.d(TAG,TAG+" ----->>>onScrolled( ");
            lastVisibleItem=mLayoutManager.findLastVisibleItemPosition();
        }
    };


    private NewsAdapter.OnItemClickListener mOntemClickListener=new NewsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            if(mData.size()<=0){
                return;
            }
            NewsBean newsBean = mAdapter.getItem(position);
            Intent intent = new Intent(getActivity(), NewsDetailActivity.class);
            intent.putExtra("news",newsBean);

            View transitionView = view.findViewById(R.id.ivNews);
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), transitionView,
                    getString(R.string.transition_news_img));

            ActivityCompat.startActivity(getActivity(),intent,optionsCompat.toBundle());


        }
    };


    @Override
    public void onRefresh() {
        pageIndex=0;
        if(mData !=null){
            mData.clear();
        }
        mNewsPresenter.loadNews(mType,pageIndex);
    }

    @Override
    public void showProgress() {
     Log.d(TAG,TAG+" ----->>>showProgress( ");
      mSwipeRefreshWidget.setRefreshing(true);
    }

    @Override
    public void addNews(List<NewsBean> newsList) {
        Log.d(TAG,TAG+" ----->>>addNews(  newsList= "+newsList.size());
        mAdapter.isShowFooter(true);
        if(mData==null){
            mData=new ArrayList<>();
        }
        mData.addAll(newsList);
        if(pageIndex==0){
            mAdapter.setmDate(mData);
        }else{
            //
            if(newsList==null || newsList.size()==0){
                mAdapter.isShowFooter(false);
            }
            mAdapter.notifyDataSetChanged();
        }
        pageIndex+=Urls.PAZE_SIZE;
    }

    @Override
    public void hideProgress() {
        Log.d(TAG,TAG+" ----->>>hideProgress( ");
        mSwipeRefreshWidget.setRefreshing(false);
    }

    @Override
    public void showLoadFailMsg() {
        Log.d(TAG,TAG+" ----->>>showLoadFailMsg( ");
        if(pageIndex==0){
            mAdapter.isShowFooter(false);
            mAdapter.notifyDataSetChanged();
        }
        View view=getActivity()==null?mRecyclerView.getRootView():getActivity()
                .findViewById(R.id.drawer_layout);
        Snackbar.make(view, getString(R.string.load_fail), Snackbar.LENGTH_SHORT).show();
    }

}
