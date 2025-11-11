package com.xchat.oauth2.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AccountExample() {
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

        public Criteria andUidIsNull() {
            addCriterion("uid is null");
            return (Criteria) this;
        }

        public Criteria andUidIsNotNull() {
            addCriterion("uid is not null");
            return (Criteria) this;
        }

        public Criteria andUidEqualTo(Long value) {
            addCriterion("uid =", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotEqualTo(Long value) {
            addCriterion("uid <>", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThan(Long value) {
            addCriterion("uid >", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidGreaterThanOrEqualTo(Long value) {
            addCriterion("uid >=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThan(Long value) {
            addCriterion("uid <", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidLessThanOrEqualTo(Long value) {
            addCriterion("uid <=", value, "uid");
            return (Criteria) this;
        }

        public Criteria andUidIn(List<Long> values) {
            addCriterion("uid in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotIn(List<Long> values) {
            addCriterion("uid not in", values, "uid");
            return (Criteria) this;
        }

        public Criteria andUidBetween(Long value1, Long value2) {
            addCriterion("uid between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andUidNotBetween(Long value1, Long value2) {
            addCriterion("uid not between", value1, value2, "uid");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andErbanNoIsNull() {
            addCriterion("erban_no is null");
            return (Criteria) this;
        }

        public Criteria andErbanNoIsNotNull() {
            addCriterion("erban_no is not null");
            return (Criteria) this;
        }

        public Criteria andErbanNoEqualTo(Long value) {
            addCriterion("erban_no =", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotEqualTo(Long value) {
            addCriterion("erban_no <>", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThan(Long value) {
            addCriterion("erban_no >", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThanOrEqualTo(Long value) {
            addCriterion("erban_no >=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThan(Long value) {
            addCriterion("erban_no <", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThanOrEqualTo(Long value) {
            addCriterion("erban_no <=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoIn(List<Long> values) {
            addCriterion("erban_no in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotIn(List<Long> values) {
            addCriterion("erban_no not in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoBetween(Long value1, Long value2) {
            addCriterion("erban_no between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotBetween(Long value1, Long value2) {
            addCriterion("erban_no not between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNull() {
            addCriterion("password is null");
            return (Criteria) this;
        }

        public Criteria andPasswordIsNotNull() {
            addCriterion("password is not null");
            return (Criteria) this;
        }

        public Criteria andPasswordEqualTo(String value) {
            addCriterion("password =", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotEqualTo(String value) {
            addCriterion("password <>", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThan(String value) {
            addCriterion("password >", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordGreaterThanOrEqualTo(String value) {
            addCriterion("password >=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThan(String value) {
            addCriterion("password <", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLessThanOrEqualTo(String value) {
            addCriterion("password <=", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordLike(String value) {
            addCriterion("password like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotLike(String value) {
            addCriterion("password not like", value, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordIn(List<String> values) {
            addCriterion("password in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotIn(List<String> values) {
            addCriterion("password not in", values, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordBetween(String value1, String value2) {
            addCriterion("password between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andPasswordNotBetween(String value1, String value2) {
            addCriterion("password not between", value1, value2, "password");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenIsNull() {
            addCriterion("netease_token is null");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenIsNotNull() {
            addCriterion("netease_token is not null");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenEqualTo(String value) {
            addCriterion("netease_token =", value, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenNotEqualTo(String value) {
            addCriterion("netease_token <>", value, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenGreaterThan(String value) {
            addCriterion("netease_token >", value, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenGreaterThanOrEqualTo(String value) {
            addCriterion("netease_token >=", value, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenLessThan(String value) {
            addCriterion("netease_token <", value, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenLessThanOrEqualTo(String value) {
            addCriterion("netease_token <=", value, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenLike(String value) {
            addCriterion("netease_token like", value, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenNotLike(String value) {
            addCriterion("netease_token not like", value, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenIn(List<String> values) {
            addCriterion("netease_token in", values, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenNotIn(List<String> values) {
            addCriterion("netease_token not in", values, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenBetween(String value1, String value2) {
            addCriterion("netease_token between", value1, value2, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andNeteaseTokenNotBetween(String value1, String value2) {
            addCriterion("netease_token not between", value1, value2, "neteaseToken");
            return (Criteria) this;
        }

        public Criteria andStateIsNull() {
            addCriterion("state is null");
            return (Criteria) this;
        }

        public Criteria andStateIsNotNull() {
            addCriterion("state is not null");
            return (Criteria) this;
        }

        public Criteria andStateEqualTo(String value) {
            addCriterion("state =", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotEqualTo(String value) {
            addCriterion("state <>", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThan(String value) {
            addCriterion("state >", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateGreaterThanOrEqualTo(String value) {
            addCriterion("state >=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThan(String value) {
            addCriterion("state <", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLessThanOrEqualTo(String value) {
            addCriterion("state <=", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateLike(String value) {
            addCriterion("state like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotLike(String value) {
            addCriterion("state not like", value, "state");
            return (Criteria) this;
        }

        public Criteria andStateIn(List<String> values) {
            addCriterion("state in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotIn(List<String> values) {
            addCriterion("state not in", values, "state");
            return (Criteria) this;
        }

        public Criteria andStateBetween(String value1, String value2) {
            addCriterion("state between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andStateNotBetween(String value1, String value2) {
            addCriterion("state not between", value1, value2, "state");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNull() {
            addCriterion("last_login_time is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNotNull() {
            addCriterion("last_login_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeEqualTo(Date value) {
            addCriterion("last_login_time =", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotEqualTo(Date value) {
            addCriterion("last_login_time <>", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThan(Date value) {
            addCriterion("last_login_time >", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_login_time >=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThan(Date value) {
            addCriterion("last_login_time <", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_login_time <=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIn(List<Date> values) {
            addCriterion("last_login_time in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotIn(List<Date> values) {
            addCriterion("last_login_time not in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeBetween(Date value1, Date value2) {
            addCriterion("last_login_time between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_login_time not between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIsNull() {
            addCriterion("last_login_ip is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIsNotNull() {
            addCriterion("last_login_ip is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpEqualTo(String value) {
            addCriterion("last_login_ip =", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotEqualTo(String value) {
            addCriterion("last_login_ip <>", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpGreaterThan(String value) {
            addCriterion("last_login_ip >", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpGreaterThanOrEqualTo(String value) {
            addCriterion("last_login_ip >=", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLessThan(String value) {
            addCriterion("last_login_ip <", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLessThanOrEqualTo(String value) {
            addCriterion("last_login_ip <=", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLike(String value) {
            addCriterion("last_login_ip like", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotLike(String value) {
            addCriterion("last_login_ip not like", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIn(List<String> values) {
            addCriterion("last_login_ip in", values, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotIn(List<String> values) {
            addCriterion("last_login_ip not in", values, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpBetween(String value1, String value2) {
            addCriterion("last_login_ip between", value1, value2, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotBetween(String value1, String value2) {
            addCriterion("last_login_ip not between", value1, value2, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpIsNull() {
            addCriterion("register_ip is null");
            return (Criteria) this;
        }

        public Criteria andRegisterIpIsNotNull() {
            addCriterion("register_ip is not null");
            return (Criteria) this;
        }

        public Criteria andRegisterIpEqualTo(String value) {
            addCriterion("register_ip =", value, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpNotEqualTo(String value) {
            addCriterion("register_ip <>", value, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpGreaterThan(String value) {
            addCriterion("register_ip >", value, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpGreaterThanOrEqualTo(String value) {
            addCriterion("register_ip >=", value, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpLessThan(String value) {
            addCriterion("register_ip <", value, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpLessThanOrEqualTo(String value) {
            addCriterion("register_ip <=", value, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpLike(String value) {
            addCriterion("register_ip like", value, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpNotLike(String value) {
            addCriterion("register_ip not like", value, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpIn(List<String> values) {
            addCriterion("register_ip in", values, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpNotIn(List<String> values) {
            addCriterion("register_ip not in", values, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpBetween(String value1, String value2) {
            addCriterion("register_ip between", value1, value2, "registerIp");
            return (Criteria) this;
        }

        public Criteria andRegisterIpNotBetween(String value1, String value2) {
            addCriterion("register_ip not between", value1, value2, "registerIp");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidIsNull() {
            addCriterion("weixin_openid is null");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidIsNotNull() {
            addCriterion("weixin_openid is not null");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidEqualTo(String value) {
            addCriterion("weixin_openid =", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidEqualTo(String value) {
            addCriterion("weixin_openid =", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidNotEqualTo(String value) {
            addCriterion("weixin_openid <>", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidGreaterThan(String value) {
            addCriterion("weixin_openid >", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidGreaterThanOrEqualTo(String value) {
            addCriterion("weixin_openid >=", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidLessThan(String value) {
            addCriterion("weixin_openid <", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidLessThanOrEqualTo(String value) {
            addCriterion("weixin_openid <=", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidLike(String value) {
            addCriterion("weixin_openid like", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidNotLike(String value) {
            addCriterion("weixin_openid not like", value, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidIn(List<String> values) {
            addCriterion("weixin_openid in", values, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidNotIn(List<String> values) {
            addCriterion("weixin_openid not in", values, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidBetween(String value1, String value2) {
            addCriterion("weixin_openid between", value1, value2, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinOpenidNotBetween(String value1, String value2) {
            addCriterion("weixin_openid not between", value1, value2, "weixinOpenid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidIsNull() {
            addCriterion("weixin_unionid is null");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidIsNotNull() {
            addCriterion("weixin_unionid is not null");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidEqualTo(String value) {
            addCriterion("weixin_unionid =", value, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidNotEqualTo(String value) {
            addCriterion("weixin_unionid <>", value, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidGreaterThan(String value) {
            addCriterion("weixin_unionid >", value, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidGreaterThanOrEqualTo(String value) {
            addCriterion("weixin_unionid >=", value, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidLessThan(String value) {
            addCriterion("weixin_unionid <", value, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidLessThanOrEqualTo(String value) {
            addCriterion("weixin_unionid <=", value, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidLike(String value) {
            addCriterion("weixin_unionid like", value, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidNotLike(String value) {
            addCriterion("weixin_unionid not like", value, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidIn(List<String> values) {
            addCriterion("weixin_unionid in", values, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidNotIn(List<String> values) {
            addCriterion("weixin_unionid not in", values, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidBetween(String value1, String value2) {
            addCriterion("weixin_unionid between", value1, value2, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andWeixinUnionidNotBetween(String value1, String value2) {
            addCriterion("weixin_unionid not between", value1, value2, "weixinUnionid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidIsNull() {
            addCriterion("qq_openid is null");
            return (Criteria) this;
        }

        public Criteria andQqOpenidIsNotNull() {
            addCriterion("qq_openid is not null");
            return (Criteria) this;
        }

        public Criteria andQqOpenidEqualTo(String value) {
            addCriterion("qq_openid =", value, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidNotEqualTo(String value) {
            addCriterion("qq_openid <>", value, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidGreaterThan(String value) {
            addCriterion("qq_openid >", value, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidGreaterThanOrEqualTo(String value) {
            addCriterion("qq_openid >=", value, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidLessThan(String value) {
            addCriterion("qq_openid <", value, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidLessThanOrEqualTo(String value) {
            addCriterion("qq_openid <=", value, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidLike(String value) {
            addCriterion("qq_openid like", value, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidNotLike(String value) {
            addCriterion("qq_openid not like", value, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidIn(List<String> values) {
            addCriterion("qq_openid in", values, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidNotIn(List<String> values) {
            addCriterion("qq_openid not in", values, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidBetween(String value1, String value2) {
            addCriterion("qq_openid between", value1, value2, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqOpenidNotBetween(String value1, String value2) {
            addCriterion("qq_openid not between", value1, value2, "qqOpenid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidIsNull() {
            addCriterion("qq_unionid is null");
            return (Criteria) this;
        }

        public Criteria andQqUnionidIsNotNull() {
            addCriterion("qq_unionid is not null");
            return (Criteria) this;
        }

        public Criteria andQqUnionidEqualTo(String value) {
            addCriterion("qq_unionid =", value, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidNotEqualTo(String value) {
            addCriterion("qq_unionid <>", value, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidGreaterThan(String value) {
            addCriterion("qq_unionid >", value, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidGreaterThanOrEqualTo(String value) {
            addCriterion("qq_unionid >=", value, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidLessThan(String value) {
            addCriterion("qq_unionid <", value, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidLessThanOrEqualTo(String value) {
            addCriterion("qq_unionid <=", value, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidLike(String value) {
            addCriterion("qq_unionid like", value, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidNotLike(String value) {
            addCriterion("qq_unionid not like", value, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidIn(List<String> values) {
            addCriterion("qq_unionid in", values, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidNotIn(List<String> values) {
            addCriterion("qq_unionid not in", values, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidBetween(String value1, String value2) {
            addCriterion("qq_unionid between", value1, value2, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andQqUnionidNotBetween(String value1, String value2) {
            addCriterion("qq_unionid not between", value1, value2, "qqUnionid");
            return (Criteria) this;
        }

        public Criteria andOsIsNull() {
            addCriterion("os is null");
            return (Criteria) this;
        }

        public Criteria andOsIsNotNull() {
            addCriterion("os is not null");
            return (Criteria) this;
        }

        public Criteria andOsEqualTo(String value) {
            addCriterion("os =", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotEqualTo(String value) {
            addCriterion("os <>", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsGreaterThan(String value) {
            addCriterion("os >", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsGreaterThanOrEqualTo(String value) {
            addCriterion("os >=", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLessThan(String value) {
            addCriterion("os <", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLessThanOrEqualTo(String value) {
            addCriterion("os <=", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLike(String value) {
            addCriterion("os like", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotLike(String value) {
            addCriterion("os not like", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsIn(List<String> values) {
            addCriterion("os in", values, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotIn(List<String> values) {
            addCriterion("os not in", values, "os");
            return (Criteria) this;
        }

        public Criteria andOsBetween(String value1, String value2) {
            addCriterion("os between", value1, value2, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotBetween(String value1, String value2) {
            addCriterion("os not between", value1, value2, "os");
            return (Criteria) this;
        }

        public Criteria andOsversionIsNull() {
            addCriterion("osVersion is null");
            return (Criteria) this;
        }

        public Criteria andOsversionIsNotNull() {
            addCriterion("osVersion is not null");
            return (Criteria) this;
        }

        public Criteria andOsversionEqualTo(String value) {
            addCriterion("osVersion =", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionNotEqualTo(String value) {
            addCriterion("osVersion <>", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionGreaterThan(String value) {
            addCriterion("osVersion >", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionGreaterThanOrEqualTo(String value) {
            addCriterion("osVersion >=", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionLessThan(String value) {
            addCriterion("osVersion <", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionLessThanOrEqualTo(String value) {
            addCriterion("osVersion <=", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionLike(String value) {
            addCriterion("osVersion like", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionNotLike(String value) {
            addCriterion("osVersion not like", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionIn(List<String> values) {
            addCriterion("osVersion in", values, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionNotIn(List<String> values) {
            addCriterion("osVersion not in", values, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionBetween(String value1, String value2) {
            addCriterion("osVersion between", value1, value2, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionNotBetween(String value1, String value2) {
            addCriterion("osVersion not between", value1, value2, "osversion");
            return (Criteria) this;
        }

        public Criteria andAppIsNull() {
            addCriterion("app is null");
            return (Criteria) this;
        }

        public Criteria andAppIsNotNull() {
            addCriterion("app is not null");
            return (Criteria) this;
        }

        public Criteria andAppEqualTo(String value) {
            addCriterion("app =", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppNotEqualTo(String value) {
            addCriterion("app <>", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppGreaterThan(String value) {
            addCriterion("app >", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppGreaterThanOrEqualTo(String value) {
            addCriterion("app >=", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppLessThan(String value) {
            addCriterion("app <", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppLessThanOrEqualTo(String value) {
            addCriterion("app <=", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppLike(String value) {
            addCriterion("app like", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppNotLike(String value) {
            addCriterion("app not like", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppIn(List<String> values) {
            addCriterion("app in", values, "app");
            return (Criteria) this;
        }

        public Criteria andAppNotIn(List<String> values) {
            addCriterion("app not in", values, "app");
            return (Criteria) this;
        }

        public Criteria andAppBetween(String value1, String value2) {
            addCriterion("app between", value1, value2, "app");
            return (Criteria) this;
        }

        public Criteria andAppNotBetween(String value1, String value2) {
            addCriterion("app not between", value1, value2, "app");
            return (Criteria) this;
        }

        public Criteria andImeiIsNull() {
            addCriterion("imei is null");
            return (Criteria) this;
        }

        public Criteria andImeiIsNotNull() {
            addCriterion("imei is not null");
            return (Criteria) this;
        }

        public Criteria andImeiEqualTo(String value) {
            addCriterion("imei =", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotEqualTo(String value) {
            addCriterion("imei <>", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiGreaterThan(String value) {
            addCriterion("imei >", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiGreaterThanOrEqualTo(String value) {
            addCriterion("imei >=", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLessThan(String value) {
            addCriterion("imei <", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLessThanOrEqualTo(String value) {
            addCriterion("imei <=", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLike(String value) {
            addCriterion("imei like", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotLike(String value) {
            addCriterion("imei not like", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiIn(List<String> values) {
            addCriterion("imei in", values, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotIn(List<String> values) {
            addCriterion("imei not in", values, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiBetween(String value1, String value2) {
            addCriterion("imei between", value1, value2, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotBetween(String value1, String value2) {
            addCriterion("imei not between", value1, value2, "imei");
            return (Criteria) this;
        }

        public Criteria andChannelIsNull() {
            addCriterion("channel is null");
            return (Criteria) this;
        }

        public Criteria andChannelIsNotNull() {
            addCriterion("channel is not null");
            return (Criteria) this;
        }

        public Criteria andChannelEqualTo(String value) {
            addCriterion("channel =", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotEqualTo(String value) {
            addCriterion("channel <>", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThan(String value) {
            addCriterion("channel >", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThanOrEqualTo(String value) {
            addCriterion("channel >=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThan(String value) {
            addCriterion("channel <", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThanOrEqualTo(String value) {
            addCriterion("channel <=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLike(String value) {
            addCriterion("channel like", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotLike(String value) {
            addCriterion("channel not like", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelIn(List<String> values) {
            addCriterion("channel in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotIn(List<String> values) {
            addCriterion("channel not in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelBetween(String value1, String value2) {
            addCriterion("channel between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotBetween(String value1, String value2) {
            addCriterion("channel not between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelIsNull() {
            addCriterion("linkedme_channel is null");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelIsNotNull() {
            addCriterion("linkedme_channel is not null");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelEqualTo(String value) {
            addCriterion("linkedme_channel =", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelNotEqualTo(String value) {
            addCriterion("linkedme_channel <>", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelGreaterThan(String value) {
            addCriterion("linkedme_channel >", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelGreaterThanOrEqualTo(String value) {
            addCriterion("linkedme_channel >=", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelLessThan(String value) {
            addCriterion("linkedme_channel <", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelLessThanOrEqualTo(String value) {
            addCriterion("linkedme_channel <=", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelLike(String value) {
            addCriterion("linkedme_channel like", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelNotLike(String value) {
            addCriterion("linkedme_channel not like", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelIn(List<String> values) {
            addCriterion("linkedme_channel in", values, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelNotIn(List<String> values) {
            addCriterion("linkedme_channel not in", values, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelBetween(String value1, String value2) {
            addCriterion("linkedme_channel between", value1, value2, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelNotBetween(String value1, String value2) {
            addCriterion("linkedme_channel not between", value1, value2, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andIspTypeIsNull() {
            addCriterion("isp_type is null");
            return (Criteria) this;
        }

        public Criteria andIspTypeIsNotNull() {
            addCriterion("isp_type is not null");
            return (Criteria) this;
        }

        public Criteria andIspTypeEqualTo(String value) {
            addCriterion("isp_type =", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeNotEqualTo(String value) {
            addCriterion("isp_type <>", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeGreaterThan(String value) {
            addCriterion("isp_type >", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeGreaterThanOrEqualTo(String value) {
            addCriterion("isp_type >=", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeLessThan(String value) {
            addCriterion("isp_type <", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeLessThanOrEqualTo(String value) {
            addCriterion("isp_type <=", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeLike(String value) {
            addCriterion("isp_type like", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeNotLike(String value) {
            addCriterion("isp_type not like", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeIn(List<String> values) {
            addCriterion("isp_type in", values, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeNotIn(List<String> values) {
            addCriterion("isp_type not in", values, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeBetween(String value1, String value2) {
            addCriterion("isp_type between", value1, value2, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeNotBetween(String value1, String value2) {
            addCriterion("isp_type not between", value1, value2, "ispType");
            return (Criteria) this;
        }

        public Criteria andNetTypeIsNull() {
            addCriterion("net_type is null");
            return (Criteria) this;
        }

        public Criteria andNetTypeIsNotNull() {
            addCriterion("net_type is not null");
            return (Criteria) this;
        }

        public Criteria andNetTypeEqualTo(String value) {
            addCriterion("net_type =", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeNotEqualTo(String value) {
            addCriterion("net_type <>", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeGreaterThan(String value) {
            addCriterion("net_type >", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeGreaterThanOrEqualTo(String value) {
            addCriterion("net_type >=", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeLessThan(String value) {
            addCriterion("net_type <", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeLessThanOrEqualTo(String value) {
            addCriterion("net_type <=", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeLike(String value) {
            addCriterion("net_type like", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeNotLike(String value) {
            addCriterion("net_type not like", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeIn(List<String> values) {
            addCriterion("net_type in", values, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeNotIn(List<String> values) {
            addCriterion("net_type not in", values, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeBetween(String value1, String value2) {
            addCriterion("net_type between", value1, value2, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeNotBetween(String value1, String value2) {
            addCriterion("net_type not between", value1, value2, "netType");
            return (Criteria) this;
        }

        public Criteria andModelIsNull() {
            addCriterion("model is null");
            return (Criteria) this;
        }

        public Criteria andModelIsNotNull() {
            addCriterion("model is not null");
            return (Criteria) this;
        }

        public Criteria andModelEqualTo(String value) {
            addCriterion("model =", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotEqualTo(String value) {
            addCriterion("model <>", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThan(String value) {
            addCriterion("model >", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThanOrEqualTo(String value) {
            addCriterion("model >=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThan(String value) {
            addCriterion("model <", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThanOrEqualTo(String value) {
            addCriterion("model <=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLike(String value) {
            addCriterion("model like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotLike(String value) {
            addCriterion("model not like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelIn(List<String> values) {
            addCriterion("model in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotIn(List<String> values) {
            addCriterion("model not in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelBetween(String value1, String value2) {
            addCriterion("model between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotBetween(String value1, String value2) {
            addCriterion("model not between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNull() {
            addCriterion("device_id is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNotNull() {
            addCriterion("device_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdEqualTo(String value) {
            addCriterion("device_id =", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotEqualTo(String value) {
            addCriterion("device_id <>", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThan(String value) {
            addCriterion("device_id >", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThanOrEqualTo(String value) {
            addCriterion("device_id >=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThan(String value) {
            addCriterion("device_id <", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThanOrEqualTo(String value) {
            addCriterion("device_id <=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLike(String value) {
            addCriterion("device_id like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotLike(String value) {
            addCriterion("device_id not like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIn(List<String> values) {
            addCriterion("device_id in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotIn(List<String> values) {
            addCriterion("device_id not in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdBetween(String value1, String value2) {
            addCriterion("device_id between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotBetween(String value1, String value2) {
            addCriterion("device_id not between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andAppVersionIsNull() {
            addCriterion("app_version is null");
            return (Criteria) this;
        }

        public Criteria andAppVersionIsNotNull() {
            addCriterion("app_version is not null");
            return (Criteria) this;
        }

        public Criteria andAppVersionEqualTo(String value) {
            addCriterion("app_version =", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionNotEqualTo(String value) {
            addCriterion("app_version <>", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionGreaterThan(String value) {
            addCriterion("app_version >", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionGreaterThanOrEqualTo(String value) {
            addCriterion("app_version >=", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionLessThan(String value) {
            addCriterion("app_version <", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionLessThanOrEqualTo(String value) {
            addCriterion("app_version <=", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionLike(String value) {
            addCriterion("app_version like", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionNotLike(String value) {
            addCriterion("app_version not like", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionIn(List<String> values) {
            addCriterion("app_version in", values, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionNotIn(List<String> values) {
            addCriterion("app_version not in", values, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionBetween(String value1, String value2) {
            addCriterion("app_version between", value1, value2, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionNotBetween(String value1, String value2) {
            addCriterion("app_version not between", value1, value2, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeIsNull() {
            addCriterion("acc_block_start_time is null");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeIsNotNull() {
            addCriterion("acc_block_start_time is not null");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeEqualTo(Date value) {
            addCriterion("acc_block_start_time =", value, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeNotEqualTo(Date value) {
            addCriterion("acc_block_start_time <>", value, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeGreaterThan(Date value) {
            addCriterion("acc_block_start_time >", value, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("acc_block_start_time >=", value, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeLessThan(Date value) {
            addCriterion("acc_block_start_time <", value, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("acc_block_start_time <=", value, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeIn(List<Date> values) {
            addCriterion("acc_block_start_time in", values, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeNotIn(List<Date> values) {
            addCriterion("acc_block_start_time not in", values, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeBetween(Date value1, Date value2) {
            addCriterion("acc_block_start_time between", value1, value2, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("acc_block_start_time not between", value1, value2, "accBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeIsNull() {
            addCriterion("acc_block_end_time is null");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeIsNotNull() {
            addCriterion("acc_block_end_time is not null");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeEqualTo(Date value) {
            addCriterion("acc_block_end_time =", value, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeNotEqualTo(Date value) {
            addCriterion("acc_block_end_time <>", value, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeGreaterThan(Date value) {
            addCriterion("acc_block_end_time >", value, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("acc_block_end_time >=", value, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeLessThan(Date value) {
            addCriterion("acc_block_end_time <", value, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("acc_block_end_time <=", value, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeIn(List<Date> values) {
            addCriterion("acc_block_end_time in", values, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeNotIn(List<Date> values) {
            addCriterion("acc_block_end_time not in", values, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeBetween(Date value1, Date value2) {
            addCriterion("acc_block_end_time between", value1, value2, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andAccBlockEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("acc_block_end_time not between", value1, value2, "accBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeIsNull() {
            addCriterion("device_block_start_time is null");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeIsNotNull() {
            addCriterion("device_block_start_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeEqualTo(Date value) {
            addCriterion("device_block_start_time =", value, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeNotEqualTo(Date value) {
            addCriterion("device_block_start_time <>", value, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeGreaterThan(Date value) {
            addCriterion("device_block_start_time >", value, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("device_block_start_time >=", value, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeLessThan(Date value) {
            addCriterion("device_block_start_time <", value, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("device_block_start_time <=", value, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeIn(List<Date> values) {
            addCriterion("device_block_start_time in", values, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeNotIn(List<Date> values) {
            addCriterion("device_block_start_time not in", values, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeBetween(Date value1, Date value2) {
            addCriterion("device_block_start_time between", value1, value2, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("device_block_start_time not between", value1, value2, "deviceBlockStartTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeIsNull() {
            addCriterion("device_block_end_time is null");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeIsNotNull() {
            addCriterion("device_block_end_time is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeEqualTo(Date value) {
            addCriterion("device_block_end_time =", value, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeNotEqualTo(Date value) {
            addCriterion("device_block_end_time <>", value, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeGreaterThan(Date value) {
            addCriterion("device_block_end_time >", value, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("device_block_end_time >=", value, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeLessThan(Date value) {
            addCriterion("device_block_end_time <", value, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("device_block_end_time <=", value, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeIn(List<Date> values) {
            addCriterion("device_block_end_time in", values, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeNotIn(List<Date> values) {
            addCriterion("device_block_end_time not in", values, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeBetween(Date value1, Date value2) {
            addCriterion("device_block_end_time between", value1, value2, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andDeviceBlockEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("device_block_end_time not between", value1, value2, "deviceBlockEndTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeIsNull() {
            addCriterion("sign_time is null");
            return (Criteria) this;
        }

        public Criteria andSignTimeIsNotNull() {
            addCriterion("sign_time is not null");
            return (Criteria) this;
        }

        public Criteria andSignTimeEqualTo(Date value) {
            addCriterion("sign_time =", value, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeNotEqualTo(Date value) {
            addCriterion("sign_time <>", value, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeGreaterThan(Date value) {
            addCriterion("sign_time >", value, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("sign_time >=", value, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeLessThan(Date value) {
            addCriterion("sign_time <", value, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeLessThanOrEqualTo(Date value) {
            addCriterion("sign_time <=", value, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeIn(List<Date> values) {
            addCriterion("sign_time in", values, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeNotIn(List<Date> values) {
            addCriterion("sign_time not in", values, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeBetween(Date value1, Date value2) {
            addCriterion("sign_time between", value1, value2, "signTime");
            return (Criteria) this;
        }

        public Criteria andSignTimeNotBetween(Date value1, Date value2) {
            addCriterion("sign_time not between", value1, value2, "signTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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
