package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class ChargeProdExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ChargeProdExample() {
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

        public Criteria andChargeProdIdIsNull() {
            addCriterion("charge_prod_id is null");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdIsNotNull() {
            addCriterion("charge_prod_id is not null");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdEqualTo(String value) {
            addCriterion("charge_prod_id =", value, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdNotEqualTo(String value) {
            addCriterion("charge_prod_id <>", value, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdGreaterThan(String value) {
            addCriterion("charge_prod_id >", value, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdGreaterThanOrEqualTo(String value) {
            addCriterion("charge_prod_id >=", value, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdLessThan(String value) {
            addCriterion("charge_prod_id <", value, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdLessThanOrEqualTo(String value) {
            addCriterion("charge_prod_id <=", value, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdLike(String value) {
            addCriterion("charge_prod_id like", value, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdNotLike(String value) {
            addCriterion("charge_prod_id not like", value, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdIn(List<String> values) {
            addCriterion("charge_prod_id in", values, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdNotIn(List<String> values) {
            addCriterion("charge_prod_id not in", values, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdBetween(String value1, String value2) {
            addCriterion("charge_prod_id between", value1, value2, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andChargeProdIdNotBetween(String value1, String value2) {
            addCriterion("charge_prod_id not between", value1, value2, "chargeProdId");
            return (Criteria) this;
        }

        public Criteria andProdNameIsNull() {
            addCriterion("prod_name is null");
            return (Criteria) this;
        }

        public Criteria andProdNameIsNotNull() {
            addCriterion("prod_name is not null");
            return (Criteria) this;
        }

        public Criteria andProdNameEqualTo(String value) {
            addCriterion("prod_name =", value, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameNotEqualTo(String value) {
            addCriterion("prod_name <>", value, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameGreaterThan(String value) {
            addCriterion("prod_name >", value, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameGreaterThanOrEqualTo(String value) {
            addCriterion("prod_name >=", value, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameLessThan(String value) {
            addCriterion("prod_name <", value, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameLessThanOrEqualTo(String value) {
            addCriterion("prod_name <=", value, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameLike(String value) {
            addCriterion("prod_name like", value, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameNotLike(String value) {
            addCriterion("prod_name not like", value, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameIn(List<String> values) {
            addCriterion("prod_name in", values, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameNotIn(List<String> values) {
            addCriterion("prod_name not in", values, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameBetween(String value1, String value2) {
            addCriterion("prod_name between", value1, value2, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdNameNotBetween(String value1, String value2) {
            addCriterion("prod_name not between", value1, value2, "prodName");
            return (Criteria) this;
        }

        public Criteria andProdDescIsNull() {
            addCriterion("prod_desc is null");
            return (Criteria) this;
        }

        public Criteria andProdDescIsNotNull() {
            addCriterion("prod_desc is not null");
            return (Criteria) this;
        }

        public Criteria andProdDescEqualTo(String value) {
            addCriterion("prod_desc =", value, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescNotEqualTo(String value) {
            addCriterion("prod_desc <>", value, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescGreaterThan(String value) {
            addCriterion("prod_desc >", value, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescGreaterThanOrEqualTo(String value) {
            addCriterion("prod_desc >=", value, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescLessThan(String value) {
            addCriterion("prod_desc <", value, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescLessThanOrEqualTo(String value) {
            addCriterion("prod_desc <=", value, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescLike(String value) {
            addCriterion("prod_desc like", value, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescNotLike(String value) {
            addCriterion("prod_desc not like", value, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescIn(List<String> values) {
            addCriterion("prod_desc in", values, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescNotIn(List<String> values) {
            addCriterion("prod_desc not in", values, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescBetween(String value1, String value2) {
            addCriterion("prod_desc between", value1, value2, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdDescNotBetween(String value1, String value2) {
            addCriterion("prod_desc not between", value1, value2, "prodDesc");
            return (Criteria) this;
        }

        public Criteria andProdStatusIsNull() {
            addCriterion("prod_status is null");
            return (Criteria) this;
        }

        public Criteria andProdStatusIsNotNull() {
            addCriterion("prod_status is not null");
            return (Criteria) this;
        }

        public Criteria andProdStatusEqualTo(Byte value) {
            addCriterion("prod_status =", value, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusNotEqualTo(Byte value) {
            addCriterion("prod_status <>", value, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusGreaterThan(Byte value) {
            addCriterion("prod_status >", value, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("prod_status >=", value, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusLessThan(Byte value) {
            addCriterion("prod_status <", value, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusLessThanOrEqualTo(Byte value) {
            addCriterion("prod_status <=", value, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusIn(List<Byte> values) {
            addCriterion("prod_status in", values, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusNotIn(List<Byte> values) {
            addCriterion("prod_status not in", values, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusBetween(Byte value1, Byte value2) {
            addCriterion("prod_status between", value1, value2, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andProdStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("prod_status not between", value1, value2, "prodStatus");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNull() {
            addCriterion("money is null");
            return (Criteria) this;
        }

        public Criteria andMoneyIsNotNull() {
            addCriterion("money is not null");
            return (Criteria) this;
        }

        public Criteria andMoneyEqualTo(Long value) {
            addCriterion("money =", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotEqualTo(Long value) {
            addCriterion("money <>", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThan(Long value) {
            addCriterion("money >", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("money >=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThan(Long value) {
            addCriterion("money <", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyLessThanOrEqualTo(Long value) {
            addCriterion("money <=", value, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyIn(List<Long> values) {
            addCriterion("money in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotIn(List<Long> values) {
            addCriterion("money not in", values, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyBetween(Long value1, Long value2) {
            addCriterion("money between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andMoneyNotBetween(Long value1, Long value2) {
            addCriterion("money not between", value1, value2, "money");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateIsNull() {
            addCriterion("change_gold_rate is null");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateIsNotNull() {
            addCriterion("change_gold_rate is not null");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateEqualTo(Integer value) {
            addCriterion("change_gold_rate =", value, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateNotEqualTo(Integer value) {
            addCriterion("change_gold_rate <>", value, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateGreaterThan(Integer value) {
            addCriterion("change_gold_rate >", value, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateGreaterThanOrEqualTo(Integer value) {
            addCriterion("change_gold_rate >=", value, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateLessThan(Integer value) {
            addCriterion("change_gold_rate <", value, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateLessThanOrEqualTo(Integer value) {
            addCriterion("change_gold_rate <=", value, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateIn(List<Integer> values) {
            addCriterion("change_gold_rate in", values, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateNotIn(List<Integer> values) {
            addCriterion("change_gold_rate not in", values, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateBetween(Integer value1, Integer value2) {
            addCriterion("change_gold_rate between", value1, value2, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andChangeGoldRateNotBetween(Integer value1, Integer value2) {
            addCriterion("change_gold_rate not between", value1, value2, "changeGoldRate");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumIsNull() {
            addCriterion("gift_gold_num is null");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumIsNotNull() {
            addCriterion("gift_gold_num is not null");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumEqualTo(Integer value) {
            addCriterion("gift_gold_num =", value, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumNotEqualTo(Integer value) {
            addCriterion("gift_gold_num <>", value, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumGreaterThan(Integer value) {
            addCriterion("gift_gold_num >", value, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("gift_gold_num >=", value, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumLessThan(Integer value) {
            addCriterion("gift_gold_num <", value, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumLessThanOrEqualTo(Integer value) {
            addCriterion("gift_gold_num <=", value, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumIn(List<Integer> values) {
            addCriterion("gift_gold_num in", values, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumNotIn(List<Integer> values) {
            addCriterion("gift_gold_num not in", values, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumBetween(Integer value1, Integer value2) {
            addCriterion("gift_gold_num between", value1, value2, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andGiftGoldNumNotBetween(Integer value1, Integer value2) {
            addCriterion("gift_gold_num not between", value1, value2, "giftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumIsNull() {
            addCriterion("first_gift_gold_num is null");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumIsNotNull() {
            addCriterion("first_gift_gold_num is not null");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumEqualTo(Integer value) {
            addCriterion("first_gift_gold_num =", value, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumNotEqualTo(Integer value) {
            addCriterion("first_gift_gold_num <>", value, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumGreaterThan(Integer value) {
            addCriterion("first_gift_gold_num >", value, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("first_gift_gold_num >=", value, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumLessThan(Integer value) {
            addCriterion("first_gift_gold_num <", value, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumLessThanOrEqualTo(Integer value) {
            addCriterion("first_gift_gold_num <=", value, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumIn(List<Integer> values) {
            addCriterion("first_gift_gold_num in", values, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumNotIn(List<Integer> values) {
            addCriterion("first_gift_gold_num not in", values, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumBetween(Integer value1, Integer value2) {
            addCriterion("first_gift_gold_num between", value1, value2, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andFirstGiftGoldNumNotBetween(Integer value1, Integer value2) {
            addCriterion("first_gift_gold_num not between", value1, value2, "firstGiftGoldNum");
            return (Criteria) this;
        }

        public Criteria andChannelIsNull() {
            addCriterion("channel is null");
            return (Criteria) this;
        }

        public Criteria andChannelIsNotNull() {
            addCriterion("channel is not null");
            return (Criteria) this;
        }

        public Criteria andChannelEqualTo(Byte value) {
            addCriterion("channel =", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotEqualTo(Byte value) {
            addCriterion("channel <>", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThan(Byte value) {
            addCriterion("channel >", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThanOrEqualTo(Byte value) {
            addCriterion("channel >=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThan(Byte value) {
            addCriterion("channel <", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThanOrEqualTo(Byte value) {
            addCriterion("channel <=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelIn(List<Byte> values) {
            addCriterion("channel in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotIn(List<Byte> values) {
            addCriterion("channel not in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelBetween(Byte value1, Byte value2) {
            addCriterion("channel between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotBetween(Byte value1, Byte value2) {
            addCriterion("channel not between", value1, value2, "channel");
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

        public Criteria andSeqNoEqualTo(Byte value) {
            addCriterion("seq_no =", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotEqualTo(Byte value) {
            addCriterion("seq_no <>", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThan(Byte value) {
            addCriterion("seq_no >", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThanOrEqualTo(Byte value) {
            addCriterion("seq_no >=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThan(Byte value) {
            addCriterion("seq_no <", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThanOrEqualTo(Byte value) {
            addCriterion("seq_no <=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoIn(List<Byte> values) {
            addCriterion("seq_no in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotIn(List<Byte> values) {
            addCriterion("seq_no not in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoBetween(Byte value1, Byte value2) {
            addCriterion("seq_no between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotBetween(Byte value1, Byte value2) {
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
