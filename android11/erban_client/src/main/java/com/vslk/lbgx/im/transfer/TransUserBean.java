package com.vslk.lbgx.im.transfer;

import java.io.Serializable;

import lombok.Data;

@Data
public class TransUserBean implements Serializable {


    /**
     * sendUid : 3
     * sendName : 试试
     * sendAvatar : http://pic.zoudewu.com/FreaqEOo0h0M1NH-kUOlf8GHmUbw?imageslim
     * recvUid : 62
     * recvName : fantasy
     * recvAvatar : http://pic.zoudewu.com/FgZPZSQyNLJxN6J2PAEXYNQJNJOc?imageslim
     * goldNum : 333
     * sendDesc : null
     */

    private int sendUid;
    private String sendName;
    private String sendAvatar;
    private int recvUid;
    private String recvName;
    private String recvAvatar;
    private int goldNum;
    private Object sendDesc;
}
