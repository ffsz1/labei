package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RedeemCodeExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RedeemCodeExample() {
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

        public Criteria andCodeIsNull() {
            addCriterion("code is null");
            return (Criteria) this;
        }

        public Criteria andCodeIsNotNull() {
            addCriterion("code is not null");
            return (Criteria) this;
        }

        public Criteria andCodeEqualTo(String value) {
            addCriterion("code =", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotEqualTo(String value) {
            addCriterion("code <>", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThan(String value) {
            addCriterion("code >", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeGreaterThanOrEqualTo(String value) {
            addCriterion("code >=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThan(String value) {
            addCriterion("code <", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLessThanOrEqualTo(String value) {
            addCriterion("code <=", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeLike(String value) {
            addCriterion("code like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotLike(String value) {
            addCriterion("code not like", value, "code");
            return (Criteria) this;
        }

        public Criteria andCodeIn(List<String> values) {
            addCriterion("code in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotIn(List<String> values) {
            addCriterion("code not in", values, "code");
            return (Criteria) this;
        }

        public Criteria andCodeBetween(String value1, String value2) {
            addCriterion("code between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andCodeNotBetween(String value1, String value2) {
            addCriterion("code not between", value1, value2, "code");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Long value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Long value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Long value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Long value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Long value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Long> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Long> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Long value1, Long value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Long value1, Long value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andUseUidIsNull() {
            addCriterion("use_uid is null");
            return (Criteria) this;
        }

        public Criteria andUseUidIsNotNull() {
            addCriterion("use_uid is not null");
            return (Criteria) this;
        }

        public Criteria andUseUidEqualTo(Long value) {
            addCriterion("use_uid =", value, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidNotEqualTo(Long value) {
            addCriterion("use_uid <>", value, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidGreaterThan(Long value) {
            addCriterion("use_uid >", value, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidGreaterThanOrEqualTo(Long value) {
            addCriterion("use_uid >=", value, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidLessThan(Long value) {
            addCriterion("use_uid <", value, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidLessThanOrEqualTo(Long value) {
            addCriterion("use_uid <=", value, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidIn(List<Long> values) {
            addCriterion("use_uid in", values, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidNotIn(List<Long> values) {
            addCriterion("use_uid not in", values, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidBetween(Long value1, Long value2) {
            addCriterion("use_uid between", value1, value2, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseUidNotBetween(Long value1, Long value2) {
            addCriterion("use_uid not between", value1, value2, "useUid");
            return (Criteria) this;
        }

        public Criteria andUseIpIsNull() {
            addCriterion("use_ip is null");
            return (Criteria) this;
        }

        public Criteria andUseIpIsNotNull() {
            addCriterion("use_ip is not null");
            return (Criteria) this;
        }

        public Criteria andUseIpEqualTo(String value) {
            addCriterion("use_ip =", value, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpNotEqualTo(String value) {
            addCriterion("use_ip <>", value, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpGreaterThan(String value) {
            addCriterion("use_ip >", value, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpGreaterThanOrEqualTo(String value) {
            addCriterion("use_ip >=", value, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpLessThan(String value) {
            addCriterion("use_ip <", value, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpLessThanOrEqualTo(String value) {
            addCriterion("use_ip <=", value, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpLike(String value) {
            addCriterion("use_ip like", value, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpNotLike(String value) {
            addCriterion("use_ip not like", value, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpIn(List<String> values) {
            addCriterion("use_ip in", values, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpNotIn(List<String> values) {
            addCriterion("use_ip not in", values, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpBetween(String value1, String value2) {
            addCriterion("use_ip between", value1, value2, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseIpNotBetween(String value1, String value2) {
            addCriterion("use_ip not between", value1, value2, "useIp");
            return (Criteria) this;
        }

        public Criteria andUseImeiIsNull() {
            addCriterion("use_imei is null");
            return (Criteria) this;
        }

        public Criteria andUseImeiIsNotNull() {
            addCriterion("use_imei is not null");
            return (Criteria) this;
        }

        public Criteria andUseImeiEqualTo(String value) {
            addCriterion("use_imei =", value, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiNotEqualTo(String value) {
            addCriterion("use_imei <>", value, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiGreaterThan(String value) {
            addCriterion("use_imei >", value, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiGreaterThanOrEqualTo(String value) {
            addCriterion("use_imei >=", value, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiLessThan(String value) {
            addCriterion("use_imei <", value, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiLessThanOrEqualTo(String value) {
            addCriterion("use_imei <=", value, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiLike(String value) {
            addCriterion("use_imei like", value, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiNotLike(String value) {
            addCriterion("use_imei not like", value, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiIn(List<String> values) {
            addCriterion("use_imei in", values, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiNotIn(List<String> values) {
            addCriterion("use_imei not in", values, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiBetween(String value1, String value2) {
            addCriterion("use_imei between", value1, value2, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseImeiNotBetween(String value1, String value2) {
            addCriterion("use_imei not between", value1, value2, "useImei");
            return (Criteria) this;
        }

        public Criteria andUseStatusIsNull() {
            addCriterion("use_status is null");
            return (Criteria) this;
        }

        public Criteria andUseStatusIsNotNull() {
            addCriterion("use_status is not null");
            return (Criteria) this;
        }

        public Criteria andUseStatusEqualTo(Integer value) {
            addCriterion("use_status =", value, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusNotEqualTo(Integer value) {
            addCriterion("use_status <>", value, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusGreaterThan(Integer value) {
            addCriterion("use_status >", value, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusGreaterThanOrEqualTo(Integer value) {
            addCriterion("use_status >=", value, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusLessThan(Integer value) {
            addCriterion("use_status <", value, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusLessThanOrEqualTo(Integer value) {
            addCriterion("use_status <=", value, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusIn(List<Integer> values) {
            addCriterion("use_status in", values, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusNotIn(List<Integer> values) {
            addCriterion("use_status not in", values, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusBetween(Integer value1, Integer value2) {
            addCriterion("use_status between", value1, value2, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseStatusNotBetween(Integer value1, Integer value2) {
            addCriterion("use_status not between", value1, value2, "useStatus");
            return (Criteria) this;
        }

        public Criteria andUseTimeIsNull() {
            addCriterion("use_time is null");
            return (Criteria) this;
        }

        public Criteria andUseTimeIsNotNull() {
            addCriterion("use_time is not null");
            return (Criteria) this;
        }

        public Criteria andUseTimeEqualTo(Date value) {
            addCriterion("use_time =", value, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeNotEqualTo(Date value) {
            addCriterion("use_time <>", value, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeGreaterThan(Date value) {
            addCriterion("use_time >", value, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("use_time >=", value, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeLessThan(Date value) {
            addCriterion("use_time <", value, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeLessThanOrEqualTo(Date value) {
            addCriterion("use_time <=", value, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeIn(List<Date> values) {
            addCriterion("use_time in", values, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeNotIn(List<Date> values) {
            addCriterion("use_time not in", values, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeBetween(Date value1, Date value2) {
            addCriterion("use_time between", value1, value2, "useTime");
            return (Criteria) this;
        }

        public Criteria andUseTimeNotBetween(Date value1, Date value2) {
            addCriterion("use_time not between", value1, value2, "useTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNull() {
            addCriterion("create_time is null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIsNotNull() {
            addCriterion("create_time is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTimeEqualTo(Date value) {
            addCriterion("create_time =", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotEqualTo(Date value) {
            addCriterion("create_time <>", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThan(Date value) {
            addCriterion("create_time >", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("create_time >=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThan(Date value) {
            addCriterion("create_time <", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeLessThanOrEqualTo(Date value) {
            addCriterion("create_time <=", value, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeIn(List<Date> values) {
            addCriterion("create_time in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotIn(List<Date> values) {
            addCriterion("create_time not in", values, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeBetween(Date value1, Date value2) {
            addCriterion("create_time between", value1, value2, "createTime");
            return (Criteria) this;
        }

        public Criteria andCreateTimeNotBetween(Date value1, Date value2) {
            addCriterion("create_time not between", value1, value2, "createTime");
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
