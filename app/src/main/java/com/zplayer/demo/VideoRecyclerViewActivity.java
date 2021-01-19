/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 下午2:12
 * ********************************************************
 */
package com.zplayer.demo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.widget.Toolbar;

import com.zcolin.gui.zrecyclerview.ZRecyclerView;
import com.zplayer.demo.adapter.SuperVideoAdapter;
import com.zplayer.demo.bean.VideoListBean;
import com.zplayer.library.ZListPlayer;
import com.zplayer.library.ZPlayer;

import java.util.ArrayList;

/**
 * 列表播放
 */
public class VideoRecyclerViewActivity extends BaseVideoRecycleViewActivity {
    private ZRecyclerView     zRecyclerView;
    private SuperVideoAdapter mRecyclerViewAdapter;
    private int mPage = 1;
    private Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.id_tool_bar);
        setSupportActionBar(toolbar);

        zRecyclerView.refreshWithPull();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_videorecycler;
    }

    @Override
    protected ZListPlayer initPlayer() {
        ZListPlayer player = findViewById(R.id.superlistplayer);
        player.getPlayer().setNetChangeListener(true)//设置监听手机网络的变化,这个参数是内部是否处理网络监听，和setOnNetChangeListener没有关系
              .setShowTopControl(false).setShowCenterControl(true).setSupportGesture(false).setScaleType(ZPlayer.SCALETYPE_FILLPARENT);

        //如果设置则使用指定的RecyclerView，否则使用默认的RecyclerView
        player.setRecyclerViewLayout(initPullRecyclerView());
        return player;
    }

    /**
     * 初始化自己定义的recyclerView
     */
    private ZRecyclerView initPullRecyclerView() {
        zRecyclerView = new ZRecyclerView(mActivity);
        zRecyclerView.setOnPullLoadMoreListener(new ZRecyclerView.PullLoadMoreListener() {
            @Override
            public void onRefresh() {
                mPage = 1;
                player.onRefresh();
                getDataFromShopList(mActivity, mPage);
                zRecyclerView.setNoMore(false);
            }

            @Override
            public void onLoadMore() {
                mPage = mPage + 1;
                getDataFromShopList(mActivity, mPage);
            }
        });

        return zRecyclerView;
    }

    /**
     * 制造假延时模拟服务器请求效果， 获取数据，添加到适配器
     */
    public void getDataFromShopList(final Activity activity, final int page) {
        new Handler().postDelayed(() -> {
            addDataToRecyclerView(setList(page), page == 1);
            zRecyclerView.setPullLoadMoreCompleted();
            if (page == 3) {
                zRecyclerView.setNoMore(true);
            }
        }, 1500);
    }

    /**
     * 适配器的初始话和数据更换
     */
    public void addDataToRecyclerView(ArrayList<VideoListBean> list, boolean isClear) {
        if (mRecyclerViewAdapter == null) {
            mRecyclerViewAdapter = new SuperVideoAdapter(mActivity);
            mRecyclerViewAdapter.addDatas(list);
            zRecyclerView.setAdapter(mRecyclerViewAdapter);
            mRecyclerViewAdapter.setPlayClick((position, data, rlPlayControl) -> player.onPlayClick(position, data.getVideoUrl(), rlPlayControl));
        } else {
            if (isClear) {
                mRecyclerViewAdapter.setDatas(list);
            } else {
                mRecyclerViewAdapter.addDatas(list);
            }
        }
    }

    /**
     * 制造假数据
     */
    public ArrayList<VideoListBean> setList(int page) {
        ArrayList<VideoListBean> dataList = new ArrayList<>();
        dataList.clear();
        VideoListBean bean1 = new VideoListBean();
        bean1.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=9502&editionType=normal");
        dataList.add(bean1);
        VideoListBean bean2 = new VideoListBean();
        bean2.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=9508&editionType=normal");
        dataList.add(bean2);
        VideoListBean bean3 = new VideoListBean();
        bean3.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=8438&editionType=normal");
        dataList.add(bean3);
        VideoListBean bean4 = new VideoListBean();
        bean4.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=8340&editionType=normal");
        dataList.add(bean4);
        VideoListBean bean5 = new VideoListBean();
        bean5.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=9392&editionType=normal");
        dataList.add(bean5);
        VideoListBean bean6 = new VideoListBean();
        bean6.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=7524&editionType=normal");
        dataList.add(bean6);
        VideoListBean bean7 = new VideoListBean();
        bean7.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=9444&editionType=normal");
        dataList.add(bean7);
        VideoListBean bean8 = new VideoListBean();
        bean8.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=9442&editionType=normal");
        dataList.add(bean8);
        VideoListBean bean9 = new VideoListBean();
        bean9.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=8530&editionType=normal");
        dataList.add(bean9);
        VideoListBean bean10 = new VideoListBean();
        bean10.setVideoUrl("http://baobab.wandoujia.com/api/v1/playUrl?vid=9418&editionType=normal");
        dataList.add(bean10);
        return dataList;
    }
}
