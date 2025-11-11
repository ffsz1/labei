package com.juxiao.xchat.service.api.comment.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.juxiao.xchat.dao.comment.dto.CommentDTO;
import com.juxiao.xchat.dao.reply.dto.ReplyDTO;

import java.util.Date;
import java.util.List;

public class CommentVo {
    private Long id;

    private Long uid;

    private Long topicid;

    private String state;

    private String comment;

    private Integer replynum;//回复数
    private Integer praisenum;//点赞数

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    private String nick;
    private Byte gender;
    private String avatar;//头像

    private List<ReplyDTO> replys;
    private Integer hasnext;
    private Integer isPraise;


    public CommentVo() {
    }
    public CommentVo(CommentDTO commentDTO) {
        this.id=commentDTO.getId();
        this.uid=commentDTO.getUid();
        this.topicid=commentDTO.getTopicid();
        this.state=commentDTO.getState();
        this.comment=commentDTO.getComment();
        this.replynum=commentDTO.getReplynum();//回复数
        this.praisenum=commentDTO.getPraisenum();//点赞数
        this.createTime=commentDTO.getCreateTime();
        this.updateTime=commentDTO.getUpdateTime();
        this.nick=commentDTO.getNick();
        this.gender=commentDTO.getGender();
        this.avatar=commentDTO.getAvatar();//头像
    }

    public List<ReplyDTO> getReplys() {
        return replys;
    }

    public void setReplys(List<ReplyDTO> replys) {
        this.replys = replys;
    }

    public Integer getHasnext() {
        return hasnext;
    }

    public void setHasnext(Integer hasnext) {
        this.hasnext = hasnext;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getTopicid() {
        return topicid;
    }

    public void setTopicid(Long topicid) {
        this.topicid = topicid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getReplynum() {
        return replynum;
    }

    public void setReplynum(Integer replynum) {
        this.replynum = replynum;
    }

    public Integer getPraisenum() {
        return praisenum;
    }

    public void setPraisenum(Integer praisenum) {
        this.praisenum = praisenum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getIsPraise() {
        return isPraise;
    }

    public void setIsPraise(Integer isPraise) {
        this.isPraise = isPraise;
    }
}
