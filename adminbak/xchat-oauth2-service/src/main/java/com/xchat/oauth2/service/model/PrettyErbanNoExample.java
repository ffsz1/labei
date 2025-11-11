package com.xchat.oauth2.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrettyErbanNoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PrettyErbanNoExample() {
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

        public Criteria andPrettyIdIsNull() {
            addCriterion("pretty_id is null");
            return (Criteria) this;
        }

        public Criteria andPrettyIdIsNotNull() {
            addCriterion("pretty_id is not null");
            return (Criteria) this;
        }

        public Criteria andPrettyIdEqualTo(Integer value) {
            addCriterion("pretty_id =", value, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdNotEqualTo(Integer value) {
            addCriterion("pretty_id <>", value, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdGreaterThan(Integer value) {
            addCriterion("pretty_id >", value, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("pretty_id >=", value, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdLessThan(Integer value) {
            addCriterion("pretty_id <", value, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdLessThanOrEqualTo(Integer value) {
            addCriterion("pretty_id <=", value, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdIn(List<Integer> values) {
            addCriterion("pretty_id in", values, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdNotIn(List<Integer> values) {
            addCriterion("pretty_id not in", values, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdBetween(Integer value1, Integer value2) {
            addCriterion("pretty_id between", value1, value2, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyIdNotBetween(Integer value1, Integer value2) {
            addCriterion("pretty_id not between", value1, value2, "prettyId");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoIsNull() {
            addCriterion("pretty_erban_no is null");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoIsNotNull() {
            addCriterion("pretty_erban_no is not null");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoEqualTo(Long value) {
            addCriterion("pretty_erban_no =", value, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoNotEqualTo(Long value) {
            addCriterion("pretty_erban_no <>", value, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoGreaterThan(Long value) {
            addCriterion("pretty_erban_no >", value, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoGreaterThanOrEqualTo(Long value) {
            addCriterion("pretty_erban_no >=", value, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoLessThan(Long value) {
            addCriterion("pretty_erban_no <", value, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoLessThanOrEqualTo(Long value) {
            addCriterion("pretty_erban_no <=", value, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoIn(List<Long> values) {
            addCriterion("pretty_erban_no in", values, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoNotIn(List<Long> values) {
            addCriterion("pretty_erban_no not in", values, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoBetween(Long value1, Long value2) {
            addCriterion("pretty_erban_no between", value1, value2, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyErbanNoNotBetween(Long value1, Long value2) {
            addCriterion("pretty_erban_no not between", value1, value2, "prettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andStatusIsNull() {
            addCriterion("status is null");
            return (Criteria) this;
        }

        public Criteria andStatusIsNotNull() {
            addCriterion("status is not null");
            return (Criteria) this;
        }

        public Criteria andStatusEqualTo(Byte value) {
            addCriterion("status =", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotEqualTo(Byte value) {
            addCriterion("status <>", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThan(Byte value) {
            addCriterion("status >", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("status >=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThan(Byte value) {
            addCriterion("status <", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusLessThanOrEqualTo(Byte value) {
            addCriterion("status <=", value, "status");
            return (Criteria) this;
        }

        public Criteria andStatusIn(List<Byte> values) {
            addCriterion("status in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotIn(List<Byte> values) {
            addCriterion("status not in", values, "status");
            return (Criteria) this;
        }

        public Criteria andStatusBetween(Byte value1, Byte value2) {
            addCriterion("status between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("status not between", value1, value2, "status");
            return (Criteria) this;
        }

        public Criteria andUseForIsNull() {
            addCriterion("use_for is null");
            return (Criteria) this;
        }

        public Criteria andUseForIsNotNull() {
            addCriterion("use_for is not null");
            return (Criteria) this;
        }

        public Criteria andUseForEqualTo(Byte value) {
            addCriterion("use_for =", value, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForNotEqualTo(Byte value) {
            addCriterion("use_for <>", value, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForGreaterThan(Byte value) {
            addCriterion("use_for >", value, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForGreaterThanOrEqualTo(Byte value) {
            addCriterion("use_for >=", value, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForLessThan(Byte value) {
            addCriterion("use_for <", value, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForLessThanOrEqualTo(Byte value) {
            addCriterion("use_for <=", value, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForIn(List<Byte> values) {
            addCriterion("use_for in", values, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForNotIn(List<Byte> values) {
            addCriterion("use_for not in", values, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForBetween(Byte value1, Byte value2) {
            addCriterion("use_for between", value1, value2, "useFor");
            return (Criteria) this;
        }

        public Criteria andUseForNotBetween(Byte value1, Byte value2) {
            addCriterion("use_for not between", value1, value2, "useFor");
            return (Criteria) this;
        }

        public Criteria andUserUidIsNull() {
            addCriterion("user_uid is null");
            return (Criteria) this;
        }

        public Criteria andUserUidIsNotNull() {
            addCriterion("user_uid is not null");
            return (Criteria) this;
        }

        public Criteria andUserUidEqualTo(Long value) {
            addCriterion("user_uid =", value, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidNotEqualTo(Long value) {
            addCriterion("user_uid <>", value, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidGreaterThan(Long value) {
            addCriterion("user_uid >", value, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidGreaterThanOrEqualTo(Long value) {
            addCriterion("user_uid >=", value, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidLessThan(Long value) {
            addCriterion("user_uid <", value, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidLessThanOrEqualTo(Long value) {
            addCriterion("user_uid <=", value, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidIn(List<Long> values) {
            addCriterion("user_uid in", values, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidNotIn(List<Long> values) {
            addCriterion("user_uid not in", values, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidBetween(Long value1, Long value2) {
            addCriterion("user_uid between", value1, value2, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserUidNotBetween(Long value1, Long value2) {
            addCriterion("user_uid not between", value1, value2, "userUid");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoIsNull() {
            addCriterion("user_erban_no is null");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoIsNotNull() {
            addCriterion("user_erban_no is not null");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoEqualTo(Long value) {
            addCriterion("user_erban_no =", value, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoNotEqualTo(Long value) {
            addCriterion("user_erban_no <>", value, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoGreaterThan(Long value) {
            addCriterion("user_erban_no >", value, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoGreaterThanOrEqualTo(Long value) {
            addCriterion("user_erban_no >=", value, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoLessThan(Long value) {
            addCriterion("user_erban_no <", value, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoLessThanOrEqualTo(Long value) {
            addCriterion("user_erban_no <=", value, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoIn(List<Long> values) {
            addCriterion("user_erban_no in", values, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoNotIn(List<Long> values) {
            addCriterion("user_erban_no not in", values, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoBetween(Long value1, Long value2) {
            addCriterion("user_erban_no between", value1, value2, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andUserErbanNoNotBetween(Long value1, Long value2) {
            addCriterion("user_erban_no not between", value1, value2, "userErbanNo");
            return (Criteria) this;
        }

        public Criteria andPrettyDescIsNull() {
            addCriterion("pretty_desc is null");
            return (Criteria) this;
        }

        public Criteria andPrettyDescIsNotNull() {
            addCriterion("pretty_desc is not null");
            return (Criteria) this;
        }

        public Criteria andPrettyDescEqualTo(String value) {
            addCriterion("pretty_desc =", value, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescNotEqualTo(String value) {
            addCriterion("pretty_desc <>", value, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescGreaterThan(String value) {
            addCriterion("pretty_desc >", value, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescGreaterThanOrEqualTo(String value) {
            addCriterion("pretty_desc >=", value, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescLessThan(String value) {
            addCriterion("pretty_desc <", value, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescLessThanOrEqualTo(String value) {
            addCriterion("pretty_desc <=", value, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescLike(String value) {
            addCriterion("pretty_desc like", value, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescNotLike(String value) {
            addCriterion("pretty_desc not like", value, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescIn(List<String> values) {
            addCriterion("pretty_desc in", values, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescNotIn(List<String> values) {
            addCriterion("pretty_desc not in", values, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescBetween(String value1, String value2) {
            addCriterion("pretty_desc between", value1, value2, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andPrettyDescNotBetween(String value1, String value2) {
            addCriterion("pretty_desc not between", value1, value2, "prettyDesc");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIsNull() {
            addCriterion("start_valid_time is null");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIsNotNull() {
            addCriterion("start_valid_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeEqualTo(Date value) {
            addCriterion("start_valid_time =", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotEqualTo(Date value) {
            addCriterion("start_valid_time <>", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeGreaterThan(Date value) {
            addCriterion("start_valid_time >", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_valid_time >=", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeLessThan(Date value) {
            addCriterion("start_valid_time <", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_valid_time <=", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIn(List<Date> values) {
            addCriterion("start_valid_time in", values, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotIn(List<Date> values) {
            addCriterion("start_valid_time not in", values, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeBetween(Date value1, Date value2) {
            addCriterion("start_valid_time between", value1, value2, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_valid_time not between", value1, value2, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIsNull() {
            addCriterion("end_valid_time is null");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIsNotNull() {
            addCriterion("end_valid_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeEqualTo(Date value) {
            addCriterion("end_valid_time =", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotEqualTo(Date value) {
            addCriterion("end_valid_time <>", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeGreaterThan(Date value) {
            addCriterion("end_valid_time >", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_valid_time >=", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeLessThan(Date value) {
            addCriterion("end_valid_time <", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_valid_time <=", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIn(List<Date> values) {
            addCriterion("end_valid_time in", values, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotIn(List<Date> values) {
            addCriterion("end_valid_time not in", values, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeBetween(Date value1, Date value2) {
            addCriterion("end_valid_time between", value1, value2, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_valid_time not between", value1, value2, "endValidTime");
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
