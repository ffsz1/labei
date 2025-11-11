package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StatPacketBounsExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatPacketBounsExample() {
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

        public Criteria andBounsIdIsNull() {
            addCriterion("bouns_id is null");
            return (Criteria) this;
        }

        public Criteria andBounsIdIsNotNull() {
            addCriterion("bouns_id is not null");
            return (Criteria) this;
        }

        public Criteria andBounsIdEqualTo(String value) {
            addCriterion("bouns_id =", value, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdNotEqualTo(String value) {
            addCriterion("bouns_id <>", value, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdGreaterThan(String value) {
            addCriterion("bouns_id >", value, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdGreaterThanOrEqualTo(String value) {
            addCriterion("bouns_id >=", value, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdLessThan(String value) {
            addCriterion("bouns_id <", value, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdLessThanOrEqualTo(String value) {
            addCriterion("bouns_id <=", value, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdLike(String value) {
            addCriterion("bouns_id like", value, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdNotLike(String value) {
            addCriterion("bouns_id not like", value, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdIn(List<String> values) {
            addCriterion("bouns_id in", values, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdNotIn(List<String> values) {
            addCriterion("bouns_id not in", values, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdBetween(String value1, String value2) {
            addCriterion("bouns_id between", value1, value2, "bounsId");
            return (Criteria) this;
        }

        public Criteria andBounsIdNotBetween(String value1, String value2) {
            addCriterion("bouns_id not between", value1, value2, "bounsId");
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

        public Criteria andChargeRecordIdIsNull() {
            addCriterion("charge_record_id is null");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdIsNotNull() {
            addCriterion("charge_record_id is not null");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdEqualTo(String value) {
            addCriterion("charge_record_id =", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdNotEqualTo(String value) {
            addCriterion("charge_record_id <>", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdGreaterThan(String value) {
            addCriterion("charge_record_id >", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdGreaterThanOrEqualTo(String value) {
            addCriterion("charge_record_id >=", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdLessThan(String value) {
            addCriterion("charge_record_id <", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdLessThanOrEqualTo(String value) {
            addCriterion("charge_record_id <=", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdLike(String value) {
            addCriterion("charge_record_id like", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdNotLike(String value) {
            addCriterion("charge_record_id not like", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdIn(List<String> values) {
            addCriterion("charge_record_id in", values, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdNotIn(List<String> values) {
            addCriterion("charge_record_id not in", values, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdBetween(String value1, String value2) {
            addCriterion("charge_record_id between", value1, value2, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdNotBetween(String value1, String value2) {
            addCriterion("charge_record_id not between", value1, value2, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Long value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Long value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Long value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Long value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Long value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Long> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Long> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Long value1, Long value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Long value1, Long value2) {
            addCriterion("amount not between", value1, value2, "amount");
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
