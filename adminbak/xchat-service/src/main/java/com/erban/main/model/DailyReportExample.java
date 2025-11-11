package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class DailyReportExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public DailyReportExample() {
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

        public Criteria andReportIdIsNull() {
            addCriterion("report_id is null");
            return (Criteria) this;
        }

        public Criteria andReportIdIsNotNull() {
            addCriterion("report_id is not null");
            return (Criteria) this;
        }

        public Criteria andReportIdEqualTo(Integer value) {
            addCriterion("report_id =", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdNotEqualTo(Integer value) {
            addCriterion("report_id <>", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdGreaterThan(Integer value) {
            addCriterion("report_id >", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("report_id >=", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdLessThan(Integer value) {
            addCriterion("report_id <", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdLessThanOrEqualTo(Integer value) {
            addCriterion("report_id <=", value, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdIn(List<Integer> values) {
            addCriterion("report_id in", values, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdNotIn(List<Integer> values) {
            addCriterion("report_id not in", values, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdBetween(Integer value1, Integer value2) {
            addCriterion("report_id between", value1, value2, "reportId");
            return (Criteria) this;
        }

        public Criteria andReportIdNotBetween(Integer value1, Integer value2) {
            addCriterion("report_id not between", value1, value2, "reportId");
            return (Criteria) this;
        }

        public Criteria andStr1IsNull() {
            addCriterion("str1 is null");
            return (Criteria) this;
        }

        public Criteria andStr1IsNotNull() {
            addCriterion("str1 is not null");
            return (Criteria) this;
        }

        public Criteria andStr1EqualTo(String value) {
            addCriterion("str1 =", value, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1NotEqualTo(String value) {
            addCriterion("str1 <>", value, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1GreaterThan(String value) {
            addCriterion("str1 >", value, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1GreaterThanOrEqualTo(String value) {
            addCriterion("str1 >=", value, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1LessThan(String value) {
            addCriterion("str1 <", value, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1LessThanOrEqualTo(String value) {
            addCriterion("str1 <=", value, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1Like(String value) {
            addCriterion("str1 like", value, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1NotLike(String value) {
            addCriterion("str1 not like", value, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1In(List<String> values) {
            addCriterion("str1 in", values, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1NotIn(List<String> values) {
            addCriterion("str1 not in", values, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1Between(String value1, String value2) {
            addCriterion("str1 between", value1, value2, "str1");
            return (Criteria) this;
        }

        public Criteria andStr1NotBetween(String value1, String value2) {
            addCriterion("str1 not between", value1, value2, "str1");
            return (Criteria) this;
        }

        public Criteria andStr2IsNull() {
            addCriterion("str2 is null");
            return (Criteria) this;
        }

        public Criteria andStr2IsNotNull() {
            addCriterion("str2 is not null");
            return (Criteria) this;
        }

        public Criteria andStr2EqualTo(String value) {
            addCriterion("str2 =", value, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2NotEqualTo(String value) {
            addCriterion("str2 <>", value, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2GreaterThan(String value) {
            addCriterion("str2 >", value, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2GreaterThanOrEqualTo(String value) {
            addCriterion("str2 >=", value, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2LessThan(String value) {
            addCriterion("str2 <", value, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2LessThanOrEqualTo(String value) {
            addCriterion("str2 <=", value, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2Like(String value) {
            addCriterion("str2 like", value, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2NotLike(String value) {
            addCriterion("str2 not like", value, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2In(List<String> values) {
            addCriterion("str2 in", values, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2NotIn(List<String> values) {
            addCriterion("str2 not in", values, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2Between(String value1, String value2) {
            addCriterion("str2 between", value1, value2, "str2");
            return (Criteria) this;
        }

        public Criteria andStr2NotBetween(String value1, String value2) {
            addCriterion("str2 not between", value1, value2, "str2");
            return (Criteria) this;
        }

        public Criteria andStr3IsNull() {
            addCriterion("str3 is null");
            return (Criteria) this;
        }

        public Criteria andStr3IsNotNull() {
            addCriterion("str3 is not null");
            return (Criteria) this;
        }

        public Criteria andStr3EqualTo(String value) {
            addCriterion("str3 =", value, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3NotEqualTo(String value) {
            addCriterion("str3 <>", value, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3GreaterThan(String value) {
            addCriterion("str3 >", value, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3GreaterThanOrEqualTo(String value) {
            addCriterion("str3 >=", value, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3LessThan(String value) {
            addCriterion("str3 <", value, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3LessThanOrEqualTo(String value) {
            addCriterion("str3 <=", value, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3Like(String value) {
            addCriterion("str3 like", value, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3NotLike(String value) {
            addCriterion("str3 not like", value, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3In(List<String> values) {
            addCriterion("str3 in", values, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3NotIn(List<String> values) {
            addCriterion("str3 not in", values, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3Between(String value1, String value2) {
            addCriterion("str3 between", value1, value2, "str3");
            return (Criteria) this;
        }

        public Criteria andStr3NotBetween(String value1, String value2) {
            addCriterion("str3 not between", value1, value2, "str3");
            return (Criteria) this;
        }

        public Criteria andStr4IsNull() {
            addCriterion("str4 is null");
            return (Criteria) this;
        }

        public Criteria andStr4IsNotNull() {
            addCriterion("str4 is not null");
            return (Criteria) this;
        }

        public Criteria andStr4EqualTo(String value) {
            addCriterion("str4 =", value, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4NotEqualTo(String value) {
            addCriterion("str4 <>", value, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4GreaterThan(String value) {
            addCriterion("str4 >", value, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4GreaterThanOrEqualTo(String value) {
            addCriterion("str4 >=", value, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4LessThan(String value) {
            addCriterion("str4 <", value, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4LessThanOrEqualTo(String value) {
            addCriterion("str4 <=", value, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4Like(String value) {
            addCriterion("str4 like", value, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4NotLike(String value) {
            addCriterion("str4 not like", value, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4In(List<String> values) {
            addCriterion("str4 in", values, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4NotIn(List<String> values) {
            addCriterion("str4 not in", values, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4Between(String value1, String value2) {
            addCriterion("str4 between", value1, value2, "str4");
            return (Criteria) this;
        }

        public Criteria andStr4NotBetween(String value1, String value2) {
            addCriterion("str4 not between", value1, value2, "str4");
            return (Criteria) this;
        }

        public Criteria andStr5IsNull() {
            addCriterion("str5 is null");
            return (Criteria) this;
        }

        public Criteria andStr5IsNotNull() {
            addCriterion("str5 is not null");
            return (Criteria) this;
        }

        public Criteria andStr5EqualTo(String value) {
            addCriterion("str5 =", value, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5NotEqualTo(String value) {
            addCriterion("str5 <>", value, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5GreaterThan(String value) {
            addCriterion("str5 >", value, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5GreaterThanOrEqualTo(String value) {
            addCriterion("str5 >=", value, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5LessThan(String value) {
            addCriterion("str5 <", value, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5LessThanOrEqualTo(String value) {
            addCriterion("str5 <=", value, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5Like(String value) {
            addCriterion("str5 like", value, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5NotLike(String value) {
            addCriterion("str5 not like", value, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5In(List<String> values) {
            addCriterion("str5 in", values, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5NotIn(List<String> values) {
            addCriterion("str5 not in", values, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5Between(String value1, String value2) {
            addCriterion("str5 between", value1, value2, "str5");
            return (Criteria) this;
        }

        public Criteria andStr5NotBetween(String value1, String value2) {
            addCriterion("str5 not between", value1, value2, "str5");
            return (Criteria) this;
        }

        public Criteria andStr6IsNull() {
            addCriterion("str6 is null");
            return (Criteria) this;
        }

        public Criteria andStr6IsNotNull() {
            addCriterion("str6 is not null");
            return (Criteria) this;
        }

        public Criteria andStr6EqualTo(String value) {
            addCriterion("str6 =", value, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6NotEqualTo(String value) {
            addCriterion("str6 <>", value, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6GreaterThan(String value) {
            addCriterion("str6 >", value, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6GreaterThanOrEqualTo(String value) {
            addCriterion("str6 >=", value, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6LessThan(String value) {
            addCriterion("str6 <", value, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6LessThanOrEqualTo(String value) {
            addCriterion("str6 <=", value, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6Like(String value) {
            addCriterion("str6 like", value, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6NotLike(String value) {
            addCriterion("str6 not like", value, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6In(List<String> values) {
            addCriterion("str6 in", values, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6NotIn(List<String> values) {
            addCriterion("str6 not in", values, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6Between(String value1, String value2) {
            addCriterion("str6 between", value1, value2, "str6");
            return (Criteria) this;
        }

        public Criteria andStr6NotBetween(String value1, String value2) {
            addCriterion("str6 not between", value1, value2, "str6");
            return (Criteria) this;
        }

        public Criteria andStr7IsNull() {
            addCriterion("str7 is null");
            return (Criteria) this;
        }

        public Criteria andStr7IsNotNull() {
            addCriterion("str7 is not null");
            return (Criteria) this;
        }

        public Criteria andStr7EqualTo(String value) {
            addCriterion("str7 =", value, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7NotEqualTo(String value) {
            addCriterion("str7 <>", value, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7GreaterThan(String value) {
            addCriterion("str7 >", value, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7GreaterThanOrEqualTo(String value) {
            addCriterion("str7 >=", value, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7LessThan(String value) {
            addCriterion("str7 <", value, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7LessThanOrEqualTo(String value) {
            addCriterion("str7 <=", value, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7Like(String value) {
            addCriterion("str7 like", value, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7NotLike(String value) {
            addCriterion("str7 not like", value, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7In(List<String> values) {
            addCriterion("str7 in", values, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7NotIn(List<String> values) {
            addCriterion("str7 not in", values, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7Between(String value1, String value2) {
            addCriterion("str7 between", value1, value2, "str7");
            return (Criteria) this;
        }

        public Criteria andStr7NotBetween(String value1, String value2) {
            addCriterion("str7 not between", value1, value2, "str7");
            return (Criteria) this;
        }

        public Criteria andStr8IsNull() {
            addCriterion("str8 is null");
            return (Criteria) this;
        }

        public Criteria andStr8IsNotNull() {
            addCriterion("str8 is not null");
            return (Criteria) this;
        }

        public Criteria andStr8EqualTo(String value) {
            addCriterion("str8 =", value, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8NotEqualTo(String value) {
            addCriterion("str8 <>", value, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8GreaterThan(String value) {
            addCriterion("str8 >", value, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8GreaterThanOrEqualTo(String value) {
            addCriterion("str8 >=", value, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8LessThan(String value) {
            addCriterion("str8 <", value, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8LessThanOrEqualTo(String value) {
            addCriterion("str8 <=", value, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8Like(String value) {
            addCriterion("str8 like", value, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8NotLike(String value) {
            addCriterion("str8 not like", value, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8In(List<String> values) {
            addCriterion("str8 in", values, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8NotIn(List<String> values) {
            addCriterion("str8 not in", values, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8Between(String value1, String value2) {
            addCriterion("str8 between", value1, value2, "str8");
            return (Criteria) this;
        }

        public Criteria andStr8NotBetween(String value1, String value2) {
            addCriterion("str8 not between", value1, value2, "str8");
            return (Criteria) this;
        }

        public Criteria andStr9IsNull() {
            addCriterion("str9 is null");
            return (Criteria) this;
        }

        public Criteria andStr9IsNotNull() {
            addCriterion("str9 is not null");
            return (Criteria) this;
        }

        public Criteria andStr9EqualTo(String value) {
            addCriterion("str9 =", value, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9NotEqualTo(String value) {
            addCriterion("str9 <>", value, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9GreaterThan(String value) {
            addCriterion("str9 >", value, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9GreaterThanOrEqualTo(String value) {
            addCriterion("str9 >=", value, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9LessThan(String value) {
            addCriterion("str9 <", value, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9LessThanOrEqualTo(String value) {
            addCriterion("str9 <=", value, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9Like(String value) {
            addCriterion("str9 like", value, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9NotLike(String value) {
            addCriterion("str9 not like", value, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9In(List<String> values) {
            addCriterion("str9 in", values, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9NotIn(List<String> values) {
            addCriterion("str9 not in", values, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9Between(String value1, String value2) {
            addCriterion("str9 between", value1, value2, "str9");
            return (Criteria) this;
        }

        public Criteria andStr9NotBetween(String value1, String value2) {
            addCriterion("str9 not between", value1, value2, "str9");
            return (Criteria) this;
        }

        public Criteria andStr10IsNull() {
            addCriterion("str10 is null");
            return (Criteria) this;
        }

        public Criteria andStr10IsNotNull() {
            addCriterion("str10 is not null");
            return (Criteria) this;
        }

        public Criteria andStr10EqualTo(String value) {
            addCriterion("str10 =", value, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10NotEqualTo(String value) {
            addCriterion("str10 <>", value, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10GreaterThan(String value) {
            addCriterion("str10 >", value, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10GreaterThanOrEqualTo(String value) {
            addCriterion("str10 >=", value, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10LessThan(String value) {
            addCriterion("str10 <", value, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10LessThanOrEqualTo(String value) {
            addCriterion("str10 <=", value, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10Like(String value) {
            addCriterion("str10 like", value, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10NotLike(String value) {
            addCriterion("str10 not like", value, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10In(List<String> values) {
            addCriterion("str10 in", values, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10NotIn(List<String> values) {
            addCriterion("str10 not in", values, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10Between(String value1, String value2) {
            addCriterion("str10 between", value1, value2, "str10");
            return (Criteria) this;
        }

        public Criteria andStr10NotBetween(String value1, String value2) {
            addCriterion("str10 not between", value1, value2, "str10");
            return (Criteria) this;
        }

        public Criteria andStr11IsNull() {
            addCriterion("str11 is null");
            return (Criteria) this;
        }

        public Criteria andStr11IsNotNull() {
            addCriterion("str11 is not null");
            return (Criteria) this;
        }

        public Criteria andStr11EqualTo(String value) {
            addCriterion("str11 =", value, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11NotEqualTo(String value) {
            addCriterion("str11 <>", value, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11GreaterThan(String value) {
            addCriterion("str11 >", value, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11GreaterThanOrEqualTo(String value) {
            addCriterion("str11 >=", value, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11LessThan(String value) {
            addCriterion("str11 <", value, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11LessThanOrEqualTo(String value) {
            addCriterion("str11 <=", value, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11Like(String value) {
            addCriterion("str11 like", value, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11NotLike(String value) {
            addCriterion("str11 not like", value, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11In(List<String> values) {
            addCriterion("str11 in", values, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11NotIn(List<String> values) {
            addCriterion("str11 not in", values, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11Between(String value1, String value2) {
            addCriterion("str11 between", value1, value2, "str11");
            return (Criteria) this;
        }

        public Criteria andStr11NotBetween(String value1, String value2) {
            addCriterion("str11 not between", value1, value2, "str11");
            return (Criteria) this;
        }

        public Criteria andStr12IsNull() {
            addCriterion("str12 is null");
            return (Criteria) this;
        }

        public Criteria andStr12IsNotNull() {
            addCriterion("str12 is not null");
            return (Criteria) this;
        }

        public Criteria andStr12EqualTo(String value) {
            addCriterion("str12 =", value, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12NotEqualTo(String value) {
            addCriterion("str12 <>", value, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12GreaterThan(String value) {
            addCriterion("str12 >", value, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12GreaterThanOrEqualTo(String value) {
            addCriterion("str12 >=", value, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12LessThan(String value) {
            addCriterion("str12 <", value, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12LessThanOrEqualTo(String value) {
            addCriterion("str12 <=", value, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12Like(String value) {
            addCriterion("str12 like", value, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12NotLike(String value) {
            addCriterion("str12 not like", value, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12In(List<String> values) {
            addCriterion("str12 in", values, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12NotIn(List<String> values) {
            addCriterion("str12 not in", values, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12Between(String value1, String value2) {
            addCriterion("str12 between", value1, value2, "str12");
            return (Criteria) this;
        }

        public Criteria andStr12NotBetween(String value1, String value2) {
            addCriterion("str12 not between", value1, value2, "str12");
            return (Criteria) this;
        }

        public Criteria andStr13IsNull() {
            addCriterion("str13 is null");
            return (Criteria) this;
        }

        public Criteria andStr13IsNotNull() {
            addCriterion("str13 is not null");
            return (Criteria) this;
        }

        public Criteria andStr13EqualTo(String value) {
            addCriterion("str13 =", value, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13NotEqualTo(String value) {
            addCriterion("str13 <>", value, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13GreaterThan(String value) {
            addCriterion("str13 >", value, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13GreaterThanOrEqualTo(String value) {
            addCriterion("str13 >=", value, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13LessThan(String value) {
            addCriterion("str13 <", value, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13LessThanOrEqualTo(String value) {
            addCriterion("str13 <=", value, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13Like(String value) {
            addCriterion("str13 like", value, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13NotLike(String value) {
            addCriterion("str13 not like", value, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13In(List<String> values) {
            addCriterion("str13 in", values, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13NotIn(List<String> values) {
            addCriterion("str13 not in", values, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13Between(String value1, String value2) {
            addCriterion("str13 between", value1, value2, "str13");
            return (Criteria) this;
        }

        public Criteria andStr13NotBetween(String value1, String value2) {
            addCriterion("str13 not between", value1, value2, "str13");
            return (Criteria) this;
        }

        public Criteria andStr14IsNull() {
            addCriterion("str14 is null");
            return (Criteria) this;
        }

        public Criteria andStr14IsNotNull() {
            addCriterion("str14 is not null");
            return (Criteria) this;
        }

        public Criteria andStr14EqualTo(String value) {
            addCriterion("str14 =", value, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14NotEqualTo(String value) {
            addCriterion("str14 <>", value, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14GreaterThan(String value) {
            addCriterion("str14 >", value, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14GreaterThanOrEqualTo(String value) {
            addCriterion("str14 >=", value, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14LessThan(String value) {
            addCriterion("str14 <", value, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14LessThanOrEqualTo(String value) {
            addCriterion("str14 <=", value, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14Like(String value) {
            addCriterion("str14 like", value, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14NotLike(String value) {
            addCriterion("str14 not like", value, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14In(List<String> values) {
            addCriterion("str14 in", values, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14NotIn(List<String> values) {
            addCriterion("str14 not in", values, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14Between(String value1, String value2) {
            addCriterion("str14 between", value1, value2, "str14");
            return (Criteria) this;
        }

        public Criteria andStr14NotBetween(String value1, String value2) {
            addCriterion("str14 not between", value1, value2, "str14");
            return (Criteria) this;
        }

        public Criteria andStr15IsNull() {
            addCriterion("str15 is null");
            return (Criteria) this;
        }

        public Criteria andStr15IsNotNull() {
            addCriterion("str15 is not null");
            return (Criteria) this;
        }

        public Criteria andStr15EqualTo(String value) {
            addCriterion("str15 =", value, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15NotEqualTo(String value) {
            addCriterion("str15 <>", value, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15GreaterThan(String value) {
            addCriterion("str15 >", value, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15GreaterThanOrEqualTo(String value) {
            addCriterion("str15 >=", value, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15LessThan(String value) {
            addCriterion("str15 <", value, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15LessThanOrEqualTo(String value) {
            addCriterion("str15 <=", value, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15Like(String value) {
            addCriterion("str15 like", value, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15NotLike(String value) {
            addCriterion("str15 not like", value, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15In(List<String> values) {
            addCriterion("str15 in", values, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15NotIn(List<String> values) {
            addCriterion("str15 not in", values, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15Between(String value1, String value2) {
            addCriterion("str15 between", value1, value2, "str15");
            return (Criteria) this;
        }

        public Criteria andStr15NotBetween(String value1, String value2) {
            addCriterion("str15 not between", value1, value2, "str15");
            return (Criteria) this;
        }

        public Criteria andStr16IsNull() {
            addCriterion("str16 is null");
            return (Criteria) this;
        }

        public Criteria andStr16IsNotNull() {
            addCriterion("str16 is not null");
            return (Criteria) this;
        }

        public Criteria andStr16EqualTo(String value) {
            addCriterion("str16 =", value, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16NotEqualTo(String value) {
            addCriterion("str16 <>", value, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16GreaterThan(String value) {
            addCriterion("str16 >", value, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16GreaterThanOrEqualTo(String value) {
            addCriterion("str16 >=", value, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16LessThan(String value) {
            addCriterion("str16 <", value, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16LessThanOrEqualTo(String value) {
            addCriterion("str16 <=", value, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16Like(String value) {
            addCriterion("str16 like", value, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16NotLike(String value) {
            addCriterion("str16 not like", value, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16In(List<String> values) {
            addCriterion("str16 in", values, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16NotIn(List<String> values) {
            addCriterion("str16 not in", values, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16Between(String value1, String value2) {
            addCriterion("str16 between", value1, value2, "str16");
            return (Criteria) this;
        }

        public Criteria andStr16NotBetween(String value1, String value2) {
            addCriterion("str16 not between", value1, value2, "str16");
            return (Criteria) this;
        }

        public Criteria andStr17IsNull() {
            addCriterion("str17 is null");
            return (Criteria) this;
        }

        public Criteria andStr17IsNotNull() {
            addCriterion("str17 is not null");
            return (Criteria) this;
        }

        public Criteria andStr17EqualTo(String value) {
            addCriterion("str17 =", value, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17NotEqualTo(String value) {
            addCriterion("str17 <>", value, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17GreaterThan(String value) {
            addCriterion("str17 >", value, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17GreaterThanOrEqualTo(String value) {
            addCriterion("str17 >=", value, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17LessThan(String value) {
            addCriterion("str17 <", value, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17LessThanOrEqualTo(String value) {
            addCriterion("str17 <=", value, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17Like(String value) {
            addCriterion("str17 like", value, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17NotLike(String value) {
            addCriterion("str17 not like", value, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17In(List<String> values) {
            addCriterion("str17 in", values, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17NotIn(List<String> values) {
            addCriterion("str17 not in", values, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17Between(String value1, String value2) {
            addCriterion("str17 between", value1, value2, "str17");
            return (Criteria) this;
        }

        public Criteria andStr17NotBetween(String value1, String value2) {
            addCriterion("str17 not between", value1, value2, "str17");
            return (Criteria) this;
        }

        public Criteria andStr18IsNull() {
            addCriterion("str18 is null");
            return (Criteria) this;
        }

        public Criteria andStr18IsNotNull() {
            addCriterion("str18 is not null");
            return (Criteria) this;
        }

        public Criteria andStr18EqualTo(String value) {
            addCriterion("str18 =", value, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18NotEqualTo(String value) {
            addCriterion("str18 <>", value, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18GreaterThan(String value) {
            addCriterion("str18 >", value, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18GreaterThanOrEqualTo(String value) {
            addCriterion("str18 >=", value, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18LessThan(String value) {
            addCriterion("str18 <", value, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18LessThanOrEqualTo(String value) {
            addCriterion("str18 <=", value, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18Like(String value) {
            addCriterion("str18 like", value, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18NotLike(String value) {
            addCriterion("str18 not like", value, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18In(List<String> values) {
            addCriterion("str18 in", values, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18NotIn(List<String> values) {
            addCriterion("str18 not in", values, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18Between(String value1, String value2) {
            addCriterion("str18 between", value1, value2, "str18");
            return (Criteria) this;
        }

        public Criteria andStr18NotBetween(String value1, String value2) {
            addCriterion("str18 not between", value1, value2, "str18");
            return (Criteria) this;
        }

        public Criteria andStr19IsNull() {
            addCriterion("str19 is null");
            return (Criteria) this;
        }

        public Criteria andStr19IsNotNull() {
            addCriterion("str19 is not null");
            return (Criteria) this;
        }

        public Criteria andStr19EqualTo(String value) {
            addCriterion("str19 =", value, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19NotEqualTo(String value) {
            addCriterion("str19 <>", value, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19GreaterThan(String value) {
            addCriterion("str19 >", value, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19GreaterThanOrEqualTo(String value) {
            addCriterion("str19 >=", value, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19LessThan(String value) {
            addCriterion("str19 <", value, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19LessThanOrEqualTo(String value) {
            addCriterion("str19 <=", value, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19Like(String value) {
            addCriterion("str19 like", value, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19NotLike(String value) {
            addCriterion("str19 not like", value, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19In(List<String> values) {
            addCriterion("str19 in", values, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19NotIn(List<String> values) {
            addCriterion("str19 not in", values, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19Between(String value1, String value2) {
            addCriterion("str19 between", value1, value2, "str19");
            return (Criteria) this;
        }

        public Criteria andStr19NotBetween(String value1, String value2) {
            addCriterion("str19 not between", value1, value2, "str19");
            return (Criteria) this;
        }

        public Criteria andStr20IsNull() {
            addCriterion("str20 is null");
            return (Criteria) this;
        }

        public Criteria andStr20IsNotNull() {
            addCriterion("str20 is not null");
            return (Criteria) this;
        }

        public Criteria andStr20EqualTo(String value) {
            addCriterion("str20 =", value, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20NotEqualTo(String value) {
            addCriterion("str20 <>", value, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20GreaterThan(String value) {
            addCriterion("str20 >", value, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20GreaterThanOrEqualTo(String value) {
            addCriterion("str20 >=", value, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20LessThan(String value) {
            addCriterion("str20 <", value, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20LessThanOrEqualTo(String value) {
            addCriterion("str20 <=", value, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20Like(String value) {
            addCriterion("str20 like", value, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20NotLike(String value) {
            addCriterion("str20 not like", value, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20In(List<String> values) {
            addCriterion("str20 in", values, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20NotIn(List<String> values) {
            addCriterion("str20 not in", values, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20Between(String value1, String value2) {
            addCriterion("str20 between", value1, value2, "str20");
            return (Criteria) this;
        }

        public Criteria andStr20NotBetween(String value1, String value2) {
            addCriterion("str20 not between", value1, value2, "str20");
            return (Criteria) this;
        }

        public Criteria andStr21IsNull() {
            addCriterion("str21 is null");
            return (Criteria) this;
        }

        public Criteria andStr21IsNotNull() {
            addCriterion("str21 is not null");
            return (Criteria) this;
        }

        public Criteria andStr21EqualTo(String value) {
            addCriterion("str21 =", value, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21NotEqualTo(String value) {
            addCriterion("str21 <>", value, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21GreaterThan(String value) {
            addCriterion("str21 >", value, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21GreaterThanOrEqualTo(String value) {
            addCriterion("str21 >=", value, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21LessThan(String value) {
            addCriterion("str21 <", value, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21LessThanOrEqualTo(String value) {
            addCriterion("str21 <=", value, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21Like(String value) {
            addCriterion("str21 like", value, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21NotLike(String value) {
            addCriterion("str21 not like", value, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21In(List<String> values) {
            addCriterion("str21 in", values, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21NotIn(List<String> values) {
            addCriterion("str21 not in", values, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21Between(String value1, String value2) {
            addCriterion("str21 between", value1, value2, "str21");
            return (Criteria) this;
        }

        public Criteria andStr21NotBetween(String value1, String value2) {
            addCriterion("str21 not between", value1, value2, "str21");
            return (Criteria) this;
        }

        public Criteria andStr22IsNull() {
            addCriterion("str22 is null");
            return (Criteria) this;
        }

        public Criteria andStr22IsNotNull() {
            addCriterion("str22 is not null");
            return (Criteria) this;
        }

        public Criteria andStr22EqualTo(String value) {
            addCriterion("str22 =", value, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22NotEqualTo(String value) {
            addCriterion("str22 <>", value, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22GreaterThan(String value) {
            addCriterion("str22 >", value, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22GreaterThanOrEqualTo(String value) {
            addCriterion("str22 >=", value, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22LessThan(String value) {
            addCriterion("str22 <", value, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22LessThanOrEqualTo(String value) {
            addCriterion("str22 <=", value, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22Like(String value) {
            addCriterion("str22 like", value, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22NotLike(String value) {
            addCriterion("str22 not like", value, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22In(List<String> values) {
            addCriterion("str22 in", values, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22NotIn(List<String> values) {
            addCriterion("str22 not in", values, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22Between(String value1, String value2) {
            addCriterion("str22 between", value1, value2, "str22");
            return (Criteria) this;
        }

        public Criteria andStr22NotBetween(String value1, String value2) {
            addCriterion("str22 not between", value1, value2, "str22");
            return (Criteria) this;
        }

        public Criteria andStr23IsNull() {
            addCriterion("str23 is null");
            return (Criteria) this;
        }

        public Criteria andStr23IsNotNull() {
            addCriterion("str23 is not null");
            return (Criteria) this;
        }

        public Criteria andStr23EqualTo(String value) {
            addCriterion("str23 =", value, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23NotEqualTo(String value) {
            addCriterion("str23 <>", value, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23GreaterThan(String value) {
            addCriterion("str23 >", value, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23GreaterThanOrEqualTo(String value) {
            addCriterion("str23 >=", value, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23LessThan(String value) {
            addCriterion("str23 <", value, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23LessThanOrEqualTo(String value) {
            addCriterion("str23 <=", value, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23Like(String value) {
            addCriterion("str23 like", value, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23NotLike(String value) {
            addCriterion("str23 not like", value, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23In(List<String> values) {
            addCriterion("str23 in", values, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23NotIn(List<String> values) {
            addCriterion("str23 not in", values, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23Between(String value1, String value2) {
            addCriterion("str23 between", value1, value2, "str23");
            return (Criteria) this;
        }

        public Criteria andStr23NotBetween(String value1, String value2) {
            addCriterion("str23 not between", value1, value2, "str23");
            return (Criteria) this;
        }

        public Criteria andStr24IsNull() {
            addCriterion("str24 is null");
            return (Criteria) this;
        }

        public Criteria andStr24IsNotNull() {
            addCriterion("str24 is not null");
            return (Criteria) this;
        }

        public Criteria andStr24EqualTo(String value) {
            addCriterion("str24 =", value, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24NotEqualTo(String value) {
            addCriterion("str24 <>", value, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24GreaterThan(String value) {
            addCriterion("str24 >", value, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24GreaterThanOrEqualTo(String value) {
            addCriterion("str24 >=", value, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24LessThan(String value) {
            addCriterion("str24 <", value, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24LessThanOrEqualTo(String value) {
            addCriterion("str24 <=", value, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24Like(String value) {
            addCriterion("str24 like", value, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24NotLike(String value) {
            addCriterion("str24 not like", value, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24In(List<String> values) {
            addCriterion("str24 in", values, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24NotIn(List<String> values) {
            addCriterion("str24 not in", values, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24Between(String value1, String value2) {
            addCriterion("str24 between", value1, value2, "str24");
            return (Criteria) this;
        }

        public Criteria andStr24NotBetween(String value1, String value2) {
            addCriterion("str24 not between", value1, value2, "str24");
            return (Criteria) this;
        }

        public Criteria andStr25IsNull() {
            addCriterion("str25 is null");
            return (Criteria) this;
        }

        public Criteria andStr25IsNotNull() {
            addCriterion("str25 is not null");
            return (Criteria) this;
        }

        public Criteria andStr25EqualTo(String value) {
            addCriterion("str25 =", value, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25NotEqualTo(String value) {
            addCriterion("str25 <>", value, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25GreaterThan(String value) {
            addCriterion("str25 >", value, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25GreaterThanOrEqualTo(String value) {
            addCriterion("str25 >=", value, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25LessThan(String value) {
            addCriterion("str25 <", value, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25LessThanOrEqualTo(String value) {
            addCriterion("str25 <=", value, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25Like(String value) {
            addCriterion("str25 like", value, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25NotLike(String value) {
            addCriterion("str25 not like", value, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25In(List<String> values) {
            addCriterion("str25 in", values, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25NotIn(List<String> values) {
            addCriterion("str25 not in", values, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25Between(String value1, String value2) {
            addCriterion("str25 between", value1, value2, "str25");
            return (Criteria) this;
        }

        public Criteria andStr25NotBetween(String value1, String value2) {
            addCriterion("str25 not between", value1, value2, "str25");
            return (Criteria) this;
        }

        public Criteria andStr26IsNull() {
            addCriterion("str26 is null");
            return (Criteria) this;
        }

        public Criteria andStr26IsNotNull() {
            addCriterion("str26 is not null");
            return (Criteria) this;
        }

        public Criteria andStr26EqualTo(String value) {
            addCriterion("str26 =", value, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26NotEqualTo(String value) {
            addCriterion("str26 <>", value, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26GreaterThan(String value) {
            addCriterion("str26 >", value, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26GreaterThanOrEqualTo(String value) {
            addCriterion("str26 >=", value, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26LessThan(String value) {
            addCriterion("str26 <", value, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26LessThanOrEqualTo(String value) {
            addCriterion("str26 <=", value, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26Like(String value) {
            addCriterion("str26 like", value, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26NotLike(String value) {
            addCriterion("str26 not like", value, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26In(List<String> values) {
            addCriterion("str26 in", values, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26NotIn(List<String> values) {
            addCriterion("str26 not in", values, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26Between(String value1, String value2) {
            addCriterion("str26 between", value1, value2, "str26");
            return (Criteria) this;
        }

        public Criteria andStr26NotBetween(String value1, String value2) {
            addCriterion("str26 not between", value1, value2, "str26");
            return (Criteria) this;
        }

        public Criteria andStr27IsNull() {
            addCriterion("str27 is null");
            return (Criteria) this;
        }

        public Criteria andStr27IsNotNull() {
            addCriterion("str27 is not null");
            return (Criteria) this;
        }

        public Criteria andStr27EqualTo(String value) {
            addCriterion("str27 =", value, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27NotEqualTo(String value) {
            addCriterion("str27 <>", value, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27GreaterThan(String value) {
            addCriterion("str27 >", value, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27GreaterThanOrEqualTo(String value) {
            addCriterion("str27 >=", value, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27LessThan(String value) {
            addCriterion("str27 <", value, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27LessThanOrEqualTo(String value) {
            addCriterion("str27 <=", value, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27Like(String value) {
            addCriterion("str27 like", value, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27NotLike(String value) {
            addCriterion("str27 not like", value, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27In(List<String> values) {
            addCriterion("str27 in", values, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27NotIn(List<String> values) {
            addCriterion("str27 not in", values, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27Between(String value1, String value2) {
            addCriterion("str27 between", value1, value2, "str27");
            return (Criteria) this;
        }

        public Criteria andStr27NotBetween(String value1, String value2) {
            addCriterion("str27 not between", value1, value2, "str27");
            return (Criteria) this;
        }

        public Criteria andStr28IsNull() {
            addCriterion("str28 is null");
            return (Criteria) this;
        }

        public Criteria andStr28IsNotNull() {
            addCriterion("str28 is not null");
            return (Criteria) this;
        }

        public Criteria andStr28EqualTo(String value) {
            addCriterion("str28 =", value, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28NotEqualTo(String value) {
            addCriterion("str28 <>", value, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28GreaterThan(String value) {
            addCriterion("str28 >", value, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28GreaterThanOrEqualTo(String value) {
            addCriterion("str28 >=", value, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28LessThan(String value) {
            addCriterion("str28 <", value, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28LessThanOrEqualTo(String value) {
            addCriterion("str28 <=", value, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28Like(String value) {
            addCriterion("str28 like", value, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28NotLike(String value) {
            addCriterion("str28 not like", value, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28In(List<String> values) {
            addCriterion("str28 in", values, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28NotIn(List<String> values) {
            addCriterion("str28 not in", values, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28Between(String value1, String value2) {
            addCriterion("str28 between", value1, value2, "str28");
            return (Criteria) this;
        }

        public Criteria andStr28NotBetween(String value1, String value2) {
            addCriterion("str28 not between", value1, value2, "str28");
            return (Criteria) this;
        }

        public Criteria andStr29IsNull() {
            addCriterion("str29 is null");
            return (Criteria) this;
        }

        public Criteria andStr29IsNotNull() {
            addCriterion("str29 is not null");
            return (Criteria) this;
        }

        public Criteria andStr29EqualTo(String value) {
            addCriterion("str29 =", value, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29NotEqualTo(String value) {
            addCriterion("str29 <>", value, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29GreaterThan(String value) {
            addCriterion("str29 >", value, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29GreaterThanOrEqualTo(String value) {
            addCriterion("str29 >=", value, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29LessThan(String value) {
            addCriterion("str29 <", value, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29LessThanOrEqualTo(String value) {
            addCriterion("str29 <=", value, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29Like(String value) {
            addCriterion("str29 like", value, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29NotLike(String value) {
            addCriterion("str29 not like", value, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29In(List<String> values) {
            addCriterion("str29 in", values, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29NotIn(List<String> values) {
            addCriterion("str29 not in", values, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29Between(String value1, String value2) {
            addCriterion("str29 between", value1, value2, "str29");
            return (Criteria) this;
        }

        public Criteria andStr29NotBetween(String value1, String value2) {
            addCriterion("str29 not between", value1, value2, "str29");
            return (Criteria) this;
        }

        public Criteria andStr30IsNull() {
            addCriterion("str30 is null");
            return (Criteria) this;
        }

        public Criteria andStr30IsNotNull() {
            addCriterion("str30 is not null");
            return (Criteria) this;
        }

        public Criteria andStr30EqualTo(String value) {
            addCriterion("str30 =", value, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30NotEqualTo(String value) {
            addCriterion("str30 <>", value, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30GreaterThan(String value) {
            addCriterion("str30 >", value, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30GreaterThanOrEqualTo(String value) {
            addCriterion("str30 >=", value, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30LessThan(String value) {
            addCriterion("str30 <", value, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30LessThanOrEqualTo(String value) {
            addCriterion("str30 <=", value, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30Like(String value) {
            addCriterion("str30 like", value, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30NotLike(String value) {
            addCriterion("str30 not like", value, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30In(List<String> values) {
            addCriterion("str30 in", values, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30NotIn(List<String> values) {
            addCriterion("str30 not in", values, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30Between(String value1, String value2) {
            addCriterion("str30 between", value1, value2, "str30");
            return (Criteria) this;
        }

        public Criteria andStr30NotBetween(String value1, String value2) {
            addCriterion("str30 not between", value1, value2, "str30");
            return (Criteria) this;
        }

        public Criteria andStr31IsNull() {
            addCriterion("str31 is null");
            return (Criteria) this;
        }

        public Criteria andStr31IsNotNull() {
            addCriterion("str31 is not null");
            return (Criteria) this;
        }

        public Criteria andStr31EqualTo(String value) {
            addCriterion("str31 =", value, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31NotEqualTo(String value) {
            addCriterion("str31 <>", value, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31GreaterThan(String value) {
            addCriterion("str31 >", value, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31GreaterThanOrEqualTo(String value) {
            addCriterion("str31 >=", value, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31LessThan(String value) {
            addCriterion("str31 <", value, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31LessThanOrEqualTo(String value) {
            addCriterion("str31 <=", value, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31Like(String value) {
            addCriterion("str31 like", value, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31NotLike(String value) {
            addCriterion("str31 not like", value, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31In(List<String> values) {
            addCriterion("str31 in", values, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31NotIn(List<String> values) {
            addCriterion("str31 not in", values, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31Between(String value1, String value2) {
            addCriterion("str31 between", value1, value2, "str31");
            return (Criteria) this;
        }

        public Criteria andStr31NotBetween(String value1, String value2) {
            addCriterion("str31 not between", value1, value2, "str31");
            return (Criteria) this;
        }

        public Criteria andStr32IsNull() {
            addCriterion("str32 is null");
            return (Criteria) this;
        }

        public Criteria andStr32IsNotNull() {
            addCriterion("str32 is not null");
            return (Criteria) this;
        }

        public Criteria andStr32EqualTo(String value) {
            addCriterion("str32 =", value, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32NotEqualTo(String value) {
            addCriterion("str32 <>", value, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32GreaterThan(String value) {
            addCriterion("str32 >", value, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32GreaterThanOrEqualTo(String value) {
            addCriterion("str32 >=", value, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32LessThan(String value) {
            addCriterion("str32 <", value, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32LessThanOrEqualTo(String value) {
            addCriterion("str32 <=", value, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32Like(String value) {
            addCriterion("str32 like", value, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32NotLike(String value) {
            addCriterion("str32 not like", value, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32In(List<String> values) {
            addCriterion("str32 in", values, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32NotIn(List<String> values) {
            addCriterion("str32 not in", values, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32Between(String value1, String value2) {
            addCriterion("str32 between", value1, value2, "str32");
            return (Criteria) this;
        }

        public Criteria andStr32NotBetween(String value1, String value2) {
            addCriterion("str32 not between", value1, value2, "str32");
            return (Criteria) this;
        }

        public Criteria andStr33IsNull() {
            addCriterion("str33 is null");
            return (Criteria) this;
        }

        public Criteria andStr33IsNotNull() {
            addCriterion("str33 is not null");
            return (Criteria) this;
        }

        public Criteria andStr33EqualTo(String value) {
            addCriterion("str33 =", value, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33NotEqualTo(String value) {
            addCriterion("str33 <>", value, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33GreaterThan(String value) {
            addCriterion("str33 >", value, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33GreaterThanOrEqualTo(String value) {
            addCriterion("str33 >=", value, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33LessThan(String value) {
            addCriterion("str33 <", value, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33LessThanOrEqualTo(String value) {
            addCriterion("str33 <=", value, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33Like(String value) {
            addCriterion("str33 like", value, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33NotLike(String value) {
            addCriterion("str33 not like", value, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33In(List<String> values) {
            addCriterion("str33 in", values, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33NotIn(List<String> values) {
            addCriterion("str33 not in", values, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33Between(String value1, String value2) {
            addCriterion("str33 between", value1, value2, "str33");
            return (Criteria) this;
        }

        public Criteria andStr33NotBetween(String value1, String value2) {
            addCriterion("str33 not between", value1, value2, "str33");
            return (Criteria) this;
        }

        public Criteria andStr34IsNull() {
            addCriterion("str34 is null");
            return (Criteria) this;
        }

        public Criteria andStr34IsNotNull() {
            addCriterion("str34 is not null");
            return (Criteria) this;
        }

        public Criteria andStr34EqualTo(String value) {
            addCriterion("str34 =", value, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34NotEqualTo(String value) {
            addCriterion("str34 <>", value, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34GreaterThan(String value) {
            addCriterion("str34 >", value, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34GreaterThanOrEqualTo(String value) {
            addCriterion("str34 >=", value, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34LessThan(String value) {
            addCriterion("str34 <", value, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34LessThanOrEqualTo(String value) {
            addCriterion("str34 <=", value, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34Like(String value) {
            addCriterion("str34 like", value, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34NotLike(String value) {
            addCriterion("str34 not like", value, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34In(List<String> values) {
            addCriterion("str34 in", values, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34NotIn(List<String> values) {
            addCriterion("str34 not in", values, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34Between(String value1, String value2) {
            addCriterion("str34 between", value1, value2, "str34");
            return (Criteria) this;
        }

        public Criteria andStr34NotBetween(String value1, String value2) {
            addCriterion("str34 not between", value1, value2, "str34");
            return (Criteria) this;
        }

        public Criteria andStr35IsNull() {
            addCriterion("str35 is null");
            return (Criteria) this;
        }

        public Criteria andStr35IsNotNull() {
            addCriterion("str35 is not null");
            return (Criteria) this;
        }

        public Criteria andStr35EqualTo(String value) {
            addCriterion("str35 =", value, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35NotEqualTo(String value) {
            addCriterion("str35 <>", value, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35GreaterThan(String value) {
            addCriterion("str35 >", value, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35GreaterThanOrEqualTo(String value) {
            addCriterion("str35 >=", value, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35LessThan(String value) {
            addCriterion("str35 <", value, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35LessThanOrEqualTo(String value) {
            addCriterion("str35 <=", value, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35Like(String value) {
            addCriterion("str35 like", value, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35NotLike(String value) {
            addCriterion("str35 not like", value, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35In(List<String> values) {
            addCriterion("str35 in", values, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35NotIn(List<String> values) {
            addCriterion("str35 not in", values, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35Between(String value1, String value2) {
            addCriterion("str35 between", value1, value2, "str35");
            return (Criteria) this;
        }

        public Criteria andStr35NotBetween(String value1, String value2) {
            addCriterion("str35 not between", value1, value2, "str35");
            return (Criteria) this;
        }

        public Criteria andStr36IsNull() {
            addCriterion("str36 is null");
            return (Criteria) this;
        }

        public Criteria andStr36IsNotNull() {
            addCriterion("str36 is not null");
            return (Criteria) this;
        }

        public Criteria andStr36EqualTo(String value) {
            addCriterion("str36 =", value, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36NotEqualTo(String value) {
            addCriterion("str36 <>", value, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36GreaterThan(String value) {
            addCriterion("str36 >", value, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36GreaterThanOrEqualTo(String value) {
            addCriterion("str36 >=", value, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36LessThan(String value) {
            addCriterion("str36 <", value, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36LessThanOrEqualTo(String value) {
            addCriterion("str36 <=", value, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36Like(String value) {
            addCriterion("str36 like", value, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36NotLike(String value) {
            addCriterion("str36 not like", value, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36In(List<String> values) {
            addCriterion("str36 in", values, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36NotIn(List<String> values) {
            addCriterion("str36 not in", values, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36Between(String value1, String value2) {
            addCriterion("str36 between", value1, value2, "str36");
            return (Criteria) this;
        }

        public Criteria andStr36NotBetween(String value1, String value2) {
            addCriterion("str36 not between", value1, value2, "str36");
            return (Criteria) this;
        }

        public Criteria andStr37IsNull() {
            addCriterion("str37 is null");
            return (Criteria) this;
        }

        public Criteria andStr37IsNotNull() {
            addCriterion("str37 is not null");
            return (Criteria) this;
        }

        public Criteria andStr37EqualTo(String value) {
            addCriterion("str37 =", value, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37NotEqualTo(String value) {
            addCriterion("str37 <>", value, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37GreaterThan(String value) {
            addCriterion("str37 >", value, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37GreaterThanOrEqualTo(String value) {
            addCriterion("str37 >=", value, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37LessThan(String value) {
            addCriterion("str37 <", value, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37LessThanOrEqualTo(String value) {
            addCriterion("str37 <=", value, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37Like(String value) {
            addCriterion("str37 like", value, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37NotLike(String value) {
            addCriterion("str37 not like", value, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37In(List<String> values) {
            addCriterion("str37 in", values, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37NotIn(List<String> values) {
            addCriterion("str37 not in", values, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37Between(String value1, String value2) {
            addCriterion("str37 between", value1, value2, "str37");
            return (Criteria) this;
        }

        public Criteria andStr37NotBetween(String value1, String value2) {
            addCriterion("str37 not between", value1, value2, "str37");
            return (Criteria) this;
        }

        public Criteria andStr38IsNull() {
            addCriterion("str38 is null");
            return (Criteria) this;
        }

        public Criteria andStr38IsNotNull() {
            addCriterion("str38 is not null");
            return (Criteria) this;
        }

        public Criteria andStr38EqualTo(String value) {
            addCriterion("str38 =", value, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38NotEqualTo(String value) {
            addCriterion("str38 <>", value, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38GreaterThan(String value) {
            addCriterion("str38 >", value, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38GreaterThanOrEqualTo(String value) {
            addCriterion("str38 >=", value, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38LessThan(String value) {
            addCriterion("str38 <", value, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38LessThanOrEqualTo(String value) {
            addCriterion("str38 <=", value, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38Like(String value) {
            addCriterion("str38 like", value, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38NotLike(String value) {
            addCriterion("str38 not like", value, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38In(List<String> values) {
            addCriterion("str38 in", values, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38NotIn(List<String> values) {
            addCriterion("str38 not in", values, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38Between(String value1, String value2) {
            addCriterion("str38 between", value1, value2, "str38");
            return (Criteria) this;
        }

        public Criteria andStr38NotBetween(String value1, String value2) {
            addCriterion("str38 not between", value1, value2, "str38");
            return (Criteria) this;
        }

        public Criteria andStr39IsNull() {
            addCriterion("str39 is null");
            return (Criteria) this;
        }

        public Criteria andStr39IsNotNull() {
            addCriterion("str39 is not null");
            return (Criteria) this;
        }

        public Criteria andStr39EqualTo(String value) {
            addCriterion("str39 =", value, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39NotEqualTo(String value) {
            addCriterion("str39 <>", value, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39GreaterThan(String value) {
            addCriterion("str39 >", value, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39GreaterThanOrEqualTo(String value) {
            addCriterion("str39 >=", value, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39LessThan(String value) {
            addCriterion("str39 <", value, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39LessThanOrEqualTo(String value) {
            addCriterion("str39 <=", value, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39Like(String value) {
            addCriterion("str39 like", value, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39NotLike(String value) {
            addCriterion("str39 not like", value, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39In(List<String> values) {
            addCriterion("str39 in", values, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39NotIn(List<String> values) {
            addCriterion("str39 not in", values, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39Between(String value1, String value2) {
            addCriterion("str39 between", value1, value2, "str39");
            return (Criteria) this;
        }

        public Criteria andStr39NotBetween(String value1, String value2) {
            addCriterion("str39 not between", value1, value2, "str39");
            return (Criteria) this;
        }

        public Criteria andStr40IsNull() {
            addCriterion("str40 is null");
            return (Criteria) this;
        }

        public Criteria andStr40IsNotNull() {
            addCriterion("str40 is not null");
            return (Criteria) this;
        }

        public Criteria andStr40EqualTo(String value) {
            addCriterion("str40 =", value, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40NotEqualTo(String value) {
            addCriterion("str40 <>", value, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40GreaterThan(String value) {
            addCriterion("str40 >", value, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40GreaterThanOrEqualTo(String value) {
            addCriterion("str40 >=", value, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40LessThan(String value) {
            addCriterion("str40 <", value, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40LessThanOrEqualTo(String value) {
            addCriterion("str40 <=", value, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40Like(String value) {
            addCriterion("str40 like", value, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40NotLike(String value) {
            addCriterion("str40 not like", value, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40In(List<String> values) {
            addCriterion("str40 in", values, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40NotIn(List<String> values) {
            addCriterion("str40 not in", values, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40Between(String value1, String value2) {
            addCriterion("str40 between", value1, value2, "str40");
            return (Criteria) this;
        }

        public Criteria andStr40NotBetween(String value1, String value2) {
            addCriterion("str40 not between", value1, value2, "str40");
            return (Criteria) this;
        }

        public Criteria andStr41IsNull() {
            addCriterion("str41 is null");
            return (Criteria) this;
        }

        public Criteria andStr41IsNotNull() {
            addCriterion("str41 is not null");
            return (Criteria) this;
        }

        public Criteria andStr41EqualTo(String value) {
            addCriterion("str41 =", value, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41NotEqualTo(String value) {
            addCriterion("str41 <>", value, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41GreaterThan(String value) {
            addCriterion("str41 >", value, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41GreaterThanOrEqualTo(String value) {
            addCriterion("str41 >=", value, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41LessThan(String value) {
            addCriterion("str41 <", value, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41LessThanOrEqualTo(String value) {
            addCriterion("str41 <=", value, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41Like(String value) {
            addCriterion("str41 like", value, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41NotLike(String value) {
            addCriterion("str41 not like", value, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41In(List<String> values) {
            addCriterion("str41 in", values, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41NotIn(List<String> values) {
            addCriterion("str41 not in", values, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41Between(String value1, String value2) {
            addCriterion("str41 between", value1, value2, "str41");
            return (Criteria) this;
        }

        public Criteria andStr41NotBetween(String value1, String value2) {
            addCriterion("str41 not between", value1, value2, "str41");
            return (Criteria) this;
        }

        public Criteria andStr42IsNull() {
            addCriterion("str42 is null");
            return (Criteria) this;
        }

        public Criteria andStr42IsNotNull() {
            addCriterion("str42 is not null");
            return (Criteria) this;
        }

        public Criteria andStr42EqualTo(String value) {
            addCriterion("str42 =", value, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42NotEqualTo(String value) {
            addCriterion("str42 <>", value, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42GreaterThan(String value) {
            addCriterion("str42 >", value, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42GreaterThanOrEqualTo(String value) {
            addCriterion("str42 >=", value, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42LessThan(String value) {
            addCriterion("str42 <", value, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42LessThanOrEqualTo(String value) {
            addCriterion("str42 <=", value, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42Like(String value) {
            addCriterion("str42 like", value, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42NotLike(String value) {
            addCriterion("str42 not like", value, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42In(List<String> values) {
            addCriterion("str42 in", values, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42NotIn(List<String> values) {
            addCriterion("str42 not in", values, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42Between(String value1, String value2) {
            addCriterion("str42 between", value1, value2, "str42");
            return (Criteria) this;
        }

        public Criteria andStr42NotBetween(String value1, String value2) {
            addCriterion("str42 not between", value1, value2, "str42");
            return (Criteria) this;
        }

        public Criteria andStr43IsNull() {
            addCriterion("str43 is null");
            return (Criteria) this;
        }

        public Criteria andStr43IsNotNull() {
            addCriterion("str43 is not null");
            return (Criteria) this;
        }

        public Criteria andStr43EqualTo(String value) {
            addCriterion("str43 =", value, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43NotEqualTo(String value) {
            addCriterion("str43 <>", value, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43GreaterThan(String value) {
            addCriterion("str43 >", value, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43GreaterThanOrEqualTo(String value) {
            addCriterion("str43 >=", value, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43LessThan(String value) {
            addCriterion("str43 <", value, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43LessThanOrEqualTo(String value) {
            addCriterion("str43 <=", value, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43Like(String value) {
            addCriterion("str43 like", value, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43NotLike(String value) {
            addCriterion("str43 not like", value, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43In(List<String> values) {
            addCriterion("str43 in", values, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43NotIn(List<String> values) {
            addCriterion("str43 not in", values, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43Between(String value1, String value2) {
            addCriterion("str43 between", value1, value2, "str43");
            return (Criteria) this;
        }

        public Criteria andStr43NotBetween(String value1, String value2) {
            addCriterion("str43 not between", value1, value2, "str43");
            return (Criteria) this;
        }

        public Criteria andStr44IsNull() {
            addCriterion("str44 is null");
            return (Criteria) this;
        }

        public Criteria andStr44IsNotNull() {
            addCriterion("str44 is not null");
            return (Criteria) this;
        }

        public Criteria andStr44EqualTo(String value) {
            addCriterion("str44 =", value, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44NotEqualTo(String value) {
            addCriterion("str44 <>", value, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44GreaterThan(String value) {
            addCriterion("str44 >", value, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44GreaterThanOrEqualTo(String value) {
            addCriterion("str44 >=", value, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44LessThan(String value) {
            addCriterion("str44 <", value, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44LessThanOrEqualTo(String value) {
            addCriterion("str44 <=", value, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44Like(String value) {
            addCriterion("str44 like", value, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44NotLike(String value) {
            addCriterion("str44 not like", value, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44In(List<String> values) {
            addCriterion("str44 in", values, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44NotIn(List<String> values) {
            addCriterion("str44 not in", values, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44Between(String value1, String value2) {
            addCriterion("str44 between", value1, value2, "str44");
            return (Criteria) this;
        }

        public Criteria andStr44NotBetween(String value1, String value2) {
            addCriterion("str44 not between", value1, value2, "str44");
            return (Criteria) this;
        }

        public Criteria andStr45IsNull() {
            addCriterion("str45 is null");
            return (Criteria) this;
        }

        public Criteria andStr45IsNotNull() {
            addCriterion("str45 is not null");
            return (Criteria) this;
        }

        public Criteria andStr45EqualTo(String value) {
            addCriterion("str45 =", value, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45NotEqualTo(String value) {
            addCriterion("str45 <>", value, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45GreaterThan(String value) {
            addCriterion("str45 >", value, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45GreaterThanOrEqualTo(String value) {
            addCriterion("str45 >=", value, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45LessThan(String value) {
            addCriterion("str45 <", value, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45LessThanOrEqualTo(String value) {
            addCriterion("str45 <=", value, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45Like(String value) {
            addCriterion("str45 like", value, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45NotLike(String value) {
            addCriterion("str45 not like", value, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45In(List<String> values) {
            addCriterion("str45 in", values, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45NotIn(List<String> values) {
            addCriterion("str45 not in", values, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45Between(String value1, String value2) {
            addCriterion("str45 between", value1, value2, "str45");
            return (Criteria) this;
        }

        public Criteria andStr45NotBetween(String value1, String value2) {
            addCriterion("str45 not between", value1, value2, "str45");
            return (Criteria) this;
        }

        public Criteria andStr46IsNull() {
            addCriterion("str46 is null");
            return (Criteria) this;
        }

        public Criteria andStr46IsNotNull() {
            addCriterion("str46 is not null");
            return (Criteria) this;
        }

        public Criteria andStr46EqualTo(String value) {
            addCriterion("str46 =", value, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46NotEqualTo(String value) {
            addCriterion("str46 <>", value, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46GreaterThan(String value) {
            addCriterion("str46 >", value, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46GreaterThanOrEqualTo(String value) {
            addCriterion("str46 >=", value, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46LessThan(String value) {
            addCriterion("str46 <", value, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46LessThanOrEqualTo(String value) {
            addCriterion("str46 <=", value, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46Like(String value) {
            addCriterion("str46 like", value, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46NotLike(String value) {
            addCriterion("str46 not like", value, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46In(List<String> values) {
            addCriterion("str46 in", values, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46NotIn(List<String> values) {
            addCriterion("str46 not in", values, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46Between(String value1, String value2) {
            addCriterion("str46 between", value1, value2, "str46");
            return (Criteria) this;
        }

        public Criteria andStr46NotBetween(String value1, String value2) {
            addCriterion("str46 not between", value1, value2, "str46");
            return (Criteria) this;
        }

        public Criteria andStr47IsNull() {
            addCriterion("str47 is null");
            return (Criteria) this;
        }

        public Criteria andStr47IsNotNull() {
            addCriterion("str47 is not null");
            return (Criteria) this;
        }

        public Criteria andStr47EqualTo(String value) {
            addCriterion("str47 =", value, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47NotEqualTo(String value) {
            addCriterion("str47 <>", value, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47GreaterThan(String value) {
            addCriterion("str47 >", value, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47GreaterThanOrEqualTo(String value) {
            addCriterion("str47 >=", value, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47LessThan(String value) {
            addCriterion("str47 <", value, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47LessThanOrEqualTo(String value) {
            addCriterion("str47 <=", value, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47Like(String value) {
            addCriterion("str47 like", value, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47NotLike(String value) {
            addCriterion("str47 not like", value, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47In(List<String> values) {
            addCriterion("str47 in", values, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47NotIn(List<String> values) {
            addCriterion("str47 not in", values, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47Between(String value1, String value2) {
            addCriterion("str47 between", value1, value2, "str47");
            return (Criteria) this;
        }

        public Criteria andStr47NotBetween(String value1, String value2) {
            addCriterion("str47 not between", value1, value2, "str47");
            return (Criteria) this;
        }

        public Criteria andStr48IsNull() {
            addCriterion("str48 is null");
            return (Criteria) this;
        }

        public Criteria andStr48IsNotNull() {
            addCriterion("str48 is not null");
            return (Criteria) this;
        }

        public Criteria andStr48EqualTo(String value) {
            addCriterion("str48 =", value, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48NotEqualTo(String value) {
            addCriterion("str48 <>", value, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48GreaterThan(String value) {
            addCriterion("str48 >", value, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48GreaterThanOrEqualTo(String value) {
            addCriterion("str48 >=", value, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48LessThan(String value) {
            addCriterion("str48 <", value, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48LessThanOrEqualTo(String value) {
            addCriterion("str48 <=", value, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48Like(String value) {
            addCriterion("str48 like", value, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48NotLike(String value) {
            addCriterion("str48 not like", value, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48In(List<String> values) {
            addCriterion("str48 in", values, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48NotIn(List<String> values) {
            addCriterion("str48 not in", values, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48Between(String value1, String value2) {
            addCriterion("str48 between", value1, value2, "str48");
            return (Criteria) this;
        }

        public Criteria andStr48NotBetween(String value1, String value2) {
            addCriterion("str48 not between", value1, value2, "str48");
            return (Criteria) this;
        }

        public Criteria andStr49IsNull() {
            addCriterion("str49 is null");
            return (Criteria) this;
        }

        public Criteria andStr49IsNotNull() {
            addCriterion("str49 is not null");
            return (Criteria) this;
        }

        public Criteria andStr49EqualTo(String value) {
            addCriterion("str49 =", value, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49NotEqualTo(String value) {
            addCriterion("str49 <>", value, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49GreaterThan(String value) {
            addCriterion("str49 >", value, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49GreaterThanOrEqualTo(String value) {
            addCriterion("str49 >=", value, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49LessThan(String value) {
            addCriterion("str49 <", value, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49LessThanOrEqualTo(String value) {
            addCriterion("str49 <=", value, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49Like(String value) {
            addCriterion("str49 like", value, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49NotLike(String value) {
            addCriterion("str49 not like", value, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49In(List<String> values) {
            addCriterion("str49 in", values, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49NotIn(List<String> values) {
            addCriterion("str49 not in", values, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49Between(String value1, String value2) {
            addCriterion("str49 between", value1, value2, "str49");
            return (Criteria) this;
        }

        public Criteria andStr49NotBetween(String value1, String value2) {
            addCriterion("str49 not between", value1, value2, "str49");
            return (Criteria) this;
        }

        public Criteria andStr50IsNull() {
            addCriterion("str50 is null");
            return (Criteria) this;
        }

        public Criteria andStr50IsNotNull() {
            addCriterion("str50 is not null");
            return (Criteria) this;
        }

        public Criteria andStr50EqualTo(String value) {
            addCriterion("str50 =", value, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50NotEqualTo(String value) {
            addCriterion("str50 <>", value, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50GreaterThan(String value) {
            addCriterion("str50 >", value, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50GreaterThanOrEqualTo(String value) {
            addCriterion("str50 >=", value, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50LessThan(String value) {
            addCriterion("str50 <", value, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50LessThanOrEqualTo(String value) {
            addCriterion("str50 <=", value, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50Like(String value) {
            addCriterion("str50 like", value, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50NotLike(String value) {
            addCriterion("str50 not like", value, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50In(List<String> values) {
            addCriterion("str50 in", values, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50NotIn(List<String> values) {
            addCriterion("str50 not in", values, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50Between(String value1, String value2) {
            addCriterion("str50 between", value1, value2, "str50");
            return (Criteria) this;
        }

        public Criteria andStr50NotBetween(String value1, String value2) {
            addCriterion("str50 not between", value1, value2, "str50");
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
