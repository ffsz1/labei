package com.xchat.oauth2.service.test;


import com.xchat.oauth2.service.core.util.JsonMapper;
import com.xchat.oauth2.service.service.JedisService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.stereotype.Service;
@Service
public class TicketRepository {

    private static final Logger LOG = LoggerFactory.getLogger(TicketRepository.class);

    private String verifierKey = "dh293Hkdjf3G";
    private JsonMapper jsonMapper = JsonMapper.nonDefaultMapper();
    private static final String REDIS_PREFIX = "tv_";

    @Autowired(required = false)
    private JedisService jedisService;
    @Value("${ticket.valid.once:true}")
    private boolean validOnce;

    private SignatureVerifier verifier = new MacSigner(verifierKey);

    public long ticketVerify(String appid, String ticket){
        try {
            Jwt jwt = JwtHelper.decodeAndVerify(ticket, verifier);
            String content = jwt.getClaims();
            System.out.println("TicketRepository--content= "+content);
            Claim claim = jsonMapper.fromJson(content, Claim.class);
            System.out.println("TicketRepository--claim.getUid()= "+claim.getUid());
            //TODO 暂时不严重一次性,方便测试
            if (jedisService != null && validOnce) {
                String ticketContent = jedisService.read(REDIS_PREFIX+claim.getTicket_id());
                if (ticketContent != null) {
                    System.out.println("TicketRepository--return -1");
                    return -1;
                }
                jedisService.write(REDIS_PREFIX+claim.getTicket_id(), "1", claim.getExp());
            }

            if(!appid.equalsIgnoreCase(claim.getClient_id())){
                System.out.println("TicketRepository--return -3");
                return -3;
            }
            System.out.println("TicketRepository--return claim.getUid()="+claim.getUid());
            return claim.getUid();
        }catch (Exception e){
//            LOG.warn("ticket varify error. appid:{} ticket:{} msg:{}",appid,ticket,e);
            return -2;
        }
    }
    public static void main(String args[]){
        TicketRepository t=new TicketRepository();
        String token="eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0OTg5ODQyMjIsInVzZXJfbmFtZSI6IjE4OTMzOTg2OTcxIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF1dGhvcml0aWVzIjpbIlJPTEVfTU9CSUxFIiwiUk9MRV9VTklUWSIsIlJPTEVfYWNjb3VudCJdLCJhdWQiOlsiZXJiYW5hcHAtcmVzb3VyY2UiXSwianRpIjoiNTZmNDcyMDktZGRmYy00NWNjLTg5YmEtNGYwNWZiNjMwNTI5IiwiY2xpZW50X2lkIjoiZXJiYW4tY2xpZW50In0.KTnI3Dr4OE9vprCtEJWp8B7ZGnNbleQ1nJhC7-BoS6c";

        String ticket="eyJhbGciOiJIUzI1NiJ9.eyJ1aWQiOjkwMDA4OSwidGlja2V0X3R5cGUiOm51bGwsImV4cCI6MzU5OSwidGlja2V0X2lkIjoiNTBlYjJiNjktZjhjYi00NGU3LTgzYzMtY2NkY2I3M2I2YmQ3IiwiY2xpZW50X2lkIjoiZXJiYW4tY2xpZW50In0.nvbuQv7JbQZFTO_6uVbKIDlqVSaQIGW1UFR96PH7fuA";
        t.ticketVerify("erbanapp-client",token);
        t.ticketVerify("erbanapp-client",ticket);

        String token2 ="eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0OTg5NzkxNzksInVzZXJfbmFtZSI6IjE4OTMzOTg2OTcxIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF1dGhvcml0aWVzIjpbIlJPTEVfTU9CSUxFIiwiUk9MRV9VTklUWSIsIlJPTEVfYWNjb3VudCJdLCJhdWQiOlsiZXJiYW5hcHAtcmVzb3VyY2UiXSwianRpIjoiYWU5MWI0NWMtMWI4Ni00OWI2LTg3MmUtNmFmMWU3YmYwZWI2IiwiY2xpZW50X2lkIjoiZXJiYW4tY2xpZW50In0.jAxQI0d5agcg4MAlhurHLirVe42O-a3KDn7j2coyeCw";
        t.ticketVerify("erbanapp-client",token2);

        String token3="eyJhbGciOiJIUzI1NiJ9.eyJleHAiOjE0OTg5NzkxNzksInVzZXJfbmFtZSI6IjE4OTMzOTg2OTcxIiwic2NvcGUiOlsicmVhZCIsIndyaXRlIl0sImF1dGhvcml0aWVzIjpbIlJPTEVfTU9CSUxFIiwiUk9MRV9VTklUWSIsIlJPTEVfYWNjb3VudCJdLCJhdWQiOlsiZXJiYW5hcHAtcmVzb3VyY2UiXSwianRpIjoiYWU5MWI0NWMtMWI4Ni00OWI2LTg3MmUtNmFmMWU3YmYwZWI2IiwiY2xpZW50X2lkIjoiZXJiYW4tY2xpZW50In0.jAxQI0d5agcg4MAlhurHLirVe42O-a3KDn7j2coyeCw";
        t.ticketVerify("erbanapp-client",token3);
    }

    public static class Claim{
        //   {"uid":21,"ticket_type":null,"exp":119,"ticket_id":"66ba0b25-c985-43e5-8437-cbb529247150","client_id":"unity-client"}
        private Long uid;
        private String ticket_type;
        private Integer exp;
        private String ticket_id;
        private String client_id;

        public Long getUid() {
            return uid;
        }

        public void setUid(Long uid) {
            this.uid = uid;
        }

        public String getTicket_type() {
            return ticket_type;
        }

        public void setTicket_type(String ticket_type) {
            this.ticket_type = ticket_type;
        }

        public Integer getExp() {
            return exp;
        }

        public void setExp(Integer exp) {
            this.exp = exp;
        }

        public String getTicket_id() {
            return ticket_id;
        }

        public void setTicket_id(String ticket_id) {
            this.ticket_id = ticket_id;
        }

        public String getClient_id() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id = client_id;
        }
    }
}
