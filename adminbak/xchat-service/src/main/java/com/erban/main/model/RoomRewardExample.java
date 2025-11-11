package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomRewardExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RoomRewardExample() {
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

        public Criteria andRewardIdIsNull() {
            addCriterion("reward_id is null");
            return (Criteria) this;
        }

        public Criteria andRewardIdIsNotNull() {
            addCriterion("reward_id is not null");
            return (Criteria) this;
        }

        public Criteria andRewardIdEqualTo(String value) {
            addCriterion("reward_id =", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdNotEqualTo(String value) {
            addCriterion("reward_id <>", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdGreaterThan(String value) {
            addCriterion("reward_id >", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdGreaterThanOrEqualTo(String value) {
            addCriterion("reward_id >=", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdLessThan(String value) {
            addCriterion("reward_id <", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdLessThanOrEqualTo(String value) {
            addCriterion("reward_id <=", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdLike(String value) {
            addCriterion("reward_id like", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdNotLike(String value) {
            addCriterion("reward_id not like", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdIn(List<String> values) {
            addCriterion("reward_id in", values, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdNotIn(List<String> values) {
            addCriterion("reward_id not in", values, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdBetween(String value1, String value2) {
            addCriterion("reward_id between", value1, value2, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdNotBetween(String value1, String value2) {
            addCriterion("reward_id not between", value1, value2, "rewardId");
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

        public Criteria andRewardMoneyIsNull() {
            addCriterion("reward_money is null");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyIsNotNull() {
            addCriterion("reward_money is not null");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyEqualTo(Long value) {
            addCriterion("reward_money =", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotEqualTo(Long value) {
            addCriterion("reward_money <>", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyGreaterThan(Long value) {
            addCriterion("reward_money >", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("reward_money >=", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyLessThan(Long value) {
            addCriterion("reward_money <", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyLessThanOrEqualTo(Long value) {
            addCriterion("reward_money <=", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyIn(List<Long> values) {
            addCriterion("reward_money in", values, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotIn(List<Long> values) {
            addCriterion("reward_money not in", values, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyBetween(Long value1, Long value2) {
            addCriterion("reward_money between", value1, value2, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotBetween(Long value1, Long value2) {
            addCriterion("reward_money not between", value1, value2, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andServDuraIsNull() {
            addCriterion("serv_dura is null");
            return (Criteria) this;
        }

        public Criteria andServDuraIsNotNull() {
            addCriterion("serv_dura is not null");
            return (Criteria) this;
        }

        public Criteria andServDuraEqualTo(Integer value) {
            addCriterion("serv_dura =", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotEqualTo(Integer value) {
            addCriterion("serv_dura <>", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraGreaterThan(Integer value) {
            addCriterion("serv_dura >", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraGreaterThanOrEqualTo(Integer value) {
            addCriterion("serv_dura >=", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraLessThan(Integer value) {
            addCriterion("serv_dura <", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraLessThanOrEqualTo(Integer value) {
            addCriterion("serv_dura <=", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraIn(List<Integer> values) {
            addCriterion("serv_dura in", values, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotIn(List<Integer> values) {
            addCriterion("serv_dura not in", values, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraBetween(Integer value1, Integer value2) {
            addCriterion("serv_dura between", value1, value2, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotBetween(Integer value1, Integer value2) {
            addCriterion("serv_dura not between", value1, value2, "servDura");
            return (Criteria) this;
        }

        public Criteria andRewardStatusIsNull() {
            addCriterion("reward_status is null");
            return (Criteria) this;
        }

        public Criteria andRewardStatusIsNotNull() {
            addCriterion("reward_status is not null");
            return (Criteria) this;
        }

        public Criteria andRewardStatusEqualTo(Byte value) {
            addCriterion("reward_status =", value, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusNotEqualTo(Byte value) {
            addCriterion("reward_status <>", value, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusGreaterThan(Byte value) {
            addCriterion("reward_status >", value, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("reward_status >=", value, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusLessThan(Byte value) {
            addCriterion("reward_status <", value, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusLessThanOrEqualTo(Byte value) {
            addCriterion("reward_status <=", value, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusIn(List<Byte> values) {
            addCriterion("reward_status in", values, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusNotIn(List<Byte> values) {
            addCriterion("reward_status not in", values, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusBetween(Byte value1, Byte value2) {
            addCriterion("reward_status between", value1, value2, "rewardStatus");
            return (Criteria) this;
        }

        public Criteria andRewardStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("reward_status not between", value1, value2, "rewardStatus");
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

        public Criteria andFinishTimeIsNull() {
            addCriterion("finish_time is null");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIsNotNull() {
            addCriterion("finish_time is not null");
            return (Criteria) this;
        }

        public Criteria andFinishTimeEqualTo(Date value) {
            addCriterion("finish_time =", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotEqualTo(Date value) {
            addCriterion("finish_time <>", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeGreaterThan(Date value) {
            addCriterion("finish_time >", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("finish_time >=", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeLessThan(Date value) {
            addCriterion("finish_time <", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeLessThanOrEqualTo(Date value) {
            addCriterion("finish_time <=", value, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeIn(List<Date> values) {
            addCriterion("finish_time in", values, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotIn(List<Date> values) {
            addCriterion("finish_time not in", values, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeBetween(Date value1, Date value2) {
            addCriterion("finish_time between", value1, value2, "finishTime");
            return (Criteria) this;
        }

        public Criteria andFinishTimeNotBetween(Date value1, Date value2) {
            addCriterion("finish_time not between", value1, value2, "finishTime");
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
