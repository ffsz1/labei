package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class RoomRcmdPoolExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RoomRcmdPoolExample() {
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

        public Criteria andRcmdIdEqualTo(Integer value) {
            addCriterion("rcmd_id =", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdNotEqualTo(Integer value) {
            addCriterion("rcmd_id <>", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdGreaterThan(Integer value) {
            addCriterion("rcmd_id >", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("rcmd_id >=", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdLessThan(Integer value) {
            addCriterion("rcmd_id <", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdLessThanOrEqualTo(Integer value) {
            addCriterion("rcmd_id <=", value, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdIn(List<Integer> values) {
            addCriterion("rcmd_id in", values, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdNotIn(List<Integer> values) {
            addCriterion("rcmd_id not in", values, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdBetween(Integer value1, Integer value2) {
            addCriterion("rcmd_id between", value1, value2, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRcmdIdNotBetween(Integer value1, Integer value2) {
            addCriterion("rcmd_id not between", value1, value2, "rcmdId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdIsNull() {
            addCriterion("room_fk_id is null");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdIsNotNull() {
            addCriterion("room_fk_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdEqualTo(Long value) {
            addCriterion("room_fk_id =", value, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdNotEqualTo(Long value) {
            addCriterion("room_fk_id <>", value, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdGreaterThan(Long value) {
            addCriterion("room_fk_id >", value, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdGreaterThanOrEqualTo(Long value) {
            addCriterion("room_fk_id >=", value, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdLessThan(Long value) {
            addCriterion("room_fk_id <", value, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdLessThanOrEqualTo(Long value) {
            addCriterion("room_fk_id <=", value, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdIn(List<Long> values) {
            addCriterion("room_fk_id in", values, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdNotIn(List<Long> values) {
            addCriterion("room_fk_id not in", values, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdBetween(Long value1, Long value2) {
            addCriterion("room_fk_id between", value1, value2, "roomFkId");
            return (Criteria) this;
        }

        public Criteria andRoomFkIdNotBetween(Long value1, Long value2) {
            addCriterion("room_fk_id not between", value1, value2, "roomFkId");
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
