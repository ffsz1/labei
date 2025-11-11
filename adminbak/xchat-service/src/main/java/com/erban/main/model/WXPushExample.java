package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WXPushExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public WXPushExample() {
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

        public Criteria andWxPushIdIsNull() {
            addCriterion("wx_push_id is null");
            return (Criteria) this;
        }

        public Criteria andWxPushIdIsNotNull() {
            addCriterion("wx_push_id is not null");
            return (Criteria) this;
        }

        public Criteria andWxPushIdEqualTo(String value) {
            addCriterion("wx_push_id =", value, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdNotEqualTo(String value) {
            addCriterion("wx_push_id <>", value, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdGreaterThan(String value) {
            addCriterion("wx_push_id >", value, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdGreaterThanOrEqualTo(String value) {
            addCriterion("wx_push_id >=", value, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdLessThan(String value) {
            addCriterion("wx_push_id <", value, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdLessThanOrEqualTo(String value) {
            addCriterion("wx_push_id <=", value, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdLike(String value) {
            addCriterion("wx_push_id like", value, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdNotLike(String value) {
            addCriterion("wx_push_id not like", value, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdIn(List<String> values) {
            addCriterion("wx_push_id in", values, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdNotIn(List<String> values) {
            addCriterion("wx_push_id not in", values, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdBetween(String value1, String value2) {
            addCriterion("wx_push_id between", value1, value2, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andWxPushIdNotBetween(String value1, String value2) {
            addCriterion("wx_push_id not between", value1, value2, "wxPushId");
            return (Criteria) this;
        }

        public Criteria andArticleCountIsNull() {
            addCriterion("article_count is null");
            return (Criteria) this;
        }

        public Criteria andArticleCountIsNotNull() {
            addCriterion("article_count is not null");
            return (Criteria) this;
        }

        public Criteria andArticleCountEqualTo(String value) {
            addCriterion("article_count =", value, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountNotEqualTo(String value) {
            addCriterion("article_count <>", value, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountGreaterThan(String value) {
            addCriterion("article_count >", value, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountGreaterThanOrEqualTo(String value) {
            addCriterion("article_count >=", value, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountLessThan(String value) {
            addCriterion("article_count <", value, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountLessThanOrEqualTo(String value) {
            addCriterion("article_count <=", value, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountLike(String value) {
            addCriterion("article_count like", value, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountNotLike(String value) {
            addCriterion("article_count not like", value, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountIn(List<String> values) {
            addCriterion("article_count in", values, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountNotIn(List<String> values) {
            addCriterion("article_count not in", values, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountBetween(String value1, String value2) {
            addCriterion("article_count between", value1, value2, "articleCount");
            return (Criteria) this;
        }

        public Criteria andArticleCountNotBetween(String value1, String value2) {
            addCriterion("article_count not between", value1, value2, "articleCount");
            return (Criteria) this;
        }

        public Criteria andIsPushIsNull() {
            addCriterion("is_push is null");
            return (Criteria) this;
        }

        public Criteria andIsPushIsNotNull() {
            addCriterion("is_push is not null");
            return (Criteria) this;
        }

        public Criteria andIsPushEqualTo(Boolean value) {
            addCriterion("is_push =", value, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushNotEqualTo(Boolean value) {
            addCriterion("is_push <>", value, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushGreaterThan(Boolean value) {
            addCriterion("is_push >", value, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_push >=", value, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushLessThan(Boolean value) {
            addCriterion("is_push <", value, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushLessThanOrEqualTo(Boolean value) {
            addCriterion("is_push <=", value, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushIn(List<Boolean> values) {
            addCriterion("is_push in", values, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushNotIn(List<Boolean> values) {
            addCriterion("is_push not in", values, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushBetween(Boolean value1, Boolean value2) {
            addCriterion("is_push between", value1, value2, "isPush");
            return (Criteria) this;
        }

        public Criteria andIsPushNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_push not between", value1, value2, "isPush");
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

        public Criteria andPubTimeIsNull() {
            addCriterion("pub_time is null");
            return (Criteria) this;
        }

        public Criteria andPubTimeIsNotNull() {
            addCriterion("pub_time is not null");
            return (Criteria) this;
        }

        public Criteria andPubTimeEqualTo(Date value) {
            addCriterion("pub_time =", value, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeNotEqualTo(Date value) {
            addCriterion("pub_time <>", value, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeGreaterThan(Date value) {
            addCriterion("pub_time >", value, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("pub_time >=", value, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeLessThan(Date value) {
            addCriterion("pub_time <", value, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeLessThanOrEqualTo(Date value) {
            addCriterion("pub_time <=", value, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeIn(List<Date> values) {
            addCriterion("pub_time in", values, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeNotIn(List<Date> values) {
            addCriterion("pub_time not in", values, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeBetween(Date value1, Date value2) {
            addCriterion("pub_time between", value1, value2, "pubTime");
            return (Criteria) this;
        }

        public Criteria andPubTimeNotBetween(Date value1, Date value2) {
            addCriterion("pub_time not between", value1, value2, "pubTime");
            return (Criteria) this;
        }

        public Criteria andMsgIdIsNull() {
            addCriterion("msg_id is null");
            return (Criteria) this;
        }

        public Criteria andMsgIdIsNotNull() {
            addCriterion("msg_id is not null");
            return (Criteria) this;
        }

        public Criteria andMsgIdEqualTo(String value) {
            addCriterion("msg_id =", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotEqualTo(String value) {
            addCriterion("msg_id <>", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThan(String value) {
            addCriterion("msg_id >", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdGreaterThanOrEqualTo(String value) {
            addCriterion("msg_id >=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThan(String value) {
            addCriterion("msg_id <", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLessThanOrEqualTo(String value) {
            addCriterion("msg_id <=", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdLike(String value) {
            addCriterion("msg_id like", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotLike(String value) {
            addCriterion("msg_id not like", value, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdIn(List<String> values) {
            addCriterion("msg_id in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotIn(List<String> values) {
            addCriterion("msg_id not in", values, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdBetween(String value1, String value2) {
            addCriterion("msg_id between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andMsgIdNotBetween(String value1, String value2) {
            addCriterion("msg_id not between", value1, value2, "msgId");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushIsNull() {
            addCriterion("is_focus_push is null");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushIsNotNull() {
            addCriterion("is_focus_push is not null");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushEqualTo(Boolean value) {
            addCriterion("is_focus_push =", value, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushNotEqualTo(Boolean value) {
            addCriterion("is_focus_push <>", value, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushGreaterThan(Boolean value) {
            addCriterion("is_focus_push >", value, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_focus_push >=", value, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushLessThan(Boolean value) {
            addCriterion("is_focus_push <", value, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushLessThanOrEqualTo(Boolean value) {
            addCriterion("is_focus_push <=", value, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushIn(List<Boolean> values) {
            addCriterion("is_focus_push in", values, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushNotIn(List<Boolean> values) {
            addCriterion("is_focus_push not in", values, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushBetween(Boolean value1, Boolean value2) {
            addCriterion("is_focus_push between", value1, value2, "isFocusPush");
            return (Criteria) this;
        }

        public Criteria andIsFocusPushNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_focus_push not between", value1, value2, "isFocusPush");
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
