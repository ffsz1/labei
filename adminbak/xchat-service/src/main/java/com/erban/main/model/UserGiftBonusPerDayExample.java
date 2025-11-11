package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UserGiftBonusPerDayExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserGiftBonusPerDayExample() {
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

        public Criteria andBonusIdIsNull() {
            addCriterion("bonus_id is null");
            return (Criteria) this;
        }

        public Criteria andBonusIdIsNotNull() {
            addCriterion("bonus_id is not null");
            return (Criteria) this;
        }

        public Criteria andBonusIdEqualTo(Integer value) {
            addCriterion("bonus_id =", value, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdNotEqualTo(Integer value) {
            addCriterion("bonus_id <>", value, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdGreaterThan(Integer value) {
            addCriterion("bonus_id >", value, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("bonus_id >=", value, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdLessThan(Integer value) {
            addCriterion("bonus_id <", value, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdLessThanOrEqualTo(Integer value) {
            addCriterion("bonus_id <=", value, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdIn(List<Integer> values) {
            addCriterion("bonus_id in", values, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdNotIn(List<Integer> values) {
            addCriterion("bonus_id not in", values, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdBetween(Integer value1, Integer value2) {
            addCriterion("bonus_id between", value1, value2, "bonusId");
            return (Criteria) this;
        }

        public Criteria andBonusIdNotBetween(Integer value1, Integer value2) {
            addCriterion("bonus_id not between", value1, value2, "bonusId");
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

        public Criteria andCurDiamondNumIsNull() {
            addCriterion("cur_diamond_num is null");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumIsNotNull() {
            addCriterion("cur_diamond_num is not null");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumEqualTo(Double value) {
            addCriterion("cur_diamond_num =", value, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumNotEqualTo(Double value) {
            addCriterion("cur_diamond_num <>", value, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumGreaterThan(Double value) {
            addCriterion("cur_diamond_num >", value, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumGreaterThanOrEqualTo(Double value) {
            addCriterion("cur_diamond_num >=", value, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumLessThan(Double value) {
            addCriterion("cur_diamond_num <", value, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumLessThanOrEqualTo(Double value) {
            addCriterion("cur_diamond_num <=", value, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumIn(List<Double> values) {
            addCriterion("cur_diamond_num in", values, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumNotIn(List<Double> values) {
            addCriterion("cur_diamond_num not in", values, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumBetween(Double value1, Double value2) {
            addCriterion("cur_diamond_num between", value1, value2, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andCurDiamondNumNotBetween(Double value1, Double value2) {
            addCriterion("cur_diamond_num not between", value1, value2, "curDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumIsNull() {
            addCriterion("forecast_diamond_num is null");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumIsNotNull() {
            addCriterion("forecast_diamond_num is not null");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumEqualTo(Double value) {
            addCriterion("forecast_diamond_num =", value, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumNotEqualTo(Double value) {
            addCriterion("forecast_diamond_num <>", value, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumGreaterThan(Double value) {
            addCriterion("forecast_diamond_num >", value, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumGreaterThanOrEqualTo(Double value) {
            addCriterion("forecast_diamond_num >=", value, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumLessThan(Double value) {
            addCriterion("forecast_diamond_num <", value, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumLessThanOrEqualTo(Double value) {
            addCriterion("forecast_diamond_num <=", value, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumIn(List<Double> values) {
            addCriterion("forecast_diamond_num in", values, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumNotIn(List<Double> values) {
            addCriterion("forecast_diamond_num not in", values, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumBetween(Double value1, Double value2) {
            addCriterion("forecast_diamond_num between", value1, value2, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andForecastDiamondNumNotBetween(Double value1, Double value2) {
            addCriterion("forecast_diamond_num not between", value1, value2, "forecastDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumIsNull() {
            addCriterion("bonus_diamond_num is null");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumIsNotNull() {
            addCriterion("bonus_diamond_num is not null");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumEqualTo(Double value) {
            addCriterion("bonus_diamond_num =", value, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumNotEqualTo(Double value) {
            addCriterion("bonus_diamond_num <>", value, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumGreaterThan(Double value) {
            addCriterion("bonus_diamond_num >", value, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumGreaterThanOrEqualTo(Double value) {
            addCriterion("bonus_diamond_num >=", value, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumLessThan(Double value) {
            addCriterion("bonus_diamond_num <", value, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumLessThanOrEqualTo(Double value) {
            addCriterion("bonus_diamond_num <=", value, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumIn(List<Double> values) {
            addCriterion("bonus_diamond_num in", values, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumNotIn(List<Double> values) {
            addCriterion("bonus_diamond_num not in", values, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumBetween(Double value1, Double value2) {
            addCriterion("bonus_diamond_num between", value1, value2, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andBonusDiamondNumNotBetween(Double value1, Double value2) {
            addCriterion("bonus_diamond_num not between", value1, value2, "bonusDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusIsNull() {
            addCriterion("today_has_finish_bonus is null");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusIsNotNull() {
            addCriterion("today_has_finish_bonus is not null");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusEqualTo(Boolean value) {
            addCriterion("today_has_finish_bonus =", value, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusNotEqualTo(Boolean value) {
            addCriterion("today_has_finish_bonus <>", value, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusGreaterThan(Boolean value) {
            addCriterion("today_has_finish_bonus >", value, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusGreaterThanOrEqualTo(Boolean value) {
            addCriterion("today_has_finish_bonus >=", value, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusLessThan(Boolean value) {
            addCriterion("today_has_finish_bonus <", value, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusLessThanOrEqualTo(Boolean value) {
            addCriterion("today_has_finish_bonus <=", value, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusIn(List<Boolean> values) {
            addCriterion("today_has_finish_bonus in", values, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusNotIn(List<Boolean> values) {
            addCriterion("today_has_finish_bonus not in", values, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusBetween(Boolean value1, Boolean value2) {
            addCriterion("today_has_finish_bonus between", value1, value2, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andTodayHasFinishBonusNotBetween(Boolean value1, Boolean value2) {
            addCriterion("today_has_finish_bonus not between", value1, value2, "todayHasFinishBonus");
            return (Criteria) this;
        }

        public Criteria andStatDateIsNull() {
            addCriterion("stat_date is null");
            return (Criteria) this;
        }

        public Criteria andStatDateIsNotNull() {
            addCriterion("stat_date is not null");
            return (Criteria) this;
        }

        public Criteria andStatDateEqualTo(Date value) {
            addCriterionForJDBCDate("stat_date =", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotEqualTo(Date value) {
            addCriterionForJDBCDate("stat_date <>", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateGreaterThan(Date value) {
            addCriterionForJDBCDate("stat_date >", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("stat_date >=", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLessThan(Date value) {
            addCriterionForJDBCDate("stat_date <", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("stat_date <=", value, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateIn(List<Date> values) {
            addCriterionForJDBCDate("stat_date in", values, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotIn(List<Date> values) {
            addCriterionForJDBCDate("stat_date not in", values, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("stat_date between", value1, value2, "statDate");
            return (Criteria) this;
        }

        public Criteria andStatDateNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("stat_date not between", value1, value2, "statDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateIsNull() {
            addCriterion("bonus_finish_date is null");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateIsNotNull() {
            addCriterion("bonus_finish_date is not null");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateEqualTo(Date value) {
            addCriterion("bonus_finish_date =", value, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateNotEqualTo(Date value) {
            addCriterion("bonus_finish_date <>", value, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateGreaterThan(Date value) {
            addCriterion("bonus_finish_date >", value, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateGreaterThanOrEqualTo(Date value) {
            addCriterion("bonus_finish_date >=", value, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateLessThan(Date value) {
            addCriterion("bonus_finish_date <", value, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateLessThanOrEqualTo(Date value) {
            addCriterion("bonus_finish_date <=", value, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateIn(List<Date> values) {
            addCriterion("bonus_finish_date in", values, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateNotIn(List<Date> values) {
            addCriterion("bonus_finish_date not in", values, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateBetween(Date value1, Date value2) {
            addCriterion("bonus_finish_date between", value1, value2, "bonusFinishDate");
            return (Criteria) this;
        }

        public Criteria andBonusFinishDateNotBetween(Date value1, Date value2) {
            addCriterion("bonus_finish_date not between", value1, value2, "bonusFinishDate");
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
