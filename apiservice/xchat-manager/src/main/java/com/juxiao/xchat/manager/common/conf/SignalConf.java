package com.juxiao.xchat.manager.common.conf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Signal;
import sun.misc.SignalHandler;

public class SignalConf implements SignalHandler {

    private static final Logger logger = LoggerFactory.getLogger(SignalConf.class);


    /**
     * 捕获到的信号id
     */
    public static int signalNum = 0;

    public static String signalName = "None";


    public static void signalCallback(Signal sn) {
        System.out.println(sn.getName() + "is recevied.##################");

        logger.error("=======================获取信号, number:{}, name:{}", sn.getNumber(), sn.getName());
        logger.error("=======================设置全局信号");
        signalNum = sn.getNumber();
        signalName = sn.getName();

    }

    @Override
    public void handle(Signal signalName) {
        signalCallback(signalName);
    }


}
