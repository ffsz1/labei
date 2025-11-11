package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChannelExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public ChannelExample() {
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

        public Criteria andAuditOptionIsNull() {
            addCriterion("audit_option is null");
            return (Criteria) this;
        }

        public Criteria andAuditOptionIsNotNull() {
            addCriterion("audit_option is not null");
            return (Criteria) this;
        }

        public Criteria andAuditOptionEqualTo(String value) {
            addCriterion("audit_option =", value, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionNotEqualTo(String value) {
            addCriterion("audit_option <>", value, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionGreaterThan(String value) {
            addCriterion("audit_option >", value, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionGreaterThanOrEqualTo(String value) {
            addCriterion("audit_option >=", value, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionLessThan(String value) {
            addCriterion("audit_option <", value, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionLessThanOrEqualTo(String value) {
            addCriterion("audit_option <=", value, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionLike(String value) {
            addCriterion("audit_option like", value, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionNotLike(String value) {
            addCriterion("audit_option not like", value, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionIn(List<String> values) {
            addCriterion("audit_option in", values, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionNotIn(List<String> values) {
            addCriterion("audit_option not in", values, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionBetween(String value1, String value2) {
            addCriterion("audit_option between", value1, value2, "auditOption");
            return (Criteria) this;
        }

        public Criteria andAuditOptionNotBetween(String value1, String value2) {
            addCriterion("audit_option not between", value1, value2, "auditOption");
            return (Criteria) this;
        }

        public Criteria andLeftLevelIsNull() {
            addCriterion("left_level is null");
            return (Criteria) this;
        }

        public Criteria andLeftLevelIsNotNull() {
            addCriterion("left_level is not null");
            return (Criteria) this;
        }

        public Criteria andLeftLevelEqualTo(Integer value) {
            addCriterion("left_level =", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelNotEqualTo(Integer value) {
            addCriterion("left_level <>", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelGreaterThan(Integer value) {
            addCriterion("left_level >", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("left_level >=", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelLessThan(Integer value) {
            addCriterion("left_level <", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelLessThanOrEqualTo(Integer value) {
            addCriterion("left_level <=", value, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelIn(List<Integer> values) {
            addCriterion("left_level in", values, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelNotIn(List<Integer> values) {
            addCriterion("left_level not in", values, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelBetween(Integer value1, Integer value2) {
            addCriterion("left_level between", value1, value2, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andLeftLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("left_level not between", value1, value2, "leftLevel");
            return (Criteria) this;
        }

        public Criteria andAuditVersionIsNull() {
            addCriterion("audit_version is null");
            return (Criteria) this;
        }

        public Criteria andAuditVersionIsNotNull() {
            addCriterion("audit_version is not null");
            return (Criteria) this;
        }

        public Criteria andAuditVersionEqualTo(String value) {
            addCriterion("audit_version =", value, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionNotEqualTo(String value) {
            addCriterion("audit_version <>", value, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionGreaterThan(String value) {
            addCriterion("audit_version >", value, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionGreaterThanOrEqualTo(String value) {
            addCriterion("audit_version >=", value, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionLessThan(String value) {
            addCriterion("audit_version <", value, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionLessThanOrEqualTo(String value) {
            addCriterion("audit_version <=", value, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionLike(String value) {
            addCriterion("audit_version like", value, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionNotLike(String value) {
            addCriterion("audit_version not like", value, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionIn(List<String> values) {
            addCriterion("audit_version in", values, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionNotIn(List<String> values) {
            addCriterion("audit_version not in", values, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionBetween(String value1, String value2) {
            addCriterion("audit_version between", value1, value2, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andAuditVersionNotBetween(String value1, String value2) {
            addCriterion("audit_version not between", value1, value2, "auditVersion");
            return (Criteria) this;
        }

        public Criteria andBeginTimeIsNull() {
            addCriterion("begin_time is null");
            return (Criteria) this;
        }

        public Criteria andBeginTimeIsNotNull() {
            addCriterion("begin_time is not null");
            return (Criteria) this;
        }

        public Criteria andBeginTimeEqualTo(Date value) {
            addCriterion("begin_time =", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeNotEqualTo(Date value) {
            addCriterion("begin_time <>", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeGreaterThan(Date value) {
            addCriterion("begin_time >", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("begin_time >=", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeLessThan(Date value) {
            addCriterion("begin_time <", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeLessThanOrEqualTo(Date value) {
            addCriterion("begin_time <=", value, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeIn(List<Date> values) {
            addCriterion("begin_time in", values, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeNotIn(List<Date> values) {
            addCriterion("begin_time not in", values, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeBetween(Date value1, Date value2) {
            addCriterion("begin_time between", value1, value2, "beginTime");
            return (Criteria) this;
        }

        public Criteria andBeginTimeNotBetween(Date value1, Date value2) {
            addCriterion("begin_time not between", value1, value2, "beginTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNull() {
            addCriterion("end_time is null");
            return (Criteria) this;
        }

        public Criteria andEndTimeIsNotNull() {
            addCriterion("end_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndTimeEqualTo(Date value) {
            addCriterion("end_time =", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotEqualTo(Date value) {
            addCriterion("end_time <>", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThan(Date value) {
            addCriterion("end_time >", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_time >=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThan(Date value) {
            addCriterion("end_time <", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_time <=", value, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeIn(List<Date> values) {
            addCriterion("end_time in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotIn(List<Date> values) {
            addCriterion("end_time not in", values, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeBetween(Date value1, Date value2) {
            addCriterion("end_time between", value1, value2, "endTime");
            return (Criteria) this;
        }

        public Criteria andEndTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_time not between", value1, value2, "endTime");
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
