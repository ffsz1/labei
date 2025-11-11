package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomHotExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RoomHotExample() {
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

        public Criteria andRoomSeqIsNull() {
            addCriterion("room_seq is null");
            return (Criteria) this;
        }

        public Criteria andRoomSeqIsNotNull() {
            addCriterion("room_seq is not null");
            return (Criteria) this;
        }

        public Criteria andRoomSeqEqualTo(Integer value) {
            addCriterion("room_seq =", value, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqNotEqualTo(Integer value) {
            addCriterion("room_seq <>", value, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqGreaterThan(Integer value) {
            addCriterion("room_seq >", value, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqGreaterThanOrEqualTo(Integer value) {
            addCriterion("room_seq >=", value, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqLessThan(Integer value) {
            addCriterion("room_seq <", value, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqLessThanOrEqualTo(Integer value) {
            addCriterion("room_seq <=", value, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqIn(List<Integer> values) {
            addCriterion("room_seq in", values, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqNotIn(List<Integer> values) {
            addCriterion("room_seq not in", values, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqBetween(Integer value1, Integer value2) {
            addCriterion("room_seq between", value1, value2, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andRoomSeqNotBetween(Integer value1, Integer value2) {
            addCriterion("room_seq not between", value1, value2, "roomSeq");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeIsNull() {
            addCriterion("start_live_time is null");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeIsNotNull() {
            addCriterion("start_live_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeEqualTo(Date value) {
            addCriterion("start_live_time =", value, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeNotEqualTo(Date value) {
            addCriterion("start_live_time <>", value, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeGreaterThan(Date value) {
            addCriterion("start_live_time >", value, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_live_time >=", value, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeLessThan(Date value) {
            addCriterion("start_live_time <", value, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_live_time <=", value, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeIn(List<Date> values) {
            addCriterion("start_live_time in", values, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeNotIn(List<Date> values) {
            addCriterion("start_live_time not in", values, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeBetween(Date value1, Date value2) {
            addCriterion("start_live_time between", value1, value2, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andStartLiveTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_live_time not between", value1, value2, "startLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeIsNull() {
            addCriterion("end_live_time is null");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeIsNotNull() {
            addCriterion("end_live_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeEqualTo(Date value) {
            addCriterion("end_live_time =", value, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeNotEqualTo(Date value) {
            addCriterion("end_live_time <>", value, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeGreaterThan(Date value) {
            addCriterion("end_live_time >", value, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_live_time >=", value, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeLessThan(Date value) {
            addCriterion("end_live_time <", value, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_live_time <=", value, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeIn(List<Date> values) {
            addCriterion("end_live_time in", values, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeNotIn(List<Date> values) {
            addCriterion("end_live_time not in", values, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeBetween(Date value1, Date value2) {
            addCriterion("end_live_time between", value1, value2, "endLiveTime");
            return (Criteria) this;
        }

        public Criteria andEndLiveTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_live_time not between", value1, value2, "endLiveTime");
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
