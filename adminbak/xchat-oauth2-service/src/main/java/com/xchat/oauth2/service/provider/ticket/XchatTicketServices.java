package com.xchat.oauth2.service.provider.ticket;

import com.beust.jcommander.internal.Maps;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.common.exceptions.HaveMultiAccountException;
import com.xchat.oauth2.service.common.exceptions.InvalidTokenException;
import com.xchat.oauth2.service.core.util.StringUtils;
import com.xchat.oauth2.service.domain.shared.security.AccountDetails;
import com.xchat.oauth2.service.provider.OAuth2Authentication;
import com.xchat.oauth2.service.provider.token.TokenStore;
import com.xchat.oauth2.service.service.UserService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author liuguofu
 */
public class XchatTicketServices implements InitializingBean {
    private int ticketValiditySeconds = 60 * 60; //ticket过期时间

    private TicketStore ticketStore;

    private TokenStore tokenStore;

    private UserService userService;

    private TicketEnhancer ticketEnhancer;

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(tokenStore, "tokenStore must be set");
        Assert.notNull(ticketStore, "ticketStore must be set");
        Assert.notNull(userService, "userService must be set");
    }

    private int calTicketValiditySeconds(OAuth2AccessToken accessToken) {
        int accessTokenExpiresIn = accessToken.getExpiresIn();
        return accessTokenExpiresIn > ticketValiditySeconds ? ticketValiditySeconds : accessTokenExpiresIn;
    }

    public Map<String, Object> issueTicket(int number, String accessTokenValue) throws InvalidTokenException, HaveMultiAccountException, UsernameNotFoundException {
        OAuth2AccessToken accessToken = tokenStore.readAccessToken(accessTokenValue);
        Map<String, Object> maps = Maps.newHashMap();
        if (accessToken == null) {
            throw new InvalidTokenException("Invalid access token: " + accessTokenValue);
        } else if (accessToken.isExpired()) {
            // tokenStore.removeAccessToken(accessToken);
            // // 同时移除accessToken分配的tickets
            // ticketStore.removeTicketUsingAccessToken(accessToken);
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        }

        List<Ticket> tickets = new ArrayList<Ticket>();
        OAuth2Authentication authentication = tokenStore.readAuthentication(accessToken);
        int ticketValidityMillSeconds = calTicketValiditySeconds(accessToken) * 1000;

        UserDetails userDetails = userService.loadUserByUsername(authentication.getName());
        AccountDetails accountDetails = userDetails instanceof AccountDetails ? (AccountDetails) userDetails : null;

        if (ticketValidityMillSeconds < 2000) {
            // accessToken剩余2秒超时, 直接抛出过期异常
            throw new InvalidTokenException("Access token expired: " + accessTokenValue);
        }
        Long uid = accountDetails.getAccount().getUid();
        String accessUidToken = ticketStore.getAccessTokenCache(uid);
        if (StringUtils.isEmpty(accessUidToken)) {
            throw new InvalidTokenException("accessTokenCache  null: " + accessTokenValue);
        }

        if (!accessTokenValue.equalsIgnoreCase(accessUidToken)) {
            // throw new InvalidTokenException("acessToken error...please relogin now" + accessTokenValue);
            throw new InvalidTokenException("该账号已在别的设备登录!");
        }

        DefaultTicket ticket = new DefaultTicket(UUID.randomUUID().toString());
        ticket.setAccessToken(accessToken);
        ticket.setExpiresIn(ticketValidityMillSeconds);
        tickets.add(ticketEnhancer != null ? ticketEnhancer.enhance(ticket, authentication, accountDetails) : ticket);
        ticketStore.storeTicket(tickets.get(0), authentication, accountDetails);
        maps.put("tickets", tickets);
        maps.put("uid", uid);
        return maps;
    }

    public void setTicketStore(TicketStore ticketStore) {
        this.ticketStore = ticketStore;
    }

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setTicketEnhancer(TicketEnhancer ticketEnhancer) {
        this.ticketEnhancer = ticketEnhancer;
    }

    public void setTicketValiditySeconds(int ticketValiditySeconds) {
        this.ticketValiditySeconds = ticketValiditySeconds;
    }
}
