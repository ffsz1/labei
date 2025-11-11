package test.juxiao.xchat.manager.external;

import com.juxiao.xchat.api.configer.ApiWebApplication;
import com.juxiao.xchat.dao.item.GiftDao;
import com.juxiao.xchat.dao.item.dto.GiftDTO;
import com.juxiao.xchat.manager.external.zego.ZegoManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Auther: alwyn
 * @Description: 礼物DAO层测试
 * @Date: 2018/9/25 11:05
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiWebApplication.class)
public class ZegoManagerTest {

    @Autowired
    private ZegoManager zegoManager;

    @Test
    public void getAccessToken() {
        String accessToken = null;
        try {
            accessToken = zegoManager.getAccessToken();
            System.out.println(accessToken);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
