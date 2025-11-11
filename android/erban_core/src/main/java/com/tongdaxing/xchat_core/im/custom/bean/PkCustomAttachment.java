package com.tongdaxing.xchat_core.im.custom.bean;

import com.alibaba.fastjson.JSONObject;
import com.tongdaxing.xchat_core.pk.bean.PkVoteInfo;

/**
 * PK自定义消息封装实体
 * @author zwk
 */
public class PkCustomAttachment extends IMCustomAttachment {
    private PkVoteInfo pkVoteInfo;

    public PkCustomAttachment(int first, int second) {
        super(first, second);
    }

    public PkVoteInfo getPkVoteInfo() {
        return pkVoteInfo;
    }

    public void setPkVoteInfo(PkVoteInfo pkVoteInfo) {
        this.pkVoteInfo = pkVoteInfo;
    }

    @Override
    protected void parseData(JSONObject data) {
        super.parseData(data);
        pkVoteInfo = new PkVoteInfo();
        pkVoteInfo.setPkType(data.getIntValue("pkType"));
        pkVoteInfo.setDuration(data.getIntValue("duration"));
        pkVoteInfo.setTimestamps(data.getLongValue("timestamps"));

        pkVoteInfo.setOpUid(data.getLongValue("opUid"));
        pkVoteInfo.setUid(data.getLongValue("uid"));
        pkVoteInfo.setNick(data.getString("nick"));
        pkVoteInfo.setAvatar(data.getString("avatar"));
        pkVoteInfo.setVoteCount(data.getIntValue("voteCount"));

        pkVoteInfo.setPkUid(data.getLongValue("pkUid"));
        pkVoteInfo.setPkNick(data.getString("pkNick"));
        pkVoteInfo.setPkAvatar(data.getString("pkAvatar"));
        pkVoteInfo.setPkVoteCount(data.getIntValue("pkVoteCount"));
    }

    @Override
    protected JSONObject packData() {
        JSONObject object = new JSONObject();
        object.put("pkType", pkVoteInfo.getPkType());
        object.put("duration", pkVoteInfo.getDuration());
        object.put("timestamps", pkVoteInfo.getTimestamps());
        object.put("opUid",pkVoteInfo.getOpUid());

        object.put("uid", pkVoteInfo.getUid());
        object.put("nick", pkVoteInfo.getNick());
        object.put("avatar", pkVoteInfo.getAvatar());
        object.put("voteCount", pkVoteInfo.getVoteCount());

        object.put("pkUid", pkVoteInfo.getPkUid());
        object.put("pkNick", pkVoteInfo.getPkNick());
        object.put("pkAvatar", pkVoteInfo.getPkAvatar());
        object.put("pkVoteCount", pkVoteInfo.getPkVoteCount());
        return object;
    }
}
