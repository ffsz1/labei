package com.tongdaxing.erban.libcommon.widget;


import com.tongdaxing.erban.libcommon.R;

public class ButtonItem {
    public static final int BUTTON_TYPE_NORMAL = 0;
    public static final int BUTTON_TYPE_CANCEL = 1;


    /** 发送礼物 */
    public static final int SEND_GIFT_ITEM = 0;
    /** 锁坑 */
    public static final int SEND_LOCK_MIC_ITEM = 1;
    /** 踢下麦 */
    public static final int SEND_KICKDOWN_MIC_ITEM = 2;
    /** 踢出房间 */
    public static final int SEND_KICKOUT_ROOM_ITEM = 3;
    /** 查看个人信息 */
    public static final int SEND_SHOW_USER_INCO_ITEM = 4;
    /** 下麦 */
    public static final int SEND_DOWN_MIC_ITEM = 5;
    /** 释放麦 */
    public static final int SEND_FREE_MIC_ITEM = 6;
    /** 设置管理员 */
    public static final int SEND_MARK_MANAGER_ITEM = 7;
    /** 取消管理员 */
    public static final int SEND_NOMARK_MANAGER_ITEM = 11;
    /** 加入黑名单 */
    public static final int SEND_MARK_BLACK_ITEM = 8;
    /** 开麦 */
    public static final int SEND_OPEN_MUTE_ITEM = 9;
    /** 闭麦 */
    public static final int SEND_CLOSE_MUTE_ITEM = 10;
    /** 抱上麦 */
    public static final int SEND_INVITE_MIC_ITEM = 12;
    /** 发起竞拍 */
    public static final int START_AUCTION = 13;

    public String mText;
    public int resourceID;
    public OnClickListener mClickListener;
    public int mButtonType;
    public int mTheme = -1;
    public String textColor;

    public ButtonItem(String text, OnClickListener l) {
        this(text, BUTTON_TYPE_NORMAL, l);
    }

    public ButtonItem(String text, int buttonType, OnClickListener l) {
        mText = text;
        mClickListener = l;
        mButtonType = buttonType;
        resourceID = R.layout.layout_common_popup_dialog_button;

    }

    public ButtonItem(String text, int buttonType, int theme, OnClickListener l) {
        mText = text;
        mClickListener = l;
        mButtonType = buttonType;
        resourceID = R.layout.layout_common_popup_dialog_button;
        mTheme = theme;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setClickListener(OnClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    public interface OnClickListener {
        void onClick();
    }
}