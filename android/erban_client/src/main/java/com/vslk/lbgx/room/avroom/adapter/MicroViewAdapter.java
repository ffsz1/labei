package com.vslk.lbgx.room.avroom.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.tongdaxing.erban.libcommon.net.rxnet.OkHttpManager;
import com.tongdaxing.xchat_core.room.bean.RoomInfo;
import com.vslk.lbgx.room.avroom.widget.WaveView;
import com.vslk.lbgx.ui.common.widget.CircleImageView;
import com.vslk.lbgx.utils.ImageLoadUtils;
import com.netease.nim.uikit.common.util.string.StringUtil;
import com.netease.nim.uikit.glide.GlideApp;
import com.tongdaxing.erban.R;
import com.tongdaxing.xchat_core.bean.IMChatRoomMember;
import com.tongdaxing.xchat_core.bean.RoomCharmInfo;
import com.tongdaxing.xchat_core.bean.RoomMicInfo;
import com.tongdaxing.xchat_core.bean.RoomQueueInfo;
import com.tongdaxing.xchat_core.im.custom.bean.RoomCharmAttachment;
import com.tongdaxing.xchat_core.manager.AvRoomDataManager;
import com.tongdaxing.xchat_core.user.IUserCore;
import com.tongdaxing.xchat_core.user.bean.UserInfo;
import com.tongdaxing.xchat_framework.coremanager.CoreManager;
import com.tongdaxing.xchat_framework.util.config.BasicConfig;
import com.tongdaxing.xchat_framework.util.util.JavaUtil;

import java.math.BigDecimal;

/**
 * @author xiaoyu
 * @date 2017/12/18
 */

public class MicroViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private OnMicroItemClickListener onMicroItemClickListener;
    private Context context;
    private String avatarPicture = "";

    public MicroViewAdapter(Context context) {
        this.context = context;
    }

    public void setOnMicroItemClickListener(OnMicroItemClickListener onMicroItemClickListener) {
        this.onMicroItemClickListener = onMicroItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View item;
        if (viewType == TYPE_BOSS) {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_boss_micro, parent, false);
            return new BossMicroViewHolder(item);
        } else {
            item = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_micro, parent, false);
            return new NormalMicroViewHolder(item);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByMicPosition(position - 1);
        if (roomQueueInfo == null) {
            return;
        }
        NormalMicroViewHolder holder = (NormalMicroViewHolder) viewHolder;
        holder.bind(roomQueueInfo, position - 1);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (position == 0) {
                        return 4;
                    } else {
                        return 1;
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 9;
    }

    private static final int TYPE_BOSS = 1;
    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_INVALID = -2;

    @Override
    public int getItemViewType(int position) {
        return (position == 0) ? TYPE_BOSS : TYPE_NORMAL;
    }

    /**
     * 老板位
     */
    private final int BOSS_POSITION = 7;

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class NormalMicroViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvNick;
        ImageView ivUpImage;
        ImageView ivLockImage;
        ImageView ivMuteImage;
        TextView ivGender;
        CircleImageView ivAvatar;
        FrameLayout rlMicLayout;
        WaveView waveView;
        View avatarBg;
        ImageView ivHeadwear;
        TextView tvCharm;
        ImageView ivHat;

        NormalMicroViewHolder(View itemView) {
            super(itemView);
            waveView = itemView.findViewById(R.id.waveview);
            rlMicLayout = itemView.findViewById(R.id.micro_layout);
            ivGender = itemView.findViewById(R.id.iv_mic_gender);
            ivUpImage = itemView.findViewById(R.id.up_image);
            ivLockImage = itemView.findViewById(R.id.lock_image);
            ivMuteImage = itemView.findViewById(R.id.mute_image);
            ivAvatar = itemView.findViewById(R.id.avatar);
            tvNick = itemView.findViewById(R.id.nick);
            avatarBg = itemView.findViewById(R.id.avatar_bg);
            ivHeadwear = itemView.findViewById(R.id.iv_headwear);
            tvCharm = itemView.findViewById(R.id.tv_micro_charm);
            ivHat = itemView.findViewById(R.id.iv_charm_hat);

            ivUpImage.setOnClickListener(this);
            ivLockImage.setOnClickListener(this);
            ivAvatar.setOnClickListener(this);
            ivAvatar.setOnLongClickListener(this);
        }

        RoomQueueInfo info;
        int position = TYPE_INVALID;

        public void clear() {
            info = null;
            position = TYPE_INVALID;
            rlMicLayout.setBackground(null);
            rlMicLayout.clearAnimation();
            waveView.stop();
            ivUpImage.setVisibility(View.VISIBLE);
            ivLockImage.setVisibility(View.GONE);
            ivMuteImage.setVisibility(View.GONE);
            ivAvatar.setVisibility(View.GONE);
            avatarBg.setVisibility(View.GONE);
            ivGender.setVisibility(View.GONE);
            tvNick.setText("");
        }

        void bind(RoomQueueInfo info, int position) {
            this.info = info;
            this.position = position;
            RoomMicInfo roomMicInfo = info.mRoomMicInfo;
            IMChatRoomMember chatRoomMember = info.mChatRoomMember;
            // 清除动画
            waveView.stop();
            rlMicLayout.setBackground(null);
            rlMicLayout.clearAnimation();
            if (position == BOSS_POSITION) {
                ivUpImage.setImageResource(R.mipmap.icon_room_up_micro_boss);
                tvNick.setTextColor(context.getResources().getColor(R.color.color_FFE66A));
            } else {
                ivUpImage.setImageResource(R.mipmap.icon_room_up_micro);
                tvNick.setTextColor(context.getResources().getColor(R.color.white));
            }
            if (roomMicInfo == null) {
                ivUpImage.setVisibility(View.VISIBLE);
                ivLockImage.setVisibility(View.GONE);
                ivMuteImage.setVisibility(View.GONE);
                ivAvatar.setVisibility(View.GONE);
                avatarBg.setVisibility(View.GONE);
                tvNick.setText("");
                ivGender.setVisibility(View.GONE);
                return;
            }

            //显示，先展示人，无视麦的锁
            if (chatRoomMember != null) {
                ivLockImage.setVisibility(View.GONE);
                ivMuteImage.setVisibility(roomMicInfo.isMicMute() ? View.VISIBLE : View.GONE);
                if (!TextUtils.isEmpty(chatRoomMember.getAccount()) && JavaUtil.str2long(chatRoomMember.getAccount()) > 0) {
                    ivUpImage.setVisibility(View.GONE);
                    ivAvatar.setVisibility(View.VISIBLE);
                    avatarBg.setVisibility(View.VISIBLE);
                    tvNick.setVisibility(View.VISIBLE);
                    if (AvRoomDataManager.get().mCurrentRoomInfo.getCharmOpen() == 1) {
                        tvCharm.setVisibility(View.VISIBLE);
                        if (AvRoomDataManager.get().getmMicCharmInfo() != null) {
                            RoomCharmInfo roomCharmInfo = AvRoomDataManager.get().getmMicCharmInfo().get(chatRoomMember.getAccount());
                            if (roomCharmInfo != null) {
                                tvCharm.setText(charmCalculate(roomCharmInfo.getValue()));
                                if (roomCharmInfo.isWithHat()) {
                                    ivHat.setVisibility(View.VISIBLE);
                                } else {
                                    ivHat.setVisibility(View.INVISIBLE);
                                }
                            } else {
                                tvCharm.setText("0");
                                ivHat.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            tvCharm.setText("0");
                            ivHat.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        tvCharm.setVisibility(View.INVISIBLE);
                        ivHat.setVisibility(View.GONE);
                    }
                    ivGender.setVisibility(View.VISIBLE);
                    tvNick.setText(chatRoomMember.getNick());
                    ivGender.setText(String.valueOf(position + 1));
                    ivGender.setBackgroundResource(chatRoomMember.getGender() == 2 ? R.mipmap.ic_room_female : R.mipmap.ic_room_male);
                    waveView.setGender(chatRoomMember.getGender());
                    ImageLoadUtils.loadAvatar(BasicConfig.INSTANCE.getAppContext(), chatRoomMember.getAvatar(), ivAvatar);

                    //--------头饰-----------
                    String headwearUrl = chatRoomMember.getHeadwear_url();
                    if (!TextUtils.isEmpty(headwearUrl)) {
                        ImageLoadUtils.loadImage(context, headwearUrl, ivHeadwear);
                        ivHeadwear.setVisibility(View.VISIBLE);
                    } else {
                        ivHeadwear.setVisibility(View.GONE);
                    }
                    //--------头像-----------
                    GlideApp.with(context)
                            .load(chatRoomMember.getAvatar())
                            .placeholder(R.drawable.ic_default_avatar)
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                                    ivAvatar.setImageDrawable(drawable);
                                    return true;
                                }
                            })
                            .into(ivAvatar);
                } else {
                    ivUpImage.setVisibility(View.VISIBLE);
                    ivAvatar.setVisibility(View.GONE);
                    avatarBg.setVisibility(View.GONE);
                    ivGender.setVisibility(View.GONE);
                    tvCharm.setVisibility(View.INVISIBLE);
                    ivHat.setVisibility(View.GONE);
                    tvNick.setText("");
                }
            } else {
                tvCharm.setVisibility(View.INVISIBLE);
                ivHat.setVisibility(View.GONE);
                //锁麦
                if (roomMicInfo.isMicLock()) {
                    ivUpImage.setVisibility(View.GONE);
                    ivMuteImage.setVisibility(roomMicInfo.isMicMute() ? View.VISIBLE : View.GONE);
                    ivLockImage.setVisibility(View.VISIBLE);
                    ivAvatar.setVisibility(View.GONE);
                    avatarBg.setVisibility(View.GONE);
                } else {
                    ivMuteImage.setVisibility(roomMicInfo.isMicMute() ? View.VISIBLE : View.GONE);
                    ivUpImage.setVisibility(View.VISIBLE);
                    ivAvatar.setVisibility(View.GONE);
                    avatarBg.setVisibility(View.GONE);
                    ivLockImage.setVisibility(View.GONE);
                }
                if (position == -1) {
                    ivGender.setVisibility(View.VISIBLE);
                    ivGender.setText("主");

                } else {
                    tvNick.setText(context.getResources().getString(R.string.micro_position, position + 1));
                    ivGender.setVisibility(View.GONE);
                }
            }
        }

        /**
         * 魅力值 单位计算
         *
         * @param charm
         * @return
         */
        public String charmCalculate(int charm) {
            if (charm < 10000) {
                return "" + charm;
            } else {
                double d = (double) charm;
                double num = d / 10000;//1.将数字转换成以万为单位的数字
                BigDecimal b = new BigDecimal(num);
                double f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();//2.转换后的数字四舍五入保留小数点后一位;
                return f1 + "万";
            }
        }

        @Override
        public void onClick(View v) {
            if (info == null || position == TYPE_INVALID || onMicroItemClickListener == null) {
                return;
            }
            if (v.getId() == R.id.up_image || v.getId() == R.id.lock_image) {
                if (position == -1) {
                    return;
                }
                onMicroItemClickListener.onUpMicBtnClick(position, info.mChatRoomMember);
            } else if (v.getId() == R.id.lock_image) {
                if (position == -1) {
                    return;
                }
                onMicroItemClickListener.onLockBtnClick(position);
            } else if (v.getId() == R.id.avatar) {
                onMicroItemClickListener.onAvatarBtnClick(position);
            } else if (v.getId() == R.id.tv_room_desc) {
                onMicroItemClickListener.onRoomSettingsClick();
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (onMicroItemClickListener != null) {
                onMicroItemClickListener.onAvatarSendMsgClick(position);
            }
            return true;
        }
    }

    public class BossMicroViewHolder extends NormalMicroViewHolder {

        /**
         * 主席位特有
         */
        ImageView tvState;
        private String userName = "";

        BossMicroViewHolder(View itemView) {
            super(itemView);
            tvState = itemView.findViewById(R.id.tv_state);
            CoreManager.addClient(this);
        }

        @Override
        void bind(RoomQueueInfo info, int position) {
            super.bind(info, position);
            try {
                RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().getRoomQueueMemberInfoByMicPosition(-1);
                if (null != roomQueueInfo && roomQueueInfo.mChatRoomMember != null) {
                    tvState.setVisibility(View.GONE);
                } else {
                    tvState.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                tvState.setVisibility(View.VISIBLE);
            }
            ivLockImage.setVisibility(View.GONE);
            ivMuteImage.setVisibility(View.GONE);
            tvCharm.setVisibility(View.INVISIBLE);
            ivUpImage.setVisibility(View.GONE);
            ivAvatar.setVisibility(View.VISIBLE);
            avatarBg.setVisibility(View.VISIBLE);
            ivHat.setVisibility(View.GONE);

            if (StringUtil.isEmpty(avatarPicture)) {
                UserInfo userInfo = CoreManager.getCore(IUserCore.class).getCacheUserInfoByUid(AvRoomDataManager.get().mCurrentRoomInfo.getUid());
                if (userInfo != null && userInfo.getAvatar() != null) {
                    if (!userInfo.getAvatar().equals(avatarPicture)) {
                        avatarPicture = userInfo.getAvatar();
                        userName = userInfo.getNick();
                        tvNick.setText(userInfo.getNick());
                        ivGender.setBackgroundResource(userInfo.getGender() == 2 ? R.mipmap.ic_room_female : R.mipmap.ic_room_male);
                        waveView.setGender(userInfo.getGender());
                        ImageLoadUtils.loadAvatar(BasicConfig.INSTANCE.getAppContext(), avatarPicture, ivAvatar);
                    }
                } else {
                    CoreManager.getCore(IUserCore.class).requestUserInfo(AvRoomDataManager.get().mCurrentRoomInfo.getUid(), new OkHttpManager.MyCallBack<UserInfo>() {
                        @Override
                        public void onError(Exception e) {

                        }

                        @Override
                        public void onResponse(UserInfo response) {
                            if (response != null && ivAvatar != null) {
                                avatarPicture = response.getAvatar();
                                tvNick.setText(response.getNick());
                                userName = response.getNick();
                                ivGender.setBackgroundResource(response.getGender() == 2 ? R.mipmap.ic_room_female : R.mipmap.ic_room_male);
                                waveView.setGender(response.getGender());
                                ImageLoadUtils.loadAvatar(BasicConfig.INSTANCE.getAppContext(), avatarPicture, ivAvatar);
                            }
                        }
                    });
                }
            } else {
                ImageLoadUtils.loadAvatar(BasicConfig.INSTANCE.getAppContext(), avatarPicture, ivAvatar);
                tvNick.setText(userName);
            }

            IMChatRoomMember roomMember = info.mChatRoomMember;
            if (roomMember != null) {
                ivGender.setText("主");
                ivGender.setBackgroundResource(roomMember.getGender() == 2 ? R.mipmap.ic_room_female : R.mipmap.ic_room_male);
                boolean isShow = false;
                long charm = 0;
                RoomInfo roomInfo = AvRoomDataManager.get().mCurrentRoomInfo;
                if (roomInfo != null && roomInfo.charmOpen != 0) {// (0.隐藏 1.显示)
                    isShow = true;
                    tvCharm.setVisibility(View.INVISIBLE);
                    if (AvRoomDataManager.get().getmMicCharmInfo() != null) {
                        RoomCharmInfo roomCharmInfo = AvRoomDataManager.get().getmMicCharmInfo().get(roomMember.getAccount());
                        if (roomCharmInfo != null) {
                            charm = roomCharmInfo.getValue();
                            if (roomCharmInfo.isWithHat()) {
                                ivHat.setVisibility(View.VISIBLE);
                            } else {
                                ivHat.setVisibility(View.INVISIBLE);
                            }
                        } else {
                            ivHat.setVisibility(View.INVISIBLE);
                        }
                    } else {
                        ivHat.setVisibility(View.INVISIBLE);
                    }
                } else {
                    tvCharm.setVisibility(View.INVISIBLE);
                    isShow = false;
                    ivHat.setVisibility(View.GONE);
                }
                onMicroItemClickListener.upMicroCharm(charm, isShow);
            }
            ivGender.setVisibility(View.GONE);
        }

        @Override
        public void clear() {
            super.clear();
            CoreManager.removeClient(this);
        }
    }

    /**
     * 更新麦位魅力值
     *
     * @param roomCharmAttachment
     */
    public void updateCharmData(RoomCharmAttachment roomCharmAttachment) {
        if (roomCharmAttachment == null || AvRoomDataManager.get().mMicQueueMemberMap == null) {
            return;
        }
        if (roomCharmAttachment.getLatestCharm() != null
                && roomCharmAttachment.getLatestCharm().size() > 0
                && roomCharmAttachment.getTimestamps() > AvRoomDataManager.get().getCharmTimestamps()) {
            boolean isFirst = AvRoomDataManager.get().getCharmTimestamps() == 0;
            AvRoomDataManager.get().setCharmTimestamps(roomCharmAttachment.getTimestamps());
            for (int i = 0; i < AvRoomDataManager.get().mMicQueueMemberMap.size(); i++) {
                RoomQueueInfo roomQueueInfo = AvRoomDataManager.get().mMicQueueMemberMap.valueAt(i);
                if (roomQueueInfo != null && roomQueueInfo.mChatRoomMember != null) {
                    if (roomQueueInfo.mChatRoomMember.getAccount() != null) {
                        RoomCharmInfo roomCharmInfo = roomCharmAttachment.getLatestCharm().get(roomQueueInfo.mChatRoomMember.getAccount());
                        if (!isFirst && roomCharmInfo != null) {
                            notifyItemChanged(roomQueueInfo.mRoomMicInfo.getPosition() + 1);
                        }
                    }
                }
            }
            if (isFirst) {
                notifyDataSetChanged();
            }
        }
    }

    public void clear(NormalMicroViewHolder holder) {
        holder.clear();
    }

    public interface OnMicroItemClickListener {

        void onAvatarBtnClick(int position);

        void onUpMicBtnClick(int position, IMChatRoomMember chatRoomMember);

        void onLockBtnClick(int position);

        void onRoomSettingsClick();

        void onContributeListClick();

        void onOnlinePeopleClick();

        void onAvatarSendMsgClick(int position);

        void upMicroCharm(long charm, boolean isShow);
    }
}
