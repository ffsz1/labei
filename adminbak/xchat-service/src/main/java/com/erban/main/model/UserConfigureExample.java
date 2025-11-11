package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class UserConfigureExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserConfigureExample() {
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

        public Criteria andSuperiorBounsIsNull() {
            addCriterion("superior_bouns is null");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsIsNotNull() {
            addCriterion("superior_bouns is not null");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsEqualTo(Byte value) {
            addCriterion("superior_bouns =", value, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsNotEqualTo(Byte value) {
            addCriterion("superior_bouns <>", value, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsGreaterThan(Byte value) {
            addCriterion("superior_bouns >", value, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsGreaterThanOrEqualTo(Byte value) {
            addCriterion("superior_bouns >=", value, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsLessThan(Byte value) {
            addCriterion("superior_bouns <", value, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsLessThanOrEqualTo(Byte value) {
            addCriterion("superior_bouns <=", value, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsIn(List<Byte> values) {
            addCriterion("superior_bouns in", values, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsNotIn(List<Byte> values) {
            addCriterion("superior_bouns not in", values, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsBetween(Byte value1, Byte value2) {
            addCriterion("superior_bouns between", value1, value2, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andSuperiorBounsNotBetween(Byte value1, Byte value2) {
            addCriterion("superior_bouns not between", value1, value2, "superiorBouns");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioIsNull() {
            addCriterion("occupation_ratio is null");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioIsNotNull() {
            addCriterion("occupation_ratio is not null");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioEqualTo(Byte value) {
            addCriterion("occupation_ratio =", value, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioNotEqualTo(Byte value) {
            addCriterion("occupation_ratio <>", value, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioGreaterThan(Byte value) {
            addCriterion("occupation_ratio >", value, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioGreaterThanOrEqualTo(Byte value) {
            addCriterion("occupation_ratio >=", value, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioLessThan(Byte value) {
            addCriterion("occupation_ratio <", value, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioLessThanOrEqualTo(Byte value) {
            addCriterion("occupation_ratio <=", value, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioIn(List<Byte> values) {
            addCriterion("occupation_ratio in", values, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioNotIn(List<Byte> values) {
            addCriterion("occupation_ratio not in", values, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioBetween(Byte value1, Byte value2) {
            addCriterion("occupation_ratio between", value1, value2, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andOccupationRatioNotBetween(Byte value1, Byte value2) {
            addCriterion("occupation_ratio not between", value1, value2, "occupationRatio");
            return (Criteria) this;
        }

        public Criteria andBankCardIsNull() {
            addCriterion("bank_card is null");
            return (Criteria) this;
        }

        public Criteria andBankCardIsNotNull() {
            addCriterion("bank_card is not null");
            return (Criteria) this;
        }

        public Criteria andBankCardEqualTo(String value) {
            addCriterion("bank_card =", value, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardNotEqualTo(String value) {
            addCriterion("bank_card <>", value, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardGreaterThan(String value) {
            addCriterion("bank_card >", value, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardGreaterThanOrEqualTo(String value) {
            addCriterion("bank_card >=", value, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardLessThan(String value) {
            addCriterion("bank_card <", value, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardLessThanOrEqualTo(String value) {
            addCriterion("bank_card <=", value, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardLike(String value) {
            addCriterion("bank_card like", value, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardNotLike(String value) {
            addCriterion("bank_card not like", value, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardIn(List<String> values) {
            addCriterion("bank_card in", values, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardNotIn(List<String> values) {
            addCriterion("bank_card not in", values, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardBetween(String value1, String value2) {
            addCriterion("bank_card between", value1, value2, "bankCard");
            return (Criteria) this;
        }

        public Criteria andBankCardNotBetween(String value1, String value2) {
            addCriterion("bank_card not between", value1, value2, "bankCard");
            return (Criteria) this;
        }

        public Criteria andCardholderIsNull() {
            addCriterion("cardholder is null");
            return (Criteria) this;
        }

        public Criteria andCardholderIsNotNull() {
            addCriterion("cardholder is not null");
            return (Criteria) this;
        }

        public Criteria andCardholderEqualTo(String value) {
            addCriterion("cardholder =", value, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderNotEqualTo(String value) {
            addCriterion("cardholder <>", value, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderGreaterThan(String value) {
            addCriterion("cardholder >", value, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderGreaterThanOrEqualTo(String value) {
            addCriterion("cardholder >=", value, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderLessThan(String value) {
            addCriterion("cardholder <", value, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderLessThanOrEqualTo(String value) {
            addCriterion("cardholder <=", value, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderLike(String value) {
            addCriterion("cardholder like", value, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderNotLike(String value) {
            addCriterion("cardholder not like", value, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderIn(List<String> values) {
            addCriterion("cardholder in", values, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderNotIn(List<String> values) {
            addCriterion("cardholder not in", values, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderBetween(String value1, String value2) {
            addCriterion("cardholder between", value1, value2, "cardholder");
            return (Criteria) this;
        }

        public Criteria andCardholderNotBetween(String value1, String value2) {
            addCriterion("cardholder not between", value1, value2, "cardholder");
            return (Criteria) this;
        }

        public Criteria andGreenRecomIsNull() {
            addCriterion("green_recom is null");
            return (Criteria) this;
        }

        public Criteria andGreenRecomIsNotNull() {
            addCriterion("green_recom is not null");
            return (Criteria) this;
        }

        public Criteria andGreenRecomEqualTo(Byte value) {
            addCriterion("green_recom =", value, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomNotEqualTo(Byte value) {
            addCriterion("green_recom <>", value, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomGreaterThan(Byte value) {
            addCriterion("green_recom >", value, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomGreaterThanOrEqualTo(Byte value) {
            addCriterion("green_recom >=", value, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomLessThan(Byte value) {
            addCriterion("green_recom <", value, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomLessThanOrEqualTo(Byte value) {
            addCriterion("green_recom <=", value, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomIn(List<Byte> values) {
            addCriterion("green_recom in", values, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomNotIn(List<Byte> values) {
            addCriterion("green_recom not in", values, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomBetween(Byte value1, Byte value2) {
            addCriterion("green_recom between", value1, value2, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andGreenRecomNotBetween(Byte value1, Byte value2) {
            addCriterion("green_recom not between", value1, value2, "greenRecom");
            return (Criteria) this;
        }

        public Criteria andNewUsersIsNull() {
            addCriterion("new_users is null");
            return (Criteria) this;
        }

        public Criteria andNewUsersIsNotNull() {
            addCriterion("new_users is not null");
            return (Criteria) this;
        }

        public Criteria andNewUsersEqualTo(Byte value) {
            addCriterion("new_users =", value, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersNotEqualTo(Byte value) {
            addCriterion("new_users <>", value, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersGreaterThan(Byte value) {
            addCriterion("new_users >", value, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersGreaterThanOrEqualTo(Byte value) {
            addCriterion("new_users >=", value, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersLessThan(Byte value) {
            addCriterion("new_users <", value, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersLessThanOrEqualTo(Byte value) {
            addCriterion("new_users <=", value, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersIn(List<Byte> values) {
            addCriterion("new_users in", values, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersNotIn(List<Byte> values) {
            addCriterion("new_users not in", values, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersBetween(Byte value1, Byte value2) {
            addCriterion("new_users between", value1, value2, "newUsers");
            return (Criteria) this;
        }

        public Criteria andNewUsersNotBetween(Byte value1, Byte value2) {
            addCriterion("new_users not between", value1, value2, "newUsers");
            return (Criteria) this;
        }

        public Criteria andGameRoomIsNull() {
            addCriterion("game_room is null");
            return (Criteria) this;
        }

        public Criteria andGameRoomIsNotNull() {
            addCriterion("game_room is not null");
            return (Criteria) this;
        }

        public Criteria andGameRoomEqualTo(Byte value) {
            addCriterion("game_room =", value, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomNotEqualTo(Byte value) {
            addCriterion("game_room <>", value, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomGreaterThan(Byte value) {
            addCriterion("game_room >", value, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomGreaterThanOrEqualTo(Byte value) {
            addCriterion("game_room >=", value, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomLessThan(Byte value) {
            addCriterion("game_room <", value, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomLessThanOrEqualTo(Byte value) {
            addCriterion("game_room <=", value, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomIn(List<Byte> values) {
            addCriterion("game_room in", values, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomNotIn(List<Byte> values) {
            addCriterion("game_room not in", values, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomBetween(Byte value1, Byte value2) {
            addCriterion("game_room between", value1, value2, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGameRoomNotBetween(Byte value1, Byte value2) {
            addCriterion("game_room not between", value1, value2, "gameRoom");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoIsNull() {
            addCriterion("green_sort_no is null");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoIsNotNull() {
            addCriterion("green_sort_no is not null");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoEqualTo(Integer value) {
            addCriterion("green_sort_no =", value, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoNotEqualTo(Integer value) {
            addCriterion("green_sort_no <>", value, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoGreaterThan(Integer value) {
            addCriterion("green_sort_no >", value, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("green_sort_no >=", value, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoLessThan(Integer value) {
            addCriterion("green_sort_no <", value, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoLessThanOrEqualTo(Integer value) {
            addCriterion("green_sort_no <=", value, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoIn(List<Integer> values) {
            addCriterion("green_sort_no in", values, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoNotIn(List<Integer> values) {
            addCriterion("green_sort_no not in", values, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoBetween(Integer value1, Integer value2) {
            addCriterion("green_sort_no between", value1, value2, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andGreenSortNoNotBetween(Integer value1, Integer value2) {
            addCriterion("green_sort_no not between", value1, value2, "greenSortNo");
            return (Criteria) this;
        }

        public Criteria andSuibaoIsNull() {
            addCriterion("suibao is null");
            return (Criteria) this;
        }

        public Criteria andSuibaoIsNotNull() {
            addCriterion("suibao is not null");
            return (Criteria) this;
        }

        public Criteria andSuibaoEqualTo(Integer value) {
            addCriterion("suibao =", value, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoNotEqualTo(Integer value) {
            addCriterion("suibao <>", value, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoGreaterThan(Integer value) {
            addCriterion("suibao >", value, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoGreaterThanOrEqualTo(Integer value) {
            addCriterion("suibao >=", value, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoLessThan(Integer value) {
            addCriterion("suibao <", value, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoLessThanOrEqualTo(Integer value) {
            addCriterion("suibao <=", value, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoIn(List<Integer> values) {
            addCriterion("suibao in", values, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoNotIn(List<Integer> values) {
            addCriterion("suibao not in", values, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoBetween(Integer value1, Integer value2) {
            addCriterion("suibao between", value1, value2, "suibao");
            return (Criteria) this;
        }

        public Criteria andSuibaoNotBetween(Integer value1, Integer value2) {
            addCriterion("suibao not between", value1, value2, "suibao");
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
