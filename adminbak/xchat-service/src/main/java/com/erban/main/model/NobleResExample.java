package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NobleResExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public NobleResExample() {
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

        public Criteria andNobleIdIsNull() {
            addCriterion("noble_id is null");
            return (Criteria) this;
        }

        public Criteria andNobleIdIsNotNull() {
            addCriterion("noble_id is not null");
            return (Criteria) this;
        }

        public Criteria andNobleIdEqualTo(Integer value) {
            addCriterion("noble_id =", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotEqualTo(Integer value) {
            addCriterion("noble_id <>", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdGreaterThan(Integer value) {
            addCriterion("noble_id >", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("noble_id >=", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdLessThan(Integer value) {
            addCriterion("noble_id <", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdLessThanOrEqualTo(Integer value) {
            addCriterion("noble_id <=", value, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdIn(List<Integer> values) {
            addCriterion("noble_id in", values, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotIn(List<Integer> values) {
            addCriterion("noble_id not in", values, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdBetween(Integer value1, Integer value2) {
            addCriterion("noble_id between", value1, value2, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleIdNotBetween(Integer value1, Integer value2) {
            addCriterion("noble_id not between", value1, value2, "nobleId");
            return (Criteria) this;
        }

        public Criteria andNobleNameIsNull() {
            addCriterion("noble_name is null");
            return (Criteria) this;
        }

        public Criteria andNobleNameIsNotNull() {
            addCriterion("noble_name is not null");
            return (Criteria) this;
        }

        public Criteria andNobleNameEqualTo(String value) {
            addCriterion("noble_name =", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotEqualTo(String value) {
            addCriterion("noble_name <>", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameGreaterThan(String value) {
            addCriterion("noble_name >", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameGreaterThanOrEqualTo(String value) {
            addCriterion("noble_name >=", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLessThan(String value) {
            addCriterion("noble_name <", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLessThanOrEqualTo(String value) {
            addCriterion("noble_name <=", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameLike(String value) {
            addCriterion("noble_name like", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotLike(String value) {
            addCriterion("noble_name not like", value, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameIn(List<String> values) {
            addCriterion("noble_name in", values, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotIn(List<String> values) {
            addCriterion("noble_name not in", values, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameBetween(String value1, String value2) {
            addCriterion("noble_name between", value1, value2, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNobleNameNotBetween(String value1, String value2) {
            addCriterion("noble_name not between", value1, value2, "nobleName");
            return (Criteria) this;
        }

        public Criteria andNameIsNull() {
            addCriterion("name is null");
            return (Criteria) this;
        }

        public Criteria andNameIsNotNull() {
            addCriterion("name is not null");
            return (Criteria) this;
        }

        public Criteria andNameEqualTo(String value) {
            addCriterion("name =", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotEqualTo(String value) {
            addCriterion("name <>", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThan(String value) {
            addCriterion("name >", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameGreaterThanOrEqualTo(String value) {
            addCriterion("name >=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThan(String value) {
            addCriterion("name <", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLessThanOrEqualTo(String value) {
            addCriterion("name <=", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameLike(String value) {
            addCriterion("name like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotLike(String value) {
            addCriterion("name not like", value, "name");
            return (Criteria) this;
        }

        public Criteria andNameIn(List<String> values) {
            addCriterion("name in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotIn(List<String> values) {
            addCriterion("name not in", values, "name");
            return (Criteria) this;
        }

        public Criteria andNameBetween(String value1, String value2) {
            addCriterion("name between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andNameNotBetween(String value1, String value2) {
            addCriterion("name not between", value1, value2, "name");
            return (Criteria) this;
        }

        public Criteria andValueIsNull() {
            addCriterion("value is null");
            return (Criteria) this;
        }

        public Criteria andValueIsNotNull() {
            addCriterion("value is not null");
            return (Criteria) this;
        }

        public Criteria andValueEqualTo(String value) {
            addCriterion("value =", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotEqualTo(String value) {
            addCriterion("value <>", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThan(String value) {
            addCriterion("value >", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueGreaterThanOrEqualTo(String value) {
            addCriterion("value >=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThan(String value) {
            addCriterion("value <", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLessThanOrEqualTo(String value) {
            addCriterion("value <=", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueLike(String value) {
            addCriterion("value like", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotLike(String value) {
            addCriterion("value not like", value, "value");
            return (Criteria) this;
        }

        public Criteria andValueIn(List<String> values) {
            addCriterion("value in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotIn(List<String> values) {
            addCriterion("value not in", values, "value");
            return (Criteria) this;
        }

        public Criteria andValueBetween(String value1, String value2) {
            addCriterion("value between", value1, value2, "value");
            return (Criteria) this;
        }

        public Criteria andValueNotBetween(String value1, String value2) {
            addCriterion("value not between", value1, value2, "value");
            return (Criteria) this;
        }

        public Criteria andPreviewIsNull() {
            addCriterion("preview is null");
            return (Criteria) this;
        }

        public Criteria andPreviewIsNotNull() {
            addCriterion("preview is not null");
            return (Criteria) this;
        }

        public Criteria andPreviewEqualTo(String value) {
            addCriterion("preview =", value, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewNotEqualTo(String value) {
            addCriterion("preview <>", value, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewGreaterThan(String value) {
            addCriterion("preview >", value, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewGreaterThanOrEqualTo(String value) {
            addCriterion("preview >=", value, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewLessThan(String value) {
            addCriterion("preview <", value, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewLessThanOrEqualTo(String value) {
            addCriterion("preview <=", value, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewLike(String value) {
            addCriterion("preview like", value, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewNotLike(String value) {
            addCriterion("preview not like", value, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewIn(List<String> values) {
            addCriterion("preview in", values, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewNotIn(List<String> values) {
            addCriterion("preview not in", values, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewBetween(String value1, String value2) {
            addCriterion("preview between", value1, value2, "preview");
            return (Criteria) this;
        }

        public Criteria andPreviewNotBetween(String value1, String value2) {
            addCriterion("preview not between", value1, value2, "preview");
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

        public Criteria andResTypeIsNull() {
            addCriterion("res_type is null");
            return (Criteria) this;
        }

        public Criteria andResTypeIsNotNull() {
            addCriterion("res_type is not null");
            return (Criteria) this;
        }

        public Criteria andResTypeEqualTo(Byte value) {
            addCriterion("res_type =", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeNotEqualTo(Byte value) {
            addCriterion("res_type <>", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeGreaterThan(Byte value) {
            addCriterion("res_type >", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("res_type >=", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeLessThan(Byte value) {
            addCriterion("res_type <", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeLessThanOrEqualTo(Byte value) {
            addCriterion("res_type <=", value, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeIn(List<Byte> values) {
            addCriterion("res_type in", values, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeNotIn(List<Byte> values) {
            addCriterion("res_type not in", values, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeBetween(Byte value1, Byte value2) {
            addCriterion("res_type between", value1, value2, "resType");
            return (Criteria) this;
        }

        public Criteria andResTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("res_type not between", value1, value2, "resType");
            return (Criteria) this;
        }

        public Criteria andIsDynIsNull() {
            addCriterion("is_dyn is null");
            return (Criteria) this;
        }

        public Criteria andIsDynIsNotNull() {
            addCriterion("is_dyn is not null");
            return (Criteria) this;
        }

        public Criteria andIsDynEqualTo(Byte value) {
            addCriterion("is_dyn =", value, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynNotEqualTo(Byte value) {
            addCriterion("is_dyn <>", value, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynGreaterThan(Byte value) {
            addCriterion("is_dyn >", value, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_dyn >=", value, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynLessThan(Byte value) {
            addCriterion("is_dyn <", value, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynLessThanOrEqualTo(Byte value) {
            addCriterion("is_dyn <=", value, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynIn(List<Byte> values) {
            addCriterion("is_dyn in", values, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynNotIn(List<Byte> values) {
            addCriterion("is_dyn not in", values, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynBetween(Byte value1, Byte value2) {
            addCriterion("is_dyn between", value1, value2, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDynNotBetween(Byte value1, Byte value2) {
            addCriterion("is_dyn not between", value1, value2, "isDyn");
            return (Criteria) this;
        }

        public Criteria andIsDefIsNull() {
            addCriterion("is_def is null");
            return (Criteria) this;
        }

        public Criteria andIsDefIsNotNull() {
            addCriterion("is_def is not null");
            return (Criteria) this;
        }

        public Criteria andIsDefEqualTo(Byte value) {
            addCriterion("is_def =", value, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefNotEqualTo(Byte value) {
            addCriterion("is_def <>", value, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefGreaterThan(Byte value) {
            addCriterion("is_def >", value, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_def >=", value, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefLessThan(Byte value) {
            addCriterion("is_def <", value, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefLessThanOrEqualTo(Byte value) {
            addCriterion("is_def <=", value, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefIn(List<Byte> values) {
            addCriterion("is_def in", values, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefNotIn(List<Byte> values) {
            addCriterion("is_def not in", values, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefBetween(Byte value1, Byte value2) {
            addCriterion("is_def between", value1, value2, "isDef");
            return (Criteria) this;
        }

        public Criteria andIsDefNotBetween(Byte value1, Byte value2) {
            addCriterion("is_def not between", value1, value2, "isDef");
            return (Criteria) this;
        }

        public Criteria andSeqIsNull() {
            addCriterion("seq is null");
            return (Criteria) this;
        }

        public Criteria andSeqIsNotNull() {
            addCriterion("seq is not null");
            return (Criteria) this;
        }

        public Criteria andSeqEqualTo(Integer value) {
            addCriterion("seq =", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotEqualTo(Integer value) {
            addCriterion("seq <>", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqGreaterThan(Integer value) {
            addCriterion("seq >", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqGreaterThanOrEqualTo(Integer value) {
            addCriterion("seq >=", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqLessThan(Integer value) {
            addCriterion("seq <", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqLessThanOrEqualTo(Integer value) {
            addCriterion("seq <=", value, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqIn(List<Integer> values) {
            addCriterion("seq in", values, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotIn(List<Integer> values) {
            addCriterion("seq not in", values, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqBetween(Integer value1, Integer value2) {
            addCriterion("seq between", value1, value2, "seq");
            return (Criteria) this;
        }

        public Criteria andSeqNotBetween(Integer value1, Integer value2) {
            addCriterion("seq not between", value1, value2, "seq");
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

        public Criteria andTmpstrIsNull() {
            addCriterion("tmpstr is null");
            return (Criteria) this;
        }

        public Criteria andTmpstrIsNotNull() {
            addCriterion("tmpstr is not null");
            return (Criteria) this;
        }

        public Criteria andTmpstrEqualTo(String value) {
            addCriterion("tmpstr =", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrNotEqualTo(String value) {
            addCriterion("tmpstr <>", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrGreaterThan(String value) {
            addCriterion("tmpstr >", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrGreaterThanOrEqualTo(String value) {
            addCriterion("tmpstr >=", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrLessThan(String value) {
            addCriterion("tmpstr <", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrLessThanOrEqualTo(String value) {
            addCriterion("tmpstr <=", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrLike(String value) {
            addCriterion("tmpstr like", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrNotLike(String value) {
            addCriterion("tmpstr not like", value, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrIn(List<String> values) {
            addCriterion("tmpstr in", values, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrNotIn(List<String> values) {
            addCriterion("tmpstr not in", values, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrBetween(String value1, String value2) {
            addCriterion("tmpstr between", value1, value2, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpstrNotBetween(String value1, String value2) {
            addCriterion("tmpstr not between", value1, value2, "tmpstr");
            return (Criteria) this;
        }

        public Criteria andTmpintIsNull() {
            addCriterion("tmpint is null");
            return (Criteria) this;
        }

        public Criteria andTmpintIsNotNull() {
            addCriterion("tmpint is not null");
            return (Criteria) this;
        }

        public Criteria andTmpintEqualTo(Integer value) {
            addCriterion("tmpint =", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintNotEqualTo(Integer value) {
            addCriterion("tmpint <>", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintGreaterThan(Integer value) {
            addCriterion("tmpint >", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintGreaterThanOrEqualTo(Integer value) {
            addCriterion("tmpint >=", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintLessThan(Integer value) {
            addCriterion("tmpint <", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintLessThanOrEqualTo(Integer value) {
            addCriterion("tmpint <=", value, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintIn(List<Integer> values) {
            addCriterion("tmpint in", values, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintNotIn(List<Integer> values) {
            addCriterion("tmpint not in", values, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintBetween(Integer value1, Integer value2) {
            addCriterion("tmpint between", value1, value2, "tmpint");
            return (Criteria) this;
        }

        public Criteria andTmpintNotBetween(Integer value1, Integer value2) {
            addCriterion("tmpint not between", value1, value2, "tmpint");
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
