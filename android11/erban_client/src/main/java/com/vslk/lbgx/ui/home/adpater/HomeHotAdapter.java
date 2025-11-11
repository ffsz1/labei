package com.vslk.lbgx.ui.home.adpater;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nimlib.sdk.media.player.OnPlayListener;
import com.qiniu.android.utils.StringUtils;
import com.tongdaxing.erban.R;
import com.tongdaxing.erban.libcommon.utils.ListUtils;
import com.tongdaxing.xchat_core.audio.AudioPlayManager;
import com.tongdaxing.xchat_core.home.HomeRoom;
import com.tongdaxing.xchat_core.home.PeiPeiBean;
import com.vslk.lbgx.event.ToHim;
import com.vslk.lbgx.room.AVRoomActivity;
import com.vslk.lbgx.room.egg.AnimationsContainer;
import com.vslk.lbgx.ui.me.user.activity.UserInfoActivity;
import com.vslk.lbgx.utils.BizUtils;
import com.vslk.lbgx.utils.ImageLoadUtils;

import java.util.List;

/**
 * <p> 首页热门adapter </p>
 *
 * @author Administrator
 * @date 2017/11/16
 */
public class HomeHotAdapter extends BaseMultiItemQuickAdapter<HomeRoom, BaseViewHolder> {

    private int radius;
    private boolean isPay = true;
    private static AudioPlayManager playManager;

    public HomeHotAdapter(Context context) {
        super(null);
        this.mContext = context;
        addItemType(0, R.layout.item_home_normal_room);
        addItemType(1, R.layout.item_room_hot_list_recycler);
        addItemType(2, R.layout.item_home_tag);
        addItemType(3, R.layout.item_home_peipei);
        addItemType(4, R.layout.item_home_mxin);
        radius = ConvertUtils.dp2px(10);
    }

    @Override
    protected void convert(BaseViewHolder holder, HomeRoom homeItem) {
        if (homeItem == null) {
            return;
        }
        switch (homeItem.getItemType()) {
            case 0:
                setNormalData(holder, homeItem);
                break;
            case 1:
                setHotListAdapter(holder, homeItem);
                break;
            case 2:
                setItemTag(holder, homeItem);
                break;
            case 3:
                setPeiPeiAdapter(holder, homeItem);
                break;
            case 4:
                setMXinAdapter(holder, homeItem);
                break;
            default:
                break;
        }
    }

    /**
     * 设置条目信息
     */
    private void setItemTag(BaseViewHolder holder, HomeRoom homeItem) {
        holder.setText(R.id.tag_name, "热门房间")
                .setImageResource(R.id.tag_img, R.drawable.ic_home_hot_room);
    }

    private void setNormalData(BaseViewHolder holder, HomeRoom homeItem) {
//        if (holder.getAdapterPosition() == 2) {
//            holder.setVisible(R.id.top, true);
//        } else {
//            holder.setVisible(R.id.top, false);
//        }
        ImageView ivOnlineAnim = holder.getView(R.id.iv_online_anim);
        ImageLoadUtils.loadImageRes(mContext, R.mipmap.ic_new_online_red, ivOnlineAnim);
        holder.setText(R.id.tv_hot_room_title, homeItem.getTitle())
                .setText(R.id.count, homeItem.getOnlineNum() + "人")
                .setText(R.id.erban_no, String.valueOf("ID: " + homeItem.getErbanNo()));
        ImageLoadUtils.loadSmallRoundBackground(mContext, homeItem.getAvatar(), holder.getView(R.id.iv_hot_room_logo), radius);
        ImageLoadUtils.loadImageTag(mContext, homeItem.tagPict, holder.getView(R.id.iv_home_tag));

        TextView tvSign = holder.getView(R.id.tv_sign);
        String description = homeItem.getUserDescription();
        if (!StringUtils.isBlank(description)) {
            tvSign.setText(description);
        }
    }

    private void setHotListAdapter(BaseViewHolder holder, HomeRoom homeItem) {
        List<HomeRoom> recommendList = homeItem.getRecommendList();
        if (!ListUtils.isListEmpty(recommendList)) {
            RecyclerView recyclerView = holder.getView(R.id.iv_room_hot_list_recycler);
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            HomeHotItemRecyclerAdapter recyclerAdapter = new HomeHotItemRecyclerAdapter();
            recyclerView.setAdapter(recyclerAdapter);
            recyclerAdapter.setNewData(recommendList);

            recyclerAdapter.setOnItemClickListener((adapter, view, position) ->
                    AVRoomActivity.start(mContext, recyclerAdapter.getData().get(position).getUid()));
        }
    }

    private void setPeiPeiAdapter(BaseViewHolder baseViewHolder, HomeRoom homeItem) {
        View goView = baseViewHolder.getView(R.id.ll_go);
        TextView count = baseViewHolder.getView(R.id.count);
        TextView tvSign = baseViewHolder.getView(R.id.tv_sign);
        ImageView gift = baseViewHolder.getView(R.id.iv_gif);
        PeiPeiBean peiPeiBean = homeItem.getPeiPeiBean();
        View syView = baseViewHolder.getView(R.id.ll_sy);
        ImageView imgPlay = baseViewHolder.getView(R.id.iv_play);
        ImageView imgPlayBg = baseViewHolder.getView(R.id.img_bg_anim);
        FrameLayout headerFlt = baseViewHolder.getView(R.id.fra_header);
        if (peiPeiBean.getVoiceDuration() <= 0) {
            syView.setVisibility(View.GONE);
        } else {
            syView.setVisibility(View.VISIBLE);
        }

        baseViewHolder.setText(R.id.tv_time, peiPeiBean.getVoiceDuration() + "``");

//        imgPlayBg.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
//            @Override
//            public void onViewAttachedToWindow(View v) {
//                setPlayAnim(peiPeiBean.getRoomState(), imgPlayBg);
//            }
//
//            @Override
//            public void onViewDetachedFromWindow(View v) {
//
//            }
//        });


        headerFlt.setOnClickListener(v -> ToHim.postToHim(peiPeiBean.getUid(), this.getClass().getName()));

        syView.setOnClickListener(v -> {
            AnimationsContainer.FramesSequenceAnimation dialogAnim = AnimationsContainer.getInstance(mContext, R.array.hot_voice, 5)
                    .createProgressDialogAnim(gift);
            playManager = new AudioPlayManager(mContext, null, new OnPlayListener() {

                    // 音频转码解码完成，会马上开始播放了
                    @Override
                    public void onPrepared() {
                        dialogAnim.start();
                        imgPlay.setImageResource(R.mipmap.ic_media_stop);
                        isPay = false;
                    }

                    // 播放结束
                    @Override
                    public void onCompletion() {
                        dialogAnim.stop();
                        System.out.println("onCompletion");
                        ImageLoadUtils.loadImageRes(mContext, R.mipmap.ic_home_voice2, gift);
                        imgPlay.setImageResource(R.mipmap.ic_media_play);
                        isPay = true;
                    }

                    // 播放被中断了
                    @Override
                    public void onInterrupt() {
                        dialogAnim.stop();
                        System.out.println("onInterrupt");
                        ImageLoadUtils.loadImageRes(mContext, R.mipmap.ic_home_voice2, gift);
                        imgPlay.setImageResource(R.mipmap.ic_media_play);
                        isPay = true;
                    }

                    // 播放过程中出错。参数为出错原因描述
                    @Override
                    public void onError(String error) {
                        dialogAnim.stop();
                        System.out.println("onError");
                        ImageLoadUtils.loadImageRes(mContext, R.mipmap.ic_home_voice2, gift);
                        imgPlay.setImageResource(R.mipmap.ic_media_play);
                        isPay = true;
                    }

                    // 播放进度报告，每隔 500ms 会回调一次，告诉当前进度。 参数为当前进度，单位为毫秒，可用于更新 UI
                    @Override
                    public void onPlaying(long curPosition) {

                    }
                });
                if (!peiPeiBean.getUserVoice().isEmpty()) {
                    playManager.setDataSource(peiPeiBean.getUserVoice());
                    playManager.play();
                } else {
                    ImageLoadUtils.loadImageRes(mContext, R.mipmap.ic_home_voice2, gift);
                    ToastUtils.showLong("TA还没有录制声音，快去提醒TA吧~");
                }
            if(!isPay){
                imgPlay.setImageResource(R.mipmap.ic_media_play);
                closePlay();
                isPay = true;
                dialogAnim.stop();
                ImageLoadUtils.loadImageRes(mContext, R.mipmap.ic_home_voice2, gift);
            }
        });
        baseViewHolder.getView(R.id.tv_room_title).setOnClickListener(v -> UserInfoActivity.start(mContext, peiPeiBean.getUid()));
        ImageLoadUtils.loadImageRes(mContext, R.mipmap.ic_home_voice2, gift);
        baseViewHolder.setText(R.id.tv_room_title, peiPeiBean.getNick());
        ((ImageView) baseViewHolder.getView(R.id.gender)).setImageResource(peiPeiBean.getGender() == 1 ? R.drawable.icon_man : R.drawable.icon_woman);
        ((ImageView) baseViewHolder.getView(R.id.sex_bg_iv)).setImageResource(peiPeiBean.getGender() == 1 ? R.mipmap.sex_man_bg : R.mipmap.sex_woman_bg);
        ImageLoadUtils.loadSmallRoundBackground(mContext, peiPeiBean.getAvatar(), baseViewHolder.getView(R.id.iv_room_pic), R.drawable.ic_default_avatar);
//        ImageLoadUtils.loadSmallRoundBackground(mContext, peiPeiBean.getAvatar(), baseViewHolder.getView(R.id.iv_room_pic), radius);
        String description = peiPeiBean.getUserDescription();
        if (!StringUtils.isBlank(description)) {
            tvSign.setText(description);
        } else {
            tvSign.setText("这个人很懒，什么都没有留下~");
        }
    }

    public static void closePlay(){
        if (playManager != null) {
            playManager.stopPlay();
        }
    }

    private void setPlayAnim(int roomState, ImageView img) {
        if (roomState == 1) {//在线或者正在播放语音时显示动画
            Animation animPlay = AnimationUtils.loadAnimation(mContext, R.anim.anim_rotate_play_audio);
            animPlay.setInterpolator(new LinearInterpolator());
            img.setVisibility(View.VISIBLE);
            img.startAnimation(animPlay);
        } else {
            img.setVisibility(View.INVISIBLE);
            img.setAnimation(null);
        }
    }


    /***
     * v3版本
     * @param baseViewHolder
     * @param homeItem
     */
    private void setMXinAdapter(BaseViewHolder baseViewHolder, HomeRoom homeItem) {
        View goView = baseViewHolder.getView(R.id.ll_go);
        TextView count = baseViewHolder.getView(R.id.count);
        TextView roomTitleTv = baseViewHolder.getView(R.id.tv_room_title);
        TextView tvSign = baseViewHolder.getView(R.id.tv_sign);
        ImageView ivhomeTag = baseViewHolder.getView(R.id.iv_home_tag);
        ImageView imgGender = baseViewHolder.getView(R.id.gender);
        ImageView imgLv = baseViewHolder.getView(R.id.iv_level);
        ImageView picIv = baseViewHolder.getView(R.id.iv_room_pic);
        PeiPeiBean peiPeiBean = homeItem.getPeiPeiBean();
        if (peiPeiBean.isFirstCharge()) {
            ivhomeTag.setImageResource(R.mipmap.new_user_msg_icon_small);
        } else {
            ivhomeTag.setImageResource(R.mipmap.new_user_msg_icon_xinjin);
        }
        if (peiPeiBean.getGender() == 1) {
            imgGender.setImageResource(R.drawable.icon_man);
        } else {
            imgGender.setImageResource(R.drawable.icon_woman);
        }
        String levelResName = "lv" + peiPeiBean.getExperLevel();
        if(peiPeiBean.getExperLevel() == 0){
            imgLv.setVisibility(View.INVISIBLE);
        }else {
            imgLv.setImageResource(BizUtils.getResource(mContext, levelResName));
            imgLv.setVisibility(View.VISIBLE);
        }
        goView.setOnClickListener(v -> NimUIKit.startP2PSession(mContext, String.valueOf(peiPeiBean.getUid()), null));
        ImageLoadUtils.loadSmallRoundBackground(mContext, peiPeiBean.getAvatar(), picIv, R.drawable.ic_default_avatar);
        picIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoActivity.start(mContext, peiPeiBean.getUid());
            }
        });

        roomTitleTv.setText(peiPeiBean.getNick());
        roomTitleTv.setOnClickListener(v -> UserInfoActivity.start(mContext, peiPeiBean.getUid()));
//        ((ImageView) baseViewHolder.getView(R.id.gender)).setImageResource(peiPeiBean.getGender() == 1 ? R.drawable.icon_man : R.drawable.icon_woman);
        String description = peiPeiBean.getUserDescription();
        if (!StringUtils.isBlank(description)) {
            tvSign.setText(description);
        } else {
            tvSign.setText("这个人很懒，什么都没有留下~");
        }
    }
}
