package com.erban.web.wechat.service;

/**
 * Created by Lily on 2017/7/29.
 */

import com.erban.main.config.WxConfig;
import com.erban.main.model.WXMsgPicInfo;
import com.erban.main.model.WXPush;
import com.erban.main.service.WXPushService;
import com.erban.main.vo.OpenIdInfoVo;
import com.erban.main.wechat.AccessToken;
import com.erban.main.wechat.message.resp.ArticleModel;
import com.erban.main.wechat.message.resp.NewsMessage;
import com.erban.main.wechat.message.resp.TextMessage;
import com.erban.main.vo.UserInfo;
import com.erban.main.wechat.util.MessageUtil;
import com.erban.main.util.WeiXinUtil;
import com.google.gson.Gson;
import com.xchat.common.wx.HttpUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 微信相关操作
 */
@Service
public class WXPubService {

    private Logger LOGGER = Logger.getLogger ( WXPubService.class );


    @Autowired
    WXPushService pushService;

    /**
     * 处理微信用户发来的请求
     *
     * @param request
     * @return
     */
    public String processRequest ( HttpServletRequest request ) {

        String respMessage = null;
        try {
            //默认的文本回复
            String respContent = "请求处理异常，请稍后再试！";

            //解析发过来的xml请求
            Map <String, String> requestMap = MessageUtil.parseXml ( request );

            //发送发方账号（open_id）
            String fromUserName = requestMap.get ( "FromUserName" );

            //公众账号
            String toUserName = requestMap.get ( "ToUserName" );

            //消息类型
            String msgType = requestMap.get ( "MsgType" );

            //回复文本消息
            TextMessage textMessage = new TextMessage ( );
            textMessage.setToUserName ( fromUserName );
            textMessage.setFromUserName ( toUserName );
            textMessage.setCreateTime ( new Date ( ).getTime ( ) );
            textMessage.setMsgType ( MessageUtil.RESP_MESSAGE_TYPE_TEXT );
            textMessage.setFuncFlag ( 0 );

            //文本消息
            if ( msgType.equals ( MessageUtil.REQ_MESSAGE_TYPE_TEXT ) ) {
                respContent = "您发送的是文本消息！";
            }
            //图片消息
            else if ( msgType.equals ( MessageUtil.REQ_MESSAGE_TYPE_IMAGE ) ) {
                respContent = "您发送的是图片消息！";

            }
            //地理位置消息
            else if ( msgType.equals ( MessageUtil.REQ_MESSAGE_TYPE_LOCATION ) ) {
                respContent = "您发送的是地理位置消息！";
            }
            //链接消息
            else if ( msgType.equals ( MessageUtil.REQ_MESSAGE_TYPE_LINK ) ) {
                respContent = "您发送的是链接消息！";
            }//语音消息
            else if ( msgType.equals ( MessageUtil.REQ_MESSAGE_TYPE_VOICE ) ) {
                String voiceData = requestMap.get ( "Recognition" );
                //================语音数据处理====================//
                respContent = "您发送的是语音消息！";
            }
            //事件推送消息
            else if ( msgType.equals ( MessageUtil.REQ_MESSAGE_TYPE_EVENT ) ) {
                //事件类型
                String eventType = requestMap.get ( "Event" );
                //订阅
                if ( eventType.equals ( MessageUtil.EVENT_TYPE_SUBSCRIBE ) ) {
                    LOGGER.info ( "================用户订阅===============" );
                    //获取一条订阅推送
                    WXPush wxPush = pushService.getSubMsg ( );
                    List <WXMsgPicInfo> wxMsgPicInfoList =
                            pushService.getPicInfoMsgByIds ( wxPush.getMsgId ( ) );
                    int articleCount = Integer.parseInt ( wxPush.getArticleCount ( ) );
                    //发送订阅消息
                    NewsMessage newsMessage = new NewsMessage ( );
                    newsMessage.setToUserName ( fromUserName );
                    newsMessage.setFromUserName ( toUserName );
                    newsMessage.setCreateTime ( new Date ( ).getTime ( ) );
                    newsMessage.setMsgType ( MessageUtil.RESP_MESSAGE_TYPE_NEWS );
                    newsMessage.setFuncFlag ( 0 );
                    newsMessage.setArticleCount ( ( articleCount ) );
                    List <ArticleModel> list = new ArrayList <ArticleModel> ( );

                    for ( int i = 0 ; i < articleCount ; i++ ) {
                        ArticleModel articleModel = new ArticleModel ( );
                        articleModel.setDescription ( wxMsgPicInfoList.get ( i ).getDescription ( ) );
                        articleModel.setPicUrl ( wxMsgPicInfoList.get ( i ).getPicUrl ( ) );
                        articleModel.setUrl ( wxMsgPicInfoList.get ( i ).getUrl ( ) );
                        articleModel.setTitle ( wxMsgPicInfoList.get ( i ).getTitle ( ) );
                        list.add ( articleModel );              //按照对象引用添加
                    }
                    newsMessage.setArticles ( list );
                    return MessageUtil.newsMessageToXml ( newsMessage );
                }
                //取消订阅
                else if ( eventType.equals ( MessageUtil.EVENT_TYPE_UNSUBSCRIBE ) ) {
                    // TODO 取消订阅后用户再收不到公众号发送的消息，因此不需要回复消息
                }
                //菜单点击事件
                else if ( eventType.equals ( MessageUtil.EVENT_TYPE_CLICK ) ) {
                    //事件KEY值
                    String eventKey = requestMap.get ( "EventKey" );
                    String menuName = "";
                    //TODO 根据自定义菜单值与eventKey比较来进行下一步操作
                    //响应按键数据
                    switch ( Integer.valueOf ( eventKey ) ) {


                        case 31:
                            menuName = "三种方式，任您蹂躏。\n" +
                                    "qq群：581321437\n" +
                                    "微信：hlwcc123\n" +
                                    "电话：020-86175180（只接受工作时间内的来电哦。）";
                            break;
                        case 33:
                            menuName = "待开发";
                            break;
                        default:
                            break;
                    }
                    respContent = menuName;
                }
            }
            textMessage.setContent ( respContent );
            respMessage = MessageUtil.textMessageToXml ( textMessage );

        } catch ( Exception e ) {
            // TODO: handle exception
            e.printStackTrace ( );
        }
        return respMessage;

    }


    public String getOpenId ( String code ) {

                /*
                        获取openID(√)
                */
        LOGGER.info ( "==============服务器未获取给用户的openid==============" );
        HashMap <String, String> openIdMap = new HashMap <String, String> ( );
        openIdMap.put ( "appid" , WxConfig.appId );
        openIdMap.put ( "secret" , WxConfig.appSecret );
        openIdMap.put ( "code" , code );
        openIdMap.put ( "grant_type" , "authorization_code" );
        //==================解析openIdInfo json字符串=========================//
        String openIdInfo = HttpUtils.URLGet (
                "https://api.weixin.qq.com/sns/oauth2/access_token" , openIdMap , HttpUtils.URL_PARAM_DECODECHARSET_UTF8 );
        LOGGER.info ( "===========获取openIdInfo===========" + openIdInfo );
        Gson gson = new Gson ( );
        OpenIdInfoVo openIdInfoVo = gson.fromJson ( openIdInfo , OpenIdInfoVo.class );
        String openId = openIdInfoVo.getOpenid ( );
        if ( openId == null ) {
            LOGGER.error ( "==========请求openId失败=================" );
            return null;
        }
        return openId;

    }


    public void sendModelMsg ( Long erbanNo , String openId , String goldNum , String resultStr ) {

        LOGGER.info ( "------------发送订单通知消息请求-----------------" );
        // 第三方用户唯一凭证
        String appId = WxConfig.appId;
        // 第三方用户唯一凭证密钥
        String appSecret = WxConfig.appSecret;
        // 调用接口获取access_token
        AccessToken at = WeiXinUtil.getAccessToken ( appId , appSecret );
        if ( null != at ) {
            WeiXinUtil.sendModelMsg ( erbanNo , openId , goldNum , at.getToken ( ) , resultStr );
        }

    }

    /**
     *获取用户信息
     */
    public UserInfo getUserInfo ( String openId ) {
        //http请求方式: GET https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN
        String url = "https://api.weixin.qq.com/cgi-bin/user/info";
        //获取access_token
        AccessToken accessToken = WeiXinUtil.getAccessToken (
                WxConfig.appId , WxConfig.appSecret );
        LOGGER.info ( "==>获取微信用户信息URL：" + url );
        LOGGER.info ( "==>accessToken：" + accessToken.getToken ( ) );
        LOGGER.info ( "==>openid：" + openId );
        String result = HttpUtils.sendGet ( url,
                "access_token="+ accessToken.getToken ( )+
                    "&openid="+openId+
                        "&lang=zh_CN");
        LOGGER.info ( "==>获取微信用户返回结果：" + result );
        UserInfo userInfo = new Gson ().fromJson ( result,UserInfo.class );
        return userInfo;


    }

}
