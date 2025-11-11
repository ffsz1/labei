package com.tongdaxing.xchat_framework.im;

import org.java_websocket.handshake.ServerHandshake;

public interface IConnectListener {
    void onSuccess(ServerHandshake serverHandshake);

    void onError(Exception e);
}
