package com.xchat.oauth2.service.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PrettyErbanNoRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public PrettyErbanNoRecordExample() {
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

        public Criteria andRecordIdEqualTo(Integer value) {
            addCriterion("record_id =", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdNotEqualTo(Integer value) {
            addCriterion("record_id <>", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdGreaterThan(Integer value) {
            addCriterion("record_id >", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("record_id >=", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdLessThan(Integer value) {
            addCriterion("record_id <", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdLessThanOrEqualTo(Integer value) {
            addCriterion("record_id <=", value, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdIn(List<Integer> values) {
            addCriterion("record_id in", values, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdNotIn(List<Integer> values) {
            addCriterion("record_id not in", values, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdBetween(Integer value1, Integer value2) {
            addCriterion("record_id between", value1, value2, "recordId");
            return (Criteria) this;
        }

        public Criteria andRecordIdNotBetween(Integer value1, Integer value2) {
            addCriterion("record_id not between", value1, value2, "recordId");
            return (Criteria) this;
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
