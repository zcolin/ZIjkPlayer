/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 下午2:12
 * ********************************************************
 */

package com.zplayer.demo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.zplayer.library.ZPlayer;

/**
 * 类描述：视频详情页
 * <p>
 * Super南仔
 * <p>
 * update by colin on 2016-10-18
 */
public class VideoPlayActivity extends BaseVideoPlayActivity implements View.OnClickListener, ZPlayer.OnNetChangeListener {

    private boolean isLive;

    /**
     * 测试地址
     */
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Toolbar toolbar = findViewById(R.id.id_tool_bar);
        setSupportActionBar(toolbar);


        isLive = getIntent().getBooleanExtra("isLive", false);
        url = getIntent().getStringExtra("url");

        player.setTitle(url)//设置视频的titleName
              .play(url);//开始播放视频

        findViewById(R.id.tv_replay).setOnClickListener(this);
        findViewById(R.id.tv_play_location).setOnClickListener(this);
        findViewById(R.id.tv_play_switch).setOnClickListener(this);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_videoplay;
    }

    @Override
    protected ZPlayer initPlayer() {
        ZPlayer player = findViewById(R.id.view_super_player);
        player.setLive(isLive)//设置该地址是直播的地址
              .setNetChangeListener(true)//设置监听手机网络的变化,这个参数是内部是否处理网络监听，和setOnNetChangeListener没有关系
              .setOnNetChangeListener(this)//实现网络变化的回调
              .setScaleType(ZPlayer.SCALETYPE_FITXY)//图片缩放方式
              .setPlayerWH(0, player.getMeasuredHeight())//设置竖屏的时候屏幕的高度，如果不设置会切换后按照16:9的高度重置
              .setAlwaysShowControl()  //设置则一直显示
              //准备好视频回调
              .onPrepared(() -> {
                  //TODO 监听视频是否已经准备完成开始播放。（可以在这里处理视频封面的显示跟隐藏）
              })
              //播放完成回调
              .onComplete(() -> {
                  //TODO 监听视频是否已经播放完成了。（可以在这里处理视频播放完成进行的操作）
              })
              //视频信息回调
              .onInfo((what, extra) -> {
                  //TODO 监听视频的相关信息。
              })
              //播放出错回调
              .onError((what, extra) -> {
                  //TODO 监听视频播放失败的回调
              });
        return player;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_replay) {
            if (player != null) {
                player.play(url);
            }
        } else if (view.getId() == R.id.tv_play_location) {
            if (isLive) {
                Toast.makeText(this, "直播不支持指定播放", Toast.LENGTH_SHORT).show();
                return;
            }
            if (player != null) {
                //这个节点是根据视频的大小来获取的。不同的视频源节点也会不一致（一般用在退出视频播放后保存对应视频的节点从而来达到记录播放）
                player.play(url, 89528);
            }
        } else if (view.getId() == R.id.tv_play_switch) {
            //切换视频播放源（一般是标清，高清的切换ps：由于我没有找到高清，标清的视频源，所以也是换相同的地址）
            if (isLive) {
                player.playSwitch(url);
            } else {
                player.playSwitch("http://baobab.wandoujia.com/api/v1/playUrl?vid=2614&editionType=high");
            }
        }
    }

    /**
     * 网络链接监听类
     */
    @Override
    public void onWifi() {
        Toast.makeText(this, "当前网络环境是WIFI", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMobile() {
        Toast.makeText(this, "当前网络环境是手机网络", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDisConnect() {
        Toast.makeText(this, "网络链接断开", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNoAvailable() {
        Toast.makeText(this, "无网络链接", Toast.LENGTH_SHORT).show();
    }
}
