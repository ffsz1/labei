package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChargeRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ChargeRecordExample() {
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

        public Criteria andChargeRecordIdIsNull() {
            addCriterion("charge_record_id is null");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdIsNotNull() {
            addCriterion("charge_record_id is not null");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdEqualTo(String value) {
            addCriterion("charge_record_id =", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdNotEqualTo(String value) {
            addCriterion("charge_record_id <>", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdGreaterThan(String value) {
            addCriterion("charge_record_id >", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdGreaterThanOrEqualTo(String value) {
            addCriterion("charge_record_id >=", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdLessThan(String value) {
            addCriterion("charge_record_id <", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdLessThanOrEqualTo(String value) {
            addCriterion("charge_record_id <=", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdLike(String value) {
            addCriterion("charge_record_id like", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdNotLike(String value) {
            addCriterion("charge_record_id not like", value, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdIn(List<String> values) {
            addCriterion("charge_record_id in", values, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdNotIn(List<String> values) {
            addCriterion("charge_record_id not in", values, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdBetween(String value1, String value2) {
            addCriterion("charge_record_id between", value1, value2, "chargeRecordId");
            return (Criteria) this;
        }

        public Criteria andChargeRecordIdNotBetween(String value1, String value2) {
            addCriterion("charge_record_id not between", value1, value2, "chargeRecordId");
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

        public Criteria andRoomUidIsNull() {
            addCriterion("room_uid is null");
            return (Criteria) this;
        }

        public Criteria andRoomUidIsNotNull() {
            addCriterion("room_uid is not null");
            return (Criteria) this;
        }

        public Criteria andRoomUidEqualTo(Long value) {
            addCriterion("room_uid =", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotEqualTo(Long value) {
            addCriterion("room_uid <>", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidGreaterThan(Long value) {
            addCriterion("room_uid >", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidGreaterThanOrEqualTo(Long value) {
            addCriterion("room_uid >=", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidLessThan(Long value) {
            addCriterion("room_uid <", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidLessThanOrEqualTo(Long value) {
            addCriterion("room_uid <=", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidIn(List<Long> values) {
            addCriterion("room_uid in", values, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotIn(List<Long> values) {
            addCriterion("room_uid not in", values, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidBetween(Long value1, Long value2) {
            addCriterion("room_uid between", value1, value2, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotBetween(Long value1, Long value2) {
            addCriterion("room_uid not between", value1, value2, "roomUid");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdIsNull() {
            addCriterion("pingxx_charge_id is null");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdIsNotNull() {
            addCriterion("pingxx_charge_id is not null");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdEqualTo(String value) {
            addCriterion("pingxx_charge_id =", value, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdNotEqualTo(String value) {
            addCriterion("pingxx_charge_id <>", value, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdGreaterThan(String value) {
            addCriterion("pingxx_charge_id >", value, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdGreaterThanOrEqualTo(String value) {
            addCriterion("pingxx_charge_id >=", value, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdLessThan(String value) {
            addCriterion("pingxx_charge_id <", value, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdLessThanOrEqualTo(String value) {
            addCriterion("pingxx_charge_id <=", value, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdLike(String value) {
            addCriterion("pingxx_charge_id like", value, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdNotLike(String value) {
            addCriterion("pingxx_charge_id not like", value, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdIn(List<String> values) {
            addCriterion("pingxx_charge_id in", values, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdNotIn(List<String> values) {
            addCriterion("pingxx_charge_id not in", values, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdBetween(String value1, String value2) {
            addCriterion("pingxx_charge_id between", value1, value2, "pingxxChargeId");
            return (Criteria) this;
        }

        public Criteria andPingxxChargeIdNotBetween(String value1, String value2) {
            addCriterion("pingxx_charge_id not between", value1, value2, "pingxxChargeId");
            return (Criteria) this;
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

        public Criteria andChannelIsNull() {
            addCriterion("channel is null");
            return (Criteria) this;
        }

        public Criteria andChannelIsNotNull() {
            addCriterion("channel is not null");
            return (Criteria) this;
        }

        public Criteria andChannelEqualTo(String value) {
            addCriterion("channel =", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotEqualTo(String value) {
            addCriterion("channel <>", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThan(String value) {
            addCriterion("channel >", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThanOrEqualTo(String value) {
            addCriterion("channel >=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThan(String value) {
            addCriterion("channel <", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThanOrEqualTo(String value) {
            addCriterion("channel <=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLike(String value) {
            addCriterion("channel like", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotLike(String value) {
            addCriterion("channel not like", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelIn(List<String> values) {
            addCriterion("channel in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotIn(List<String> values) {
            addCriterion("channel not in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelBetween(String value1, String value2) {
            addCriterion("channel between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotBetween(String value1, String value2) {
            addCriterion("channel not between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andBussTypeIsNull() {
            addCriterion("buss_type is null");
            return (Criteria) this;
        }

        public Criteria andBussTypeIsNotNull() {
            addCriterion("buss_type is not null");
            return (Criteria) this;
        }

        public Criteria andBussTypeEqualTo(Byte value) {
            addCriterion("buss_type =", value, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeNotEqualTo(Byte value) {
            addCriterion("buss_type <>", value, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeGreaterThan(Byte value) {
            addCriterion("buss_type >", value, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("buss_type >=", value, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeLessThan(Byte value) {
            addCriterion("buss_type <", value, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeLessThanOrEqualTo(Byte value) {
            addCriterion("buss_type <=", value, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeIn(List<Byte> values) {
            addCriterion("buss_type in", values, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeNotIn(List<Byte> values) {
            addCriterion("buss_type not in", values, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeBetween(Byte value1, Byte value2) {
            addCriterion("buss_type between", value1, value2, "bussType");
            return (Criteria) this;
        }

        public Criteria andBussTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("buss_type not between", value1, value2, "bussType");
            return (Criteria) this;
        }

        public Criteria andChargeStatusIsNull() {
            addCriterion("charge_status is null");
            return (Criteria) this;
        }

        public Criteria andChargeStatusIsNotNull() {
            addCriterion("charge_status is not null");
            return (Criteria) this;
        }

        public Criteria andChargeStatusEqualTo(Byte value) {
            addCriterion("charge_status =", value, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusNotEqualTo(Byte value) {
            addCriterion("charge_status <>", value, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusGreaterThan(Byte value) {
            addCriterion("charge_status >", value, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("charge_status >=", value, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusLessThan(Byte value) {
            addCriterion("charge_status <", value, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusLessThanOrEqualTo(Byte value) {
            addCriterion("charge_status <=", value, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusIn(List<Byte> values) {
            addCriterion("charge_status in", values, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusNotIn(List<Byte> values) {
            addCriterion("charge_status not in", values, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusBetween(Byte value1, Byte value2) {
            addCriterion("charge_status between", value1, value2, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("charge_status not between", value1, value2, "chargeStatus");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescIsNull() {
            addCriterion("charge_status_desc is null");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescIsNotNull() {
            addCriterion("charge_status_desc is not null");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescEqualTo(String value) {
            addCriterion("charge_status_desc =", value, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescNotEqualTo(String value) {
            addCriterion("charge_status_desc <>", value, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescGreaterThan(String value) {
            addCriterion("charge_status_desc >", value, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescGreaterThanOrEqualTo(String value) {
            addCriterion("charge_status_desc >=", value, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescLessThan(String value) {
            addCriterion("charge_status_desc <", value, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescLessThanOrEqualTo(String value) {
            addCriterion("charge_status_desc <=", value, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescLike(String value) {
            addCriterion("charge_status_desc like", value, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescNotLike(String value) {
            addCriterion("charge_status_desc not like", value, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescIn(List<String> values) {
            addCriterion("charge_status_desc in", values, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescNotIn(List<String> values) {
            addCriterion("charge_status_desc not in", values, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescBetween(String value1, String value2) {
            addCriterion("charge_status_desc between", value1, value2, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andChargeStatusDescNotBetween(String value1, String value2) {
            addCriterion("charge_status_desc not between", value1, value2, "chargeStatusDesc");
            return (Criteria) this;
        }

        public Criteria andAmountIsNull() {
            addCriterion("amount is null");
            return (Criteria) this;
        }

        public Criteria andAmountIsNotNull() {
            addCriterion("amount is not null");
            return (Criteria) this;
        }

        public Criteria andAmountEqualTo(Long value) {
            addCriterion("amount =", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotEqualTo(Long value) {
            addCriterion("amount <>", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThan(Long value) {
            addCriterion("amount >", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("amount >=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThan(Long value) {
            addCriterion("amount <", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountLessThanOrEqualTo(Long value) {
            addCriterion("amount <=", value, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountIn(List<Long> values) {
            addCriterion("amount in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotIn(List<Long> values) {
            addCriterion("amount not in", values, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountBetween(Long value1, Long value2) {
            addCriterion("amount between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andAmountNotBetween(Long value1, Long value2) {
            addCriterion("amount not between", value1, value2, "amount");
            return (Criteria) this;
        }

        public Criteria andTotalGoldIsNull() {
            addCriterion("total_gold is null");
            return (Criteria) this;
        }

        public Criteria andTotalGoldIsNotNull() {
            addCriterion("total_gold is not null");
            return (Criteria) this;
        }

        public Criteria andTotalGoldEqualTo(Long value) {
            addCriterion("total_gold =", value, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNotEqualTo(Long value) {
            addCriterion("total_gold <>", value, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldGreaterThan(Long value) {
            addCriterion("total_gold >", value, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldGreaterThanOrEqualTo(Long value) {
            addCriterion("total_gold >=", value, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldLessThan(Long value) {
            addCriterion("total_gold <", value, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldLessThanOrEqualTo(Long value) {
            addCriterion("total_gold <=", value, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldIn(List<Long> values) {
            addCriterion("total_gold in", values, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNotIn(List<Long> values) {
            addCriterion("total_gold not in", values, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldBetween(Long value1, Long value2) {
            addCriterion("total_gold between", value1, value2, "totalGold");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNotBetween(Long value1, Long value2) {
            addCriterion("total_gold not between", value1, value2, "totalGold");
            return (Criteria) this;
        }

        public Criteria andClientIpIsNull() {
            addCriterion("client_ip is null");
            return (Criteria) this;
        }

        public Criteria andClientIpIsNotNull() {
            addCriterion("client_ip is not null");
            return (Criteria) this;
        }

        public Criteria andClientIpEqualTo(String value) {
            addCriterion("client_ip =", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotEqualTo(String value) {
            addCriterion("client_ip <>", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpGreaterThan(String value) {
            addCriterion("client_ip >", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpGreaterThanOrEqualTo(String value) {
            addCriterion("client_ip >=", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLessThan(String value) {
            addCriterion("client_ip <", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLessThanOrEqualTo(String value) {
            addCriterion("client_ip <=", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpLike(String value) {
            addCriterion("client_ip like", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotLike(String value) {
            addCriterion("client_ip not like", value, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpIn(List<String> values) {
            addCriterion("client_ip in", values, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotIn(List<String> values) {
            addCriterion("client_ip not in", values, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpBetween(String value1, String value2) {
            addCriterion("client_ip between", value1, value2, "clientIp");
            return (Criteria) this;
        }

        public Criteria andClientIpNotBetween(String value1, String value2) {
            addCriterion("client_ip not between", value1, value2, "clientIp");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidIsNull() {
            addCriterion("wx_pub_openid is null");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidIsNotNull() {
            addCriterion("wx_pub_openid is not null");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidEqualTo(String value) {
            addCriterion("wx_pub_openid =", value, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidNotEqualTo(String value) {
            addCriterion("wx_pub_openid <>", value, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidGreaterThan(String value) {
            addCriterion("wx_pub_openid >", value, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidGreaterThanOrEqualTo(String value) {
            addCriterion("wx_pub_openid >=", value, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidLessThan(String value) {
            addCriterion("wx_pub_openid <", value, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidLessThanOrEqualTo(String value) {
            addCriterion("wx_pub_openid <=", value, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidLike(String value) {
            addCriterion("wx_pub_openid like", value, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidNotLike(String value) {
            addCriterion("wx_pub_openid not like", value, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidIn(List<String> values) {
            addCriterion("wx_pub_openid in", values, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidNotIn(List<String> values) {
            addCriterion("wx_pub_openid not in", values, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidBetween(String value1, String value2) {
            addCriterion("wx_pub_openid between", value1, value2, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubOpenidNotBetween(String value1, String value2) {
            addCriterion("wx_pub_openid not between", value1, value2, "wxPubOpenid");
            return (Criteria) this;
        }

        public Criteria andSubjectIsNull() {
            addCriterion("subject is null");
            return (Criteria) this;
        }

        public Criteria andSubjectIsNotNull() {
            addCriterion("subject is not null");
            return (Criteria) this;
        }

        public Criteria andSubjectEqualTo(String value) {
            addCriterion("subject =", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectNotEqualTo(String value) {
            addCriterion("subject <>", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectGreaterThan(String value) {
            addCriterion("subject >", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectGreaterThanOrEqualTo(String value) {
            addCriterion("subject >=", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectLessThan(String value) {
            addCriterion("subject <", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectLessThanOrEqualTo(String value) {
            addCriterion("subject <=", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectLike(String value) {
            addCriterion("subject like", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectNotLike(String value) {
            addCriterion("subject not like", value, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectIn(List<String> values) {
            addCriterion("subject in", values, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectNotIn(List<String> values) {
            addCriterion("subject not in", values, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectBetween(String value1, String value2) {
            addCriterion("subject between", value1, value2, "subject");
            return (Criteria) this;
        }

        public Criteria andSubjectNotBetween(String value1, String value2) {
            addCriterion("subject not between", value1, value2, "subject");
            return (Criteria) this;
        }

        public Criteria andBodyIsNull() {
            addCriterion("body is null");
            return (Criteria) this;
        }

        public Criteria andBodyIsNotNull() {
            addCriterion("body is not null");
            return (Criteria) this;
        }

        public Criteria andBodyEqualTo(String value) {
            addCriterion("body =", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotEqualTo(String value) {
            addCriterion("body <>", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyGreaterThan(String value) {
            addCriterion("body >", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyGreaterThanOrEqualTo(String value) {
            addCriterion("body >=", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLessThan(String value) {
            addCriterion("body <", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLessThanOrEqualTo(String value) {
            addCriterion("body <=", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyLike(String value) {
            addCriterion("body like", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotLike(String value) {
            addCriterion("body not like", value, "body");
            return (Criteria) this;
        }

        public Criteria andBodyIn(List<String> values) {
            addCriterion("body in", values, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotIn(List<String> values) {
            addCriterion("body not in", values, "body");
            return (Criteria) this;
        }

        public Criteria andBodyBetween(String value1, String value2) {
            addCriterion("body between", value1, value2, "body");
            return (Criteria) this;
        }

        public Criteria andBodyNotBetween(String value1, String value2) {
            addCriterion("body not between", value1, value2, "body");
            return (Criteria) this;
        }

        public Criteria andExtraIsNull() {
            addCriterion("extra is null");
            return (Criteria) this;
        }

        public Criteria andExtraIsNotNull() {
            addCriterion("extra is not null");
            return (Criteria) this;
        }

        public Criteria andExtraEqualTo(String value) {
            addCriterion("extra =", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotEqualTo(String value) {
            addCriterion("extra <>", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraGreaterThan(String value) {
            addCriterion("extra >", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraGreaterThanOrEqualTo(String value) {
            addCriterion("extra >=", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLessThan(String value) {
            addCriterion("extra <", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLessThanOrEqualTo(String value) {
            addCriterion("extra <=", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraLike(String value) {
            addCriterion("extra like", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotLike(String value) {
            addCriterion("extra not like", value, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraIn(List<String> values) {
            addCriterion("extra in", values, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotIn(List<String> values) {
            addCriterion("extra not in", values, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraBetween(String value1, String value2) {
            addCriterion("extra between", value1, value2, "extra");
            return (Criteria) this;
        }

        public Criteria andExtraNotBetween(String value1, String value2) {
            addCriterion("extra not between", value1, value2, "extra");
            return (Criteria) this;
        }

        public Criteria andMetadataIsNull() {
            addCriterion("metadata is null");
            return (Criteria) this;
        }

        public Criteria andMetadataIsNotNull() {
            addCriterion("metadata is not null");
            return (Criteria) this;
        }

        public Criteria andMetadataEqualTo(String value) {
            addCriterion("metadata =", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataNotEqualTo(String value) {
            addCriterion("metadata <>", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataGreaterThan(String value) {
            addCriterion("metadata >", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataGreaterThanOrEqualTo(String value) {
            addCriterion("metadata >=", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataLessThan(String value) {
            addCriterion("metadata <", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataLessThanOrEqualTo(String value) {
            addCriterion("metadata <=", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataLike(String value) {
            addCriterion("metadata like", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataNotLike(String value) {
            addCriterion("metadata not like", value, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataIn(List<String> values) {
            addCriterion("metadata in", values, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataNotIn(List<String> values) {
            addCriterion("metadata not in", values, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataBetween(String value1, String value2) {
            addCriterion("metadata between", value1, value2, "metadata");
            return (Criteria) this;
        }

        public Criteria andMetadataNotBetween(String value1, String value2) {
            addCriterion("metadata not between", value1, value2, "metadata");
            return (Criteria) this;
        }

        public Criteria andChargeDescIsNull() {
            addCriterion("charge_desc is null");
            return (Criteria) this;
        }

        public Criteria andChargeDescIsNotNull() {
            addCriterion("charge_desc is not null");
            return (Criteria) this;
        }

        public Criteria andChargeDescEqualTo(String value) {
            addCriterion("charge_desc =", value, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescNotEqualTo(String value) {
            addCriterion("charge_desc <>", value, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescGreaterThan(String value) {
            addCriterion("charge_desc >", value, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescGreaterThanOrEqualTo(String value) {
            addCriterion("charge_desc >=", value, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescLessThan(String value) {
            addCriterion("charge_desc <", value, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescLessThanOrEqualTo(String value) {
            addCriterion("charge_desc <=", value, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescLike(String value) {
            addCriterion("charge_desc like", value, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescNotLike(String value) {
            addCriterion("charge_desc not like", value, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescIn(List<String> values) {
            addCriterion("charge_desc in", values, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescNotIn(List<String> values) {
            addCriterion("charge_desc not in", values, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescBetween(String value1, String value2) {
            addCriterion("charge_desc between", value1, value2, "chargeDesc");
            return (Criteria) this;
        }

        public Criteria andChargeDescNotBetween(String value1, String value2) {
            addCriterion("charge_desc not between", value1, value2, "chargeDesc");
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
