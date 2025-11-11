package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppButtonMenuConfExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AppButtonMenuConfExample() {
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

        public Criteria andConfIdIsNull() {
            addCriterion("conf_id is null");
            return (Criteria) this;
        }

        public Criteria andConfIdIsNotNull() {
            addCriterion("conf_id is not null");
            return (Criteria) this;
        }

        public Criteria andConfIdEqualTo(Integer value) {
            addCriterion("conf_id =", value, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdNotEqualTo(Integer value) {
            addCriterion("conf_id <>", value, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdGreaterThan(Integer value) {
            addCriterion("conf_id >", value, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("conf_id >=", value, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdLessThan(Integer value) {
            addCriterion("conf_id <", value, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdLessThanOrEqualTo(Integer value) {
            addCriterion("conf_id <=", value, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdIn(List<Integer> values) {
            addCriterion("conf_id in", values, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdNotIn(List<Integer> values) {
            addCriterion("conf_id not in", values, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdBetween(Integer value1, Integer value2) {
            addCriterion("conf_id between", value1, value2, "confId");
            return (Criteria) this;
        }

        public Criteria andConfIdNotBetween(Integer value1, Integer value2) {
            addCriterion("conf_id not between", value1, value2, "confId");
            return (Criteria) this;
        }

        public Criteria andConfLocIsNull() {
            addCriterion("conf_loc is null");
            return (Criteria) this;
        }

        public Criteria andConfLocIsNotNull() {
            addCriterion("conf_loc is not null");
            return (Criteria) this;
        }

        public Criteria andConfLocEqualTo(Byte value) {
            addCriterion("conf_loc =", value, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocNotEqualTo(Byte value) {
            addCriterion("conf_loc <>", value, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocGreaterThan(Byte value) {
            addCriterion("conf_loc >", value, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocGreaterThanOrEqualTo(Byte value) {
            addCriterion("conf_loc >=", value, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocLessThan(Byte value) {
            addCriterion("conf_loc <", value, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocLessThanOrEqualTo(Byte value) {
            addCriterion("conf_loc <=", value, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocIn(List<Byte> values) {
            addCriterion("conf_loc in", values, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocNotIn(List<Byte> values) {
            addCriterion("conf_loc not in", values, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocBetween(Byte value1, Byte value2) {
            addCriterion("conf_loc between", value1, value2, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfLocNotBetween(Byte value1, Byte value2) {
            addCriterion("conf_loc not between", value1, value2, "confLoc");
            return (Criteria) this;
        }

        public Criteria andConfTypeIsNull() {
            addCriterion("conf_type is null");
            return (Criteria) this;
        }

        public Criteria andConfTypeIsNotNull() {
            addCriterion("conf_type is not null");
            return (Criteria) this;
        }

        public Criteria andConfTypeEqualTo(Byte value) {
            addCriterion("conf_type =", value, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeNotEqualTo(Byte value) {
            addCriterion("conf_type <>", value, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeGreaterThan(Byte value) {
            addCriterion("conf_type >", value, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("conf_type >=", value, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeLessThan(Byte value) {
            addCriterion("conf_type <", value, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeLessThanOrEqualTo(Byte value) {
            addCriterion("conf_type <=", value, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeIn(List<Byte> values) {
            addCriterion("conf_type in", values, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeNotIn(List<Byte> values) {
            addCriterion("conf_type not in", values, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeBetween(Byte value1, Byte value2) {
            addCriterion("conf_type between", value1, value2, "confType");
            return (Criteria) this;
        }

        public Criteria andConfTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("conf_type not between", value1, value2, "confType");
            return (Criteria) this;
        }

        public Criteria andConfStatusIsNull() {
            addCriterion("conf_status is null");
            return (Criteria) this;
        }

        public Criteria andConfStatusIsNotNull() {
            addCriterion("conf_status is not null");
            return (Criteria) this;
        }

        public Criteria andConfStatusEqualTo(Byte value) {
            addCriterion("conf_status =", value, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusNotEqualTo(Byte value) {
            addCriterion("conf_status <>", value, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusGreaterThan(Byte value) {
            addCriterion("conf_status >", value, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("conf_status >=", value, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusLessThan(Byte value) {
            addCriterion("conf_status <", value, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusLessThanOrEqualTo(Byte value) {
            addCriterion("conf_status <=", value, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusIn(List<Byte> values) {
            addCriterion("conf_status in", values, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusNotIn(List<Byte> values) {
            addCriterion("conf_status not in", values, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusBetween(Byte value1, Byte value2) {
            addCriterion("conf_status between", value1, value2, "confStatus");
            return (Criteria) this;
        }

        public Criteria andConfStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("conf_status not between", value1, value2, "confStatus");
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
