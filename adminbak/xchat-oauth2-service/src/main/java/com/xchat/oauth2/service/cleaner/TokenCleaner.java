package com.xchat.oauth2.service.cleaner;

import com.xchat.oauth2.service.infrastructure.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Types;
import java.util.Date;

/**
 * @author liuguofu
 *         on 3/18/15.
 */
@Service
public class TokenCleaner implements Cleaner {

    private static final Log LOG = LogFactory.getLog(TokenCleaner.class);

    private static final String DEFAULT_EXPIRED_ACCESS_TOKEN_FROM_EXPIRED_REFRESH_TOKEN_STATEMENT = "delete oct from oauth_access_token as oct, oauth_refresh_token as ort where oct.refresh_token = ort.token_id and ort.expiration < ?";

    private static final String DEFAULT_EXPIRED_REFRESH_TOKEN_DELETE_STATEMENT = "delete from oauth_refresh_token where expiration < ?";

    private String deleteExpiredRefressTokenSql = DEFAULT_EXPIRED_REFRESH_TOKEN_DELETE_STATEMENT;

    private String deleteExpiredAccessTokenFromExpiredRefreshTokenSql = DEFAULT_EXPIRED_ACCESS_TOKEN_FROM_EXPIRED_REFRESH_TOKEN_STATEMENT;

    @Autowired
    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    private void init(){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void clean() {
        try {
            Date now = DateUtils.now();
            jdbcTemplate.update(deleteExpiredAccessTokenFromExpiredRefreshTokenSql,new Object[]{now},new int[]{Types.TIMESTAMP});
            jdbcTemplate.update(deleteExpiredRefressTokenSql, new Object[]{now}, new int[]{Types.TIMESTAMP});
        } catch (Exception e) {
            LOG.error("failed to delete expired access token or expired refresh token",e);
        }
    }
}
