package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class StatSumRoomExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatSumRoomExample() {
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

        public Criteria andRoomUidIsNull() {
            addCriterion("room_uid is null");
            return (Criteria) this;
        }

        public Criteria andRoomUidIsNotNull() {
            addCriterion("room_uid is not null");
            return (Criteria) this;
        }

        public Criteria andRoomUidEqualTo(Long value) {
            addCriterion("room_uid =", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotEqualTo(Long value) {
            addCriterion("room_uid <>", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidGreaterThan(Long value) {
            addCriterion("room_uid >", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidGreaterThanOrEqualTo(Long value) {
            addCriterion("room_uid >=", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidLessThan(Long value) {
            addCriterion("room_uid <", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidLessThanOrEqualTo(Long value) {
            addCriterion("room_uid <=", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidIn(List<Long> values) {
            addCriterion("room_uid in", values, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotIn(List<Long> values) {
            addCriterion("room_uid not in", values, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidBetween(Long value1, Long value2) {
            addCriterion("room_uid between", value1, value2, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotBetween(Long value1, Long value2) {
            addCriterion("room_uid not between", value1, value2, "roomUid");
            return (Criteria) this;
        }

        public Criteria andMoodsIsNull() {
            addCriterion("moods is null");
            return (Criteria) this;
        }

        public Criteria andMoodsIsNotNull() {
            addCriterion("moods is not null");
            return (Criteria) this;
        }

        public Criteria andMoodsEqualTo(Long value) {
            addCriterion("moods =", value, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsNotEqualTo(Long value) {
            addCriterion("moods <>", value, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsGreaterThan(Long value) {
            addCriterion("moods >", value, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsGreaterThanOrEqualTo(Long value) {
            addCriterion("moods >=", value, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsLessThan(Long value) {
            addCriterion("moods <", value, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsLessThanOrEqualTo(Long value) {
            addCriterion("moods <=", value, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsIn(List<Long> values) {
            addCriterion("moods in", values, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsNotIn(List<Long> values) {
            addCriterion("moods not in", values, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsBetween(Long value1, Long value2) {
            addCriterion("moods between", value1, value2, "moods");
            return (Criteria) this;
        }

        public Criteria andMoodsNotBetween(Long value1, Long value2) {
            addCriterion("moods not between", value1, value2, "moods");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesIsNull() {
            addCriterion("room_into_peoples is null");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesIsNotNull() {
            addCriterion("room_into_peoples is not null");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesEqualTo(Long value) {
            addCriterion("room_into_peoples =", value, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesNotEqualTo(Long value) {
            addCriterion("room_into_peoples <>", value, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesGreaterThan(Long value) {
            addCriterion("room_into_peoples >", value, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesGreaterThanOrEqualTo(Long value) {
            addCriterion("room_into_peoples >=", value, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesLessThan(Long value) {
            addCriterion("room_into_peoples <", value, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesLessThanOrEqualTo(Long value) {
            addCriterion("room_into_peoples <=", value, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesIn(List<Long> values) {
            addCriterion("room_into_peoples in", values, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesNotIn(List<Long> values) {
            addCriterion("room_into_peoples not in", values, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesBetween(Long value1, Long value2) {
            addCriterion("room_into_peoples between", value1, value2, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andRoomIntoPeoplesNotBetween(Long value1, Long value2) {
            addCriterion("room_into_peoples not between", value1, value2, "roomIntoPeoples");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeIsNull() {
            addCriterion("sum_live_time is null");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeIsNotNull() {
            addCriterion("sum_live_time is not null");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeEqualTo(Long value) {
            addCriterion("sum_live_time =", value, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeNotEqualTo(Long value) {
            addCriterion("sum_live_time <>", value, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeGreaterThan(Long value) {
            addCriterion("sum_live_time >", value, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeGreaterThanOrEqualTo(Long value) {
            addCriterion("sum_live_time >=", value, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeLessThan(Long value) {
            addCriterion("sum_live_time <", value, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeLessThanOrEqualTo(Long value) {
            addCriterion("sum_live_time <=", value, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeIn(List<Long> values) {
            addCriterion("sum_live_time in", values, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeNotIn(List<Long> values) {
            addCriterion("sum_live_time not in", values, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeBetween(Long value1, Long value2) {
            addCriterion("sum_live_time between", value1, value2, "sumLiveTime");
            return (Criteria) this;
        }

        public Criteria andSumLiveTimeNotBetween(Long value1, Long value2) {
            addCriterion("sum_live_time not between", value1, value2, "sumLiveTime");
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
