package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserPacketExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserPacketExample() {
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

        public Criteria andPacketNumIsNull() {
            addCriterion("packet_num is null");
            return (Criteria) this;
        }

        public Criteria andPacketNumIsNotNull() {
            addCriterion("packet_num is not null");
            return (Criteria) this;
        }

        public Criteria andPacketNumEqualTo(Double value) {
            addCriterion("packet_num =", value, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumNotEqualTo(Double value) {
            addCriterion("packet_num <>", value, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumGreaterThan(Double value) {
            addCriterion("packet_num >", value, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumGreaterThanOrEqualTo(Double value) {
            addCriterion("packet_num >=", value, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumLessThan(Double value) {
            addCriterion("packet_num <", value, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumLessThanOrEqualTo(Double value) {
            addCriterion("packet_num <=", value, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumIn(List<Double> values) {
            addCriterion("packet_num in", values, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumNotIn(List<Double> values) {
            addCriterion("packet_num not in", values, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumBetween(Double value1, Double value2) {
            addCriterion("packet_num between", value1, value2, "packetNum");
            return (Criteria) this;
        }

        public Criteria andPacketNumNotBetween(Double value1, Double value2) {
            addCriterion("packet_num not between", value1, value2, "packetNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumIsNull() {
            addCriterion("hist_packet_num is null");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumIsNotNull() {
            addCriterion("hist_packet_num is not null");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumEqualTo(Double value) {
            addCriterion("hist_packet_num =", value, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumNotEqualTo(Double value) {
            addCriterion("hist_packet_num <>", value, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumGreaterThan(Double value) {
            addCriterion("hist_packet_num >", value, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumGreaterThanOrEqualTo(Double value) {
            addCriterion("hist_packet_num >=", value, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumLessThan(Double value) {
            addCriterion("hist_packet_num <", value, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumLessThanOrEqualTo(Double value) {
            addCriterion("hist_packet_num <=", value, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumIn(List<Double> values) {
            addCriterion("hist_packet_num in", values, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumNotIn(List<Double> values) {
            addCriterion("hist_packet_num not in", values, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumBetween(Double value1, Double value2) {
            addCriterion("hist_packet_num between", value1, value2, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andHistPacketNumNotBetween(Double value1, Double value2) {
            addCriterion("hist_packet_num not between", value1, value2, "histPacketNum");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeIsNull() {
            addCriterion("first_get_time is null");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeIsNotNull() {
            addCriterion("first_get_time is not null");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeEqualTo(Date value) {
            addCriterion("first_get_time =", value, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeNotEqualTo(Date value) {
            addCriterion("first_get_time <>", value, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeGreaterThan(Date value) {
            addCriterion("first_get_time >", value, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("first_get_time >=", value, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeLessThan(Date value) {
            addCriterion("first_get_time <", value, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeLessThanOrEqualTo(Date value) {
            addCriterion("first_get_time <=", value, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeIn(List<Date> values) {
            addCriterion("first_get_time in", values, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeNotIn(List<Date> values) {
            addCriterion("first_get_time not in", values, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeBetween(Date value1, Date value2) {
            addCriterion("first_get_time between", value1, value2, "firstGetTime");
            return (Criteria) this;
        }

        public Criteria andFirstGetTimeNotBetween(Date value1, Date value2) {
            addCriterion("first_get_time not between", value1, value2, "firstGetTime");
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

        public Criteria andUpdateTimeIsNull() {
            addCriterion("update_time is null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIsNotNull() {
            addCriterion("update_time is not null");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeEqualTo(Date value) {
            addCriterion("update_time =", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotEqualTo(Date value) {
            addCriterion("update_time <>", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThan(Date value) {
            addCriterion("update_time >", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("update_time >=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThan(Date value) {
            addCriterion("update_time <", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeLessThanOrEqualTo(Date value) {
            addCriterion("update_time <=", value, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeIn(List<Date> values) {
            addCriterion("update_time in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotIn(List<Date> values) {
            addCriterion("update_time not in", values, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeBetween(Date value1, Date value2) {
            addCriterion("update_time between", value1, value2, "updateTime");
            return (Criteria) this;
        }

        public Criteria andUpdateTimeNotBetween(Date value1, Date value2) {
            addCriterion("update_time not between", value1, value2, "updateTime");
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
