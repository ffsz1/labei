package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDrawRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UserDrawRecordExample() {
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

        public Criteria andDrawStatusIsNull() {
            addCriterion("draw_status is null");
            return (Criteria) this;
        }

        public Criteria andDrawStatusIsNotNull() {
            addCriterion("draw_status is not null");
            return (Criteria) this;
        }

        public Criteria andDrawStatusEqualTo(Byte value) {
            addCriterion("draw_status =", value, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusNotEqualTo(Byte value) {
            addCriterion("draw_status <>", value, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusGreaterThan(Byte value) {
            addCriterion("draw_status >", value, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("draw_status >=", value, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusLessThan(Byte value) {
            addCriterion("draw_status <", value, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusLessThanOrEqualTo(Byte value) {
            addCriterion("draw_status <=", value, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusIn(List<Byte> values) {
            addCriterion("draw_status in", values, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusNotIn(List<Byte> values) {
            addCriterion("draw_status not in", values, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusBetween(Byte value1, Byte value2) {
            addCriterion("draw_status between", value1, value2, "drawStatus");
            return (Criteria) this;
        }

        public Criteria andDrawStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("draw_status not between", value1, value2, "drawStatus");
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

        public Criteria andSrcObjIdIsNull() {
            addCriterion("src_obj_id is null");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdIsNotNull() {
            addCriterion("src_obj_id is not null");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdEqualTo(String value) {
            addCriterion("src_obj_id =", value, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdNotEqualTo(String value) {
            addCriterion("src_obj_id <>", value, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdGreaterThan(String value) {
            addCriterion("src_obj_id >", value, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdGreaterThanOrEqualTo(String value) {
            addCriterion("src_obj_id >=", value, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdLessThan(String value) {
            addCriterion("src_obj_id <", value, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdLessThanOrEqualTo(String value) {
            addCriterion("src_obj_id <=", value, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdLike(String value) {
            addCriterion("src_obj_id like", value, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdNotLike(String value) {
            addCriterion("src_obj_id not like", value, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdIn(List<String> values) {
            addCriterion("src_obj_id in", values, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdNotIn(List<String> values) {
            addCriterion("src_obj_id not in", values, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdBetween(String value1, String value2) {
            addCriterion("src_obj_id between", value1, value2, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjIdNotBetween(String value1, String value2) {
            addCriterion("src_obj_id not between", value1, value2, "srcObjId");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameIsNull() {
            addCriterion("src_obj_name is null");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameIsNotNull() {
            addCriterion("src_obj_name is not null");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameEqualTo(String value) {
            addCriterion("src_obj_name =", value, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameNotEqualTo(String value) {
            addCriterion("src_obj_name <>", value, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameGreaterThan(String value) {
            addCriterion("src_obj_name >", value, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameGreaterThanOrEqualTo(String value) {
            addCriterion("src_obj_name >=", value, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameLessThan(String value) {
            addCriterion("src_obj_name <", value, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameLessThanOrEqualTo(String value) {
            addCriterion("src_obj_name <=", value, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameLike(String value) {
            addCriterion("src_obj_name like", value, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameNotLike(String value) {
            addCriterion("src_obj_name not like", value, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameIn(List<String> values) {
            addCriterion("src_obj_name in", values, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameNotIn(List<String> values) {
            addCriterion("src_obj_name not in", values, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameBetween(String value1, String value2) {
            addCriterion("src_obj_name between", value1, value2, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjNameNotBetween(String value1, String value2) {
            addCriterion("src_obj_name not between", value1, value2, "srcObjName");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountIsNull() {
            addCriterion("src_obj_amount is null");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountIsNotNull() {
            addCriterion("src_obj_amount is not null");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountEqualTo(Long value) {
            addCriterion("src_obj_amount =", value, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountNotEqualTo(Long value) {
            addCriterion("src_obj_amount <>", value, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountGreaterThan(Long value) {
            addCriterion("src_obj_amount >", value, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountGreaterThanOrEqualTo(Long value) {
            addCriterion("src_obj_amount >=", value, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountLessThan(Long value) {
            addCriterion("src_obj_amount <", value, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountLessThanOrEqualTo(Long value) {
            addCriterion("src_obj_amount <=", value, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountIn(List<Long> values) {
            addCriterion("src_obj_amount in", values, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountNotIn(List<Long> values) {
            addCriterion("src_obj_amount not in", values, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountBetween(Long value1, Long value2) {
            addCriterion("src_obj_amount between", value1, value2, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andSrcObjAmountNotBetween(Long value1, Long value2) {
            addCriterion("src_obj_amount not between", value1, value2, "srcObjAmount");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdIsNull() {
            addCriterion("draw_prize_id is null");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdIsNotNull() {
            addCriterion("draw_prize_id is not null");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdEqualTo(Integer value) {
            addCriterion("draw_prize_id =", value, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdNotEqualTo(Integer value) {
            addCriterion("draw_prize_id <>", value, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdGreaterThan(Integer value) {
            addCriterion("draw_prize_id >", value, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("draw_prize_id >=", value, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdLessThan(Integer value) {
            addCriterion("draw_prize_id <", value, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdLessThanOrEqualTo(Integer value) {
            addCriterion("draw_prize_id <=", value, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdIn(List<Integer> values) {
            addCriterion("draw_prize_id in", values, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdNotIn(List<Integer> values) {
            addCriterion("draw_prize_id not in", values, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdBetween(Integer value1, Integer value2) {
            addCriterion("draw_prize_id between", value1, value2, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeIdNotBetween(Integer value1, Integer value2) {
            addCriterion("draw_prize_id not between", value1, value2, "drawPrizeId");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameIsNull() {
            addCriterion("draw_prize_name is null");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameIsNotNull() {
            addCriterion("draw_prize_name is not null");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameEqualTo(String value) {
            addCriterion("draw_prize_name =", value, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameNotEqualTo(String value) {
            addCriterion("draw_prize_name <>", value, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameGreaterThan(String value) {
            addCriterion("draw_prize_name >", value, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameGreaterThanOrEqualTo(String value) {
            addCriterion("draw_prize_name >=", value, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameLessThan(String value) {
            addCriterion("draw_prize_name <", value, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameLessThanOrEqualTo(String value) {
            addCriterion("draw_prize_name <=", value, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameLike(String value) {
            addCriterion("draw_prize_name like", value, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameNotLike(String value) {
            addCriterion("draw_prize_name not like", value, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameIn(List<String> values) {
            addCriterion("draw_prize_name in", values, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameNotIn(List<String> values) {
            addCriterion("draw_prize_name not in", values, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameBetween(String value1, String value2) {
            addCriterion("draw_prize_name between", value1, value2, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizeNameNotBetween(String value1, String value2) {
            addCriterion("draw_prize_name not between", value1, value2, "drawPrizeName");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutIsNull() {
            addCriterion("draw_prize_putout is null");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutIsNotNull() {
            addCriterion("draw_prize_putout is not null");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutEqualTo(Byte value) {
            addCriterion("draw_prize_putout =", value, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutNotEqualTo(Byte value) {
            addCriterion("draw_prize_putout <>", value, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutGreaterThan(Byte value) {
            addCriterion("draw_prize_putout >", value, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutGreaterThanOrEqualTo(Byte value) {
            addCriterion("draw_prize_putout >=", value, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutLessThan(Byte value) {
            addCriterion("draw_prize_putout <", value, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutLessThanOrEqualTo(Byte value) {
            addCriterion("draw_prize_putout <=", value, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutIn(List<Byte> values) {
            addCriterion("draw_prize_putout in", values, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutNotIn(List<Byte> values) {
            addCriterion("draw_prize_putout not in", values, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutBetween(Byte value1, Byte value2) {
            addCriterion("draw_prize_putout between", value1, value2, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andDrawPrizePutoutNotBetween(Byte value1, Byte value2) {
            addCriterion("draw_prize_putout not between", value1, value2, "drawPrizePutout");
            return (Criteria) this;
        }

        public Criteria andRecordDescIsNull() {
            addCriterion("record_desc is null");
            return (Criteria) this;
        }

        public Criteria andRecordDescIsNotNull() {
            addCriterion("record_desc is not null");
            return (Criteria) this;
        }

        public Criteria andRecordDescEqualTo(String value) {
            addCriterion("record_desc =", value, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescNotEqualTo(String value) {
            addCriterion("record_desc <>", value, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescGreaterThan(String value) {
            addCriterion("record_desc >", value, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescGreaterThanOrEqualTo(String value) {
            addCriterion("record_desc >=", value, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescLessThan(String value) {
            addCriterion("record_desc <", value, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescLessThanOrEqualTo(String value) {
            addCriterion("record_desc <=", value, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescLike(String value) {
            addCriterion("record_desc like", value, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescNotLike(String value) {
            addCriterion("record_desc not like", value, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescIn(List<String> values) {
            addCriterion("record_desc in", values, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescNotIn(List<String> values) {
            addCriterion("record_desc not in", values, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescBetween(String value1, String value2) {
            addCriterion("record_desc between", value1, value2, "recordDesc");
            return (Criteria) this;
        }

        public Criteria andRecordDescNotBetween(String value1, String value2) {
            addCriterion("record_desc not between", value1, value2, "recordDesc");
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
