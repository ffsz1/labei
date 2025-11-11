package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiftSendRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public GiftSendRecordExample() {
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

        public Criteria andSendRecordIdIsNull() {
            addCriterion("send_record_id is null");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdIsNotNull() {
            addCriterion("send_record_id is not null");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdEqualTo(Long value) {
            addCriterion("send_record_id =", value, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdNotEqualTo(Long value) {
            addCriterion("send_record_id <>", value, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdGreaterThan(Long value) {
            addCriterion("send_record_id >", value, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdGreaterThanOrEqualTo(Long value) {
            addCriterion("send_record_id >=", value, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdLessThan(Long value) {
            addCriterion("send_record_id <", value, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdLessThanOrEqualTo(Long value) {
            addCriterion("send_record_id <=", value, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdIn(List<Long> values) {
            addCriterion("send_record_id in", values, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdNotIn(List<Long> values) {
            addCriterion("send_record_id not in", values, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdBetween(Long value1, Long value2) {
            addCriterion("send_record_id between", value1, value2, "sendRecordId");
            return (Criteria) this;
        }

        public Criteria andSendRecordIdNotBetween(Long value1, Long value2) {
            addCriterion("send_record_id not between", value1, value2, "sendRecordId");
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

        public Criteria andReciveUidIsNull() {
            addCriterion("recive_uid is null");
            return (Criteria) this;
        }

        public Criteria andReciveUidIsNotNull() {
            addCriterion("recive_uid is not null");
            return (Criteria) this;
        }

        public Criteria andReciveUidEqualTo(Long value) {
            addCriterion("recive_uid =", value, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidNotEqualTo(Long value) {
            addCriterion("recive_uid <>", value, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidGreaterThan(Long value) {
            addCriterion("recive_uid >", value, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidGreaterThanOrEqualTo(Long value) {
            addCriterion("recive_uid >=", value, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidLessThan(Long value) {
            addCriterion("recive_uid <", value, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidLessThanOrEqualTo(Long value) {
            addCriterion("recive_uid <=", value, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidIn(List<Long> values) {
            addCriterion("recive_uid in", values, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidNotIn(List<Long> values) {
            addCriterion("recive_uid not in", values, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidBetween(Long value1, Long value2) {
            addCriterion("recive_uid between", value1, value2, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andReciveUidNotBetween(Long value1, Long value2) {
            addCriterion("recive_uid not between", value1, value2, "reciveUid");
            return (Criteria) this;
        }

        public Criteria andSendEnvIsNull() {
            addCriterion("send_env is null");
            return (Criteria) this;
        }

        public Criteria andSendEnvIsNotNull() {
            addCriterion("send_env is not null");
            return (Criteria) this;
        }

        public Criteria andSendEnvEqualTo(Byte value) {
            addCriterion("send_env =", value, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvNotEqualTo(Byte value) {
            addCriterion("send_env <>", value, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvGreaterThan(Byte value) {
            addCriterion("send_env >", value, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvGreaterThanOrEqualTo(Byte value) {
            addCriterion("send_env >=", value, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvLessThan(Byte value) {
            addCriterion("send_env <", value, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvLessThanOrEqualTo(Byte value) {
            addCriterion("send_env <=", value, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvIn(List<Byte> values) {
            addCriterion("send_env in", values, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvNotIn(List<Byte> values) {
            addCriterion("send_env not in", values, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvBetween(Byte value1, Byte value2) {
            addCriterion("send_env between", value1, value2, "sendEnv");
            return (Criteria) this;
        }

        public Criteria andSendEnvNotBetween(Byte value1, Byte value2) {
            addCriterion("send_env not between", value1, value2, "sendEnv");
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

        public Criteria andRoomTypeIsNull() {
            addCriterion("room_type is null");
            return (Criteria) this;
        }

        public Criteria andRoomTypeIsNotNull() {
            addCriterion("room_type is not null");
            return (Criteria) this;
        }

        public Criteria andRoomTypeEqualTo(Byte value) {
            addCriterion("room_type =", value, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeNotEqualTo(Byte value) {
            addCriterion("room_type <>", value, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeGreaterThan(Byte value) {
            addCriterion("room_type >", value, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("room_type >=", value, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeLessThan(Byte value) {
            addCriterion("room_type <", value, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeLessThanOrEqualTo(Byte value) {
            addCriterion("room_type <=", value, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeIn(List<Byte> values) {
            addCriterion("room_type in", values, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeNotIn(List<Byte> values) {
            addCriterion("room_type not in", values, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeBetween(Byte value1, Byte value2) {
            addCriterion("room_type between", value1, value2, "roomType");
            return (Criteria) this;
        }

        public Criteria andRoomTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("room_type not between", value1, value2, "roomType");
            return (Criteria) this;
        }

        public Criteria andGiftIdIsNull() {
            addCriterion("gift_id is null");
            return (Criteria) this;
        }

        public Criteria andGiftIdIsNotNull() {
            addCriterion("gift_id is not null");
            return (Criteria) this;
        }

        public Criteria andGiftIdEqualTo(Integer value) {
            addCriterion("gift_id =", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdNotEqualTo(Integer value) {
            addCriterion("gift_id <>", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdGreaterThan(Integer value) {
            addCriterion("gift_id >", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("gift_id >=", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdLessThan(Integer value) {
            addCriterion("gift_id <", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdLessThanOrEqualTo(Integer value) {
            addCriterion("gift_id <=", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdIn(List<Integer> values) {
            addCriterion("gift_id in", values, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdNotIn(List<Integer> values) {
            addCriterion("gift_id not in", values, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdBetween(Integer value1, Integer value2) {
            addCriterion("gift_id between", value1, value2, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdNotBetween(Integer value1, Integer value2) {
            addCriterion("gift_id not between", value1, value2, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftNumIsNull() {
            addCriterion("gift_num is null");
            return (Criteria) this;
        }

        public Criteria andGiftNumIsNotNull() {
            addCriterion("gift_num is not null");
            return (Criteria) this;
        }

        public Criteria andGiftNumEqualTo(Integer value) {
            addCriterion("gift_num =", value, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumNotEqualTo(Integer value) {
            addCriterion("gift_num <>", value, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumGreaterThan(Integer value) {
            addCriterion("gift_num >", value, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("gift_num >=", value, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumLessThan(Integer value) {
            addCriterion("gift_num <", value, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumLessThanOrEqualTo(Integer value) {
            addCriterion("gift_num <=", value, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumIn(List<Integer> values) {
            addCriterion("gift_num in", values, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumNotIn(List<Integer> values) {
            addCriterion("gift_num not in", values, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumBetween(Integer value1, Integer value2) {
            addCriterion("gift_num between", value1, value2, "giftNum");
            return (Criteria) this;
        }

        public Criteria andGiftNumNotBetween(Integer value1, Integer value2) {
            addCriterion("gift_num not between", value1, value2, "giftNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumIsNull() {
            addCriterion("total_gold_num is null");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumIsNotNull() {
            addCriterion("total_gold_num is not null");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumEqualTo(Long value) {
            addCriterion("total_gold_num =", value, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumNotEqualTo(Long value) {
            addCriterion("total_gold_num <>", value, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumGreaterThan(Long value) {
            addCriterion("total_gold_num >", value, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumGreaterThanOrEqualTo(Long value) {
            addCriterion("total_gold_num >=", value, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumLessThan(Long value) {
            addCriterion("total_gold_num <", value, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumLessThanOrEqualTo(Long value) {
            addCriterion("total_gold_num <=", value, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumIn(List<Long> values) {
            addCriterion("total_gold_num in", values, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumNotIn(List<Long> values) {
            addCriterion("total_gold_num not in", values, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumBetween(Long value1, Long value2) {
            addCriterion("total_gold_num between", value1, value2, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalGoldNumNotBetween(Long value1, Long value2) {
            addCriterion("total_gold_num not between", value1, value2, "totalGoldNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumIsNull() {
            addCriterion("total_diamond_num is null");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumIsNotNull() {
            addCriterion("total_diamond_num is not null");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumEqualTo(Double value) {
            addCriterion("total_diamond_num =", value, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumNotEqualTo(Double value) {
            addCriterion("total_diamond_num <>", value, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumGreaterThan(Double value) {
            addCriterion("total_diamond_num >", value, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumGreaterThanOrEqualTo(Double value) {
            addCriterion("total_diamond_num >=", value, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumLessThan(Double value) {
            addCriterion("total_diamond_num <", value, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumLessThanOrEqualTo(Double value) {
            addCriterion("total_diamond_num <=", value, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumIn(List<Double> values) {
            addCriterion("total_diamond_num in", values, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumNotIn(List<Double> values) {
            addCriterion("total_diamond_num not in", values, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumBetween(Double value1, Double value2) {
            addCriterion("total_diamond_num between", value1, value2, "totalDiamondNum");
            return (Criteria) this;
        }

        public Criteria andTotalDiamondNumNotBetween(Double value1, Double value2) {
            addCriterion("total_diamond_num not between", value1, value2, "totalDiamondNum");
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
