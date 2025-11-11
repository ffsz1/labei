package com.xchat.oauth2.web.controller.third;

import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.web.controller.BaseController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author liuguofu
 *         on 6/19/15.
 */
@Controller
@RequestMapping("/exchange")
public class ExchangeAccessTokenController extends BaseController {

//    @Autowired
//    private ExchangeAccessTokenService exchangeAccessTokenService;

    @RequestMapping("token")
    @ResponseBody
    public OAuth2AccessToken exchange(String third_access_token,String third_openid,String token_from){
        return null;
//      return exchangeAccessTokenService.exchange(token_from,third_access_token,third_openid);
    }

}
