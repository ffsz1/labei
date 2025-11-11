package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FansExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FansExample() {
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

        public Criteria andFanIdIsNull() {
            addCriterion("fan_id is null");
            return (Criteria) this;
        }

        public Criteria andFanIdIsNotNull() {
            addCriterion("fan_id is not null");
            return (Criteria) this;
        }

        public Criteria andFanIdEqualTo(Long value) {
            addCriterion("fan_id =", value, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdNotEqualTo(Long value) {
            addCriterion("fan_id <>", value, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdGreaterThan(Long value) {
            addCriterion("fan_id >", value, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdGreaterThanOrEqualTo(Long value) {
            addCriterion("fan_id >=", value, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdLessThan(Long value) {
            addCriterion("fan_id <", value, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdLessThanOrEqualTo(Long value) {
            addCriterion("fan_id <=", value, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdIn(List<Long> values) {
            addCriterion("fan_id in", values, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdNotIn(List<Long> values) {
            addCriterion("fan_id not in", values, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdBetween(Long value1, Long value2) {
            addCriterion("fan_id between", value1, value2, "fanId");
            return (Criteria) this;
        }

        public Criteria andFanIdNotBetween(Long value1, Long value2) {
            addCriterion("fan_id not between", value1, value2, "fanId");
            return (Criteria) this;
        }

        public Criteria andLikeUidIsNull() {
            addCriterion("like_uid is null");
            return (Criteria) this;
        }

        public Criteria andLikeUidIsNotNull() {
            addCriterion("like_uid is not null");
            return (Criteria) this;
        }

        public Criteria andLikeUidEqualTo(Long value) {
            addCriterion("like_uid =", value, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidNotEqualTo(Long value) {
            addCriterion("like_uid <>", value, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidGreaterThan(Long value) {
            addCriterion("like_uid >", value, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidGreaterThanOrEqualTo(Long value) {
            addCriterion("like_uid >=", value, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidLessThan(Long value) {
            addCriterion("like_uid <", value, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidLessThanOrEqualTo(Long value) {
            addCriterion("like_uid <=", value, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidIn(List<Long> values) {
            addCriterion("like_uid in", values, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidNotIn(List<Long> values) {
            addCriterion("like_uid not in", values, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidBetween(Long value1, Long value2) {
            addCriterion("like_uid between", value1, value2, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikeUidNotBetween(Long value1, Long value2) {
            addCriterion("like_uid not between", value1, value2, "likeUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidIsNull() {
            addCriterion("liked_uid is null");
            return (Criteria) this;
        }

        public Criteria andLikedUidIsNotNull() {
            addCriterion("liked_uid is not null");
            return (Criteria) this;
        }

        public Criteria andLikedUidEqualTo(Long value) {
            addCriterion("liked_uid =", value, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidNotEqualTo(Long value) {
            addCriterion("liked_uid <>", value, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidGreaterThan(Long value) {
            addCriterion("liked_uid >", value, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidGreaterThanOrEqualTo(Long value) {
            addCriterion("liked_uid >=", value, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidLessThan(Long value) {
            addCriterion("liked_uid <", value, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidLessThanOrEqualTo(Long value) {
            addCriterion("liked_uid <=", value, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidIn(List<Long> values) {
            addCriterion("liked_uid in", values, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidNotIn(List<Long> values) {
            addCriterion("liked_uid not in", values, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidBetween(Long value1, Long value2) {
            addCriterion("liked_uid between", value1, value2, "likedUid");
            return (Criteria) this;
        }

        public Criteria andLikedUidNotBetween(Long value1, Long value2) {
            addCriterion("liked_uid not between", value1, value2, "likedUid");
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
