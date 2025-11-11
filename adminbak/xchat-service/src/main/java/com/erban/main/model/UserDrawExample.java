package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDrawExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserDrawExample() {
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

        public Criteria andLeftDrawNumIsNull() {
            addCriterion("left_draw_num is null");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumIsNotNull() {
            addCriterion("left_draw_num is not null");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumEqualTo(Integer value) {
            addCriterion("left_draw_num =", value, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumNotEqualTo(Integer value) {
            addCriterion("left_draw_num <>", value, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumGreaterThan(Integer value) {
            addCriterion("left_draw_num >", value, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("left_draw_num >=", value, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumLessThan(Integer value) {
            addCriterion("left_draw_num <", value, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumLessThanOrEqualTo(Integer value) {
            addCriterion("left_draw_num <=", value, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumIn(List<Integer> values) {
            addCriterion("left_draw_num in", values, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumNotIn(List<Integer> values) {
            addCriterion("left_draw_num not in", values, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumBetween(Integer value1, Integer value2) {
            addCriterion("left_draw_num between", value1, value2, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andLeftDrawNumNotBetween(Integer value1, Integer value2) {
            addCriterion("left_draw_num not between", value1, value2, "leftDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumIsNull() {
            addCriterion("total_draw_num is null");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumIsNotNull() {
            addCriterion("total_draw_num is not null");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumEqualTo(Integer value) {
            addCriterion("total_draw_num =", value, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumNotEqualTo(Integer value) {
            addCriterion("total_draw_num <>", value, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumGreaterThan(Integer value) {
            addCriterion("total_draw_num >", value, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_draw_num >=", value, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumLessThan(Integer value) {
            addCriterion("total_draw_num <", value, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumLessThanOrEqualTo(Integer value) {
            addCriterion("total_draw_num <=", value, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumIn(List<Integer> values) {
            addCriterion("total_draw_num in", values, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumNotIn(List<Integer> values) {
            addCriterion("total_draw_num not in", values, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumBetween(Integer value1, Integer value2) {
            addCriterion("total_draw_num between", value1, value2, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalDrawNumNotBetween(Integer value1, Integer value2) {
            addCriterion("total_draw_num not between", value1, value2, "totalDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumIsNull() {
            addCriterion("total_win_draw_num is null");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumIsNotNull() {
            addCriterion("total_win_draw_num is not null");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumEqualTo(Integer value) {
            addCriterion("total_win_draw_num =", value, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumNotEqualTo(Integer value) {
            addCriterion("total_win_draw_num <>", value, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumGreaterThan(Integer value) {
            addCriterion("total_win_draw_num >", value, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("total_win_draw_num >=", value, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumLessThan(Integer value) {
            addCriterion("total_win_draw_num <", value, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumLessThanOrEqualTo(Integer value) {
            addCriterion("total_win_draw_num <=", value, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumIn(List<Integer> values) {
            addCriterion("total_win_draw_num in", values, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumNotIn(List<Integer> values) {
            addCriterion("total_win_draw_num not in", values, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumBetween(Integer value1, Integer value2) {
            addCriterion("total_win_draw_num between", value1, value2, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andTotalWinDrawNumNotBetween(Integer value1, Integer value2) {
            addCriterion("total_win_draw_num not between", value1, value2, "totalWinDrawNum");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareIsNull() {
            addCriterion("is_first_share is null");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareIsNotNull() {
            addCriterion("is_first_share is not null");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareEqualTo(Boolean value) {
            addCriterion("is_first_share =", value, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareNotEqualTo(Boolean value) {
            addCriterion("is_first_share <>", value, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareGreaterThan(Boolean value) {
            addCriterion("is_first_share >", value, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_first_share >=", value, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareLessThan(Boolean value) {
            addCriterion("is_first_share <", value, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareLessThanOrEqualTo(Boolean value) {
            addCriterion("is_first_share <=", value, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareIn(List<Boolean> values) {
            addCriterion("is_first_share in", values, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareNotIn(List<Boolean> values) {
            addCriterion("is_first_share not in", values, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareBetween(Boolean value1, Boolean value2) {
            addCriterion("is_first_share between", value1, value2, "isFirstShare");
            return (Criteria) this;
        }

        public Criteria andIsFirstShareNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_first_share not between", value1, value2, "isFirstShare");
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
