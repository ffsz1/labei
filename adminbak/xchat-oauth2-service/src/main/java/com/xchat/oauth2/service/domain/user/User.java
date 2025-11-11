package com.xchat.oauth2.service.domain.user;

import com.xchat.oauth2.service.domain.AbstractDomain;

import java.util.Date;

/**
 * @author liuguofu
 */
public class User extends AbstractDomain {

    private String username;
    private String password;

    private Date birthday;
    private String phone;
    private String email;
    //Default user is initial when create database, do not delete
    private boolean defaultUser = false;

    private Date lastLoginTime;
    private String lastLoginIp;

    private Boolean gender;
    private String avatar;
    private String nickname;
    private String region;
    private String description;
    private Date signTime;
    private Date updateTime;

    private String qqOpenId;
    private String weixinOpenId;
    private String weiboOpenId;

    private String qqNick;
    private String weixinNick;
    private String weiboNick;

    private String mainAccount;

    private Long roomId;
    private Long playListId;

    public User() {
    }

    public User(String username, String password, String phone, String email) {
        this.username = username;
        this.password = password;
        this.phone = phone;
        this.email = email;
    }

    public boolean defaultUser() {
        return defaultUser;
    }

    public String username() {
        return username;
    }

    public String password() {
        return password;
    }

    public Date birthday(){
        return birthday;
    }

    public String phone() {
        return phone;
    }

    public String email() {
        return email;
    }


    public String lastLoginIp(){
        return lastLoginIp;
    }

    public Boolean gender(){
        return gender;
    }

    public String avatar(){
        return avatar;
    }

    public String nickname(){
        return nickname;
    }

    public String region(){
        return region;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getPlayListId() {
        return playListId;
    }

    public void setPlayListId(Long playListId) {
        this.playListId = playListId;
    }

    public String description(){
        return description;
    }

    public Date updateTime(){
        return updateTime;
    }

    public Date lastLoginTime() {
        return lastLoginTime;
    }

    public String qqOpenId(){
        return qqOpenId;
    }

    public String weixinOpenId(){
        return weixinOpenId;
    }

    public String weiboOpenId(){
        return weiboOpenId;
    }

    public User email(String email) {
        this.email = email;
        return this;
    }

    public User phone(String phone) {
        this.phone = phone;
        return this;
    }


    public User username(String username) {
        this.username = username;
        return this;
    }

    public User password(String password) {
        this.password = password;
        return this;
    }

    public User birthday(Date birthday){
        this.birthday = birthday;
        return this;
    }

    public User lastLoginIp(String lastLoginIp){
        this.lastLoginIp = lastLoginIp;
        return this;
    }

    public User gender(Boolean gender){
        this.gender = gender;
        return this;
    }

    public User avatar(String avatar){
        this.avatar = avatar;
        return this;
    }

    public User nickname(String nickname){
        this.nickname = nickname;
        return this;
    }

    public User region(String region){
        this.region = region;
        return this;
    }

    public User description(String description){
        this.description = description;
        return this;
    }

    public User signTime(Date signTime){
        this.signTime = signTime;
        return this;
    }

    public User lastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        return this;
    }

    public User qqOpenId(String qqOpenId){
        this.qqOpenId = qqOpenId;
        return this;
    }

    public User weixinOpenId(String weixinOpenId){
        this.weixinOpenId = weixinOpenId;
        return this;
    }

    public User weiboOpenId(String weiboOpenId){
        this.weiboOpenId = weiboOpenId;
        return this;
    }

    public String qqNick() {
        return qqNick;
    }

    public User qqNick(String qqNick) {
        this.qqNick = qqNick;
        return this;
    }

    public String weixinNick() {
        return weixinNick;
    }

    public User weixinNick(String weixinNick) {
        this.weixinNick = weixinNick;
        return this;
    }

    public String weiboNick() {
        return weiboNick;
    }

    public User weiboNick(String weiboNick) {
        this.weiboNick = weiboNick;
        return this;
    }

    public String mainAccount() {
        return mainAccount;
    }

    public User mainAccount(String mainAccount) {
        this.mainAccount = mainAccount;
        return this;
    }

    public void updateTime(Date updateTime){
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", defaultUser=" + defaultUser +
                ", lastLoginTime=" + lastLoginTime +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                ", gender=" + gender +
                ", avatar='" + avatar + '\'' +
                ", nickname='" + nickname + '\'' +
                ", region='" + region + '\'' +
                ", description='" + description + '\'' +
                ", signTime=" + signTime +
                ", updateTime=" + updateTime +
                ", qqOpenId='" + qqOpenId + '\'' +
                ", weixinOpenId='" + weixinOpenId + '\'' +
                ", weiboOpenId='" + weiboOpenId + '\'' +
                ", qqNick='" + qqNick + '\'' +
                ", weixinNick='" + weixinNick + '\'' +
                ", weiboNick='" + weiboNick + '\'' +
                '}';
    }
}
