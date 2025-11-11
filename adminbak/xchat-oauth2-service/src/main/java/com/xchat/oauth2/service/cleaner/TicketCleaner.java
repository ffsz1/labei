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

/**
 * @author liuguofu
 *         on 3/18/15.
 */
@Service
public class TicketCleaner implements Cleaner {

    private static final Log LOG = LogFactory.getLog(TicketCleaner.class);

    private static final String DEFAULT_EXPIRED_TICKET_DELETE_STATEMENT = "delete from oauth_ticket where expiration > ?";

    private String deleteExpiredTicketSql = DEFAULT_EXPIRED_TICKET_DELETE_STATEMENT;

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
            jdbcTemplate.update(deleteExpiredTicketSql, new Object[]{DateUtils.now()}, new int[]{Types.TIMESTAMP});
        } catch (Exception e) {
            LOG.error("failed to delete expired ticket");
        }
    }
}
