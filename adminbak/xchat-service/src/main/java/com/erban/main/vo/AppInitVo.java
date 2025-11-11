package com.erban.main.vo;

import com.erban.main.model.FaceJson;
import com.erban.main.model.NobleRight;
import com.erban.main.model.NobleZip;

import java.util.List;

/**
 * APP初始化拉取的数据
 */
public class AppInitVo {

    private FaceJson faceJson;  // 表情配置
    private SplashVo splashVo;  // 闪屏配置
    private NobleZip nobleZip;  // 贵族资源ZIP包
    private List<NobleRight> rights;  // 贵族特权

    public NobleZip getNobleZip() {
        return nobleZip;
    }

    public void setNobleZip(NobleZip nobleZip) {
        this.nobleZip = nobleZip;
    }

    public SplashVo getSplashVo() {
        return splashVo;
    }

    public void setSplashVo(SplashVo splashVo) {
        this.splashVo = splashVo;
    }

    public FaceJson getFaceJson() {
        return faceJson;
    }

    public void setFaceJson(FaceJson faceJson) {
        this.faceJson = faceJson;
    }

    public List<NobleRight> getRights() {
        return rights;
    }

    public void setRights(List<NobleRight> rights) {
        this.rights = rights;
    }
}
