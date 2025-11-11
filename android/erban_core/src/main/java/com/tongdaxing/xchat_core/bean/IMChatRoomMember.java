package com.tongdaxing.xchat_core.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.netease.nimlib.sdk.chatroom.constant.MemberType;

import java.io.Serializable;

public class IMChatRoomMember implements Serializable, Parcelable {
    private String account;     // 成员账号
    private MemberType type;    // 成员类型：主要分为游客和非游客，非游客又分成受限用户、普通用户、创建者、管理员;

    /// 进入聊天室时提交
    private String nick;        // 聊天室内的昵称字段，预留字段，可从NimUserInfo中取，也可以由用户进聊天室时提交。
    private String avatar;      // 聊天室内的头像，预留字段，可从NimUserInfo中取avatar，可以由用户进聊天室时提交。
    //财富等级
    private int exper_level;
    //魅力等级
    private int charm_level;
    //是否是萌新
    private boolean is_new_user;
    //座驾名称
    private String car_name;
    //座驾动画url
    private String car_url;
    //头饰
    private String headwear_url;
    //1是活人，3是机器人
    private String defUser;
    // 仅成员在线时有效
    private boolean is_online;   // 成员是否处于在线状态，仅特殊成员才可能离线，对游客/匿名用户而言只能是在线。
    private boolean is_black_list;// 是否在黑名单中
    private boolean is_mute;    // 是禁言用户
    private boolean is_manager;

    public boolean isIs_manager() {
        return is_manager;
    }

    public void setIs_manager(boolean is_manager) {
        this.is_manager = is_manager;
    }

    private long enter_time;     // 进入聊天室的时间点,对于离线成员该字段为空
    private int gender;
    private int online_num;//在线人数用于退出和进入消息
    private long timestamp;//在线人数时间戳用与判断在线人数更新的及时性


    public IMChatRoomMember() {
    }

    public IMChatRoomMember(com.netease.nimlib.sdk.chatroom.model.ChatRoomMember member) {
        this();
        if (member != null) {

            setAccount(member.getAccount());
            setMemberType(member.getMemberType());
            setNick(member.getNick());
            setAvatar(member.getAvatar());

            setIs_online(member.isOnline());
            setIs_black_list(member.isInBlackList());
            setIs_mute(member.isMuted());
            setEnter_time(member.getEnterTime());
        }
    }

    public String getDef_user() {
        return defUser;
    }

    public void setDef_user(String def_user) {
        this.defUser = def_user;
    }

    /**
     * 获取成员帐号
     *
     * @return 成员account
     */
    public String getAccount() {
        return account;
    }

    /**
     * 获取成员类型
     * 成员类型：主要分为游客和非游客。
     * 非游客又分成受限用户、普通用户、创建者、管理员;
     *
     * @return 成员类型
     */
    public MemberType getMemberType() {
        return type;
    }

    /**
     * 获取昵称
     * 可从NimUserInfo中取，也可以由用户进聊天室时提交。
     *
     * @return 昵称
     */
    public String getNick() {
        return nick;
    }

    /**
     * 获取头像
     * 可从NimUserInfo中取avatar，可以由用户进聊天室时提交。
     *
     * @return 头像
     */
    public String getAvatar() {
        return avatar;
    }


    /**
     * 获取进入聊天室时间
     * 对于离线成员该字段为空
     *
     * @return 进入聊天室时间
     */
    public long getEnter_time() {
        return enter_time;
    }

    /**
     * 判断用户是否处于在线状态
     * 仅特殊成员才可能离线，对游客/匿名用户而言只能是在线。
     *
     * @return 是否在线
     */
    public boolean isIs_online() {
        return is_online;
    }

    /**
     * 判断用户是否在黑名单中
     *
     * @return 是否在黑名单中
     */
    public boolean isIs_black_list() {
        return is_black_list;
    }

    /**
     * 判断用户是否被禁言
     *
     * @return 是否被禁言
     */
    public boolean isIs_mute() {
        return is_mute;
    }


    /**
     * 设置用户帐号
     *
     * @param account 用户帐号
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 设置成员类型
     *
     * @param type 成员类型
     */
    public void setMemberType(MemberType type) {
        this.type = type;
    }

    /**
     * 设置成员昵称
     *
     * @param nick 昵称
     */
    public void setNick(String nick) {
        this.nick = nick;
    }

    /**
     * 设置成员头像
     *
     * @param avatar 头像
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


    /**
     * 设置在线状态
     *
     * @param is_online 在线状态
     */
    public void setIs_online(boolean is_online) {
        this.is_online = is_online;
    }

    /**
     * 设置进入聊天室时间
     *
     * @param enter_time 进入聊天室时间
     */
    public void setEnter_time(long enter_time) {
        this.enter_time = enter_time;
    }

    /**
     * 设置是否在黑名单中
     *
     * @param is_black_list 是否设置为黑名单
     */
    public void setIs_black_list(boolean is_black_list) {
        this.is_black_list = is_black_list;
    }

    /**
     * 设置是否禁言
     *
     * @param is_mute 是否禁言
     */
    public void setIs_mute(boolean is_mute) {
        this.is_mute = is_mute;
    }


    public int getExperLevel() {
        return exper_level;
    }

    public void setExperLevel(int experLevel) {
        this.exper_level = experLevel;
    }

    public int getCharmLevel() {
        return charm_level;
    }

    public void setCharmLevel(int charmLevel) {
        this.charm_level = charmLevel;
    }

    public boolean isIs_new_user() {
        return is_new_user;
    }

    public void setIs_new_user(boolean is_new_user) {
        this.is_new_user = is_new_user;
    }

    public MemberType getType() {
        return type;
    }

    public void setType(MemberType type) {
        this.type = type;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public int getExper_level() {
        return exper_level;
    }

    public void setExper_level(int exper_level) {
        this.exper_level = exper_level;
    }

    public boolean getIs_new_user() {
        return is_new_user;
    }

    public String getHeadwear_url() {
        return headwear_url;
    }

    public void setHeadwear_url(String headwear_url) {
        this.headwear_url = headwear_url;
    }

    public String getCar_url() {
        return car_url;
    }

    public void setCar_url(String car_url) {
        this.car_url = car_url;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getCharm_level() {
        return charm_level;
    }

    public void setCharm_level(int charm_level) {
        this.charm_level = charm_level;
    }

    public int getOnline_num() {
        return online_num < 0 ? 0 : online_num;
    }

    public void setOnline_num(int online_num) {
        this.online_num = online_num;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * ********************************** 序列化 **********************************
     */


    protected IMChatRoomMember(Parcel in) {
        account = in.readString();
        type = MemberType.typeOfValue(in.readInt());
        nick = in.readString();
        avatar = in.readString();
        is_online = in.readByte() == 0x01;
        is_black_list = in.readByte() == 0x01;
        is_mute = in.readByte() == 0x01;
        is_manager = in.readByte() == 0x01;
        enter_time = in.readLong();
        exper_level = in.readInt();
        charm_level = in.readInt();
        is_new_user = in.readByte() == 0x01;
        car_name = in.readString();
        car_url = in.readString();
        headwear_url = in.readString();
        gender = in.readInt();
        online_num = in.readInt();
        timestamp = in.readLong();
        defUser = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(account);
        dest.writeInt(type.getValue());
        dest.writeString(nick);
        dest.writeString(avatar);
        dest.writeByte(is_online ? (byte) 0x01 : 0x00);
        dest.writeByte(is_black_list ? (byte) 0x01 : 0x00);
        dest.writeByte(is_mute ? (byte) 0x01 : 0x00);
        dest.writeByte(is_manager ? (byte) 0x01 : 0x00);
        dest.writeLong(enter_time);
        dest.writeInt(charm_level);
        dest.writeInt(exper_level);
        dest.writeByte(is_new_user ? (byte) 0x01 : 0x00);
        dest.writeString(car_name);
        dest.writeString(car_url);
        dest.writeString(headwear_url);
        dest.writeInt(gender);
        dest.writeInt(online_num);
        dest.writeLong(timestamp);
        dest.writeString(defUser);
    }

    @Override
    public String toString() {
        return "IMChatRoomMember{" +
                "account='" + account + '\'' +
                ", type=" + type +
                ", nick='" + nick + '\'' +
                ", avatar='" + avatar + '\'' +
                ", exper_level=" + exper_level +
                ", charm_level=" + charm_level +
                ", is_new_user=" + is_new_user +
                ", car_name='" + car_name + '\'' +
                ", car_url='" + car_url + '\'' +
                ", headwear_url='" + headwear_url + '\'' +
                ", defUser='" + defUser + '\'' +
                ", is_online=" + is_online +
                ", is_black_list=" + is_black_list +
                ", is_mute=" + is_mute +
                ", is_manager=" + is_manager +
                ", enter_time=" + enter_time +
                ", gender=" + gender +
                ", online_num=" + online_num +
                ", timestamp=" + timestamp +
                '}';
    }

    public static final Parcelable.Creator<IMChatRoomMember> CREATOR = new Parcelable.Creator<IMChatRoomMember>() {
        @Override
        public IMChatRoomMember createFromParcel(Parcel in) {
            return new IMChatRoomMember(in);
        }

        @Override
        public IMChatRoomMember[] newArray(int size) {
            return new IMChatRoomMember[size];
        }
    };
}
