/*
 * **********************************************************
 *   author   colin
 *   company  fosung
 *   email    wanglin2046@126.com
 *   date     16-10-19 下午3:41
 * *********************************************************
 */
package com.zplayer.demo.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zcolin.gui.zrecyclerview.BaseRecyclerAdapter;
import com.zplayer.demo.R;
import com.zplayer.demo.bean.VideoListBean;
import com.zplayer.library.utils.ZPlayerUtils;


/**
 * 布局可以更改，但是fl_super_video和rl_player_control这两个必须在同一个Viewgroup之下，并且Id不能更改
 * 参见libIjkPlayer的view_super_listplayer_item.xml
 */
public class SuperVideoAdapter extends BaseRecyclerAdapter<VideoListBean> {

    private PlayClickListener playClick;
    private Activity          activity;

    public SuperVideoAdapter(Activity activity) {
        this.activity = activity;
    }

    public void setPlayClick(PlayClickListener playClick) {
        this.playClick = playClick;
    }

    @Override
    public int getItemLayoutId(int viewType) {
        return R.layout.view_super_listplayer_item;
    }

    @Override
    public void setUpData(CommonHolder holder, final int position, int viewType, final VideoListBean data) {
        final RelativeLayout rlayPlayerControl = getView(holder, R.id.IjkPlayer_rl_player_control);
        final RelativeLayout rlayPlayer = getView(holder, R.id.rl_super_video_layout);
        TextView tvTitle = getView(holder, R.id.tv_super_play_title);
        tvTitle.setText(data.getTitleName());
        ImageView ivPlay = getView(holder, R.id.iv_super_play);

        if (rlayPlayer != null) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rlayPlayer.getLayoutParams();
            layoutParams.height = (int) (ZPlayerUtils.getScreenWidth(activity) * 0.5652f);//这值是网上抄来的，我设置了这个之后就没有全屏回来拉伸的效果，具体为什么我也不太清楚
            rlayPlayer.setLayoutParams(layoutParams);
        }

        //点击回调 播放视频
        ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playClick != null)
                    playClick.onPlayClick(position, data, rlayPlayerControl);
            }
        });
    }


    public interface PlayClickListener {
        void onPlayClick(int position, VideoListBean data, RelativeLayout rlPlayControl);
    }
}
