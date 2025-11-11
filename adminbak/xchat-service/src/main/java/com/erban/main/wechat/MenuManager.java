package com.erban.web.wechat;


import com.erban.main.config.SystemConfig;
import com.erban.main.config.WxConfig;
import com.erban.main.util.WeiXinUtil;
import com.erban.main.wechat.AccessToken;
import com.erban.main.wechat.pojo.Button;
import com.erban.main.wechat.pojo.CommonButton;
import com.erban.main.wechat.pojo.ComplexButton;
import com.erban.main.wechat.pojo.Menu;
import com.xchat.common.config.GlobalConfig;
import com.xchat.common.constant.Constant;
import org.apache.log4j.Logger;

/**
 * MenuManager
 *
 * @author 电子小孩
 * 菜单创建及管理类
 * 2016年5月27日 下午5:11:32
 */
public class MenuManager {

    private static final Logger LOGGER = Logger.getLogger ( MenuManager.class );

    public static String createMenu ( ) {
        LOGGER.debug ( "------------开始创建菜单--------------------" );
        // 第三方用户唯一凭证
        String appId = WxConfig.appId;
        LOGGER.info ( "=====appId:" + appId );
        // 第三方用户唯一凭证密钥
        String appSecret = WxConfig.appSecret;
        LOGGER.info ( "=====appSecret:" + appSecret );
        // 调用接口获取access_token
        AccessToken at = WeiXinUtil.getAccessToken ( appId , appSecret );

        String result = "";
        if ( null != at ) {
            LOGGER.info ( "==========AccessToken:" + at.getToken ( ) + "==========" );
            // 调用接口创建菜单
            result = WeiXinUtil.createMenu ( getMenu ( ) , at.getToken ( ) );
            // 判断菜单创建结果
            if ( result.equals ( "0" ) ) {
                LOGGER.info ( "菜单创建成功！" );
            } else {
                LOGGER.info ( "菜单创建失败，错误码：" + result );
            }
        } else {
            LOGGER.info ( "-----------获取 AccessToken 失败----------" );
        }
        return result;
    }

    /**
     * 组装菜单数据
     * view - URL跳转
     * check-事件推送
     *
     * @return
     */
    private static Menu getMenu ( ) {
        CommonButton btn11 = new CommonButton ( );
        btn11.setName ( "领取红包" );
        btn11.setType ( "view" );
        btn11.setKey ( "11" );
        btn11.setUrl ( WxConfig.home );
        //==========与设备对接菜单=============//
        CommonButton btn21 = new CommonButton ( );
        btn21.setName ( "金币充值" );
        btn21.setType ( "view" );
        btn21.setKey ( "21" );
        btn21.setUrl ( "https://open.weixin.qq.com/connect/oauth2/authorize?" +
                "appid=" + WxConfig.appId +
                "&redirect_uri=" + WxConfig.getCodeUrl +
                "&response_type=code&scope=snsapi_userinfo" +
                "&state=park" +
                "&connect_redirect=1" );
        CommonButton btn31 = new CommonButton ( );
        btn31.setName ( "调戏客服" );
        btn31.setType ( "click" );
        btn31.setKey ( "31" );
        CommonButton btn32 = new CommonButton ( );
        btn32.setName ( "常见疑难" );
        btn32.setType ( "view" );
        btn32.setKey ( "32" );
        btn32.setUrl ( WxConfig.guide );
        CommonButton btn33 = new CommonButton ( );
        btn33.setName (GlobalConfig.appName + "小故事" );
        btn33.setType ( "click" );
        btn33.setKey ( "33" );


        ComplexButton mainBtn3 = new ComplexButton ( );
        mainBtn3.setName ( "自助" );
        mainBtn3.setSub_button ( new CommonButton[] { btn31 , btn32 , btn33 } );

        Menu menu = new Menu ( );
        menu.setButton ( new Button[] { btn11 , btn21 , mainBtn3 } );

        return menu;


    }


}
