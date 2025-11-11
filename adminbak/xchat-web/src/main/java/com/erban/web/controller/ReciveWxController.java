
package com.erban.web.controller;

import com.erban.main.service.wx.WxService;
import com.erban.main.vo.WxConfigVo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Created by liuguofu on 2017/7/8.
 */
@Controller
@RequestMapping("/wx")
public class ReciveWxController {
    public static final Logger logger = LoggerFactory.getLogger(ReciveWxController.class);
    @Autowired
    private WxService wxService;

    @RequestMapping(value = "check",method = RequestMethod.GET)
    public void getWxConfig(HttpServletRequest request, HttpServletResponse response) throws Exception{
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        logger.info("signature="+signature+"&timestamp="+timestamp+"&nonce="+nonce+"&echostr="+echostr);
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
//        if (WxUtil.checkSignature(signature, timestamp, nonce)) {
//            out.print(echostr);
//            System.out.println("微信服务验证成功！");
//        }
        out.close();
        out = null;
    }
    @RequestMapping(value = "config", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult getWxConfig(String url) {
        BusiResult<WxConfigVo> busiResult=null;
        try {
            busiResult=wxService.getWxConfig(url);
        } catch (Exception e) {
            logger.error("getWxConfig error..."+e.getMessage());
            busiResult.setCode(BusiStatus.BUSIERROR.value());
            busiResult.setMessage("服务器好繁忙，请稍后重试！");
            return busiResult;
        }
        return busiResult;
    }


}
