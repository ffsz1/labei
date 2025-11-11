package com.erban.main.dto;

/**
 * @author chris
 * @Title: 求签DTO数据封装
 * @date 2018/9/13
 * @time 下午5:29
 */
public class RoomGameConfigDTO {

    private Integer id;

    private Long uid;

    private String erbanNo;

    private String nick;

    private String start;

    private String end;

    private Integer status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getErbanNo() {
        return erbanNo;
    }

    public void setErbanNo(String erbanNo) {
        this.erbanNo = erbanNo;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RoomGameConfigDTO{" +
                "id=" + id +
                ", uid=" + uid +
                ", erbanNo='" + erbanNo + '\'' +
                ", nick='" + nick + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", status=" + status +
                '}';
    }
}
