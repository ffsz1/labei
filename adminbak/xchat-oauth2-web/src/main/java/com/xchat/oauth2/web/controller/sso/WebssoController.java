package com.xchat.oauth2.web.controller.sso;

import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.model.Account;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.AccountService;
import com.xchat.oauth2.service.sso.WebssoService;
import com.xchat.oauth2.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuguofu
 *         on 6/25/15.
 */
@Controller
@RequestMapping("/sso")
public class WebssoController extends BaseController {



}
