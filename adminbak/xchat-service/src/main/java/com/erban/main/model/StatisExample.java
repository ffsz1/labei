package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatisExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatisExample() {
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

        public Criteria andStatisIdIsNull() {
            addCriterion("statis_id is null");
            return (Criteria) this;
        }

        public Criteria andStatisIdIsNotNull() {
            addCriterion("statis_id is not null");
            return (Criteria) this;
        }

        public Criteria andStatisIdEqualTo(String value) {
            addCriterion("statis_id =", value, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdNotEqualTo(String value) {
            addCriterion("statis_id <>", value, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdGreaterThan(String value) {
            addCriterion("statis_id >", value, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdGreaterThanOrEqualTo(String value) {
            addCriterion("statis_id >=", value, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdLessThan(String value) {
            addCriterion("statis_id <", value, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdLessThanOrEqualTo(String value) {
            addCriterion("statis_id <=", value, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdLike(String value) {
            addCriterion("statis_id like", value, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdNotLike(String value) {
            addCriterion("statis_id not like", value, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdIn(List<String> values) {
            addCriterion("statis_id in", values, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdNotIn(List<String> values) {
            addCriterion("statis_id not in", values, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdBetween(String value1, String value2) {
            addCriterion("statis_id between", value1, value2, "statisId");
            return (Criteria) this;
        }

        public Criteria andStatisIdNotBetween(String value1, String value2) {
            addCriterion("statis_id not between", value1, value2, "statisId");
            return (Criteria) this;
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

        public Criteria andFromLoginUrlIsNull() {
            addCriterion("from_login_url is null");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlIsNotNull() {
            addCriterion("from_login_url is not null");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlEqualTo(String value) {
            addCriterion("from_login_url =", value, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlNotEqualTo(String value) {
            addCriterion("from_login_url <>", value, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlGreaterThan(String value) {
            addCriterion("from_login_url >", value, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlGreaterThanOrEqualTo(String value) {
            addCriterion("from_login_url >=", value, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlLessThan(String value) {
            addCriterion("from_login_url <", value, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlLessThanOrEqualTo(String value) {
            addCriterion("from_login_url <=", value, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlLike(String value) {
            addCriterion("from_login_url like", value, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlNotLike(String value) {
            addCriterion("from_login_url not like", value, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlIn(List<String> values) {
            addCriterion("from_login_url in", values, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlNotIn(List<String> values) {
            addCriterion("from_login_url not in", values, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlBetween(String value1, String value2) {
            addCriterion("from_login_url between", value1, value2, "fromLoginUrl");
            return (Criteria) this;
        }

        public Criteria andFromLoginUrlNotBetween(String value1, String value2) {
            addCriterion("from_login_url not between", value1, value2, "fromLoginUrl");
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
