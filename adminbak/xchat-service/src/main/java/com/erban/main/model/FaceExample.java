package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FaceExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public FaceExample() {
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

        public Criteria andFaceIdIsNull() {
            addCriterion("face_id is null");
            return (Criteria) this;
        }

        public Criteria andFaceIdIsNotNull() {
            addCriterion("face_id is not null");
            return (Criteria) this;
        }

        public Criteria andFaceIdEqualTo(Integer value) {
            addCriterion("face_id =", value, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdNotEqualTo(Integer value) {
            addCriterion("face_id <>", value, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdGreaterThan(Integer value) {
            addCriterion("face_id >", value, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("face_id >=", value, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdLessThan(Integer value) {
            addCriterion("face_id <", value, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdLessThanOrEqualTo(Integer value) {
            addCriterion("face_id <=", value, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdIn(List<Integer> values) {
            addCriterion("face_id in", values, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdNotIn(List<Integer> values) {
            addCriterion("face_id not in", values, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdBetween(Integer value1, Integer value2) {
            addCriterion("face_id between", value1, value2, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceIdNotBetween(Integer value1, Integer value2) {
            addCriterion("face_id not between", value1, value2, "faceId");
            return (Criteria) this;
        }

        public Criteria andFaceNameIsNull() {
            addCriterion("face_name is null");
            return (Criteria) this;
        }

        public Criteria andFaceNameIsNotNull() {
            addCriterion("face_name is not null");
            return (Criteria) this;
        }

        public Criteria andFaceNameEqualTo(String value) {
            addCriterion("face_name =", value, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameNotEqualTo(String value) {
            addCriterion("face_name <>", value, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameGreaterThan(String value) {
            addCriterion("face_name >", value, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameGreaterThanOrEqualTo(String value) {
            addCriterion("face_name >=", value, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameLessThan(String value) {
            addCriterion("face_name <", value, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameLessThanOrEqualTo(String value) {
            addCriterion("face_name <=", value, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameLike(String value) {
            addCriterion("face_name like", value, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameNotLike(String value) {
            addCriterion("face_name not like", value, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameIn(List<String> values) {
            addCriterion("face_name in", values, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameNotIn(List<String> values) {
            addCriterion("face_name not in", values, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameBetween(String value1, String value2) {
            addCriterion("face_name between", value1, value2, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceNameNotBetween(String value1, String value2) {
            addCriterion("face_name not between", value1, value2, "faceName");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdIsNull() {
            addCriterion("face_parent_id is null");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdIsNotNull() {
            addCriterion("face_parent_id is not null");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdEqualTo(Integer value) {
            addCriterion("face_parent_id =", value, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdNotEqualTo(Integer value) {
            addCriterion("face_parent_id <>", value, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdGreaterThan(Integer value) {
            addCriterion("face_parent_id >", value, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("face_parent_id >=", value, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdLessThan(Integer value) {
            addCriterion("face_parent_id <", value, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdLessThanOrEqualTo(Integer value) {
            addCriterion("face_parent_id <=", value, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdIn(List<Integer> values) {
            addCriterion("face_parent_id in", values, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdNotIn(List<Integer> values) {
            addCriterion("face_parent_id not in", values, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdBetween(Integer value1, Integer value2) {
            addCriterion("face_parent_id between", value1, value2, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFaceParentIdNotBetween(Integer value1, Integer value2) {
            addCriterion("face_parent_id not between", value1, value2, "faceParentId");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlIsNull() {
            addCriterion("face_pic_url is null");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlIsNotNull() {
            addCriterion("face_pic_url is not null");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlEqualTo(String value) {
            addCriterion("face_pic_url =", value, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlNotEqualTo(String value) {
            addCriterion("face_pic_url <>", value, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlGreaterThan(String value) {
            addCriterion("face_pic_url >", value, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("face_pic_url >=", value, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlLessThan(String value) {
            addCriterion("face_pic_url <", value, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlLessThanOrEqualTo(String value) {
            addCriterion("face_pic_url <=", value, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlLike(String value) {
            addCriterion("face_pic_url like", value, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlNotLike(String value) {
            addCriterion("face_pic_url not like", value, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlIn(List<String> values) {
            addCriterion("face_pic_url in", values, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlNotIn(List<String> values) {
            addCriterion("face_pic_url not in", values, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlBetween(String value1, String value2) {
            addCriterion("face_pic_url between", value1, value2, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andFacePicUrlNotBetween(String value1, String value2) {
            addCriterion("face_pic_url not between", value1, value2, "facePicUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlIsNull() {
            addCriterion("has_gif_url is null");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlIsNotNull() {
            addCriterion("has_gif_url is not null");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlEqualTo(Boolean value) {
            addCriterion("has_gif_url =", value, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlNotEqualTo(Boolean value) {
            addCriterion("has_gif_url <>", value, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlGreaterThan(Boolean value) {
            addCriterion("has_gif_url >", value, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_gif_url >=", value, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlLessThan(Boolean value) {
            addCriterion("has_gif_url <", value, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlLessThanOrEqualTo(Boolean value) {
            addCriterion("has_gif_url <=", value, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlIn(List<Boolean> values) {
            addCriterion("has_gif_url in", values, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlNotIn(List<Boolean> values) {
            addCriterion("has_gif_url not in", values, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlBetween(Boolean value1, Boolean value2) {
            addCriterion("has_gif_url between", value1, value2, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andHasGifUrlNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_gif_url not between", value1, value2, "hasGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlIsNull() {
            addCriterion("face_gif_url is null");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlIsNotNull() {
            addCriterion("face_gif_url is not null");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlEqualTo(String value) {
            addCriterion("face_gif_url =", value, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlNotEqualTo(String value) {
            addCriterion("face_gif_url <>", value, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlGreaterThan(String value) {
            addCriterion("face_gif_url >", value, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlGreaterThanOrEqualTo(String value) {
            addCriterion("face_gif_url >=", value, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlLessThan(String value) {
            addCriterion("face_gif_url <", value, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlLessThanOrEqualTo(String value) {
            addCriterion("face_gif_url <=", value, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlLike(String value) {
            addCriterion("face_gif_url like", value, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlNotLike(String value) {
            addCriterion("face_gif_url not like", value, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlIn(List<String> values) {
            addCriterion("face_gif_url in", values, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlNotIn(List<String> values) {
            addCriterion("face_gif_url not in", values, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlBetween(String value1, String value2) {
            addCriterion("face_gif_url between", value1, value2, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andFaceGifUrlNotBetween(String value1, String value2) {
            addCriterion("face_gif_url not between", value1, value2, "faceGifUrl");
            return (Criteria) this;
        }

        public Criteria andSeqNoIsNull() {
            addCriterion("seq_no is null");
            return (Criteria) this;
        }

        public Criteria andSeqNoIsNotNull() {
            addCriterion("seq_no is not null");
            return (Criteria) this;
        }

        public Criteria andSeqNoEqualTo(Integer value) {
            addCriterion("seq_no =", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotEqualTo(Integer value) {
            addCriterion("seq_no <>", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThan(Integer value) {
            addCriterion("seq_no >", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoGreaterThanOrEqualTo(Integer value) {
            addCriterion("seq_no >=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThan(Integer value) {
            addCriterion("seq_no <", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoLessThanOrEqualTo(Integer value) {
            addCriterion("seq_no <=", value, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoIn(List<Integer> values) {
            addCriterion("seq_no in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotIn(List<Integer> values) {
            addCriterion("seq_no not in", values, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoBetween(Integer value1, Integer value2) {
            addCriterion("seq_no between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andSeqNoNotBetween(Integer value1, Integer value2) {
            addCriterion("seq_no not between", value1, value2, "seqNo");
            return (Criteria) this;
        }

        public Criteria andFaceStatusIsNull() {
            addCriterion("face_status is null");
            return (Criteria) this;
        }

        public Criteria andFaceStatusIsNotNull() {
            addCriterion("face_status is not null");
            return (Criteria) this;
        }

        public Criteria andFaceStatusEqualTo(Byte value) {
            addCriterion("face_status =", value, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusNotEqualTo(Byte value) {
            addCriterion("face_status <>", value, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusGreaterThan(Byte value) {
            addCriterion("face_status >", value, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("face_status >=", value, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusLessThan(Byte value) {
            addCriterion("face_status <", value, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusLessThanOrEqualTo(Byte value) {
            addCriterion("face_status <=", value, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusIn(List<Byte> values) {
            addCriterion("face_status in", values, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusNotIn(List<Byte> values) {
            addCriterion("face_status not in", values, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusBetween(Byte value1, Byte value2) {
            addCriterion("face_status between", value1, value2, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andFaceStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("face_status not between", value1, value2, "faceStatus");
            return (Criteria) this;
        }

        public Criteria andIsShowIsNull() {
            addCriterion("is_show is null");
            return (Criteria) this;
        }

        public Criteria andIsShowIsNotNull() {
            addCriterion("is_show is not null");
            return (Criteria) this;
        }

        public Criteria andIsShowEqualTo(Boolean value) {
            addCriterion("is_show =", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotEqualTo(Boolean value) {
            addCriterion("is_show <>", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowGreaterThan(Boolean value) {
            addCriterion("is_show >", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_show >=", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowLessThan(Boolean value) {
            addCriterion("is_show <", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowLessThanOrEqualTo(Boolean value) {
            addCriterion("is_show <=", value, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowIn(List<Boolean> values) {
            addCriterion("is_show in", values, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotIn(List<Boolean> values) {
            addCriterion("is_show not in", values, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowBetween(Boolean value1, Boolean value2) {
            addCriterion("is_show between", value1, value2, "isShow");
            return (Criteria) this;
        }

        public Criteria andIsShowNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_show not between", value1, value2, "isShow");
            return (Criteria) this;
        }

        public Criteria andFaceValueIsNull() {
            addCriterion("face_value is null");
            return (Criteria) this;
        }

        public Criteria andFaceValueIsNotNull() {
            addCriterion("face_value is not null");
            return (Criteria) this;
        }

        public Criteria andFaceValueEqualTo(Integer value) {
            addCriterion("face_value =", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueNotEqualTo(Integer value) {
            addCriterion("face_value <>", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueGreaterThan(Integer value) {
            addCriterion("face_value >", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueGreaterThanOrEqualTo(Integer value) {
            addCriterion("face_value >=", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueLessThan(Integer value) {
            addCriterion("face_value <", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueLessThanOrEqualTo(Integer value) {
            addCriterion("face_value <=", value, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueIn(List<Integer> values) {
            addCriterion("face_value in", values, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueNotIn(List<Integer> values) {
            addCriterion("face_value not in", values, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueBetween(Integer value1, Integer value2) {
            addCriterion("face_value between", value1, value2, "faceValue");
            return (Criteria) this;
        }

        public Criteria andFaceValueNotBetween(Integer value1, Integer value2) {
            addCriterion("face_value not between", value1, value2, "faceValue");
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
