package com.tongdaxing.xchat_core.find.family;

import java.util.List;

/**
 * 创建者      Created by dell
 * 创建时间    2018/12/10
 * 描述
 *
 * 更新者      dell
 * 更新时间
 * 更新描述
 *
 * @author dell
 */
public class SquareMemberInfo {

    private int count;
    private List<Avatar> ulist;

    public class Avatar {
        private String avatar;

        public String getAvatar() {
            return avatar;
        }

        @Override
        public String toString() {
            return "Avatar{" +
                    "avatar='" + avatar + '\'' +
                    '}';
        }
    }

    public int getCount() {
        return count;
    }

    public List<Avatar> getUlist() {
        return ulist;
    }

    @Override
    public String toString() {
        return "SquareMemberInfo{" +
                "count=" + count +
                ", ulist=" + ulist +
                '}';
    }
}
