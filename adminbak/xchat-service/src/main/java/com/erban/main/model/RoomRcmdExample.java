package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomRcmdExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RoomRcmdExample() {
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

        public Criteria andRcmdIdIsNull() {
            addCriterion("rcmd_id is null");
            return (Criteria) this;
        }

        public Criteria andRcmdIdIsNotNull() {
            addCriterion("rcmd_id is not null");
            return (Criteria) this;
        }

        public Criteria andRcmdIdEqualTo(Long value) {
            addCriterion("rcmd_id =", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdNotEqualTo(Long value) {
            addCriterion("rcmd_id <>", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdGreaterThan(Long value) {
            addCriterion("rcmd_id >", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdGreaterThanOrEqualTo(Long value) {
            addCriterion("rcmd_id >=", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdLessThan(Long value) {
            addCriterion("rcmd_id <", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdLessThanOrEqualTo(Long value) {
            addCriterion("rcmd_id <=", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdIn(List<Long> values) {
            addCriterion("rcmd_id in", values, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdNotIn(List<Long> values) {
            addCriterion("rcmd_id not in", values, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdBetween(Long value1, Long value2) {
            addCriterion("rcmd_id between", value1, value2, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdNotBetween(Long value1, Long value2) {
            addCriterion("rcmd_id not between", value1, value2, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andMinOnlineIsNull() {
            addCriterion("min_online is null");
            return (Criteria) this;
        }

        public Criteria andMinOnlineIsNotNull() {
            addCriterion("min_online is not null");
            return (Criteria) this;
        }

        public Criteria andMinOnlineEqualTo(Integer value) {
            addCriterion("min_online =", value, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineNotEqualTo(Integer value) {
            addCriterion("min_online <>", value, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineGreaterThan(Integer value) {
            addCriterion("min_online >", value, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineGreaterThanOrEqualTo(Integer value) {
            addCriterion("min_online >=", value, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineLessThan(Integer value) {
            addCriterion("min_online <", value, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineLessThanOrEqualTo(Integer value) {
            addCriterion("min_online <=", value, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineIn(List<Integer> values) {
            addCriterion("min_online in", values, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineNotIn(List<Integer> values) {
            addCriterion("min_online not in", values, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineBetween(Integer value1, Integer value2) {
            addCriterion("min_online between", value1, value2, "minOnline");
            return (Criteria) this;
        }

        public Criteria andMinOnlineNotBetween(Integer value1, Integer value2) {
            addCriterion("min_online not between", value1, value2, "minOnline");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeIsNull() {
            addCriterion("rcmd_type is null");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeIsNotNull() {
            addCriterion("rcmd_type is not null");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeEqualTo(Boolean value) {
            addCriterion("rcmd_type =", value, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeNotEqualTo(Boolean value) {
            addCriterion("rcmd_type <>", value, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeGreaterThan(Boolean value) {
            addCriterion("rcmd_type >", value, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("rcmd_type >=", value, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeLessThan(Boolean value) {
            addCriterion("rcmd_type <", value, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeLessThanOrEqualTo(Boolean value) {
            addCriterion("rcmd_type <=", value, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeIn(List<Boolean> values) {
            addCriterion("rcmd_type in", values, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeNotIn(List<Boolean> values) {
            addCriterion("rcmd_type not in", values, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeBetween(Boolean value1, Boolean value2) {
            addCriterion("rcmd_type between", value1, value2, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andRcmdTypeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("rcmd_type not between", value1, value2, "rcmdType");
            return (Criteria) this;
        }

        public Criteria andStartDateIsNull() {
            addCriterion("start_date is null");
            return (Criteria) this;
        }

        public Criteria andStartDateIsNotNull() {
            addCriterion("start_date is not null");
            return (Criteria) this;
        }

        public Criteria andStartDateEqualTo(Date value) {
            addCriterion("start_date =", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotEqualTo(Date value) {
            addCriterion("start_date <>", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateGreaterThan(Date value) {
            addCriterion("start_date >", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateGreaterThanOrEqualTo(Date value) {
            addCriterion("start_date >=", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLessThan(Date value) {
            addCriterion("start_date <", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateLessThanOrEqualTo(Date value) {
            addCriterion("start_date <=", value, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateIn(List<Date> values) {
            addCriterion("start_date in", values, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotIn(List<Date> values) {
            addCriterion("start_date not in", values, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateBetween(Date value1, Date value2) {
            addCriterion("start_date between", value1, value2, "startDate");
            return (Criteria) this;
        }

        public Criteria andStartDateNotBetween(Date value1, Date value2) {
            addCriterion("start_date not between", value1, value2, "startDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNull() {
            addCriterion("end_date is null");
            return (Criteria) this;
        }

        public Criteria andEndDateIsNotNull() {
            addCriterion("end_date is not null");
            return (Criteria) this;
        }

        public Criteria andEndDateEqualTo(Date value) {
            addCriterion("end_date =", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotEqualTo(Date value) {
            addCriterion("end_date <>", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThan(Date value) {
            addCriterion("end_date >", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateGreaterThanOrEqualTo(Date value) {
            addCriterion("end_date >=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThan(Date value) {
            addCriterion("end_date <", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateLessThanOrEqualTo(Date value) {
            addCriterion("end_date <=", value, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateIn(List<Date> values) {
            addCriterion("end_date in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotIn(List<Date> values) {
            addCriterion("end_date not in", values, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateBetween(Date value1, Date value2) {
            addCriterion("end_date between", value1, value2, "endDate");
            return (Criteria) this;
        }

        public Criteria andEndDateNotBetween(Date value1, Date value2) {
            addCriterion("end_date not between", value1, value2, "endDate");
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
