package com.netease.nim.uikit.common.ui.imageview;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.netease.nim.uikit.NimUIKit;
import com.netease.nim.uikit.R;
import com.netease.nim.uikit.glide.GlideApp;
import com.netease.nimlib.sdk.msg.constant.MsgTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.nos.model.NosThumbParam;
import com.netease.nimlib.sdk.nos.util.NosThumbImageUtil;
import com.netease.nimlib.sdk.robot.model.RobotAttachment;
import com.netease.nimlib.sdk.team.model.Team;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;

/**
 * Created by huangjun on 2015/11/13.
 */
public class HeadImageView extends CircleImageView {

    public static final int DEFAULT_AVATAR_THUMB_SIZE = (int) NimUIKit.getContext().getResources().getDimension(R.dimen.avatar_max_size);
    public static final int DEFAULT_AVATAR_NOTIFICATION_ICON_SIZE = (int) NimUIKit.getContext().getResources().getDimension(R.dimen.avatar_notification_size);
    private static final String PIC_PROCESSING = "?imageslim";
    private static final String ACCESS_URL = "img.erbanyy.com";

    public HeadImageView(Context context) {
        super(context);
    }

    public HeadImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeadImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 加载用户头像（默认大小的缩略图）
     *
     * @param url 头像地址
     */
    public void loadAvatar(final String url) {
        doLoadImage(url, R.drawable.nim_avatar_default, DEFAULT_AVATAR_THUMB_SIZE);
    }

    /**
     * 加载用户头像（默认大小的缩略图）
     *
     * @param account 用户账号
     */
    public void loadBuddyAvatar(String account) {
        final UserInfo userInfo = NimUIKit.getUserInfoProvider().getUserInfo(account);
        doLoadImage(userInfo != null ? userInfo.getAvatar() : null, R.drawable.nim_avatar_default, DEFAULT_AVATAR_THUMB_SIZE);
    }

    /**
     * 加载用户头像（默认大小的缩略图）
     *
     * @param message 消息
     */
    public void loadBuddyAvatar(IMMessage message) {
        String account = message.getFromAccount();
        if (message.getMsgType() == MsgTypeEnum.robot) {
            RobotAttachment attachment = (RobotAttachment) message.getAttachment();
            if (attachment.isRobotSend()) {
                account = attachment.getFromRobotAccount();
            }
        }
        loadBuddyAvatar(account);
    }

    /**
     * 加载群头像（默认大小的缩略图）
     *
     * @param team 群
     */
    public void loadTeamIconByTeam(final Team team) {
        doLoadImage(team != null ? team.getIcon() : null, R.drawable.nim_avatar_group, DEFAULT_AVATAR_THUMB_SIZE);
    }

    /**
     * ImageLoader异步加载
     */
    private void doLoadImage(final String avatar, final int defaultResId, final int thumbSize) {
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
        if (avatar.contains(ACCESS_URL)) {
            if (!avatar.contains("?")) {
                sb.append(PIC_PROCESSING);
            }
            sb.append("|imageView2/1/w/100/h/100");
        }
        final String thumbUrl = sb.toString();
        GlideApp.with(getContext().getApplicationContext())
                .load(thumbUrl).centerCrop()
                .placeholder(defaultResId)
                .error(defaultResId)
                .override(thumbSize, thumbSize)
                .into(this);
    }

    /**
     * 解决ViewHolder复用问题
     */
    public void resetImageView() {
        setImageBitmap(null);
    }

    /**
     * 生成头像缩略图NOS URL地址（用作ImageLoader缓存的key）
     */
    private static String makeAvatarThumbNosUrl(final String url, final int thumbSize) {
        if (TextUtils.isEmpty(url)) {
            return url;
        }

        return thumbSize > 0 ? NosThumbImageUtil.makeImageThumbUrl(url, NosThumbParam.ThumbType.Crop, thumbSize, thumbSize) : url;
    }

    public static String getAvatarCacheKey(final String url) {
        return makeAvatarThumbNosUrl(url, DEFAULT_AVATAR_THUMB_SIZE);
    }
}
