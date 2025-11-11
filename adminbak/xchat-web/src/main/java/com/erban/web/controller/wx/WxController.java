//package com.erban.web.controller.wx;
//
//import com.alibaba.fastjson.JSONObject;
//import com.erban.main.service.user.UsersService;
//import com.erban.main.service.wx.WxService;
//import com.erban.web.common.BaseController;
//import com.xchat.common.result.BusiResult;
//import com.xchat.common.status.BusiStatus;
//import com.xchat.oauth2.service.http.HttpUitls;
//import com.xchat.oauth2.service.service.wx.WeixinService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
//
//import javax.servlet.http.HttpServletRequest;
//
//@Controller
//@RequestMapping("/wx")
//public class WxController extends BaseController {
//    @Autowired
//    private UsersService usersService;
//    @Autowired
//    private WxService wxService;
//    @Autowired
//    private WeixinService weixinService;
//
//    /**
//     * 微信公众号登录
//     *
//     * @param code
//     * @param state
//     * @return
//     */
//    @RequestMapping("/pub/login")
//    public ModelAndView login(HttpServletRequest request,
//                              @RequestParam(value = "code", required = false) String code,
//                              @RequestParam(value = "state", required = false) String state) {
//        BusiResult<JSONObject> result = weixinService.wxpubLogin(code, state);
//        if (result.getCode() != 200) {
//            return new ModelAndView(new MappingJackson2JsonView()).addObject(result);
//        }
//
//        String ip = HttpUitls.getRealIpAddress(request);
//        JSONObject userinfo = result.getData();
//        try {
//            usersService.updateWxpubUser(ip, userinfo, null, null);
//        } catch (Exception e) {
//            logger.error("[ 微信公众号登录 ]更新用户信息异常：", e);
//            return new ModelAndView(new MappingJackson2JsonView()).addObject("code", "500").addObject("message", "请刷新页面重试");
//        }
//        return new ModelAndView("redirect:" + state + (state.endsWith(".html") ? "?" : "&") + "openId=" + userinfo.getString("openid") + "&unionid=" + userinfo.getString("unionid"));
//    }
//
//    /**
//     * 微信公众号登录
//     *
//     * @param code
//     * @param state
//     * @return
//     */
//    @RequestMapping("/pub/login/{roomUid}/{shareUid}")
//    public ModelAndView loginSharePacket(HttpServletRequest request,
//                                         @RequestParam(value = "code", required = false) String code,
//                                         @RequestParam(value = "state", required = false) String state,
//                                         @PathVariable("roomUid") String roomUid,
//                                         @PathVariable("shareUid") String shareUid) {
//        BusiResult<JSONObject> result = weixinService.wxpubLogin(code, state);
//        if (result.getCode() != 200) {
//            return new ModelAndView(new MappingJackson2JsonView()).addObject(result);
//        }
//
//        String ip = HttpUitls.getRealIpAddress(request);
//        JSONObject userinfo = result.getData();
//        try {
//            usersService.updateWxpubUser(ip, userinfo, roomUid, shareUid);
//        } catch (Exception e) {
//            logger.error("[ 微信公众号登录 ]更新用户信息异常：", e);
//            return new ModelAndView(new MappingJackson2JsonView()).addObject("code", "500").addObject("message", "请刷新页面重试");
//        }
//
//        return new ModelAndView("redirect:" + state + (state.endsWith(".html") ? "?" : "&") + "openId=" + userinfo.getString("openid") + "&unionid=" + userinfo.getString("unionid"));
//    }
//
//    @RequestMapping(value = "/smallProgram/getToken", method = RequestMethod.GET)
//    @ResponseBody
//    public BusiResult getToken(Integer appid, String appKey, String idname, Long expiredAdd) {
//        try {
//            return wxService.getToken(appid, appKey, idname, expiredAdd);
//        } catch (Exception e) {
//            logger.error("getToken Exception:" + e.getMessage());
//            return new BusiResult(BusiStatus.SERVERERROR);
//        }
//    }
//
//    @RequestMapping(value = "/smallProgram/getUser", method = RequestMethod.GET)
//    @ResponseBody
//    public BusiResult getUser(String rawData, String code) {
//        try {
//            if (rawData == null || code == null) {
//                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
//            }
//            return wxService.getUser(rawData, code);
//        } catch (Exception e) {
//            logger.error("getUser Exception:" + e.getMessage());
//            return new BusiResult(BusiStatus.SERVERERROR);
//        }
//    }
//
//    @RequestMapping(value = "/smallProgram/getUserByOpenId", method = RequestMethod.GET)
//    @ResponseBody
//    public BusiResult getUserByOpenId(String openId) {
//        try {
//            if (openId == null) {
//                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
//            }
//            return wxService.getUserByOpenId(openId);
//        } catch (Exception e) {
//            logger.error("getUserByOpenId Exception:" + e.getMessage());
//            return new BusiResult(BusiStatus.SERVERERROR);
//        }
//    }
////
////    @RequestMapping(value = "/smallProgram/openRoom", method = RequestMethod.GET)
////    @ResponseBody
////    public BusiResult openRoom(String openId){
////        try{
////            if (openId == null) {
////                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
////            }
////            return wxService.openRoom(openId);
////        }catch (Exception e){
////            logger.error("openRoom Exception:" + e.getMessage());
////            return new BusiResult(BusiStatus.SERVERERROR);
////        }
////    }
//
//    @RequestMapping(value = "/smallProgram/saveRecord", method = RequestMethod.GET)
//    @ResponseBody
//    public BusiResult saveRecord(String anchorId, String openId) {
//        try {
//            if (anchorId == null || openId == null) {
//                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
//            }
//            return wxService.saveRecord(anchorId, openId);
//        } catch (Exception e) {
//            logger.error("saveRecord Exception:" + e.getMessage());
//            return new BusiResult(BusiStatus.SERVERERROR);
//        }
//    }
//
//    @RequestMapping(value = "/smallProgram/getRecord", method = RequestMethod.GET)
//    @ResponseBody
//    public BusiResult getRecord(String openId) {
//        try {
//            if (openId == null) {
//                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
//            }
//            return wxService.getRecord(openId);
//        } catch (Exception e) {
//            logger.error("getRecord Exception:" + e.getMessage());
//            return new BusiResult(BusiStatus.SERVERERROR);
//        }
//    }
//
//}
