package com.erban.web.controller;


import com.erban.main.config.WxConfig;
import com.erban.main.vo.UserInfo;
import com.erban.web.wechat.MenuManager;
import com.erban.web.wechat.service.WXPubService;
import com.erban.main.wechat.util.SignUtil;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.PropertyUtil;
import com.xchat.common.utils.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by 北岭山下 on 2017/7/28.
 */

/**
 * 微信公众号请求处理
 */
@Controller
public class WXCoreController {

    /**
     *
     */
    private Logger LOGGER = Logger.getLogger ( WXCoreController.class );

    @Autowired
    private WXPubService wxPubService;


    @RequestMapping ( value = "/wxController", method = RequestMethod.GET )
    public void coreHandlerGET ( HttpServletRequest httpServletRequest ,
                                 HttpServletResponse httpServletResponse ) throws IOException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        LOGGER.info ( "-------开始验证请求是否来自微信-----------！" );
        //微信加密签名
        String signature = httpServletRequest.getParameter ( "signature" );
        //时间戳
        String timestamp = httpServletRequest.getParameter ( "timestamp" );
        //随机数
        String nonce = httpServletRequest.getParameter ( "nonce" );
        //随机字符串
        String echostr = httpServletRequest.getParameter ( "echostr" );
        LOGGER.info ( "---接收到的来自" + httpServletRequest.getRemoteHost ( ) + ",请求参数：signature:" + signature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",echostr:" + echostr );
        PrintWriter out = httpServletResponse.getWriter ( );
        //通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if ( signature == null || timestamp == null || nonce == null || echostr == null ) {
            out.write ( "you records has recorded,please leave it now !" );
        } else {
            if ( SignUtil.checkSignature ( signature , timestamp , nonce ) ) {
                out.write ( echostr );
            }
        }

        out.close ( );

    }

    @RequestMapping ( value = "/wxController", method = RequestMethod.POST )
    public void coreHandlerPOST ( HttpServletRequest httpServletRequest ,
                                  HttpServletResponse response ) throws IOException {
        // 将请求、响应的编码均设置为UTF-8（防止中文乱码）
        response.setContentType ( "text/html;charset=utf-8" );
        httpServletRequest.setCharacterEncoding ( "UTF-8" );
        //调用核心业务类处理微信请求
        String respMsg = wxPubService.processRequest ( httpServletRequest );
//                LOGGER.info ( "微信公众号处理后的信息："+ respMsg);
        PrintWriter writer = response.getWriter ( );
        writer.write ( respMsg );
        writer.close ( );
    }




    /**
     * 微信公众号创建接口
     * @param request
     * @param response
     * @return
     */
    @RequestMapping ( value = "/createMenu", method = RequestMethod.GET )
    @ResponseBody
    public BusiResult createMenu ( HttpServletRequest request ,
                                   HttpServletResponse response ) {
        BusiResult busiResult = new BusiResult ( BusiStatus.SUCCESS );
        LOGGER.info ( "----------更新微信公众号菜单----------" );
        try{
            String resutl = MenuManager.createMenu ( );
            busiResult.setData ( resutl );
        }catch(Exception e){
            LOGGER.error ( "##########更新微信公众号菜单失败##########" );
            e.printStackTrace();
            return new BusiResult ( BusiStatus.BUSIERROR );
        }
        return busiResult;
    }
    /**
     * 获取用户信息回调接口
     *
     *  https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx009d793f92c24eec&redirect_uri=http%3a%2f%2fwww.erbanyy.com%2fgetUserInfoCallback&response_type=code&scope=snsapi_base#wechat_redirect
     */

    @RequestMapping(value = "/getUserInfoCallback")
    public void getUserInfoCallback(HttpServletRequest request , HttpServletResponse response){

        String code = request.getParameter ( "code" );
        if ( StringUtils.isEmpty ( code )  ){
            LOGGER.error ( "#########code 传入参数错误#########" ) ;
            return;
        }
        LOGGER.info ( "=========获取code：" + code + "  =============" );
        try{
            //获取openID
            String openId = wxPubService.getOpenId ( code );//o5fpU1bSN0b8xcU3syC8Md8ZfrJ4
//            String openId = "o5fpU1bSN0b8xcU3syC8Md8ZfrJ4";
            if(openId != null){
                LOGGER.info ( "=========获取openId：" + openId + "=============" );
                UserInfo userInfo = wxPubService.getUserInfo(openId);
                LOGGER.info ( "==>userInfo"+userInfo.toString () );
                response.sendRedirect ( WxConfig.giveGold + "?"+userInfo.toString () );
            }else{
                LOGGER.error ( "#########获取openId失败#########" ) ;
            }
        }catch(Exception e){
            LOGGER.error ( "#########获取用户信息失败#########" ) ;
            e.printStackTrace();
        }


    }


}
