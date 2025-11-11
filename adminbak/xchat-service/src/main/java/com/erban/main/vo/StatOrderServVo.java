package com.erban.main.vo;

public class StatOrderServVo {
    private Long avgChatTime;

    private Long chatTime;

    private Integer chatNum;

    private Integer oneChat;
    private Integer moreChat;

    public void setOneChat(Integer oneChat) {
        this.oneChat = oneChat;
    }

    public void setMoreChat(Integer moreChat) {
        this.moreChat = moreChat;
    }

    public Integer getOneChat() {

        return oneChat;
    }

    public Integer getMoreChat() {
        return moreChat;
    }

    public void setChatNum(Integer chatNum) {
        this.chatNum = chatNum;
    }

    public Integer getChatNum() {

        return chatNum;
    }

    public void setChatTime(Long chatTime) {
        this.chatTime = chatTime;
    }

    public Long getChatTime() {

        return chatTime;
    }

    public void setAvgChatTime(Long avgChatTime) {
        this.avgChatTime = avgChatTime;
    }

    public Long getAvgChatTime() {

        return avgChatTime;
    }
}
