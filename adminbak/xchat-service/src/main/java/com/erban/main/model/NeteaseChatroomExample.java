package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class NeteaseChatroomExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NeteaseChatroomExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andAttachIsNull() {
            addCriterion("attach is null");
            return (Criteria) this;
        }

        public Criteria andAttachIsNotNull() {
            addCriterion("attach is not null");
            return (Criteria) this;
        }

        public Criteria andAttachEqualTo(String value) {
            addCriterion("attach =", value, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachNotEqualTo(String value) {
            addCriterion("attach <>", value, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachGreaterThan(String value) {
            addCriterion("attach >", value, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachGreaterThanOrEqualTo(String value) {
            addCriterion("attach >=", value, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachLessThan(String value) {
            addCriterion("attach <", value, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachLessThanOrEqualTo(String value) {
            addCriterion("attach <=", value, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachLike(String value) {
            addCriterion("attach like", value, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachNotLike(String value) {
            addCriterion("attach not like", value, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachIn(List<String> values) {
            addCriterion("attach in", values, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachNotIn(List<String> values) {
            addCriterion("attach not in", values, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachBetween(String value1, String value2) {
            addCriterion("attach between", value1, value2, "attach");
            return (Criteria) this;
        }

        public Criteria andAttachNotBetween(String value1, String value2) {
            addCriterion("attach not between", value1, value2, "attach");
            return (Criteria) this;
        }

        public Criteria andExtIsNull() {
            addCriterion("ext is null");
            return (Criteria) this;
        }

        public Criteria andExtIsNotNull() {
            addCriterion("ext is not null");
            return (Criteria) this;
        }

        public Criteria andExtEqualTo(String value) {
            addCriterion("ext =", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotEqualTo(String value) {
            addCriterion("ext <>", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtGreaterThan(String value) {
            addCriterion("ext >", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtGreaterThanOrEqualTo(String value) {
            addCriterion("ext >=", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLessThan(String value) {
            addCriterion("ext <", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLessThanOrEqualTo(String value) {
            addCriterion("ext <=", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtLike(String value) {
            addCriterion("ext like", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotLike(String value) {
            addCriterion("ext not like", value, "ext");
            return (Criteria) this;
        }

        public Criteria andExtIn(List<String> values) {
            addCriterion("ext in", values, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotIn(List<String> values) {
            addCriterion("ext not in", values, "ext");
            return (Criteria) this;
        }

        public Criteria andExtBetween(String value1, String value2) {
            addCriterion("ext between", value1, value2, "ext");
            return (Criteria) this;
        }

        public Criteria andExtNotBetween(String value1, String value2) {
            addCriterion("ext not between", value1, value2, "ext");
            return (Criteria) this;
        }

        public Criteria andFromAccountIsNull() {
            addCriterion("from_account is null");
            return (Criteria) this;
        }

        public Criteria andFromAccountIsNotNull() {
            addCriterion("from_account is not null");
            return (Criteria) this;
        }

        public Criteria andFromAccountEqualTo(String value) {
            addCriterion("from_account =", value, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountNotEqualTo(String value) {
            addCriterion("from_account <>", value, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountGreaterThan(String value) {
            addCriterion("from_account >", value, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountGreaterThanOrEqualTo(String value) {
            addCriterion("from_account >=", value, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountLessThan(String value) {
            addCriterion("from_account <", value, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountLessThanOrEqualTo(String value) {
            addCriterion("from_account <=", value, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountLike(String value) {
            addCriterion("from_account like", value, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountNotLike(String value) {
            addCriterion("from_account not like", value, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountIn(List<String> values) {
            addCriterion("from_account in", values, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountNotIn(List<String> values) {
            addCriterion("from_account not in", values, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountBetween(String value1, String value2) {
            addCriterion("from_account between", value1, value2, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAccountNotBetween(String value1, String value2) {
            addCriterion("from_account not between", value1, value2, "fromAccount");
            return (Criteria) this;
        }

        public Criteria andFromAvatorIsNull() {
            addCriterion("from_avator is null");
            return (Criteria) this;
        }

        public Criteria andFromAvatorIsNotNull() {
            addCriterion("from_avator is not null");
            return (Criteria) this;
        }

        public Criteria andFromAvatorEqualTo(String value) {
            addCriterion("from_avator =", value, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorNotEqualTo(String value) {
            addCriterion("from_avator <>", value, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorGreaterThan(String value) {
            addCriterion("from_avator >", value, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorGreaterThanOrEqualTo(String value) {
            addCriterion("from_avator >=", value, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorLessThan(String value) {
            addCriterion("from_avator <", value, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorLessThanOrEqualTo(String value) {
            addCriterion("from_avator <=", value, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorLike(String value) {
            addCriterion("from_avator like", value, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorNotLike(String value) {
            addCriterion("from_avator not like", value, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorIn(List<String> values) {
            addCriterion("from_avator in", values, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorNotIn(List<String> values) {
            addCriterion("from_avator not in", values, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorBetween(String value1, String value2) {
            addCriterion("from_avator between", value1, value2, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromAvatorNotBetween(String value1, String value2) {
            addCriterion("from_avator not between", value1, value2, "fromAvator");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeIsNull() {
            addCriterion("from_client_type is null");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeIsNotNull() {
            addCriterion("from_client_type is not null");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeEqualTo(String value) {
            addCriterion("from_client_type =", value, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeNotEqualTo(String value) {
            addCriterion("from_client_type <>", value, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeGreaterThan(String value) {
            addCriterion("from_client_type >", value, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeGreaterThanOrEqualTo(String value) {
            addCriterion("from_client_type >=", value, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeLessThan(String value) {
            addCriterion("from_client_type <", value, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeLessThanOrEqualTo(String value) {
            addCriterion("from_client_type <=", value, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeLike(String value) {
            addCriterion("from_client_type like", value, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeNotLike(String value) {
            addCriterion("from_client_type not like", value, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeIn(List<String> values) {
            addCriterion("from_client_type in", values, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeNotIn(List<String> values) {
            addCriterion("from_client_type not in", values, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeBetween(String value1, String value2) {
            addCriterion("from_client_type between", value1, value2, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromClientTypeNotBetween(String value1, String value2) {
            addCriterion("from_client_type not between", value1, value2, "fromClientType");
            return (Criteria) this;
        }

        public Criteria andFromExtIsNull() {
            addCriterion("from_ext is null");
            return (Criteria) this;
        }

        public Criteria andFromExtIsNotNull() {
            addCriterion("from_ext is not null");
            return (Criteria) this;
        }

        public Criteria andFromExtEqualTo(String value) {
            addCriterion("from_ext =", value, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtNotEqualTo(String value) {
            addCriterion("from_ext <>", value, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtGreaterThan(String value) {
            addCriterion("from_ext >", value, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtGreaterThanOrEqualTo(String value) {
            addCriterion("from_ext >=", value, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtLessThan(String value) {
            addCriterion("from_ext <", value, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtLessThanOrEqualTo(String value) {
            addCriterion("from_ext <=", value, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtLike(String value) {
            addCriterion("from_ext like", value, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtNotLike(String value) {
            addCriterion("from_ext not like", value, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtIn(List<String> values) {
            addCriterion("from_ext in", values, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtNotIn(List<String> values) {
            addCriterion("from_ext not in", values, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtBetween(String value1, String value2) {
            addCriterion("from_ext between", value1, value2, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromExtNotBetween(String value1, String value2) {
            addCriterion("from_ext not between", value1, value2, "fromExt");
            return (Criteria) this;
        }

        public Criteria andFromNickIsNull() {
            addCriterion("from_nick is null");
            return (Criteria) this;
        }

        public Criteria andFromNickIsNotNull() {
            addCriterion("from_nick is not null");
            return (Criteria) this;
        }

        public Criteria andFromNickEqualTo(String value) {
            addCriterion("from_nick =", value, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickNotEqualTo(String value) {
            addCriterion("from_nick <>", value, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickGreaterThan(String value) {
            addCriterion("from_nick >", value, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickGreaterThanOrEqualTo(String value) {
            addCriterion("from_nick >=", value, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickLessThan(String value) {
            addCriterion("from_nick <", value, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickLessThanOrEqualTo(String value) {
            addCriterion("from_nick <=", value, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickLike(String value) {
            addCriterion("from_nick like", value, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickNotLike(String value) {
            addCriterion("from_nick not like", value, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickIn(List<String> values) {
            addCriterion("from_nick in", values, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickNotIn(List<String> values) {
            addCriterion("from_nick not in", values, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickBetween(String value1, String value2) {
            addCriterion("from_nick between", value1, value2, "fromNick");
            return (Criteria) this;
        }

        public Criteria andFromNickNotBetween(String value1, String value2) {
            addCriterion("from_nick not between", value1, value2, "fromNick");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampIsNull() {
            addCriterion("msg_timestamp is null");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampIsNotNull() {
            addCriterion("msg_timestamp is not null");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampEqualTo(String value) {
            addCriterion("msg_timestamp =", value, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampNotEqualTo(String value) {
            addCriterion("msg_timestamp <>", value, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampGreaterThan(String value) {
            addCriterion("msg_timestamp >", value, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampGreaterThanOrEqualTo(String value) {
            addCriterion("msg_timestamp >=", value, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampLessThan(String value) {
            addCriterion("msg_timestamp <", value, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampLessThanOrEqualTo(String value) {
            addCriterion("msg_timestamp <=", value, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampLike(String value) {
            addCriterion("msg_timestamp like", value, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampNotLike(String value) {
            addCriterion("msg_timestamp not like", value, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampIn(List<String> values) {
            addCriterion("msg_timestamp in", values, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampNotIn(List<String> values) {
            addCriterion("msg_timestamp not in", values, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampBetween(String value1, String value2) {
            addCriterion("msg_timestamp between", value1, value2, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTimestampNotBetween(String value1, String value2) {
            addCriterion("msg_timestamp not between", value1, value2, "msgTimestamp");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNull() {
            addCriterion("msg_type is null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNotNull() {
            addCriterion("msg_type is not null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeEqualTo(String value) {
            addCriterion("msg_type =", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotEqualTo(String value) {
            addCriterion("msg_type <>", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThan(String value) {
            addCriterion("msg_type >", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThanOrEqualTo(String value) {
            addCriterion("msg_type >=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThan(String value) {
            addCriterion("msg_type <", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThanOrEqualTo(String value) {
            addCriterion("msg_type <=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLike(String value) {
            addCriterion("msg_type like", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotLike(String value) {
            addCriterion("msg_type not like", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIn(List<String> values) {
            addCriterion("msg_type in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotIn(List<String> values) {
            addCriterion("msg_type not in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeBetween(String value1, String value2) {
            addCriterion("msg_type between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotBetween(String value1, String value2) {
            addCriterion("msg_type not between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgidClientIsNull() {
            addCriterion("msgid_client is null");
            return (Criteria) this;
        }

        public Criteria andMsgidClientIsNotNull() {
            addCriterion("msgid_client is not null");
            return (Criteria) this;
        }

        public Criteria andMsgidClientEqualTo(String value) {
            addCriterion("msgid_client =", value, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientNotEqualTo(String value) {
            addCriterion("msgid_client <>", value, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientGreaterThan(String value) {
            addCriterion("msgid_client >", value, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientGreaterThanOrEqualTo(String value) {
            addCriterion("msgid_client >=", value, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientLessThan(String value) {
            addCriterion("msgid_client <", value, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientLessThanOrEqualTo(String value) {
            addCriterion("msgid_client <=", value, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientLike(String value) {
            addCriterion("msgid_client like", value, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientNotLike(String value) {
            addCriterion("msgid_client not like", value, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientIn(List<String> values) {
            addCriterion("msgid_client in", values, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientNotIn(List<String> values) {
            addCriterion("msgid_client not in", values, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientBetween(String value1, String value2) {
            addCriterion("msgid_client between", value1, value2, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andMsgidClientNotBetween(String value1, String value2) {
            addCriterion("msgid_client not between", value1, value2, "msgidClient");
            return (Criteria) this;
        }

        public Criteria andResendFlagIsNull() {
            addCriterion("resend_flag is null");
            return (Criteria) this;
        }

        public Criteria andResendFlagIsNotNull() {
            addCriterion("resend_flag is not null");
            return (Criteria) this;
        }

        public Criteria andResendFlagEqualTo(String value) {
            addCriterion("resend_flag =", value, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagNotEqualTo(String value) {
            addCriterion("resend_flag <>", value, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagGreaterThan(String value) {
            addCriterion("resend_flag >", value, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagGreaterThanOrEqualTo(String value) {
            addCriterion("resend_flag >=", value, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagLessThan(String value) {
            addCriterion("resend_flag <", value, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagLessThanOrEqualTo(String value) {
            addCriterion("resend_flag <=", value, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagLike(String value) {
            addCriterion("resend_flag like", value, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagNotLike(String value) {
            addCriterion("resend_flag not like", value, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagIn(List<String> values) {
            addCriterion("resend_flag in", values, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagNotIn(List<String> values) {
            addCriterion("resend_flag not in", values, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagBetween(String value1, String value2) {
            addCriterion("resend_flag between", value1, value2, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andResendFlagNotBetween(String value1, String value2) {
            addCriterion("resend_flag not between", value1, value2, "resendFlag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagIsNull() {
            addCriterion("role_info_timetag is null");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagIsNotNull() {
            addCriterion("role_info_timetag is not null");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagEqualTo(String value) {
            addCriterion("role_info_timetag =", value, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagNotEqualTo(String value) {
            addCriterion("role_info_timetag <>", value, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagGreaterThan(String value) {
            addCriterion("role_info_timetag >", value, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagGreaterThanOrEqualTo(String value) {
            addCriterion("role_info_timetag >=", value, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagLessThan(String value) {
            addCriterion("role_info_timetag <", value, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagLessThanOrEqualTo(String value) {
            addCriterion("role_info_timetag <=", value, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagLike(String value) {
            addCriterion("role_info_timetag like", value, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagNotLike(String value) {
            addCriterion("role_info_timetag not like", value, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagIn(List<String> values) {
            addCriterion("role_info_timetag in", values, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagNotIn(List<String> values) {
            addCriterion("role_info_timetag not in", values, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagBetween(String value1, String value2) {
            addCriterion("role_info_timetag between", value1, value2, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoleInfoTimetagNotBetween(String value1, String value2) {
            addCriterion("role_info_timetag not between", value1, value2, "roleInfoTimetag");
            return (Criteria) this;
        }

        public Criteria andRoomIdIsNull() {
            addCriterion("room_id is null");
            return (Criteria) this;
        }

        public Criteria andRoomIdIsNotNull() {
            addCriterion("room_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoomIdEqualTo(String value) {
            addCriterion("room_id =", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotEqualTo(String value) {
            addCriterion("room_id <>", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdGreaterThan(String value) {
            addCriterion("room_id >", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdGreaterThanOrEqualTo(String value) {
            addCriterion("room_id >=", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLessThan(String value) {
            addCriterion("room_id <", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLessThanOrEqualTo(String value) {
            addCriterion("room_id <=", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLike(String value) {
            addCriterion("room_id like", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotLike(String value) {
            addCriterion("room_id not like", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdIn(List<String> values) {
            addCriterion("room_id in", values, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotIn(List<String> values) {
            addCriterion("room_id not in", values, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdBetween(String value1, String value2) {
            addCriterion("room_id between", value1, value2, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotBetween(String value1, String value2) {
            addCriterion("room_id not between", value1, value2, "roomId");
            return (Criteria) this;
        }

        public Criteria andAntispamIsNull() {
            addCriterion("antispam is null");
            return (Criteria) this;
        }

        public Criteria andAntispamIsNotNull() {
            addCriterion("antispam is not null");
            return (Criteria) this;
        }

        public Criteria andAntispamEqualTo(String value) {
            addCriterion("antispam =", value, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamNotEqualTo(String value) {
            addCriterion("antispam <>", value, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamGreaterThan(String value) {
            addCriterion("antispam >", value, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamGreaterThanOrEqualTo(String value) {
            addCriterion("antispam >=", value, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamLessThan(String value) {
            addCriterion("antispam <", value, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamLessThanOrEqualTo(String value) {
            addCriterion("antispam <=", value, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamLike(String value) {
            addCriterion("antispam like", value, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamNotLike(String value) {
            addCriterion("antispam not like", value, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamIn(List<String> values) {
            addCriterion("antispam in", values, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamNotIn(List<String> values) {
            addCriterion("antispam not in", values, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamBetween(String value1, String value2) {
            addCriterion("antispam between", value1, value2, "antispam");
            return (Criteria) this;
        }

        public Criteria andAntispamNotBetween(String value1, String value2) {
            addCriterion("antispam not between", value1, value2, "antispam");
            return (Criteria) this;
        }

        public Criteria andYidunResIsNull() {
            addCriterion("yidun_res is null");
            return (Criteria) this;
        }

        public Criteria andYidunResIsNotNull() {
            addCriterion("yidun_res is not null");
            return (Criteria) this;
        }

        public Criteria andYidunResEqualTo(String value) {
            addCriterion("yidun_res =", value, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResNotEqualTo(String value) {
            addCriterion("yidun_res <>", value, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResGreaterThan(String value) {
            addCriterion("yidun_res >", value, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResGreaterThanOrEqualTo(String value) {
            addCriterion("yidun_res >=", value, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResLessThan(String value) {
            addCriterion("yidun_res <", value, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResLessThanOrEqualTo(String value) {
            addCriterion("yidun_res <=", value, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResLike(String value) {
            addCriterion("yidun_res like", value, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResNotLike(String value) {
            addCriterion("yidun_res not like", value, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResIn(List<String> values) {
            addCriterion("yidun_res in", values, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResNotIn(List<String> values) {
            addCriterion("yidun_res not in", values, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResBetween(String value1, String value2) {
            addCriterion("yidun_res between", value1, value2, "yidunRes");
            return (Criteria) this;
        }

        public Criteria andYidunResNotBetween(String value1, String value2) {
            addCriterion("yidun_res not between", value1, value2, "yidunRes");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}
