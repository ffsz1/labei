package com.netease.nim.uikit.recent.holder;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.cache.NimUserInfoCache;
import com.netease.nim.uikit.cache.TeamDataCache;
import com.netease.nim.uikit.common.ui.drop.DropFake;
import com.netease.nim.uikit.common.ui.drop.DropManager;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseQuickAdapter;
import com.netease.nim.uikit.common.ui.recyclerview.holder.BaseViewHolder;
import com.netease.nim.uikit.common.ui.recyclerview.holder.RecyclerViewHolder;
import com.netease.nim.uikit.common.util.sys.ScreenUtil;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nim.uikit.glide.GlideApp;
import com.netease.nim.uikit.recent.RecentContactsCallback;
import com.netease.nim.uikit.recent.RecentContactsFragment;
import com.netease.nim.uikit.recent.adapter.RecentContactAdapter;
import com.netease.nim.uikit.session.emoji.MoonUtil;
import com.netease.nim.uikit.uinfo.UserInfoHelper;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

/**
 * @author dell
 */
public abstract class RecentViewHolder extends RecyclerViewHolder<BaseQuickAdapter, BaseViewHolder, RecentContact> {

    public RecentViewHolder(BaseQuickAdapter adapter) {
        super(adapter);
    }

    private int lastUnreadCount = 0;

    protected ImageView imgHead;
    protected ImageView iv_gender;

    protected TextView tvNickname;

    protected TextView tvMessage;

    protected TextView tvDatetime;

    // 消息发送错误状态标记，目前没有逻辑处理
    protected ImageView imgMsgStatus;

    // 未读红点（一个占坑，一个全屏动画）
    protected DropFake tvUnread;

    private ImageView imgUnreadExplosion;

    protected TextView tvOnlineState;

    protected TextView tv_unread_number_tip;

    // 子类覆写
    protected abstract String getContent(RecentContact recent);

    @Override
    public void convert(BaseViewHolder holder, RecentContact data, int position, boolean isScrolling) {
        inflate(holder, data);
        refresh(holder, data, position);
    }

    public void inflate(BaseViewHolder holder, final RecentContact recent) {
        this.imgHead = holder.getView(R.id.img_head);
        this.tvNickname = holder.getView(R.id.tv_nickname);
        this.tvMessage = holder.getView(R.id.tv_message);
        this.tvUnread = holder.getView(R.id.unread_number_tip);
        this.imgUnreadExplosion = holder.getView(R.id.unread_number_explosion);
        this.tvDatetime = holder.getView(R.id.tv_date_time);
        this.imgMsgStatus = holder.getView(R.id.img_msg_status);
        this.tvOnlineState = holder.getView(R.id.tv_online_state);
        this.tv_unread_number_tip = holder.getView(R.id.tv_unread_number_tip);
        this.iv_gender = holder.getView(R.id.iv_gender);
        holder.addOnClickListener(R.id.unread_number_tip);

        this.tvUnread.setTouchListener(new DropFake.ITouchListener() {
            @Override
            public void onDown() {
                DropManager.getInstance().setCurrentId(recent);
                DropManager.getInstance().down(tvUnread, tvUnread.getText());
            }

            @Override
            public void onMove(float curX, float curY) {
                DropManager.getInstance().move(curX, curY);
            }

            @Override
            public void onUp() {
                DropManager.getInstance().up();
            }
        });
    }

    public void refresh(BaseViewHolder holder, RecentContact recent, final int position) {
        // unread count animation
        boolean shouldBoom = lastUnreadCount > 0 && recent.getUnreadCount() == 0; // 未读数从N->0执行爆裂动画;
        lastUnreadCount = recent.getUnreadCount();

        updateBackground(holder, recent, position);

        loadPortrait(recent);

        updateNickLabel(UserInfoHelper.getUserTitleName(recent.getContactId(), recent.getSessionType()));

        updateOnlineState(recent);

        updateMsgLabel(holder, recent);

        updateNewIndicator(recent);

        int userGender = NimUserInfoCache.getInstance().getUserGender(recent.getContactId());
        if (userGender == 1) {
            iv_gender.setBackground(holder.getContext().getResources().getDrawable(R.drawable.icon_man));
        } else if (userGender == 2) {
            iv_gender.setBackground(holder.getContext().getResources().getDrawable(R.drawable.icon_woman));
        } else {
            iv_gender.setVisibility(View.INVISIBLE);
        }

        if (shouldBoom) {
            Object o = DropManager.getInstance().getCurrentId();
            if (o instanceof String && o.equals("0")) {
                imgUnreadExplosion.setImageResource(R.drawable.explosion);
                imgUnreadExplosion.setVisibility(View.VISIBLE);
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        ((AnimationDrawable) imgUnreadExplosion.getDrawable()).start();
                        // 解决部分手机动画无法播放的问题（例如华为荣耀）
                        getAdapter().notifyItemChanged(getAdapter().getViewHolderPosition(position));
                    }
                });
            }
        } else {
            imgUnreadExplosion.setVisibility(View.GONE);
        }
    }

    private void updateBackground(BaseViewHolder holder, RecentContact recent, int position) {
        if ((recent.getTag() & RecentContactsFragment.RECENT_TAG_STICKY) == 0) {
            holder.getConvertView().setBackgroundResource(R.drawable.bg_common_touch_while);
        } else {
            holder.getConvertView().setBackgroundResource(R.drawable.nim_recent_contact_sticky_selecter);
        }
    }

    protected void loadPortrait(RecentContact recent) {

        // 设置头像
        if (recent.getSessionType() == SessionTypeEnum.P2P) {
            //替换成圆形图片加载
//            imgHead.loadBuddyAvatar(recent.getContactId());
            final UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(recent.getContactId());

            doLoadImage(imgHead.getContext(), userInfo != null ? userInfo.getAvatar() : null, imgHead, R.drawable.nim_avatar_default);

        } else if (recent.getSessionType() == SessionTypeEnum.Team) {
            Team team = TeamDataCache.getInstance().getTeamById(recent.getContactId());
//            imgHead.loadTeamIconByTeam(team);
            doLoadImage(imgHead.getContext(), team != null ? team.getIcon() : null, imgHead, R.drawable.nim_avatar_group);
        }
    }

    /**
     * ImageLoader异步加载
     */
    private void doLoadImage(Context context, final String avatar, ImageView img, final int defaultResId) {
        if (TextUtils.isEmpty(avatar)) {
            return;
        }
        /*
         * 若使用网易云信云存储，这里可以设置下载图片的压缩尺寸，生成下载URL
         * 如果图片来源是非网易云信云存储，请不要使用NosThumbImageUtil
         */
        // 之前云信处理图片url的方法，已失效
        //final String thumbUrl = makeAvatarThumbNosUrl(avatar, thumbSize);
        // 新的图片url处理方法
        StringBuilder sb = new StringBuilder(avatar);
        if (avatar.contains("img.erbanyy.com")) {
            if (!avatar.contains("?")) {
                sb.append("?imageslim");
            }
            sb.append("|imageView2/1/w/100/h/100");
        }
        GlideApp.with(context.getApplicationContext())
                .load(sb.toString())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new CircleCrop())
                .placeholder(defaultResId)
                .error(defaultResId)
                .into(img);
    }

    private void updateNewIndicator(RecentContact recent) {
        int unreadNum = recent.getUnreadCount();
//        tvUnread.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
//        tvUnread.setText(unreadCountShowRule(unreadNum));
        tv_unread_number_tip.setVisibility(unreadNum > 0 ? View.VISIBLE : View.GONE);
        tv_unread_number_tip.setText(unreadCountShowRule(unreadNum));
    }

    private void updateMsgLabel(BaseViewHolder holder, RecentContact recent) {
        // 显示消息具体内容
        MoonUtil.identifyRecentVHFaceExpressionAndTags(holder.getContext(), tvMessage, getContent(recent), -1, 0.45f);
        //tvMessage.setText(getContent());
        MsgStatusEnum status = recent.getMsgStatus();
        switch (status) {
            case fail:
                imgMsgStatus.setImageResource(R.drawable.nim_g_ic_failed_small);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            case sending:
                imgMsgStatus.setImageResource(R.drawable.nim_recent_contact_ic_sending);
                imgMsgStatus.setVisibility(View.VISIBLE);
                break;
            default:
                imgMsgStatus.setVisibility(View.GONE);
                break;
        }

        String timeString = TimeUtil.getTimeShowString(recent.getTime(), true);
        tvDatetime.setText(timeString);
    }

    protected String getOnlineStateContent(RecentContact recent) {
        return "";
    }

    protected void updateOnlineState(RecentContact recent) {

        Log.e("getSessionType", "updateOnlineState: " + recent.getSessionType());
        if (recent.getSessionType() == SessionTypeEnum.Team) {
            tvOnlineState.setVisibility(View.GONE);
        } else {
            String onlineStateContent = getOnlineStateContent(recent);
            if (TextUtils.isEmpty(onlineStateContent)) {
                tvOnlineState.setVisibility(View.GONE);
            } else {
                tvOnlineState.setVisibility(View.VISIBLE);
                tvOnlineState.setText(getOnlineStateContent(recent));
            }
        }
    }

    protected void updateNickLabel(String nick) {
        int labelWidth = ScreenUtil.screenWidth;
        labelWidth -= ScreenUtil.dip2px(50 + 70); // 减去固定的头像和时间宽度

        if (labelWidth > 0) {
            tvNickname.setMaxWidth(labelWidth);
        }

        tvNickname.setText(nick);
    }

    protected String unreadCountShowRule(int unread) {
        unread = Math.min(unread, 99);
        return String.valueOf(unread);
    }

    protected RecentContactsCallback getCallback() {
        return ((RecentContactAdapter) getAdapter()).getCallback();
    }
}
