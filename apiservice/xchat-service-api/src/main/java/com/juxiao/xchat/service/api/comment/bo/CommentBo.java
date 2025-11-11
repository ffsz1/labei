package com.juxiao.xchat.service.api.comment.bo;

import com.juxiao.xchat.dao.comment.domain.Comment;

public class CommentBo {
    private Long id;   //主键

    private Long uid;  //用户id

    private Long topicid; //话题id

    private String state;//状态

    private String comment;//评论


    public Comment getComments(){
        Comment comment=new Comment();
        comment.setId(this.getId());
        comment.setUid(this.getUid());
        comment.setTopicid(this.getTopicid());
        comment.setState(this.getState());
        comment.setComment(this.getComment());
        return comment;
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
}
