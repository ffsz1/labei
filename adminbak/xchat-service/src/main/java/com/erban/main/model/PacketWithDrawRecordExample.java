package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacketWithDrawRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PacketWithDrawRecordExample() {
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

        public Criteria andRecordIdIsNull() {
            addCriterion("record_id is null");
            return (Criteria) this;
        }

        public Criteria andRecordIdIsNotNull() {
            addCriterion("record_id is not null");
            return (Criteria) this;
        }

        public Criteria andRecordIdEqualTo(String value) {
            addCriterion("record_id =", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdNotEqualTo(String value) {
            addCriterion("record_id <>", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdGreaterThan(String value) {
            addCriterion("record_id >", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdGreaterThanOrEqualTo(String value) {
            addCriterion("record_id >=", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdLessThan(String value) {
            addCriterion("record_id <", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdLessThanOrEqualTo(String value) {
            addCriterion("record_id <=", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdLike(String value) {
            addCriterion("record_id like", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdNotLike(String value) {
            addCriterion("record_id not like", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdIn(List<String> values) {
            addCriterion("record_id in", values, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdNotIn(List<String> values) {
            addCriterion("record_id not in", values, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdBetween(String value1, String value2) {
            addCriterion("record_id between", value1, value2, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdNotBetween(String value1, String value2) {
            addCriterion("record_id not between", value1, value2, "recordId");
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

        public Criteria andRecordStatusIsNull() {
            addCriterion("record_status is null");
            return (Criteria) this;
        }

        public Criteria andRecordStatusIsNotNull() {
            addCriterion("record_status is not null");
            return (Criteria) this;
        }

        public Criteria andRecordStatusEqualTo(Byte value) {
            addCriterion("record_status =", value, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusNotEqualTo(Byte value) {
            addCriterion("record_status <>", value, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusGreaterThan(Byte value) {
            addCriterion("record_status >", value, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("record_status >=", value, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusLessThan(Byte value) {
            addCriterion("record_status <", value, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusLessThanOrEqualTo(Byte value) {
            addCriterion("record_status <=", value, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusIn(List<Byte> values) {
            addCriterion("record_status in", values, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusNotIn(List<Byte> values) {
            addCriterion("record_status not in", values, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusBetween(Byte value1, Byte value2) {
            addCriterion("record_status between", value1, value2, "recordStatus");
            return (Criteria) this;
        }

        public Criteria andRecordStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("record_status not between", value1, value2, "recordStatus");
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

        public Criteria andBankCardNameIsNull() {
            addCriterion("bank_card_name is null");
            return (Criteria) this;
        }

        public Criteria andBankCardNameIsNotNull() {
            addCriterion("bank_card_name is not null");
            return (Criteria) this;
        }

        public Criteria andBankCardNameEqualTo(String value) {
            addCriterion("bank_card_name =", value, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameNotEqualTo(String value) {
            addCriterion("bank_card_name <>", value, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameGreaterThan(String value) {
            addCriterion("bank_card_name >", value, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameGreaterThanOrEqualTo(String value) {
            addCriterion("bank_card_name >=", value, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameLessThan(String value) {
            addCriterion("bank_card_name <", value, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameLessThanOrEqualTo(String value) {
            addCriterion("bank_card_name <=", value, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameLike(String value) {
            addCriterion("bank_card_name like", value, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameNotLike(String value) {
            addCriterion("bank_card_name not like", value, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameIn(List<String> values) {
            addCriterion("bank_card_name in", values, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameNotIn(List<String> values) {
            addCriterion("bank_card_name not in", values, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameBetween(String value1, String value2) {
            addCriterion("bank_card_name between", value1, value2, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andBankCardNameNotBetween(String value1, String value2) {
            addCriterion("bank_card_name not between", value1, value2, "bankCardName");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeIsNull() {
            addCriterion("open_bank_code is null");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeIsNotNull() {
            addCriterion("open_bank_code is not null");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeEqualTo(String value) {
            addCriterion("open_bank_code =", value, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeNotEqualTo(String value) {
            addCriterion("open_bank_code <>", value, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeGreaterThan(String value) {
            addCriterion("open_bank_code >", value, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeGreaterThanOrEqualTo(String value) {
            addCriterion("open_bank_code >=", value, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeLessThan(String value) {
            addCriterion("open_bank_code <", value, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeLessThanOrEqualTo(String value) {
            addCriterion("open_bank_code <=", value, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeLike(String value) {
            addCriterion("open_bank_code like", value, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeNotLike(String value) {
            addCriterion("open_bank_code not like", value, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeIn(List<String> values) {
            addCriterion("open_bank_code in", values, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeNotIn(List<String> values) {
            addCriterion("open_bank_code not in", values, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeBetween(String value1, String value2) {
            addCriterion("open_bank_code between", value1, value2, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andOpenBankCodeNotBetween(String value1, String value2) {
            addCriterion("open_bank_code not between", value1, value2, "openBankCode");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeIsNull() {
            addCriterion("real_tran_type is null");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeIsNotNull() {
            addCriterion("real_tran_type is not null");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeEqualTo(Boolean value) {
            addCriterion("real_tran_type =", value, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeNotEqualTo(Boolean value) {
            addCriterion("real_tran_type <>", value, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeGreaterThan(Boolean value) {
            addCriterion("real_tran_type >", value, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeGreaterThanOrEqualTo(Boolean value) {
            addCriterion("real_tran_type >=", value, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeLessThan(Boolean value) {
            addCriterion("real_tran_type <", value, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeLessThanOrEqualTo(Boolean value) {
            addCriterion("real_tran_type <=", value, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeIn(List<Boolean> values) {
            addCriterion("real_tran_type in", values, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeNotIn(List<Boolean> values) {
            addCriterion("real_tran_type not in", values, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeBetween(Boolean value1, Boolean value2) {
            addCriterion("real_tran_type between", value1, value2, "realTranType");
            return (Criteria) this;
        }

        public Criteria andRealTranTypeNotBetween(Boolean value1, Boolean value2) {
            addCriterion("real_tran_type not between", value1, value2, "realTranType");
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
