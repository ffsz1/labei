package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NobleRecomRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NobleRecomRecordExample() {
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

        public Criteria andNobleIdIsNull() {
            addCriterion("noble_id is null");
            return (Criteria) this;
        }

        public Criteria andNobleIdIsNotNull() {
            addCriterion("noble_id is not null");
            return (Criteria) this;
        }

        public Criteria andNobleIdEqualTo(Integer value) {
            addCriterion("noble_id =", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotEqualTo(Integer value) {
            addCriterion("noble_id <>", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdGreaterThan(Integer value) {
            addCriterion("noble_id >", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("noble_id >=", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdLessThan(Integer value) {
            addCriterion("noble_id <", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdLessThanOrEqualTo(Integer value) {
            addCriterion("noble_id <=", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdIn(List<Integer> values) {
            addCriterion("noble_id in", values, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotIn(List<Integer> values) {
            addCriterion("noble_id not in", values, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdBetween(Integer value1, Integer value2) {
            addCriterion("noble_id between", value1, value2, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("noble_id not between", value1, value2, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleNameIsNull() {
            addCriterion("noble_name is null");
            return (Criteria) this;
        }

        public Criteria andNobleNameIsNotNull() {
            addCriterion("noble_name is not null");
            return (Criteria) this;
        }

        public Criteria andNobleNameEqualTo(String value) {
            addCriterion("noble_name =", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotEqualTo(String value) {
            addCriterion("noble_name <>", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameGreaterThan(String value) {
            addCriterion("noble_name >", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameGreaterThanOrEqualTo(String value) {
            addCriterion("noble_name >=", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLessThan(String value) {
            addCriterion("noble_name <", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLessThanOrEqualTo(String value) {
            addCriterion("noble_name <=", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLike(String value) {
            addCriterion("noble_name like", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotLike(String value) {
            addCriterion("noble_name not like", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameIn(List<String> values) {
            addCriterion("noble_name in", values, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotIn(List<String> values) {
            addCriterion("noble_name not in", values, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameBetween(String value1, String value2) {
            addCriterion("noble_name between", value1, value2, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotBetween(String value1, String value2) {
            addCriterion("noble_name not between", value1, value2, "nobleName");
            return (Criteria) this;
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

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andHasNoticeIsNull() {
            addCriterion("has_notice is null");
            return (Criteria) this;
        }

        public Criteria andHasNoticeIsNotNull() {
            addCriterion("has_notice is not null");
            return (Criteria) this;
        }

        public Criteria andHasNoticeEqualTo(Byte value) {
            addCriterion("has_notice =", value, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeNotEqualTo(Byte value) {
            addCriterion("has_notice <>", value, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeGreaterThan(Byte value) {
            addCriterion("has_notice >", value, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeGreaterThanOrEqualTo(Byte value) {
            addCriterion("has_notice >=", value, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeLessThan(Byte value) {
            addCriterion("has_notice <", value, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeLessThanOrEqualTo(Byte value) {
            addCriterion("has_notice <=", value, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeIn(List<Byte> values) {
            addCriterion("has_notice in", values, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeNotIn(List<Byte> values) {
            addCriterion("has_notice not in", values, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeBetween(Byte value1, Byte value2) {
            addCriterion("has_notice between", value1, value2, "hasNotice");
            return (Criteria) this;
        }

        public Criteria andHasNoticeNotBetween(Byte value1, Byte value2) {
            addCriterion("has_notice not between", value1, value2, "hasNotice");
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
