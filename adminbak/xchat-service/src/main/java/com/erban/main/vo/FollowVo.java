package com.erban.main.vo;

import com.beust.jcommander.internal.Lists;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by liuguofu on 2017/7/10.
 */
public class FollowVo implements Comparable<FollowVo> {
    private Long uid;
    private boolean valid;

    private String avatar;
    private String nick;
    private Integer fansNum;

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void setFansNum(Integer fansNum) {
        this.fansNum = fansNum;
    }

    public String getAvatar() {

        return avatar;
    }

    public String getNick() {
        return nick;
    }

    public Integer getFansNum() {
        return fansNum;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public int compareTo(FollowVo followVo) {
        boolean validVo = followVo.isValid();
        boolean validThis = this.valid;
        int validVoInt = 0;
        int validThisInt = 0;
        if (validVo) {
            validVoInt = 1;
        }
        if (validThis) {
            validThisInt = 1;
        }
        if (validVoInt > validThisInt)
            return 1;
        else if (validVoInt < validThisInt)
            return -1;
        else
            return 0;
    }

    public static void main(String args[]) {
        FollowVo followVo = new FollowVo();
        followVo.setUid(1L);
        followVo.setValid(false);


        FollowVo followVo1 = new FollowVo();
        followVo1.setUid(2L);
        followVo1.setValid(true);


        FollowVo followVo2 = new FollowVo();
        followVo2.setUid(3L);
        followVo2.setValid(false);


        FollowVo followVo3 = new FollowVo();
        followVo3.setUid(4L);
        followVo3.setValid(true);

        FollowVo followVo4 = new FollowVo();
        followVo4.setUid(5L);
        followVo4.setValid(false);
        List<FollowVo> list = Lists.newArrayList();

        list.add(followVo);
        list.add(followVo1);
        list.add(followVo2);
        list.add(followVo3);
        list.add(followVo4);

        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i).getUid());
        }
        Collections.sort(list);
        System.out.println("----------------");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i).getUid());
        }
    }
}
