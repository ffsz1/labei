package com.xchat.oauth2.web.controller.ticket;

import com.google.common.collect.Maps;
import com.xchat.common.utils.Utils;
import com.xchat.oauth2.service.common.exceptions.UnsupportedIssueTypeException;
import com.xchat.oauth2.service.common.exceptions.myexception.InvalidVersionException;
import com.xchat.oauth2.service.core.util.HttpServletUtils;
import com.xchat.oauth2.service.provider.ticket.Ticket;
import com.xchat.oauth2.service.provider.ticket.XchatTicketServices;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.account.AccountService;
import com.xchat.oauth2.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.xchat.oauth2.service.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author liuguofu
 * on 3/17/15.
 */
@Controller
@RequestMapping("/oauth")
public class TicketController extends BaseController {
    private int defalueIssueTicketNumber = 1;

    protected final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private XchatTicketServices xchatTicketServices;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private AccountService accountService;

    @RequestMapping("ticket")
    @ResponseBody
    public Object issueTicket(String issue_type, String access_token, String appid, String appVersion,
                              HttpServletResponse response, HttpServletRequest request) throws Exception {
        // if (Utils.version2long(appVersion) < Utils.version2long("1.1.0")) {// 1.1.0 版本解决充值后台发送IM消息
        //     try {
        //         throw new UnsupportedIssueTypeException("请联系客服更新版本");
        //     } catch (UnsupportedIssueTypeException e1) {
        //         response.setStatus(40400);
        //         return e1;
        //     }
        // }

        logger.info("oauth/ticket=" + access_token + ".........");
        try {
            Map<String, Object> ret = Maps.newHashMap();
            TicketRes ticketRes = new TicketRes();
            // TODO 上线前去掉开关判断
            if (jedisService.get("erban_check_version") != null && (StringUtils.isBlank(appVersion))) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                PrintWriter printWriter = response.getWriter();
                printWriter.write("{\"code\": 401,\"message\": \"need login!\"}");
                printWriter.flush();
                printWriter.close();
                return false;
                // throw new InvalidVersionException("版本过低，请升级版本");
            }

            logger.info("issueTicket-> access_token:{" + access_token + "},issue_type:{" + issue_type + "},appid:{" + appid + "},appVersion:{" + appVersion + "}");
            switch (issue_type) {
                case Ticket.ONCE_TYPE: {
                    Map<String, Object> ticketsMap = xchatTicketServices.issueTicket(1, access_token);
                    ticketRes.setIssue_type(Ticket.ONCE_TYPE);
                    ticketRes.setTickets((List<Ticket>) ticketsMap.get("tickets"));
                    ticketRes.setAccid((String) ticketsMap.get("accid"));
                    ticketRes.setNetEaseToken((String) ticketsMap.get("netEaseToken"));
                    ticketRes.setUid((Long) ticketsMap.get("uid"));
                    ret.put("code", 200);
                    ret.put("data", ticketRes);
                    accountService.updateLastLoginTime((Long) ticketsMap.get("uid"),HttpServletUtils.getRemoteIp(request));
                    return ret;
                }
                case Ticket.MULTI_TYPE: {
                    Map<String, Object> ticketsMap = xchatTicketServices.issueTicket(defalueIssueTicketNumber,
                            access_token);
                    ticketRes.setIssue_type(Ticket.MULTI_TYPE);
                    ticketRes.setTickets((List<Ticket>) ticketsMap.get("tickets"));
                    ticketRes.setAccid((String) ticketsMap.get("accid"));
                    ticketRes.setNetEaseToken((String) ticketsMap.get("netEaseToken"));
                    ticketRes.setUid((Long) ticketsMap.get("uid"));
                    ret.put("code", 200);
                    ret.put("data", ticketRes);
                    accountService.updateLastLoginTime((Long) ticketsMap.get("uid"),HttpServletUtils.getRemoteIp(request));
                    return ret;
                }
                default: {
                    throw new UnsupportedIssueTypeException("unsupported ticket issue type");
                }
            }
        } catch (InvalidTokenException e) {
            response.setStatus(401);
            return e;
        } catch (UnsupportedIssueTypeException e1) {
            response.setStatus(400);
            return e1;
        }
    }

    private class TicketRes {
        private String issue_type;
        private List<TicketVo> tickets;
        private String accid;
        private Long uid;
        private String netEaseToken;

        public String getIssue_type() {
            return issue_type;
        }

        public String getAccid() {
            return accid;
        }

        public Long getUid() {
            return uid;
        }

        public String getNetEaseToken() {
            return netEaseToken;
        }

        public void setIssue_type(String issue_type) {
            this.issue_type = issue_type;
        }

        public List<TicketVo> getTickets() {
            return tickets;
        }

        public void setAccid(String accid) {
            this.accid = accid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public void setNetEaseToken(String netEaseToken) {
            this.netEaseToken = netEaseToken;
        }

        public void setTickets(List<Ticket> tickets) {
            if (tickets == null) {
                return;
            }
            this.tickets = new ArrayList<TicketVo>();
            for (Ticket ticket : tickets) {
                this.tickets.add(new TicketVo(ticket));
            }
        }
    }

    private class TicketVo {
        private String ticket;
        private int expires_in;
        private String ticket_type;
        private String scope;

        public TicketVo(Ticket ticket) {
            setTicket(ticket.getValue());
            setExpires_in(ticket.getExpiresIn());
            setTicket_type(ticket.getTicketType());
            setScope(setToString(ticket.getScope()));
        }

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }

        public String getTicket_type() {
            return ticket_type;
        }

        public void setTicket_type(String ticket_type) {
            this.ticket_type = ticket_type;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        private String setToString(Set<String> stringSet) {
            if (stringSet == null) {
                return null;
            }
            return StringUtils.join(stringSet, " ");
        }
    }
}
