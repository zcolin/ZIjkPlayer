/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-19 下午1:05
 * *********************************************************
 */
package com.supercwn.player;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.supercwn.player.adapter.SuperVideoAdapter;
import com.supercwn.player.bean.VideoListBean;
import com.superplayer.library.SuperListPlayer;

/**
 * 视频列表播放基类
 */
public abstract class BaseVideoRecycleViewActivity extends AppCompatActivity {
    protected SuperListPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(getContentViewId());

        player = initPlayer();
    }

    //已经做了setContentView，如果客户端再次设置，不在操作
    @Override
    public void setContentView(int layoutResID) {
    }

    /**
     * 实现此函数后子类不要再进行setContentView（）操作
     */
    protected abstract int getContentViewId();

    /**
     * 初始化Player操作
     */
    protected abstract SuperListPlayer initPlayer();

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
    }

    protected class AdapterOnPlayClick implements SuperVideoAdapter.PlayClickListener {
        @Override
        public void onPlayClick(int position, VideoListBean data, RelativeLayout image) {
            player.onPlayClick(position, data.getVideoUrl(), image);
        }
    }
}
