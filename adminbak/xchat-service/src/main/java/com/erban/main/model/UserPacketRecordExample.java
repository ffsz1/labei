package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserPacketRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserPacketRecordExample() {
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

        public Criteria andPacketIdIsNull() {
            addCriterion("packet_id is null");
            return (Criteria) this;
        }

        public Criteria andPacketIdIsNotNull() {
            addCriterion("packet_id is not null");
            return (Criteria) this;
        }

        public Criteria andPacketIdEqualTo(String value) {
            addCriterion("packet_id =", value, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdNotEqualTo(String value) {
            addCriterion("packet_id <>", value, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdGreaterThan(String value) {
            addCriterion("packet_id >", value, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdGreaterThanOrEqualTo(String value) {
            addCriterion("packet_id >=", value, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdLessThan(String value) {
            addCriterion("packet_id <", value, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdLessThanOrEqualTo(String value) {
            addCriterion("packet_id <=", value, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdLike(String value) {
            addCriterion("packet_id like", value, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdNotLike(String value) {
            addCriterion("packet_id not like", value, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdIn(List<String> values) {
            addCriterion("packet_id in", values, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdNotIn(List<String> values) {
            addCriterion("packet_id not in", values, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdBetween(String value1, String value2) {
            addCriterion("packet_id between", value1, value2, "packetId");
            return (Criteria) this;
        }

        public Criteria andPacketIdNotBetween(String value1, String value2) {
            addCriterion("packet_id not between", value1, value2, "packetId");
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

        public Criteria andSrcUidIsNull() {
            addCriterion("src_uid is null");
            return (Criteria) this;
        }

        public Criteria andSrcUidIsNotNull() {
            addCriterion("src_uid is not null");
            return (Criteria) this;
        }

        public Criteria andSrcUidEqualTo(Long value) {
            addCriterion("src_uid =", value, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidNotEqualTo(Long value) {
            addCriterion("src_uid <>", value, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidGreaterThan(Long value) {
            addCriterion("src_uid >", value, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidGreaterThanOrEqualTo(Long value) {
            addCriterion("src_uid >=", value, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidLessThan(Long value) {
            addCriterion("src_uid <", value, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidLessThanOrEqualTo(Long value) {
            addCriterion("src_uid <=", value, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidIn(List<Long> values) {
            addCriterion("src_uid in", values, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidNotIn(List<Long> values) {
            addCriterion("src_uid not in", values, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidBetween(Long value1, Long value2) {
            addCriterion("src_uid between", value1, value2, "srcUid");
            return (Criteria) this;
        }

        public Criteria andSrcUidNotBetween(Long value1, Long value2) {
            addCriterion("src_uid not between", value1, value2, "srcUid");
            return (Criteria) this;
        }

        public Criteria andTypeIsNull() {
            addCriterion("type is null");
            return (Criteria) this;
        }

        public Criteria andTypeIsNotNull() {
            addCriterion("type is not null");
            return (Criteria) this;
        }

        public Criteria andTypeEqualTo(Byte value) {
            addCriterion("type =", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotEqualTo(Byte value) {
            addCriterion("type <>", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThan(Byte value) {
            addCriterion("type >", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("type >=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThan(Byte value) {
            addCriterion("type <", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeLessThanOrEqualTo(Byte value) {
            addCriterion("type <=", value, "type");
            return (Criteria) this;
        }

        public Criteria andTypeIn(List<Byte> values) {
            addCriterion("type in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotIn(List<Byte> values) {
            addCriterion("type not in", values, "type");
            return (Criteria) this;
        }

        public Criteria andTypeBetween(Byte value1, Byte value2) {
            addCriterion("type between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("type not between", value1, value2, "type");
            return (Criteria) this;
        }

        public Criteria andObjIdIsNull() {
            addCriterion("obj_id is null");
            return (Criteria) this;
        }

        public Criteria andObjIdIsNotNull() {
            addCriterion("obj_id is not null");
            return (Criteria) this;
        }

        public Criteria andObjIdEqualTo(String value) {
            addCriterion("obj_id =", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdNotEqualTo(String value) {
            addCriterion("obj_id <>", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdGreaterThan(String value) {
            addCriterion("obj_id >", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdGreaterThanOrEqualTo(String value) {
            addCriterion("obj_id >=", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdLessThan(String value) {
            addCriterion("obj_id <", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdLessThanOrEqualTo(String value) {
            addCriterion("obj_id <=", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdLike(String value) {
            addCriterion("obj_id like", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdNotLike(String value) {
            addCriterion("obj_id not like", value, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdIn(List<String> values) {
            addCriterion("obj_id in", values, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdNotIn(List<String> values) {
            addCriterion("obj_id not in", values, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdBetween(String value1, String value2) {
            addCriterion("obj_id between", value1, value2, "objId");
            return (Criteria) this;
        }

        public Criteria andObjIdNotBetween(String value1, String value2) {
            addCriterion("obj_id not between", value1, value2, "objId");
            return (Criteria) this;
        }

        public Criteria andHasUnpackIsNull() {
            addCriterion("has_unpack is null");
            return (Criteria) this;
        }

        public Criteria andHasUnpackIsNotNull() {
            addCriterion("has_unpack is not null");
            return (Criteria) this;
        }

        public Criteria andHasUnpackEqualTo(Boolean value) {
            addCriterion("has_unpack =", value, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackNotEqualTo(Boolean value) {
            addCriterion("has_unpack <>", value, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackGreaterThan(Boolean value) {
            addCriterion("has_unpack >", value, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_unpack >=", value, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackLessThan(Boolean value) {
            addCriterion("has_unpack <", value, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackLessThanOrEqualTo(Boolean value) {
            addCriterion("has_unpack <=", value, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackIn(List<Boolean> values) {
            addCriterion("has_unpack in", values, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackNotIn(List<Boolean> values) {
            addCriterion("has_unpack not in", values, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackBetween(Boolean value1, Boolean value2) {
            addCriterion("has_unpack between", value1, value2, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andHasUnpackNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_unpack not between", value1, value2, "hasUnpack");
            return (Criteria) this;
        }

        public Criteria andPacketStatusIsNull() {
            addCriterion("packet_status is null");
            return (Criteria) this;
        }

        public Criteria andPacketStatusIsNotNull() {
            addCriterion("packet_status is not null");
            return (Criteria) this;
        }

        public Criteria andPacketStatusEqualTo(Byte value) {
            addCriterion("packet_status =", value, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusNotEqualTo(Byte value) {
            addCriterion("packet_status <>", value, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusGreaterThan(Byte value) {
            addCriterion("packet_status >", value, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("packet_status >=", value, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusLessThan(Byte value) {
            addCriterion("packet_status <", value, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusLessThanOrEqualTo(Byte value) {
            addCriterion("packet_status <=", value, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusIn(List<Byte> values) {
            addCriterion("packet_status in", values, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusNotIn(List<Byte> values) {
            addCriterion("packet_status not in", values, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusBetween(Byte value1, Byte value2) {
            addCriterion("packet_status between", value1, value2, "packetStatus");
            return (Criteria) this;
        }

        public Criteria andPacketStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("packet_status not between", value1, value2, "packetStatus");
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
