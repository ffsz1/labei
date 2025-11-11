package com.juxiao.xchat.manager.external.weixin;

public interface WeixinCustomManager {

    /**
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     * @author: chenjunsheng
     * @date 2018/5/8
     */
    String checkWeixinSignature(String signature, String timestamp, String nonce, String echostr);

    /**
     * 接收客服加密消息
     *
     * @param encrypt
     */
    void receiveWeixinMessage(String encrypt);
}
