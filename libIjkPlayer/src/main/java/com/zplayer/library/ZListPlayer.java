/*
 * *********************************************************
 *   author   colin
 *   company  telchina
 *   email    wanglin2046@126.com
 *   date     18-1-9 下午2:12
 * ********************************************************
 */
package com.zplayer.library;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.zplayer.library.mediaplayer.IjkVideoView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 针对列表的Player
 */
public class ZListPlayer extends RelativeLayout {

    private MChildAttachStateChageListener childAttachStateChageListener = new MChildAttachStateChageListener();
    private ZPlayer             player;
    private RecyclerView        recyclerView;
    private FrameLayout         flRecyclerViewParent;
    private RelativeLayout      rlFullScreenLay;
    private LinearLayoutManager layoutManager;
    private int postion     = -1; //当前item位置
    private int lastPostion = -1; //上次的item位置
    private Context context;

    public ZListPlayer(Context context) {
        this(context, null);
    }

    public ZListPlayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZListPlayer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        init();
    }

    private void init() {
        player = new ZPlayer(context);
        LayoutInflater.from(context).inflate(R.layout.view_super_listplayer, this);
        flRecyclerViewParent = findViewById(R.id.fl_recycleview_superlistvideo);
        recyclerView = findViewById(R.id.recycleview_superlistvideo);
        rlFullScreenLay = findViewById(R.id.rl_full_screen);

        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);

        player.onComplete(() -> {
            if (player.isFullScreen()) {
                player.toggleFullScreen();
            } else {
                showView(R.id.IjkPlayer_rl_player_control);
            }
        });


        recyclerView.addOnChildAttachStateChangeListener(childAttachStateChageListener);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    /**
     * 默认已经有了RecyclerView，如果需要设置自己的RecyclerView，使用此函数
     */
    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        recyclerView.addOnChildAttachStateChangeListener(childAttachStateChageListener);
        flRecyclerViewParent.removeAllViews();
        flRecyclerViewParent.addView(recyclerView);

        if (recyclerView.getLayoutManager() == null || !(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }
    }

    /**
     * 默认已经有了RecyclerView，如果需要设置自己的RecyclerView，使用此函数
     * 因为有些下拉刷新组件是有外层view包裹的，就需要使用这个函数设置RecyclerView了
     */
    public void setRecyclerViewLayout(ViewGroup recyclerViewLayout) {
        this.recyclerView = getRecyclerViewFromeViewGroup(recyclerViewLayout);
        if (recyclerView == null) {
            throw new IllegalArgumentException("ViewGroup 中没有RecyclerView");
        }
        this.recyclerView.addOnChildAttachStateChangeListener(childAttachStateChageListener);
        flRecyclerViewParent.removeAllViews();
        flRecyclerViewParent.addView(recyclerViewLayout);

        if (recyclerView.getLayoutManager() == null || !(recyclerView.getLayoutManager() instanceof LinearLayoutManager)) {
            layoutManager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(layoutManager);
        } else {
            layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        }
    }

    /**
     * 遍历ViewGroup获取RecyclerView
     */
    private RecyclerView getRecyclerViewFromeViewGroup(ViewGroup recyclerViewLayout) {
        int size = recyclerViewLayout.getChildCount();
        for (int i = 0; i < size; i++) {
            if (recyclerViewLayout.getChildAt(i) instanceof RecyclerView) {
                return (RecyclerView) (recyclerViewLayout.getChildAt(i));
            } else if (recyclerViewLayout.getChildAt(i) instanceof ViewGroup) {
                return getRecyclerViewFromeViewGroup((ViewGroup) (recyclerViewLayout.getChildAt(i)));
            }
        }
        return null;
    }

    /**
     * 监听屏幕状态屏蔽系统操作栏，如华为系的操作条
     * 在Activity的onConfigurationChanged中调用
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if (player != null) {
            player.onConfigurationChanged(newConfig);
            if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
                rlFullScreenLay.setVisibility(View.GONE);
                rlFullScreenLay.removeAllViews();
                recyclerView.setVisibility(View.VISIBLE);
                if (postion <= layoutManager.findLastVisibleItemPosition() && postion >= layoutManager.findFirstVisibleItemPosition()) {
                    View view = recyclerView.findViewHolderForAdapterPosition(postion).itemView;
                    FrameLayout frameLayout = view.findViewById(R.id.IjkPlayer_fl_super_video);
                    frameLayout.removeAllViews();
                    ViewGroup last = (ViewGroup) player.getParent();//找到videoitemview的父类，然后remove
                    if (last != null) {
                        last.removeAllViews();
                    }
                    frameLayout.addView(player);
                }
                //                rlFullScreenLay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                ViewGroup viewGroup = (ViewGroup) player.getParent();
                if (viewGroup == null) {
                    return;
                }
                viewGroup.removeAllViews();
                rlFullScreenLay.addView(player);
                rlFullScreenLay.setVisibility(View.VISIBLE);
                //                rlFullScreenLay.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        } else {
            rlFullScreenLay.setVisibility(View.GONE);
        }
    }

    /**
     * 点击Item播放需要调用的方法
     */
    public void onPlayClick(int position, String playUrl, RelativeLayout rlayPlayerControl) {
        rlayPlayerControl.setVisibility(View.GONE);
        if (player.isPlaying() && lastPostion == position) {
            return;
        }

        postion = position;
        if (player.isPlaying() || player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
            if (position != lastPostion) {
                player.stop();
                player.release();
            }
        }
        if (lastPostion != -1) {
            showView(R.id.IjkPlayer_rl_player_control);
        }

        View view = recyclerView.findViewHolderForAdapterPosition(position).itemView;
        FrameLayout frameLayout = view.findViewById(R.id.IjkPlayer_fl_super_video);
        frameLayout.removeAllViews();
        showView(R.id.IjkPlayer_rl_player_control);
        frameLayout.addView(player);
        player.play(playUrl);
        lastPostion = position;
    }

    public void onDestroy() {
        player.onDestroy();
    }

    public boolean onBackPressed() {
        return player.onBackPressed();
    }

    public void onPause() {
        player.onPause();
    }

    public void onResume() {
        player.onResume();
    }

    /**
     * 使用下拉刷新控件时需要调用
     */
    public void onRefresh() {
        if (player != null) {
            player.stop();
            player.release();
            showView(R.id.IjkPlayer_rl_player_control);
        }
    }


    public ZPlayer getPlayer() {
        return player;
    }

    /**
     * 设置全屏切换监听
     */
    public void setOnFullScreenListener(ZPlayer.OnFullScreenListener onFullScreenListener) {
        player.setOnFullScreenListener(onFullScreenListener);
    }

    /**
     * 显示列表中的视图(仅仅对列表播放的一个方法)
     */
    private void showView(int viewId) {
        ViewGroup last = (ViewGroup) player.getParent();//找到videoitemview的父类，然后remove
        if (last != null) {
            last.removeAllViews();
            View itemView = (View) last.getParent();
            if (itemView != null) {
                View viewById = itemView.findViewById(viewId);
                if (viewById != null) {
                    viewById.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private class MChildAttachStateChageListener implements RecyclerView.OnChildAttachStateChangeListener {
        @Override
        public void onChildViewAttachedToWindow(View view) {
            int index = recyclerView.getChildAdapterPosition(view);
            View controlview = view.findViewById(R.id.IjkPlayer_rl_player_control);
            if (controlview == null) {
                return;
            }

            controlview.setVisibility(View.VISIBLE);
            if (index == postion) {
                FrameLayout frameLayout = view.findViewById(R.id.IjkPlayer_fl_super_video);
                frameLayout.removeAllViews();
                if (player.isPlaying() || player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                    controlview.setVisibility(View.GONE);
                }
                if (player.isPlaying() || player.getVideoStatus() == IjkVideoView.STATE_PAUSED) {
                    if (player.getParent() != null) {
                        ((ViewGroup) player.getParent()).removeAllViews();
                    }
                    frameLayout.addView(player);
                }
            }
        }

        @Override
        public void onChildViewDetachedFromWindow(View view) {
            int index = recyclerView.getChildAdapterPosition(view);
            if ((index) == postion) {
                if (player != null) {
                    player.stop();
                    player.release();
                    showView(R.id.IjkPlayer_rl_player_control);
                }
            }
        }
    }
}
