package com.juxiao.xchat.manager.common.conf;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import sun.misc.Signal;

@Component
public class ControlConf implements InitializingBean {



    @Override
    public void afterPropertiesSet(){
        Signal sig = new Signal(getOSSignalType());
        Signal.handle(sig, new SignalConf());
    }


    private String getOSSignalType()
    {
        return System.getProperties().getProperty("os.name").
                toLowerCase().startsWith("win") ? "INT" : "USR2";
    }
}
