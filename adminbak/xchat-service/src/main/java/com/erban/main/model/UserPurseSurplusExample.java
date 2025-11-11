package com.erban.main.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/10/9
 * @time 16:24
 */
public class UserPurseSurplusExample {

    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserPurseSurplusExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andSurplusDateIsNull() {
            addCriterion("surplus_date is null");
            return (Criteria) this;
        }

        public Criteria andSurplusDateIsNotNull() {
            addCriterion("surplus_date is not null");
            return (Criteria) this;
        }

        public Criteria andSurplusDateEqualTo(Date value) {
            addCriterionForJDBCDate("surplus_date =", value, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("surplus_date <>", value, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateGreaterThan(Date value) {
            addCriterionForJDBCDate("surplus_date >", value, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("surplus_date >=", value, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateLessThan(Date value) {
            addCriterionForJDBCDate("surplus_date <", value, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("surplus_date <=", value, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateIn(List<Date> values) {
            addCriterionForJDBCDate("surplus_date in", values, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("surplus_date not in", values, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("surplus_date between", value1, value2, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("surplus_date not between", value1, value2, "surplusDate");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldIsNull() {
            addCriterion("surplus_gold is null");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldIsNotNull() {
            addCriterion("surplus_gold is not null");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldEqualTo(BigDecimal value) {
            addCriterion("surplus_gold =", value, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldNotEqualTo(BigDecimal value) {
            addCriterion("surplus_gold <>", value, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldGreaterThan(BigDecimal value) {
            addCriterion("surplus_gold >", value, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("surplus_gold >=", value, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldLessThan(BigDecimal value) {
            addCriterion("surplus_gold <", value, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldLessThanOrEqualTo(BigDecimal value) {
            addCriterion("surplus_gold <=", value, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldIn(List<BigDecimal> values) {
            addCriterion("surplus_gold in", values, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldNotIn(List<BigDecimal> values) {
            addCriterion("surplus_gold not in", values, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("surplus_gold between", value1, value2, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusGoldNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("surplus_gold not between", value1, value2, "surplusGold");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsIsNull() {
            addCriterion("surplus_diamonds is null");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsIsNotNull() {
            addCriterion("surplus_diamonds is not null");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsEqualTo(BigDecimal value) {
            addCriterion("surplus_diamonds =", value, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsNotEqualTo(BigDecimal value) {
            addCriterion("surplus_diamonds <>", value, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsGreaterThan(BigDecimal value) {
            addCriterion("surplus_diamonds >", value, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsGreaterThanOrEqualTo(BigDecimal value) {
            addCriterion("surplus_diamonds >=", value, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsLessThan(BigDecimal value) {
            addCriterion("surplus_diamonds <", value, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsLessThanOrEqualTo(BigDecimal value) {
            addCriterion("surplus_diamonds <=", value, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsIn(List<BigDecimal> values) {
            addCriterion("surplus_diamonds in", values, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsNotIn(List<BigDecimal> values) {
            addCriterion("surplus_diamonds not in", values, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("surplus_diamonds between", value1, value2, "surplusDiamonds");
            return (Criteria) this;
        }

        public Criteria andSurplusDiamondsNotBetween(BigDecimal value1, BigDecimal value2) {
            addCriterion("surplus_diamonds not between", value1, value2, "surplusDiamonds");
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
