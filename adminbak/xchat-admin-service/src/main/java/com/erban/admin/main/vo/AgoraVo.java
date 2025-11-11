package com.erban.admin.main.vo;

/**
 * @author laizhilong
 * @Title:
 * @Package com.erban.admin.main.vo
 * @date 2018/8/1
 * @time 14:11
 */
public class AgoraVo {

    private Boolean success;
    private String request_id;
    private AgoraData data;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public AgoraData getData() {
        return data;
    }

    public void setData(AgoraData data) {
        this.data = data;
    }

    public class AgoraData{
        private Boolean channel_exist;
        private Integer mode;
        private Long[] broadcasters;
        private Long[] audience;
        private Integer audience_total;

        public Boolean getChannel_exist() {
            return channel_exist;
        }

        public void setChannel_exist(Boolean channel_exist) {
            this.channel_exist = channel_exist;
        }

        public Integer getMode() {
            return mode;
        }

        public void setMode(Integer mode) {
            this.mode = mode;
        }

        public Long[] getBroadcasters() {
            return broadcasters;
        }

        public void setBroadcasters(Long[] broadcasters) {
            this.broadcasters = broadcasters;
        }

        public Long[] getAudience() {
            return audience;
        }

        public void setAudience(Long[] audience) {
            this.audience = audience;
        }

        public Integer getAudience_total() {
            return audience_total;
        }

        public void setAudience_total(Integer audience_total) {
            this.audience_total = audience_total;
        }
    }
}
