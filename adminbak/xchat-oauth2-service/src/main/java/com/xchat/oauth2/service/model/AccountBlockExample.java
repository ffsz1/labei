package com.xchat.oauth2.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountBlockExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public AccountBlockExample() {
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

        public Criteria andBlockIdIsNull() {
            addCriterion("block_id is null");
            return (Criteria) this;
        }

        public Criteria andBlockIdIsNotNull() {
            addCriterion("block_id is not null");
            return (Criteria) this;
        }

        public Criteria andBlockIdEqualTo(Integer value) {
            addCriterion("block_id =", value, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdNotEqualTo(Integer value) {
            addCriterion("block_id <>", value, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdGreaterThan(Integer value) {
            addCriterion("block_id >", value, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("block_id >=", value, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdLessThan(Integer value) {
            addCriterion("block_id <", value, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdLessThanOrEqualTo(Integer value) {
            addCriterion("block_id <=", value, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdIn(List<Integer> values) {
            addCriterion("block_id in", values, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdNotIn(List<Integer> values) {
            addCriterion("block_id not in", values, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdBetween(Integer value1, Integer value2) {
            addCriterion("block_id between", value1, value2, "blockId");
            return (Criteria) this;
        }

        public Criteria andBlockIdNotBetween(Integer value1, Integer value2) {
            addCriterion("block_id not between", value1, value2, "blockId");
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

        public Criteria andErbanNoIsNull() {
            addCriterion("erban_no is null");
            return (Criteria) this;
        }

        public Criteria andErbanNoIsNotNull() {
            addCriterion("erban_no is not null");
            return (Criteria) this;
        }

        public Criteria andErbanNoEqualTo(Long value) {
            addCriterion("erban_no =", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotEqualTo(Long value) {
            addCriterion("erban_no <>", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThan(Long value) {
            addCriterion("erban_no >", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThanOrEqualTo(Long value) {
            addCriterion("erban_no >=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThan(Long value) {
            addCriterion("erban_no <", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThanOrEqualTo(Long value) {
            addCriterion("erban_no <=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoIn(List<Long> values) {
            addCriterion("erban_no in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotIn(List<Long> values) {
            addCriterion("erban_no not in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoBetween(Long value1, Long value2) {
            addCriterion("erban_no between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotBetween(Long value1, Long value2) {
            addCriterion("erban_no not between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNull() {
            addCriterion("device_id is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNotNull() {
            addCriterion("device_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdEqualTo(String value) {
            addCriterion("device_id =", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotEqualTo(String value) {
            addCriterion("device_id <>", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThan(String value) {
            addCriterion("device_id >", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThanOrEqualTo(String value) {
            addCriterion("device_id >=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThan(String value) {
            addCriterion("device_id <", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThanOrEqualTo(String value) {
            addCriterion("device_id <=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLike(String value) {
            addCriterion("device_id like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotLike(String value) {
            addCriterion("device_id not like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIn(List<String> values) {
            addCriterion("device_id in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotIn(List<String> values) {
            addCriterion("device_id not in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdBetween(String value1, String value2) {
            addCriterion("device_id between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotBetween(String value1, String value2) {
            addCriterion("device_id not between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andBlockTypeIsNull() {
            addCriterion("block_type is null");
            return (Criteria) this;
        }

        public Criteria andBlockTypeIsNotNull() {
            addCriterion("block_type is not null");
            return (Criteria) this;
        }

        public Criteria andBlockTypeEqualTo(Byte value) {
            addCriterion("block_type =", value, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeNotEqualTo(Byte value) {
            addCriterion("block_type <>", value, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeGreaterThan(Byte value) {
            addCriterion("block_type >", value, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("block_type >=", value, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeLessThan(Byte value) {
            addCriterion("block_type <", value, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeLessThanOrEqualTo(Byte value) {
            addCriterion("block_type <=", value, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeIn(List<Byte> values) {
            addCriterion("block_type in", values, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeNotIn(List<Byte> values) {
            addCriterion("block_type not in", values, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeBetween(Byte value1, Byte value2) {
            addCriterion("block_type between", value1, value2, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("block_type not between", value1, value2, "blockType");
            return (Criteria) this;
        }

        public Criteria andBlockIpIsNull() {
            addCriterion("block_ip is null");
            return (Criteria) this;
        }

        public Criteria andBlockIpIsNotNull() {
            addCriterion("block_ip is not null");
            return (Criteria) this;
        }

        public Criteria andBlockIpEqualTo(String value) {
            addCriterion("block_ip =", value, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpNotEqualTo(String value) {
            addCriterion("block_ip <>", value, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpGreaterThan(String value) {
            addCriterion("block_ip >", value, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpGreaterThanOrEqualTo(String value) {
            addCriterion("block_ip >=", value, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpLessThan(String value) {
            addCriterion("block_ip <", value, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpLessThanOrEqualTo(String value) {
            addCriterion("block_ip <=", value, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpLike(String value) {
            addCriterion("block_ip like", value, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpNotLike(String value) {
            addCriterion("block_ip not like", value, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpIn(List<String> values) {
            addCriterion("block_ip in", values, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpNotIn(List<String> values) {
            addCriterion("block_ip not in", values, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpBetween(String value1, String value2) {
            addCriterion("block_ip between", value1, value2, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockIpNotBetween(String value1, String value2) {
            addCriterion("block_ip not between", value1, value2, "blockIp");
            return (Criteria) this;
        }

        public Criteria andBlockStatusIsNull() {
            addCriterion("block_status is null");
            return (Criteria) this;
        }

        public Criteria andBlockStatusIsNotNull() {
            addCriterion("block_status is not null");
            return (Criteria) this;
        }

        public Criteria andBlockStatusEqualTo(Byte value) {
            addCriterion("block_status =", value, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusNotEqualTo(Byte value) {
            addCriterion("block_status <>", value, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusGreaterThan(Byte value) {
            addCriterion("block_status >", value, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("block_status >=", value, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusLessThan(Byte value) {
            addCriterion("block_status <", value, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusLessThanOrEqualTo(Byte value) {
            addCriterion("block_status <=", value, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusIn(List<Byte> values) {
            addCriterion("block_status in", values, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusNotIn(List<Byte> values) {
            addCriterion("block_status not in", values, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusBetween(Byte value1, Byte value2) {
            addCriterion("block_status between", value1, value2, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("block_status not between", value1, value2, "blockStatus");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteIsNull() {
            addCriterion("block_minute is null");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteIsNotNull() {
            addCriterion("block_minute is not null");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteEqualTo(Integer value) {
            addCriterion("block_minute =", value, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteNotEqualTo(Integer value) {
            addCriterion("block_minute <>", value, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteGreaterThan(Integer value) {
            addCriterion("block_minute >", value, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteGreaterThanOrEqualTo(Integer value) {
            addCriterion("block_minute >=", value, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteLessThan(Integer value) {
            addCriterion("block_minute <", value, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteLessThanOrEqualTo(Integer value) {
            addCriterion("block_minute <=", value, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteIn(List<Integer> values) {
            addCriterion("block_minute in", values, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteNotIn(List<Integer> values) {
            addCriterion("block_minute not in", values, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteBetween(Integer value1, Integer value2) {
            addCriterion("block_minute between", value1, value2, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockMinuteNotBetween(Integer value1, Integer value2) {
            addCriterion("block_minute not between", value1, value2, "blockMinute");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeIsNull() {
            addCriterion("block_start_time is null");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeIsNotNull() {
            addCriterion("block_start_time is not null");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeEqualTo(Date value) {
            addCriterion("block_start_time =", value, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeNotEqualTo(Date value) {
            addCriterion("block_start_time <>", value, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeGreaterThan(Date value) {
            addCriterion("block_start_time >", value, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("block_start_time >=", value, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeLessThan(Date value) {
            addCriterion("block_start_time <", value, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("block_start_time <=", value, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeIn(List<Date> values) {
            addCriterion("block_start_time in", values, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeNotIn(List<Date> values) {
            addCriterion("block_start_time not in", values, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeBetween(Date value1, Date value2) {
            addCriterion("block_start_time between", value1, value2, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("block_start_time not between", value1, value2, "blockStartTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeIsNull() {
            addCriterion("block_end_time is null");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeIsNotNull() {
            addCriterion("block_end_time is not null");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeEqualTo(Date value) {
            addCriterion("block_end_time =", value, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeNotEqualTo(Date value) {
            addCriterion("block_end_time <>", value, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeGreaterThan(Date value) {
            addCriterion("block_end_time >", value, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("block_end_time >=", value, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeLessThan(Date value) {
            addCriterion("block_end_time <", value, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("block_end_time <=", value, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeIn(List<Date> values) {
            addCriterion("block_end_time in", values, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeNotIn(List<Date> values) {
            addCriterion("block_end_time not in", values, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeBetween(Date value1, Date value2) {
            addCriterion("block_end_time between", value1, value2, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("block_end_time not between", value1, value2, "blockEndTime");
            return (Criteria) this;
        }

        public Criteria andBlockDescIsNull() {
            addCriterion("block_desc is null");
            return (Criteria) this;
        }

        public Criteria andBlockDescIsNotNull() {
            addCriterion("block_desc is not null");
            return (Criteria) this;
        }

        public Criteria andBlockDescEqualTo(String value) {
            addCriterion("block_desc =", value, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescNotEqualTo(String value) {
            addCriterion("block_desc <>", value, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescGreaterThan(String value) {
            addCriterion("block_desc >", value, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescGreaterThanOrEqualTo(String value) {
            addCriterion("block_desc >=", value, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescLessThan(String value) {
            addCriterion("block_desc <", value, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescLessThanOrEqualTo(String value) {
            addCriterion("block_desc <=", value, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescLike(String value) {
            addCriterion("block_desc like", value, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescNotLike(String value) {
            addCriterion("block_desc not like", value, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescIn(List<String> values) {
            addCriterion("block_desc in", values, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescNotIn(List<String> values) {
            addCriterion("block_desc not in", values, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescBetween(String value1, String value2) {
            addCriterion("block_desc between", value1, value2, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andBlockDescNotBetween(String value1, String value2) {
            addCriterion("block_desc not between", value1, value2, "blockDesc");
            return (Criteria) this;
        }

        public Criteria andAdminIdIsNull() {
            addCriterion("admin_id is null");
            return (Criteria) this;
        }

        public Criteria andAdminIdIsNotNull() {
            addCriterion("admin_id is not null");
            return (Criteria) this;
        }

        public Criteria andAdminIdEqualTo(Integer value) {
            addCriterion("admin_id =", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdNotEqualTo(Integer value) {
            addCriterion("admin_id <>", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdGreaterThan(Integer value) {
            addCriterion("admin_id >", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("admin_id >=", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdLessThan(Integer value) {
            addCriterion("admin_id <", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdLessThanOrEqualTo(Integer value) {
            addCriterion("admin_id <=", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdIn(List<Integer> values) {
            addCriterion("admin_id in", values, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdNotIn(List<Integer> values) {
            addCriterion("admin_id not in", values, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdBetween(Integer value1, Integer value2) {
            addCriterion("admin_id between", value1, value2, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdNotBetween(Integer value1, Integer value2) {
            addCriterion("admin_id not between", value1, value2, "adminId");
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
