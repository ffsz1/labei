package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WithDrawPacketCashProdExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WithDrawPacketCashProdExample() {
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

        public Criteria andPacketProdCashIdIsNull() {
            addCriterion("packet_prod_cash_id is null");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdIsNotNull() {
            addCriterion("packet_prod_cash_id is not null");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdEqualTo(Integer value) {
            addCriterion("packet_prod_cash_id =", value, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdNotEqualTo(Integer value) {
            addCriterion("packet_prod_cash_id <>", value, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdGreaterThan(Integer value) {
            addCriterion("packet_prod_cash_id >", value, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("packet_prod_cash_id >=", value, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdLessThan(Integer value) {
            addCriterion("packet_prod_cash_id <", value, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdLessThanOrEqualTo(Integer value) {
            addCriterion("packet_prod_cash_id <=", value, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdIn(List<Integer> values) {
            addCriterion("packet_prod_cash_id in", values, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdNotIn(List<Integer> values) {
            addCriterion("packet_prod_cash_id not in", values, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdBetween(Integer value1, Integer value2) {
            addCriterion("packet_prod_cash_id between", value1, value2, "packetProdCashId");
            return (Criteria) this;
        }

        public Criteria andPacketProdCashIdNotBetween(Integer value1, Integer value2) {
            addCriterion("packet_prod_cash_id not between", value1, value2, "packetProdCashId");
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

        public Criteria andProdStautsIsNull() {
            addCriterion("prod_stauts is null");
            return (Criteria) this;
        }

        public Criteria andProdStautsIsNotNull() {
            addCriterion("prod_stauts is not null");
            return (Criteria) this;
        }

        public Criteria andProdStautsEqualTo(Byte value) {
            addCriterion("prod_stauts =", value, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsNotEqualTo(Byte value) {
            addCriterion("prod_stauts <>", value, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsGreaterThan(Byte value) {
            addCriterion("prod_stauts >", value, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsGreaterThanOrEqualTo(Byte value) {
            addCriterion("prod_stauts >=", value, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsLessThan(Byte value) {
            addCriterion("prod_stauts <", value, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsLessThanOrEqualTo(Byte value) {
            addCriterion("prod_stauts <=", value, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsIn(List<Byte> values) {
            addCriterion("prod_stauts in", values, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsNotIn(List<Byte> values) {
            addCriterion("prod_stauts not in", values, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsBetween(Byte value1, Byte value2) {
            addCriterion("prod_stauts between", value1, value2, "prodStauts");
            return (Criteria) this;
        }

        public Criteria andProdStautsNotBetween(Byte value1, Byte value2) {
            addCriterion("prod_stauts not between", value1, value2, "prodStauts");
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
