package com.xchat.oauth2.service.provider.ticket.store;

import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.common.OAuth2AccessToken;
import com.xchat.oauth2.service.common.OAuth2RefreshToken;
import com.xchat.oauth2.service.common.util.SerializationUtils;
import com.xchat.oauth2.service.core.util.StringUtils;
import com.xchat.oauth2.service.domain.shared.security.AccountDetails;
import com.xchat.oauth2.service.provider.OAuth2Authentication;
import com.xchat.oauth2.service.provider.ticket.Ticket;
import com.xchat.oauth2.service.provider.ticket.TicketStore;
import com.xchat.oauth2.service.service.JedisService;
import com.xchat.oauth2.service.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author liuguofu
 *         on 3/16/15.
 */
public class JdbcRedisTicketStore implements TicketStore {

    private static final Log LOG = LogFactory.getLog(JdbcRedisTicketStore.class);

    private static final String DEFAULT_TICKET_INSERT_STATEMENT = "insert into oauth_ticket (ticket_id, ticket, user_name, user_id, access_token, refresh_token, expiration) values (?, ?, ?, ?, ?, ?, ?)";

    private static final String DEFAULT_TICKET_STATEMENT = "select ticket_id, ticket from oauth_ticket where ticket_id = ?";

    private static final String DEFAULT_TICKET_DELETE_FROM_ACCESS_TOKEN_STATEMENT = "delete from oauth_ticket where access_token = ?";

    private static final String DEFAULT_TICKET_DELETE_FROM_REFRESH_TOKEN_STATEMENT = "delete from oauth_ticket where refresh_token = ?";

    private static final String DEFAULT_TICKET_DELETE_STATEMENT = "delete from oauth_ticket where ticket_id = ?";

    private static final String DEFAULT_TICKETS_FROM_ACCESS_TOKEN_SELECT_STATEMENT = "select ticket_id, ticket from oauth_ticket where access_token = ?";

    private static final String DEFAULT_TICKETS_FROM_REFRESH_TOKEN_SELECT_STATEMENT = "select ticket_id, ticket from oauth_ticket where refresh_token = ?";

    private String insertTicketSql = DEFAULT_TICKET_INSERT_STATEMENT;

    private String selectTicketSql = DEFAULT_TICKET_STATEMENT;

    private String deleteTicketFromAccessTokenSql = DEFAULT_TICKET_DELETE_FROM_ACCESS_TOKEN_STATEMENT;

    private String deleteTicketFromRefreshTokenSql = DEFAULT_TICKET_DELETE_FROM_REFRESH_TOKEN_STATEMENT;

    private String deleteTicketSql = DEFAULT_TICKET_DELETE_STATEMENT;

    private String selectTicketsFromAccessTokenSql = DEFAULT_TICKETS_FROM_ACCESS_TOKEN_SELECT_STATEMENT;

    private String selectTicketsFromRefreshTokenSql = DEFAULT_TICKETS_FROM_REFRESH_TOKEN_SELECT_STATEMENT;

    private JedisService jedisService;

    private final JdbcTemplate jdbcTemplate;

    private final UserService userService;

    Gson gson=new Gson();
    public JdbcRedisTicketStore(DataSource dataSource,JedisService jedisService, UserService userService){
        Assert.notNull(dataSource,"dataSource required");
        Assert.notNull(jedisService,"JedisService required");
        Assert.notNull(userService,"Userservice required");
        this.jedisService = jedisService;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.userService = userService;
    }

    private void storeTicketInRedis(Ticket ticket, Long uid){
        jedisService.write(ticket.getValue(),uid.toString(),ticket.getExpiresIn());

    }

    private void saveUidTicketRedisCache(Long uid,String ticketStr){
        jedisService.hwrite(RedisKey.uid_ticket.getKey(),uid.toString(),ticketStr);
    }

    public Ticket getTicketCacheByUid(Long uid){
        String ticketStr=jedisService.hget(RedisKey.uid_ticket.getKey(),uid.toString());
        if(StringUtils.isEmpty(ticketStr)){
            return null;
        }
        Ticket ticket=gson.fromJson(ticketStr,Ticket.class);
        return ticket;
    }
    public String getAccessTokenCache(Long uid){
        String oauthToken=jedisService.hget(RedisKey.uid_access_token.getKey(),uid.toString());
        if(StringUtils.isEmpty(oauthToken)){
            return null;
        }
        return oauthToken;

    }



    private void removeTicketInRedis(Ticket ticket){
        removeTicketInRedis(ticket.getValue());
    }

    private void removeTicketInRedis(String ticketValue){
        jedisService.disableCache(ticketValue);
    }

    private void removeTicketInRedisUsingAccessToken(String accessToken){
        Collection<Ticket> tickets = findTicketsByAccessToken(accessToken);
        if(tickets != null && tickets.size() > 0){
            for(Ticket ticket : tickets){
                removeTicketInRedis(ticket);
            }
        }
    }

    private void removeTicketInRedisUsingRefreshToken(String refreshToken){
        Collection<Ticket> tickets = findTicketsByRefreshToken(refreshToken);
        if(tickets != null && tickets.size() > 0){
            for(Ticket ticket : tickets){
                removeTicketInRedis(ticket);
            }
        }
    }

    @Override
    public void storeTicket(Ticket ticket,OAuth2Authentication authentication,AccountDetails userDetails){
        String accessToken = null;
        String refreshToken = null;
        String userName = null;
        Long uid = null;
        Date expiration = ticket.getExpiration();
        if(ticket.getAccessToken() != null){
            accessToken = ticket.getAccessToken().getValue();
            if(ticket.getAccessToken().getRefreshToken() != null) {
                refreshToken = ticket.getAccessToken().getRefreshToken().getValue();
            }
        }
        if(authentication != null) {
            userName = authentication.getName();
        }
        if(userDetails != null && userDetails.getAccount() != null){
            uid = userDetails.getAccount().getUid();
        }

//        jdbcTemplate.update(insertTicketSql,
//                new Object[] {ticket.getValue(),new SqlLobValue(serializeTicket(ticket)), userName, uid, extractTokenKey(accessToken),extractTokenKey(refreshToken),expiration},
//                new int[]{Types.VARCHAR, Types.BLOB, Types.VARCHAR,Types.BIGINT, Types.VARCHAR,Types.VARCHAR,Types.TIMESTAMP});

//        storeTicketInRedis(ticket,uid);
        saveUidTicketRedisCache(uid,ticket.getValue());
    }




    @Override
    public Ticket readTicket(String ticketValue) {
        Ticket ticket = null;
        try{
            ticket = jdbcTemplate.queryForObject(selectTicketSql, new RowMapper<Ticket>() {
                @Override
                public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return deserializeTicket(rs.getBytes(2));
                }
            },ticket);
        }catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find ticket for ticketValue " + ticketValue);
            }
        }
        catch (IllegalArgumentException e) {
            LOG.warn("Failed to deserialize ticket for " + ticketValue);
            removeTicket(ticketValue);
        }
        return ticket;
    }

    @Override
    public void removeTicket(Ticket ticket) {
        removeTicket(ticket.getValue());
    }

    public void removeTicket(String ticketValue){
        jdbcTemplate.update(deleteTicketSql,ticketValue);
        removeTicketInRedis(ticketValue);
    }

    @Override
    public void removeTicketUsingAccessToken(OAuth2AccessToken accessToken) {
        removeTicketUsingAccessToken(accessToken.getValue());
    }

    public void removeTicketUsingAccessToken(String accessToken){
        removeTicketInRedisUsingAccessToken(accessToken);
        jdbcTemplate.update(deleteTicketFromAccessTokenSql,new Object[] {extractTokenKey(accessToken)},new int[]{Types.VARCHAR});
    }

    @Override
    public void removeTicketUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        removeTicketUsingRefreshToken(refreshToken.getValue());
    }

    public void removeTicketUsingRefreshToken(String refreshToken){
        removeTicketInRedisUsingRefreshToken(refreshToken);
        jdbcTemplate.update(deleteTicketFromRefreshTokenSql,new Object[]{extractTokenKey(refreshToken)},new int[]{Types.VARCHAR});
    }

    @Override
    public Collection<Ticket> findTicketsByAccessToken(OAuth2AccessToken accessToken) {
        return findTicketsByAccessToken(accessToken.getValue());
    }

    public Collection<Ticket> findTicketsByAccessToken(String accessToken){
        List<Ticket> tickets = new ArrayList<Ticket>();

        try{
            tickets = jdbcTemplate.query(selectTicketsFromAccessTokenSql,new SafeTicketRowMapper(),extractTokenKey(accessToken));
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find ticket for access token " + accessToken);
            }
        }
        tickets = removeNulls(tickets);

        return tickets;
    }



    @Override
    public Collection<Ticket> findTicketsByRefreshToken(OAuth2RefreshToken refreshToken) {
        return findTicketsByRefreshToken(refreshToken.getValue());
    }

    public Collection<Ticket> findTicketsByRefreshToken(String refreshToken){
        List<Ticket> tickets = new ArrayList<Ticket>();

        try{
            tickets = jdbcTemplate.query(selectTicketsFromRefreshTokenSql,new SafeTicketRowMapper(),extractTokenKey(refreshToken));
        }
        catch (EmptyResultDataAccessException e) {
            if (LOG.isInfoEnabled()) {
                LOG.info("Failed to find ticket for refresh token " + refreshToken);
            }
        }
        tickets = removeNulls(tickets);

        return tickets;
    }

    private List<Ticket> removeNulls(List<Ticket> tickets) {
        List<Ticket> ticketList = new ArrayList<Ticket>();
        for (Ticket ticket : tickets) {
            if (ticket != null) {
                ticketList.add(ticket);
            }
        }
        return ticketList;
    }

    protected byte[] serializeTicket(Ticket ticket) {
        return SerializationUtils.serialize(ticket);
    }

    protected Ticket deserializeTicket(byte[] ticket) {
        return SerializationUtils.deserialize(ticket);
    }

    protected String extractTokenKey(String value) {
        if (value == null) {
            return null;
        }
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
        }

        try {
            byte[] bytes = digest.digest(value.getBytes("UTF-8"));
            return String.format("%032x", new BigInteger(1, bytes));
        }
        catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
        }
    }

    private final class SafeTicketRowMapper implements RowMapper<Ticket> {
        public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
            try {
                return deserializeTicket(rs.getBytes(2));
            }
            catch (IllegalArgumentException e) {
                String ticket = rs.getString(1);
                jdbcTemplate.update(deleteTicketSql, ticket);
                return null;
            }
        }
    }
}
