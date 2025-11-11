package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class NeteaseConversationExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NeteaseConversationExample() {
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

        public Criteria andConvTypeIsNull() {
            addCriterion("conv_type is null");
            return (Criteria) this;
        }

        public Criteria andConvTypeIsNotNull() {
            addCriterion("conv_type is not null");
            return (Criteria) this;
        }

        public Criteria andConvTypeEqualTo(String value) {
            addCriterion("conv_type =", value, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeNotEqualTo(String value) {
            addCriterion("conv_type <>", value, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeGreaterThan(String value) {
            addCriterion("conv_type >", value, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeGreaterThanOrEqualTo(String value) {
            addCriterion("conv_type >=", value, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeLessThan(String value) {
            addCriterion("conv_type <", value, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeLessThanOrEqualTo(String value) {
            addCriterion("conv_type <=", value, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeLike(String value) {
            addCriterion("conv_type like", value, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeNotLike(String value) {
            addCriterion("conv_type not like", value, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeIn(List<String> values) {
            addCriterion("conv_type in", values, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeNotIn(List<String> values) {
            addCriterion("conv_type not in", values, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeBetween(String value1, String value2) {
            addCriterion("conv_type between", value1, value2, "convType");
            return (Criteria) this;
        }

        public Criteria andConvTypeNotBetween(String value1, String value2) {
            addCriterion("conv_type not between", value1, value2, "convType");
            return (Criteria) this;
        }

        public Criteria andToStrIsNull() {
            addCriterion("to_str is null");
            return (Criteria) this;
        }

        public Criteria andToStrIsNotNull() {
            addCriterion("to_str is not null");
            return (Criteria) this;
        }

        public Criteria andToStrEqualTo(String value) {
            addCriterion("to_str =", value, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrNotEqualTo(String value) {
            addCriterion("to_str <>", value, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrGreaterThan(String value) {
            addCriterion("to_str >", value, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrGreaterThanOrEqualTo(String value) {
            addCriterion("to_str >=", value, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrLessThan(String value) {
            addCriterion("to_str <", value, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrLessThanOrEqualTo(String value) {
            addCriterion("to_str <=", value, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrLike(String value) {
            addCriterion("to_str like", value, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrNotLike(String value) {
            addCriterion("to_str not like", value, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrIn(List<String> values) {
            addCriterion("to_str in", values, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrNotIn(List<String> values) {
            addCriterion("to_str not in", values, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrBetween(String value1, String value2) {
            addCriterion("to_str between", value1, value2, "toStr");
            return (Criteria) this;
        }

        public Criteria andToStrNotBetween(String value1, String value2) {
            addCriterion("to_str not between", value1, value2, "toStr");
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

        public Criteria andFromDeviceIdIsNull() {
            addCriterion("from_device_id is null");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdIsNotNull() {
            addCriterion("from_device_id is not null");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdEqualTo(String value) {
            addCriterion("from_device_id =", value, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdNotEqualTo(String value) {
            addCriterion("from_device_id <>", value, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdGreaterThan(String value) {
            addCriterion("from_device_id >", value, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdGreaterThanOrEqualTo(String value) {
            addCriterion("from_device_id >=", value, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdLessThan(String value) {
            addCriterion("from_device_id <", value, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdLessThanOrEqualTo(String value) {
            addCriterion("from_device_id <=", value, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdLike(String value) {
            addCriterion("from_device_id like", value, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdNotLike(String value) {
            addCriterion("from_device_id not like", value, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdIn(List<String> values) {
            addCriterion("from_device_id in", values, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdNotIn(List<String> values) {
            addCriterion("from_device_id not in", values, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdBetween(String value1, String value2) {
            addCriterion("from_device_id between", value1, value2, "fromDeviceId");
            return (Criteria) this;
        }

        public Criteria andFromDeviceIdNotBetween(String value1, String value2) {
            addCriterion("from_device_id not between", value1, value2, "fromDeviceId");
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

        public Criteria andBodyIsNull() {
            addCriterion("body is null");
            return (Criteria) this;
        }

        public Criteria andBodyIsNotNull() {
            addCriterion("body is not null");
            return (Criteria) this;
        }

        public Criteria andBodyEqualTo(String value) {
            addCriterion("body =", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotEqualTo(String value) {
            addCriterion("body <>", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyGreaterThan(String value) {
            addCriterion("body >", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyGreaterThanOrEqualTo(String value) {
            addCriterion("body >=", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLessThan(String value) {
            addCriterion("body <", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLessThanOrEqualTo(String value) {
            addCriterion("body <=", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLike(String value) {
            addCriterion("body like", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotLike(String value) {
            addCriterion("body not like", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyIn(List<String> values) {
            addCriterion("body in", values, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotIn(List<String> values) {
            addCriterion("body not in", values, "body");
            return (Criteria) this;
        }

        public Criteria andBodyBetween(String value1, String value2) {
            addCriterion("body between", value1, value2, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotBetween(String value1, String value2) {
            addCriterion("body not between", value1, value2, "body");
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

        public Criteria andMsgidServerIsNull() {
            addCriterion("msgid_server is null");
            return (Criteria) this;
        }

        public Criteria andMsgidServerIsNotNull() {
            addCriterion("msgid_server is not null");
            return (Criteria) this;
        }

        public Criteria andMsgidServerEqualTo(String value) {
            addCriterion("msgid_server =", value, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerNotEqualTo(String value) {
            addCriterion("msgid_server <>", value, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerGreaterThan(String value) {
            addCriterion("msgid_server >", value, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerGreaterThanOrEqualTo(String value) {
            addCriterion("msgid_server >=", value, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerLessThan(String value) {
            addCriterion("msgid_server <", value, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerLessThanOrEqualTo(String value) {
            addCriterion("msgid_server <=", value, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerLike(String value) {
            addCriterion("msgid_server like", value, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerNotLike(String value) {
            addCriterion("msgid_server not like", value, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerIn(List<String> values) {
            addCriterion("msgid_server in", values, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerNotIn(List<String> values) {
            addCriterion("msgid_server not in", values, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerBetween(String value1, String value2) {
            addCriterion("msgid_server between", value1, value2, "msgidServer");
            return (Criteria) this;
        }

        public Criteria andMsgidServerNotBetween(String value1, String value2) {
            addCriterion("msgid_server not between", value1, value2, "msgidServer");
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

        public Criteria andCustomSafeFlagIsNull() {
            addCriterion("custom_safe_flag is null");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagIsNotNull() {
            addCriterion("custom_safe_flag is not null");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagEqualTo(String value) {
            addCriterion("custom_safe_flag =", value, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagNotEqualTo(String value) {
            addCriterion("custom_safe_flag <>", value, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagGreaterThan(String value) {
            addCriterion("custom_safe_flag >", value, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagGreaterThanOrEqualTo(String value) {
            addCriterion("custom_safe_flag >=", value, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagLessThan(String value) {
            addCriterion("custom_safe_flag <", value, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagLessThanOrEqualTo(String value) {
            addCriterion("custom_safe_flag <=", value, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagLike(String value) {
            addCriterion("custom_safe_flag like", value, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagNotLike(String value) {
            addCriterion("custom_safe_flag not like", value, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagIn(List<String> values) {
            addCriterion("custom_safe_flag in", values, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagNotIn(List<String> values) {
            addCriterion("custom_safe_flag not in", values, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagBetween(String value1, String value2) {
            addCriterion("custom_safe_flag between", value1, value2, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomSafeFlagNotBetween(String value1, String value2) {
            addCriterion("custom_safe_flag not between", value1, value2, "customSafeFlag");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextIsNull() {
            addCriterion("custom_apns_text is null");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextIsNotNull() {
            addCriterion("custom_apns_text is not null");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextEqualTo(String value) {
            addCriterion("custom_apns_text =", value, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextNotEqualTo(String value) {
            addCriterion("custom_apns_text <>", value, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextGreaterThan(String value) {
            addCriterion("custom_apns_text >", value, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextGreaterThanOrEqualTo(String value) {
            addCriterion("custom_apns_text >=", value, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextLessThan(String value) {
            addCriterion("custom_apns_text <", value, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextLessThanOrEqualTo(String value) {
            addCriterion("custom_apns_text <=", value, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextLike(String value) {
            addCriterion("custom_apns_text like", value, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextNotLike(String value) {
            addCriterion("custom_apns_text not like", value, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextIn(List<String> values) {
            addCriterion("custom_apns_text in", values, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextNotIn(List<String> values) {
            addCriterion("custom_apns_text not in", values, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextBetween(String value1, String value2) {
            addCriterion("custom_apns_text between", value1, value2, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andCustomApnsTextNotBetween(String value1, String value2) {
            addCriterion("custom_apns_text not between", value1, value2, "customApnsText");
            return (Criteria) this;
        }

        public Criteria andTMembersIsNull() {
            addCriterion("t_members is null");
            return (Criteria) this;
        }

        public Criteria andTMembersIsNotNull() {
            addCriterion("t_members is not null");
            return (Criteria) this;
        }

        public Criteria andTMembersEqualTo(String value) {
            addCriterion("t_members =", value, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersNotEqualTo(String value) {
            addCriterion("t_members <>", value, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersGreaterThan(String value) {
            addCriterion("t_members >", value, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersGreaterThanOrEqualTo(String value) {
            addCriterion("t_members >=", value, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersLessThan(String value) {
            addCriterion("t_members <", value, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersLessThanOrEqualTo(String value) {
            addCriterion("t_members <=", value, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersLike(String value) {
            addCriterion("t_members like", value, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersNotLike(String value) {
            addCriterion("t_members not like", value, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersIn(List<String> values) {
            addCriterion("t_members in", values, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersNotIn(List<String> values) {
            addCriterion("t_members not in", values, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersBetween(String value1, String value2) {
            addCriterion("t_members between", value1, value2, "tMembers");
            return (Criteria) this;
        }

        public Criteria andTMembersNotBetween(String value1, String value2) {
            addCriterion("t_members not between", value1, value2, "tMembers");
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
