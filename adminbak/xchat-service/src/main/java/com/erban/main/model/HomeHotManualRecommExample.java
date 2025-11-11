package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeHotManualRecommExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public HomeHotManualRecommExample() {
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

        public Criteria andRecommIdIsNull() {
            addCriterion("recomm_id is null");
            return (Criteria) this;
        }

        public Criteria andRecommIdIsNotNull() {
            addCriterion("recomm_id is not null");
            return (Criteria) this;
        }

        public Criteria andRecommIdEqualTo(Integer value) {
            addCriterion("recomm_id =", value, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdNotEqualTo(Integer value) {
            addCriterion("recomm_id <>", value, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdGreaterThan(Integer value) {
            addCriterion("recomm_id >", value, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("recomm_id >=", value, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdLessThan(Integer value) {
            addCriterion("recomm_id <", value, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdLessThanOrEqualTo(Integer value) {
            addCriterion("recomm_id <=", value, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdIn(List<Integer> values) {
            addCriterion("recomm_id in", values, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdNotIn(List<Integer> values) {
            addCriterion("recomm_id not in", values, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdBetween(Integer value1, Integer value2) {
            addCriterion("recomm_id between", value1, value2, "recommId");
            return (Criteria) this;
        }

        public Criteria andRecommIdNotBetween(Integer value1, Integer value2) {
            addCriterion("recomm_id not between", value1, value2, "recommId");
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

        public Criteria andSeqNoIsNull() {
            addCriterion("seq_no is null");
            return (Criteria) this;
        }

        public Criteria andSeqNoIsNotNull() {
            addCriterion("seq_no is not null");
            return (Criteria) this;
        }

        public Criteria andSeqNoEqualTo(Integer value) {
            addCriterion("seq_no =", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotEqualTo(Integer value) {
            addCriterion("seq_no <>", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThan(Integer value) {
            addCriterion("seq_no >", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("seq_no >=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThan(Integer value) {
            addCriterion("seq_no <", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThanOrEqualTo(Integer value) {
            addCriterion("seq_no <=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoIn(List<Integer> values) {
            addCriterion("seq_no in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotIn(List<Integer> values) {
            addCriterion("seq_no not in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoBetween(Integer value1, Integer value2) {
            addCriterion("seq_no between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotBetween(Integer value1, Integer value2) {
            addCriterion("seq_no not between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIsNull() {
            addCriterion("start_valid_time is null");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIsNotNull() {
            addCriterion("start_valid_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeEqualTo(Date value) {
            addCriterion("start_valid_time =", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotEqualTo(Date value) {
            addCriterion("start_valid_time <>", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeGreaterThan(Date value) {
            addCriterion("start_valid_time >", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_valid_time >=", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeLessThan(Date value) {
            addCriterion("start_valid_time <", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_valid_time <=", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIn(List<Date> values) {
            addCriterion("start_valid_time in", values, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotIn(List<Date> values) {
            addCriterion("start_valid_time not in", values, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeBetween(Date value1, Date value2) {
            addCriterion("start_valid_time between", value1, value2, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_valid_time not between", value1, value2, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIsNull() {
            addCriterion("end_valid_time is null");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIsNotNull() {
            addCriterion("end_valid_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeEqualTo(Date value) {
            addCriterion("end_valid_time =", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotEqualTo(Date value) {
            addCriterion("end_valid_time <>", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeGreaterThan(Date value) {
            addCriterion("end_valid_time >", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_valid_time >=", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeLessThan(Date value) {
            addCriterion("end_valid_time <", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_valid_time <=", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIn(List<Date> values) {
            addCriterion("end_valid_time in", values, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotIn(List<Date> values) {
            addCriterion("end_valid_time not in", values, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeBetween(Date value1, Date value2) {
            addCriterion("end_valid_time between", value1, value2, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_valid_time not between", value1, value2, "endValidTime");
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

        public Criteria andViewTypeIsNull() {
            addCriterion("view_type is null");
            return (Criteria) this;
        }

        public Criteria andViewTypeIsNotNull() {
            addCriterion("view_type is not null");
            return (Criteria) this;
        }

        public Criteria andViewTypeEqualTo(Byte value) {
            addCriterion("view_type =", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeNotEqualTo(Byte value) {
            addCriterion("view_type <>", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeGreaterThan(Byte value) {
            addCriterion("view_type >", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("view_type >=", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeLessThan(Byte value) {
            addCriterion("view_type <", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeLessThanOrEqualTo(Byte value) {
            addCriterion("view_type <=", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeIn(List<Byte> values) {
            addCriterion("view_type in", values, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeNotIn(List<Byte> values) {
            addCriterion("view_type not in", values, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeBetween(Byte value1, Byte value2) {
            addCriterion("view_type between", value1, value2, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("view_type not between", value1, value2, "viewType");
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
