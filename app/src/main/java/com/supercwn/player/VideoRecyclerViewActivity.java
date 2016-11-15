/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-19 下午1:05
 * *********************************************************
 */
package com.supercwn.player;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.supercwn.player.adapter.SuperVideoAdapter;
import com.supercwn.player.bean.VideoListBean;
import com.superplayer.library.SuperListPlayer;
import com.superplayer.library.SuperPlayer;

import java.util.ArrayList;

/**
 * 列表播放
 */
public class VideoRecyclerViewActivity extends BaseVideoRecycleViewActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.id_tool_bar);
        setSupportActionBar(toolbar);
        
        setData();
        initAdapter();
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_videorecycler;
    }

    @Override
    protected SuperListPlayer initPlayer() {
        SuperListPlayer player = (SuperListPlayer) findViewById(R.id.superlistplayer);
        player.getPlayer()
              .setNetChangeListener(true)//设置监听手机网络的变化,这个参数是内部是否处理网络监听，和setOnNetChangeListener没有关系
              .setShowTopControl(false)
              .setSupportGesture(false)
              .setScaleType(SuperPlayer.SCALETYPE_FILLPARENT);
        return player;
    }

    /**
     * 初始化适配器
     */
    private void initAdapter() {
        SuperVideoAdapter mAdapter = new SuperVideoAdapter(this);
        mAdapter.addDatas(setData());
        player.setAdapter(mAdapter);
        mAdapter.setPlayClick(new SuperVideoAdapter.PlayClickListener() {
            @Override
            public void onPlayClick(int position, VideoListBean data, RelativeLayout rlPlayControl) {
                player.onPlayClick(position, data.getVideoUrl(), rlPlayControl);
            }
        });
    }

    private ArrayList<VideoListBean> setData() {
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
