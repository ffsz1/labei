package com.tongdaxing.xchat_core.initial;


import java.io.Serializable;

/**
 * @author xiaoyu
 * @date 2017/12/8
 */
public class InitInfo implements Serializable {
    private FaceComponent faceJson;
    private SplashComponent splashVo;

    public FaceComponent getFaceJson() {
        return faceJson;
    }

    public void setFaceJson(FaceComponent faceJson) {
        this.faceJson = faceJson;
    }

    public SplashComponent getSplashVo() {
        return splashVo;
    }

    public void setSplashVo(SplashComponent splashVo) {
        this.splashVo = splashVo;
    }

    @Override
    public String toString() {
        return "InitInfo{" +
                "faceJson=" + faceJson +
                ", splashVo=" + splashVo +
                '}';
    }
}
