package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class WithDrawCashProdExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WithDrawCashProdExample() {
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

        public Criteria andCashProdIdIsNull() {
            addCriterion("cash_prod_id is null");
            return (Criteria) this;
        }

        public Criteria andCashProdIdIsNotNull() {
            addCriterion("cash_prod_id is not null");
            return (Criteria) this;
        }

        public Criteria andCashProdIdEqualTo(String value) {
            addCriterion("cash_prod_id =", value, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdNotEqualTo(String value) {
            addCriterion("cash_prod_id <>", value, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdGreaterThan(String value) {
            addCriterion("cash_prod_id >", value, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdGreaterThanOrEqualTo(String value) {
            addCriterion("cash_prod_id >=", value, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdLessThan(String value) {
            addCriterion("cash_prod_id <", value, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdLessThanOrEqualTo(String value) {
            addCriterion("cash_prod_id <=", value, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdLike(String value) {
            addCriterion("cash_prod_id like", value, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdNotLike(String value) {
            addCriterion("cash_prod_id not like", value, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdIn(List<String> values) {
            addCriterion("cash_prod_id in", values, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdNotIn(List<String> values) {
            addCriterion("cash_prod_id not in", values, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdBetween(String value1, String value2) {
            addCriterion("cash_prod_id between", value1, value2, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdIdNotBetween(String value1, String value2) {
            addCriterion("cash_prod_id not between", value1, value2, "cashProdId");
            return (Criteria) this;
        }

        public Criteria andCashProdNameIsNull() {
            addCriterion("cash_prod_name is null");
            return (Criteria) this;
        }

        public Criteria andCashProdNameIsNotNull() {
            addCriterion("cash_prod_name is not null");
            return (Criteria) this;
        }

        public Criteria andCashProdNameEqualTo(String value) {
            addCriterion("cash_prod_name =", value, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameNotEqualTo(String value) {
            addCriterion("cash_prod_name <>", value, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameGreaterThan(String value) {
            addCriterion("cash_prod_name >", value, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameGreaterThanOrEqualTo(String value) {
            addCriterion("cash_prod_name >=", value, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameLessThan(String value) {
            addCriterion("cash_prod_name <", value, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameLessThanOrEqualTo(String value) {
            addCriterion("cash_prod_name <=", value, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameLike(String value) {
            addCriterion("cash_prod_name like", value, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameNotLike(String value) {
            addCriterion("cash_prod_name not like", value, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameIn(List<String> values) {
            addCriterion("cash_prod_name in", values, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameNotIn(List<String> values) {
            addCriterion("cash_prod_name not in", values, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameBetween(String value1, String value2) {
            addCriterion("cash_prod_name between", value1, value2, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andCashProdNameNotBetween(String value1, String value2) {
            addCriterion("cash_prod_name not between", value1, value2, "cashProdName");
            return (Criteria) this;
        }

        public Criteria andDiamondNumIsNull() {
            addCriterion("diamond_num is null");
            return (Criteria) this;
        }

        public Criteria andDiamondNumIsNotNull() {
            addCriterion("diamond_num is not null");
            return (Criteria) this;
        }

        public Criteria andDiamondNumEqualTo(Long value) {
            addCriterion("diamond_num =", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumNotEqualTo(Long value) {
            addCriterion("diamond_num <>", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumGreaterThan(Long value) {
            addCriterion("diamond_num >", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumGreaterThanOrEqualTo(Long value) {
            addCriterion("diamond_num >=", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumLessThan(Long value) {
            addCriterion("diamond_num <", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumLessThanOrEqualTo(Long value) {
            addCriterion("diamond_num <=", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumIn(List<Long> values) {
            addCriterion("diamond_num in", values, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumNotIn(List<Long> values) {
            addCriterion("diamond_num not in", values, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumBetween(Long value1, Long value2) {
            addCriterion("diamond_num between", value1, value2, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumNotBetween(Long value1, Long value2) {
            addCriterion("diamond_num not between", value1, value2, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andCashNumIsNull() {
            addCriterion("cash_num is null");
            return (Criteria) this;
        }

        public Criteria andCashNumIsNotNull() {
            addCriterion("cash_num is not null");
            return (Criteria) this;
        }

        public Criteria andCashNumEqualTo(Long value) {
            addCriterion("cash_num =", value, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumNotEqualTo(Long value) {
            addCriterion("cash_num <>", value, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumGreaterThan(Long value) {
            addCriterion("cash_num >", value, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumGreaterThanOrEqualTo(Long value) {
            addCriterion("cash_num >=", value, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumLessThan(Long value) {
            addCriterion("cash_num <", value, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumLessThanOrEqualTo(Long value) {
            addCriterion("cash_num <=", value, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumIn(List<Long> values) {
            addCriterion("cash_num in", values, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumNotIn(List<Long> values) {
            addCriterion("cash_num not in", values, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumBetween(Long value1, Long value2) {
            addCriterion("cash_num between", value1, value2, "cashNum");
            return (Criteria) this;
        }

        public Criteria andCashNumNotBetween(Long value1, Long value2) {
            addCriterion("cash_num not between", value1, value2, "cashNum");
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
