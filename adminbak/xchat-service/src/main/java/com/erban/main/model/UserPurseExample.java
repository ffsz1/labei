package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserPurseExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserPurseExample() {
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

        public Criteria andChargeGoldNumIsNull() {
            addCriterion("charge_gold_num is null");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumIsNotNull() {
            addCriterion("charge_gold_num is not null");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumEqualTo(Long value) {
            addCriterion("charge_gold_num =", value, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumNotEqualTo(Long value) {
            addCriterion("charge_gold_num <>", value, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumGreaterThan(Long value) {
            addCriterion("charge_gold_num >", value, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumGreaterThanOrEqualTo(Long value) {
            addCriterion("charge_gold_num >=", value, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumLessThan(Long value) {
            addCriterion("charge_gold_num <", value, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumLessThanOrEqualTo(Long value) {
            addCriterion("charge_gold_num <=", value, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumIn(List<Long> values) {
            addCriterion("charge_gold_num in", values, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumNotIn(List<Long> values) {
            addCriterion("charge_gold_num not in", values, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumBetween(Long value1, Long value2) {
            addCriterion("charge_gold_num between", value1, value2, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andChargeGoldNumNotBetween(Long value1, Long value2) {
            addCriterion("charge_gold_num not between", value1, value2, "chargeGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumIsNull() {
            addCriterion("noble_gold_num is null");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumIsNotNull() {
            addCriterion("noble_gold_num is not null");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumEqualTo(Long value) {
            addCriterion("noble_gold_num =", value, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumNotEqualTo(Long value) {
            addCriterion("noble_gold_num <>", value, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumGreaterThan(Long value) {
            addCriterion("noble_gold_num >", value, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumGreaterThanOrEqualTo(Long value) {
            addCriterion("noble_gold_num >=", value, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumLessThan(Long value) {
            addCriterion("noble_gold_num <", value, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumLessThanOrEqualTo(Long value) {
            addCriterion("noble_gold_num <=", value, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumIn(List<Long> values) {
            addCriterion("noble_gold_num in", values, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumNotIn(List<Long> values) {
            addCriterion("noble_gold_num not in", values, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumBetween(Long value1, Long value2) {
            addCriterion("noble_gold_num between", value1, value2, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andNobleGoldNumNotBetween(Long value1, Long value2) {
            addCriterion("noble_gold_num not between", value1, value2, "nobleGoldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumIsNull() {
            addCriterion("gold_num is null");
            return (Criteria) this;
        }

        public Criteria andGoldNumIsNotNull() {
            addCriterion("gold_num is not null");
            return (Criteria) this;
        }

        public Criteria andGoldNumEqualTo(Long value) {
            addCriterion("gold_num =", value, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumNotEqualTo(Long value) {
            addCriterion("gold_num <>", value, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumGreaterThan(Long value) {
            addCriterion("gold_num >", value, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumGreaterThanOrEqualTo(Long value) {
            addCriterion("gold_num >=", value, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumLessThan(Long value) {
            addCriterion("gold_num <", value, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumLessThanOrEqualTo(Long value) {
            addCriterion("gold_num <=", value, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumIn(List<Long> values) {
            addCriterion("gold_num in", values, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumNotIn(List<Long> values) {
            addCriterion("gold_num not in", values, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumBetween(Long value1, Long value2) {
            addCriterion("gold_num between", value1, value2, "goldNum");
            return (Criteria) this;
        }

        public Criteria andGoldNumNotBetween(Long value1, Long value2) {
            addCriterion("gold_num not between", value1, value2, "goldNum");
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

        public Criteria andDiamondNumEqualTo(Double value) {
            addCriterion("diamond_num =", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumNotEqualTo(Double value) {
            addCriterion("diamond_num <>", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumGreaterThan(Double value) {
            addCriterion("diamond_num >", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumGreaterThanOrEqualTo(Double value) {
            addCriterion("diamond_num >=", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumLessThan(Double value) {
            addCriterion("diamond_num <", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumLessThanOrEqualTo(Double value) {
            addCriterion("diamond_num <=", value, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumIn(List<Double> values) {
            addCriterion("diamond_num in", values, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumNotIn(List<Double> values) {
            addCriterion("diamond_num not in", values, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumBetween(Double value1, Double value2) {
            addCriterion("diamond_num between", value1, value2, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDiamondNumNotBetween(Double value1, Double value2) {
            addCriterion("diamond_num not between", value1, value2, "diamondNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumIsNull() {
            addCriterion("deposit_num is null");
            return (Criteria) this;
        }

        public Criteria andDepositNumIsNotNull() {
            addCriterion("deposit_num is not null");
            return (Criteria) this;
        }

        public Criteria andDepositNumEqualTo(Long value) {
            addCriterion("deposit_num =", value, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumNotEqualTo(Long value) {
            addCriterion("deposit_num <>", value, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumGreaterThan(Long value) {
            addCriterion("deposit_num >", value, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumGreaterThanOrEqualTo(Long value) {
            addCriterion("deposit_num >=", value, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumLessThan(Long value) {
            addCriterion("deposit_num <", value, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumLessThanOrEqualTo(Long value) {
            addCriterion("deposit_num <=", value, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumIn(List<Long> values) {
            addCriterion("deposit_num in", values, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumNotIn(List<Long> values) {
            addCriterion("deposit_num not in", values, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumBetween(Long value1, Long value2) {
            addCriterion("deposit_num between", value1, value2, "depositNum");
            return (Criteria) this;
        }

        public Criteria andDepositNumNotBetween(Long value1, Long value2) {
            addCriterion("deposit_num not between", value1, value2, "depositNum");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeIsNull() {
            addCriterion("is_first_charge is null");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeIsNotNull() {
            addCriterion("is_first_charge is not null");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeEqualTo(Boolean value) {
            addCriterion("is_first_charge =", value, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeNotEqualTo(Boolean value) {
            addCriterion("is_first_charge <>", value, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeGreaterThan(Boolean value) {
            addCriterion("is_first_charge >", value, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_first_charge >=", value, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeLessThan(Boolean value) {
            addCriterion("is_first_charge <", value, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeLessThanOrEqualTo(Boolean value) {
            addCriterion("is_first_charge <=", value, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeIn(List<Boolean> values) {
            addCriterion("is_first_charge in", values, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeNotIn(List<Boolean> values) {
            addCriterion("is_first_charge not in", values, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeBetween(Boolean value1, Boolean value2) {
            addCriterion("is_first_charge between", value1, value2, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andIsFirstChargeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_first_charge not between", value1, value2, "isFirstCharge");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeIsNull() {
            addCriterion("first_recharge_time is null");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeIsNotNull() {
            addCriterion("first_recharge_time is not null");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeEqualTo(Date value) {
            addCriterion("first_recharge_time =", value, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeNotEqualTo(Date value) {
            addCriterion("first_recharge_time <>", value, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeGreaterThan(Date value) {
            addCriterion("first_recharge_time >", value, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("first_recharge_time >=", value, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeLessThan(Date value) {
            addCriterion("first_recharge_time <", value, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeLessThanOrEqualTo(Date value) {
            addCriterion("first_recharge_time <=", value, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeIn(List<Date> values) {
            addCriterion("first_recharge_time in", values, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeNotIn(List<Date> values) {
            addCriterion("first_recharge_time not in", values, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeBetween(Date value1, Date value2) {
            addCriterion("first_recharge_time between", value1, value2, "firstRechargeTime");
            return (Criteria) this;
        }

        public Criteria andFirstRechargeTimeNotBetween(Date value1, Date value2) {
            addCriterion("first_recharge_time not between", value1, value2, "firstRechargeTime");
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
