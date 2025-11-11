package com.erban.admin.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountBannedExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Integer offset;

    public AccountBannedExample() {
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

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
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

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
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

        public Criteria andBannedTypeIsNull() {
            addCriterion("banned_type is null");
            return (Criteria) this;
        }

        public Criteria andBannedTypeIsNotNull() {
            addCriterion("banned_type is not null");
            return (Criteria) this;
        }

        public Criteria andBannedTypeEqualTo(String value) {
            addCriterion("banned_type =", value, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeNotEqualTo(String value) {
            addCriterion("banned_type <>", value, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeGreaterThan(String value) {
            addCriterion("banned_type >", value, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeGreaterThanOrEqualTo(String value) {
            addCriterion("banned_type >=", value, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeLessThan(String value) {
            addCriterion("banned_type <", value, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeLessThanOrEqualTo(String value) {
            addCriterion("banned_type <=", value, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeLike(String value) {
            addCriterion("banned_type like", value, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeNotLike(String value) {
            addCriterion("banned_type not like", value, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeIn(List<String> values) {
            addCriterion("banned_type in", values, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeNotIn(List<String> values) {
            addCriterion("banned_type not in", values, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeBetween(String value1, String value2) {
            addCriterion("banned_type between", value1, value2, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedTypeNotBetween(String value1, String value2) {
            addCriterion("banned_type not between", value1, value2, "bannedType");
            return (Criteria) this;
        }

        public Criteria andBannedIpIsNull() {
            addCriterion("banned_ip is null");
            return (Criteria) this;
        }

        public Criteria andBannedIpIsNotNull() {
            addCriterion("banned_ip is not null");
            return (Criteria) this;
        }

        public Criteria andBannedIpEqualTo(String value) {
            addCriterion("banned_ip =", value, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpNotEqualTo(String value) {
            addCriterion("banned_ip <>", value, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpGreaterThan(String value) {
            addCriterion("banned_ip >", value, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpGreaterThanOrEqualTo(String value) {
            addCriterion("banned_ip >=", value, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpLessThan(String value) {
            addCriterion("banned_ip <", value, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpLessThanOrEqualTo(String value) {
            addCriterion("banned_ip <=", value, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpLike(String value) {
            addCriterion("banned_ip like", value, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpNotLike(String value) {
            addCriterion("banned_ip not like", value, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpIn(List<String> values) {
            addCriterion("banned_ip in", values, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpNotIn(List<String> values) {
            addCriterion("banned_ip not in", values, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpBetween(String value1, String value2) {
            addCriterion("banned_ip between", value1, value2, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedIpNotBetween(String value1, String value2) {
            addCriterion("banned_ip not between", value1, value2, "bannedIp");
            return (Criteria) this;
        }

        public Criteria andBannedStatusIsNull() {
            addCriterion("banned_status is null");
            return (Criteria) this;
        }

        public Criteria andBannedStatusIsNotNull() {
            addCriterion("banned_status is not null");
            return (Criteria) this;
        }

        public Criteria andBannedStatusEqualTo(Byte value) {
            addCriterion("banned_status =", value, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusNotEqualTo(Byte value) {
            addCriterion("banned_status <>", value, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusGreaterThan(Byte value) {
            addCriterion("banned_status >", value, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("banned_status >=", value, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusLessThan(Byte value) {
            addCriterion("banned_status <", value, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusLessThanOrEqualTo(Byte value) {
            addCriterion("banned_status <=", value, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusIn(List<Byte> values) {
            addCriterion("banned_status in", values, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusNotIn(List<Byte> values) {
            addCriterion("banned_status not in", values, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusBetween(Byte value1, Byte value2) {
            addCriterion("banned_status between", value1, value2, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("banned_status not between", value1, value2, "bannedStatus");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteIsNull() {
            addCriterion("banned_minute is null");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteIsNotNull() {
            addCriterion("banned_minute is not null");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteEqualTo(Integer value) {
            addCriterion("banned_minute =", value, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteNotEqualTo(Integer value) {
            addCriterion("banned_minute <>", value, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteGreaterThan(Integer value) {
            addCriterion("banned_minute >", value, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteGreaterThanOrEqualTo(Integer value) {
            addCriterion("banned_minute >=", value, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteLessThan(Integer value) {
            addCriterion("banned_minute <", value, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteLessThanOrEqualTo(Integer value) {
            addCriterion("banned_minute <=", value, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteIn(List<Integer> values) {
            addCriterion("banned_minute in", values, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteNotIn(List<Integer> values) {
            addCriterion("banned_minute not in", values, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteBetween(Integer value1, Integer value2) {
            addCriterion("banned_minute between", value1, value2, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedMinuteNotBetween(Integer value1, Integer value2) {
            addCriterion("banned_minute not between", value1, value2, "bannedMinute");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeIsNull() {
            addCriterion("banned_start_time is null");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeIsNotNull() {
            addCriterion("banned_start_time is not null");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeEqualTo(Date value) {
            addCriterion("banned_start_time =", value, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeNotEqualTo(Date value) {
            addCriterion("banned_start_time <>", value, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeGreaterThan(Date value) {
            addCriterion("banned_start_time >", value, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("banned_start_time >=", value, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeLessThan(Date value) {
            addCriterion("banned_start_time <", value, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("banned_start_time <=", value, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeIn(List<Date> values) {
            addCriterion("banned_start_time in", values, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeNotIn(List<Date> values) {
            addCriterion("banned_start_time not in", values, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeBetween(Date value1, Date value2) {
            addCriterion("banned_start_time between", value1, value2, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("banned_start_time not between", value1, value2, "bannedStartTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeIsNull() {
            addCriterion("banned_end_time is null");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeIsNotNull() {
            addCriterion("banned_end_time is not null");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeEqualTo(Date value) {
            addCriterion("banned_end_time =", value, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeNotEqualTo(Date value) {
            addCriterion("banned_end_time <>", value, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeGreaterThan(Date value) {
            addCriterion("banned_end_time >", value, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("banned_end_time >=", value, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeLessThan(Date value) {
            addCriterion("banned_end_time <", value, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("banned_end_time <=", value, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeIn(List<Date> values) {
            addCriterion("banned_end_time in", values, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeNotIn(List<Date> values) {
            addCriterion("banned_end_time not in", values, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeBetween(Date value1, Date value2) {
            addCriterion("banned_end_time between", value1, value2, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("banned_end_time not between", value1, value2, "bannedEndTime");
            return (Criteria) this;
        }

        public Criteria andBannedDescIsNull() {
            addCriterion("banned_desc is null");
            return (Criteria) this;
        }

        public Criteria andBannedDescIsNotNull() {
            addCriterion("banned_desc is not null");
            return (Criteria) this;
        }

        public Criteria andBannedDescEqualTo(String value) {
            addCriterion("banned_desc =", value, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescNotEqualTo(String value) {
            addCriterion("banned_desc <>", value, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescGreaterThan(String value) {
            addCriterion("banned_desc >", value, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescGreaterThanOrEqualTo(String value) {
            addCriterion("banned_desc >=", value, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescLessThan(String value) {
            addCriterion("banned_desc <", value, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescLessThanOrEqualTo(String value) {
            addCriterion("banned_desc <=", value, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescLike(String value) {
            addCriterion("banned_desc like", value, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescNotLike(String value) {
            addCriterion("banned_desc not like", value, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescIn(List<String> values) {
            addCriterion("banned_desc in", values, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescNotIn(List<String> values) {
            addCriterion("banned_desc not in", values, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescBetween(String value1, String value2) {
            addCriterion("banned_desc between", value1, value2, "bannedDesc");
            return (Criteria) this;
        }

        public Criteria andBannedDescNotBetween(String value1, String value2) {
            addCriterion("banned_desc not between", value1, value2, "bannedDesc");
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

    /**
     */
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
