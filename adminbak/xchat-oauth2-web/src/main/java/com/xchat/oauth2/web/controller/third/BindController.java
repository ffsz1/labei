package com.xchat.oauth2.web.controller.third;

import com.xchat.oauth2.service.core.exception.IllegalParameterException;
import com.xchat.oauth2.service.service.AuthService;
import com.xchat.oauth2.web.common.ServiceResult;
import com.xchat.oauth2.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * @author liuguofu
 *         on 7/29/15.
 */
@Controller
@RequestMapping("/account")
public class BindController extends BaseController {

    @Autowired
    private AuthService authService;

    @RequestMapping(value = "bind",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult<Map> bind(String token, String bind_account_access_token, String bind_account_openid, String bind_account_type, String bind_phone, String bind_password, String bind_code, String from){

        if(StringUtils.isBlank(token)){
            throw new IllegalParameterException("illegal parameter.");
        }

        long uid = authService.ticketAuth(token);

        Map<String,Object> bindInfo = null;

        if("QQ".equalsIgnoreCase(bind_account_type) || "WEIBO".equalsIgnoreCase(bind_account_type) || "WEIXIN".equalsIgnoreCase(bind_account_type)){
            if(StringUtils.isBlank(bind_account_access_token) || StringUtils.isBlank(bind_account_openid)){
                throw new IllegalParameterException("illegal parameter.");
            }
//            bindInfo = bindService.bindThridAccount(uid, bind_account_access_token, bind_account_openid,bind_account_type);
        }else if("ERD".equalsIgnoreCase(bind_account_type)){
            if(StringUtils.isBlank(bind_phone) || StringUtils.isBlank(bind_password) || StringUtils.isBlank(bind_code)){
                throw new IllegalParameterException("illegal parameter.");
            }
//            bindInfo = bindService.bindPhone(uid,bind_phone,bind_password,bind_code);
        }else{
            throw new IllegalParameterException("illegal parameter.");
        }

        return new ServiceResult<Map>().setData(bindInfo).setCode(200);
    }
    @RequestMapping(value = "unbind",method = RequestMethod.POST)
    @ResponseBody
    public ServiceResult<Map> unbind(String token,String unbind_account_type,String unbind_account_openid){
        if(StringUtils.isBlank(token) || StringUtils.isBlank(unbind_account_type) || StringUtils.isBlank(unbind_account_openid)){
            throw new IllegalParameterException("illegal parameter.");
        }

        long uid = authService.ticketAuth(token);
        Map<String,Object> bindInfo = null;

        if("QQ".equalsIgnoreCase(unbind_account_type) || "WEIBO".equalsIgnoreCase(unbind_account_type) || "WEIXIN".equalsIgnoreCase(unbind_account_type) || "ERD".equalsIgnoreCase(unbind_account_type)) {
//            bindInfo = bindService.unbind(uid,unbind_account_type,unbind_account_openid);
        }else{
            throw new IllegalParameterException("illegal parameter.");
        }
        return new ServiceResult<Map>().setData(bindInfo).setCode(200);
    }
    @RequestMapping("bindinfo/get")
    @ResponseBody
    public ServiceResult<Map> bindInfo(String token){
        long uid = authService.ticketAuth(token);
//        Map<String,Object> bindInfo = bindService.getBindInfo(uid);
//        return new ServiceResult<Map>().setData(bindInfo).setCode(200);
        return null;
    }
}
