package com.tongdaxing.xchat_core.auth;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhouxiangfeng on 2017/5/4.
 */

public class TicketInfo implements Serializable{
    private String issue_type;
    private List<Ticket> tickets;

    public String getIssue_type() {
        return issue_type;
    }

    public void setIssue_type(String issue_type) {
        this.issue_type = issue_type;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public class Ticket implements Serializable{
        private String ticket;
        private String expires_in;

        public String getTicket() {
            return ticket;
        }

        public void setTicket(String ticket) {
            this.ticket = ticket;
        }

        public String getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(String expires_in) {
            this.expires_in = expires_in;
        }
    }
}
