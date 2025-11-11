package com.xchat.oauth2.service.service;

import com.xchat.oauth2.service.core.exception.AuthenticationException;

import org.springframework.stereotype.Service;

/**
 * @author liuguofu
 *         on 4/10/15.
 */
@Service
public class AuthService {
//    @Autowired
//    private AccctService accctService;

    public long ticketAuth(String ticket){
        try {
            Long uid=0L;
//            Long uid = accctService.ticketVerify(AppIds.ACCCT_APPID, ticket);
            if(uid == null || uid <=0){
                throw new AuthenticationException("ticket invalid");
            }
            return uid;
        }catch (Exception e){
            throw new AuthenticationException("ticket invalid");
        }
    }
}
