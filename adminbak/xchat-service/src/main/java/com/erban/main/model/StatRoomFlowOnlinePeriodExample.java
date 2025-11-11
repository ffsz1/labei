package com.erban.main.model;

import java.util.ArrayList;
import java.util.List;

public class StatRoomFlowOnlinePeriodExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public StatRoomFlowOnlinePeriodExample() {
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

        public Criteria andRoomIdIsNull() {
            addCriterion("room_id is null");
            return (Criteria) this;
        }

        public Criteria andRoomIdIsNotNull() {
            addCriterion("room_id is not null");
            return (Criteria) this;
        }

        public Criteria andRoomIdEqualTo(Long value) {
            addCriterion("room_id =", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotEqualTo(Long value) {
            addCriterion("room_id <>", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdGreaterThan(Long value) {
            addCriterion("room_id >", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdGreaterThanOrEqualTo(Long value) {
            addCriterion("room_id >=", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLessThan(Long value) {
            addCriterion("room_id <", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdLessThanOrEqualTo(Long value) {
            addCriterion("room_id <=", value, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdIn(List<Long> values) {
            addCriterion("room_id in", values, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotIn(List<Long> values) {
            addCriterion("room_id not in", values, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdBetween(Long value1, Long value2) {
            addCriterion("room_id between", value1, value2, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomIdNotBetween(Long value1, Long value2) {
            addCriterion("room_id not between", value1, value2, "roomId");
            return (Criteria) this;
        }

        public Criteria andRoomPwdIsNull() {
            addCriterion("room_pwd is null");
            return (Criteria) this;
        }

        public Criteria andRoomPwdIsNotNull() {
            addCriterion("room_pwd is not null");
            return (Criteria) this;
        }

        public Criteria andRoomPwdEqualTo(String value) {
            addCriterion("room_pwd =", value, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdNotEqualTo(String value) {
            addCriterion("room_pwd <>", value, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdGreaterThan(String value) {
            addCriterion("room_pwd >", value, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdGreaterThanOrEqualTo(String value) {
            addCriterion("room_pwd >=", value, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdLessThan(String value) {
            addCriterion("room_pwd <", value, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdLessThanOrEqualTo(String value) {
            addCriterion("room_pwd <=", value, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdLike(String value) {
            addCriterion("room_pwd like", value, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdNotLike(String value) {
            addCriterion("room_pwd not like", value, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdIn(List<String> values) {
            addCriterion("room_pwd in", values, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdNotIn(List<String> values) {
            addCriterion("room_pwd not in", values, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdBetween(String value1, String value2) {
            addCriterion("room_pwd between", value1, value2, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andRoomPwdNotBetween(String value1, String value2) {
            addCriterion("room_pwd not between", value1, value2, "roomPwd");
            return (Criteria) this;
        }

        public Criteria andValidIsNull() {
            addCriterion("valid is null");
            return (Criteria) this;
        }

        public Criteria andValidIsNotNull() {
            addCriterion("valid is not null");
            return (Criteria) this;
        }

        public Criteria andValidEqualTo(Boolean value) {
            addCriterion("valid =", value, "valid");
            return (Criteria) this;
        }

        public Criteria andValidNotEqualTo(Boolean value) {
            addCriterion("valid <>", value, "valid");
            return (Criteria) this;
        }

        public Criteria andValidGreaterThan(Boolean value) {
            addCriterion("valid >", value, "valid");
            return (Criteria) this;
        }

        public Criteria andValidGreaterThanOrEqualTo(Boolean value) {
            addCriterion("valid >=", value, "valid");
            return (Criteria) this;
        }

        public Criteria andValidLessThan(Boolean value) {
            addCriterion("valid <", value, "valid");
            return (Criteria) this;
        }

        public Criteria andValidLessThanOrEqualTo(Boolean value) {
            addCriterion("valid <=", value, "valid");
            return (Criteria) this;
        }

        public Criteria andValidIn(List<Boolean> values) {
            addCriterion("valid in", values, "valid");
            return (Criteria) this;
        }

        public Criteria andValidNotIn(List<Boolean> values) {
            addCriterion("valid not in", values, "valid");
            return (Criteria) this;
        }

        public Criteria andValidBetween(Boolean value1, Boolean value2) {
            addCriterion("valid between", value1, value2, "valid");
            return (Criteria) this;
        }

        public Criteria andValidNotBetween(Boolean value1, Boolean value2) {
            addCriterion("valid not between", value1, value2, "valid");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("title is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("title is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("title =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("title <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("title >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("title >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("title <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("title <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("title like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("title not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("title in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("title not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("title between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("title not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTagIdIsNull() {
            addCriterion("tag_id is null");
            return (Criteria) this;
        }

        public Criteria andTagIdIsNotNull() {
            addCriterion("tag_id is not null");
            return (Criteria) this;
        }

        public Criteria andTagIdEqualTo(Integer value) {
            addCriterion("tag_id =", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotEqualTo(Integer value) {
            addCriterion("tag_id <>", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdGreaterThan(Integer value) {
            addCriterion("tag_id >", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("tag_id >=", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdLessThan(Integer value) {
            addCriterion("tag_id <", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdLessThanOrEqualTo(Integer value) {
            addCriterion("tag_id <=", value, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdIn(List<Integer> values) {
            addCriterion("tag_id in", values, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotIn(List<Integer> values) {
            addCriterion("tag_id not in", values, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdBetween(Integer value1, Integer value2) {
            addCriterion("tag_id between", value1, value2, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagIdNotBetween(Integer value1, Integer value2) {
            addCriterion("tag_id not between", value1, value2, "tagId");
            return (Criteria) this;
        }

        public Criteria andTagPictIsNull() {
            addCriterion("tag_pict is null");
            return (Criteria) this;
        }

        public Criteria andTagPictIsNotNull() {
            addCriterion("tag_pict is not null");
            return (Criteria) this;
        }

        public Criteria andTagPictEqualTo(String value) {
            addCriterion("tag_pict =", value, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictNotEqualTo(String value) {
            addCriterion("tag_pict <>", value, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictGreaterThan(String value) {
            addCriterion("tag_pict >", value, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictGreaterThanOrEqualTo(String value) {
            addCriterion("tag_pict >=", value, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictLessThan(String value) {
            addCriterion("tag_pict <", value, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictLessThanOrEqualTo(String value) {
            addCriterion("tag_pict <=", value, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictLike(String value) {
            addCriterion("tag_pict like", value, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictNotLike(String value) {
            addCriterion("tag_pict not like", value, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictIn(List<String> values) {
            addCriterion("tag_pict in", values, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictNotIn(List<String> values) {
            addCriterion("tag_pict not in", values, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictBetween(String value1, String value2) {
            addCriterion("tag_pict between", value1, value2, "tagPict");
            return (Criteria) this;
        }

        public Criteria andTagPictNotBetween(String value1, String value2) {
            addCriterion("tag_pict not between", value1, value2, "tagPict");
            return (Criteria) this;
        }

        public Criteria andRoomTagIsNull() {
            addCriterion("room_tag is null");
            return (Criteria) this;
        }

        public Criteria andRoomTagIsNotNull() {
            addCriterion("room_tag is not null");
            return (Criteria) this;
        }

        public Criteria andRoomTagEqualTo(String value) {
            addCriterion("room_tag =", value, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagNotEqualTo(String value) {
            addCriterion("room_tag <>", value, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagGreaterThan(String value) {
            addCriterion("room_tag >", value, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagGreaterThanOrEqualTo(String value) {
            addCriterion("room_tag >=", value, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagLessThan(String value) {
            addCriterion("room_tag <", value, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagLessThanOrEqualTo(String value) {
            addCriterion("room_tag <=", value, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagLike(String value) {
            addCriterion("room_tag like", value, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagNotLike(String value) {
            addCriterion("room_tag not like", value, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagIn(List<String> values) {
            addCriterion("room_tag in", values, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagNotIn(List<String> values) {
            addCriterion("room_tag not in", values, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagBetween(String value1, String value2) {
            addCriterion("room_tag between", value1, value2, "roomTag");
            return (Criteria) this;
        }

        public Criteria andRoomTagNotBetween(String value1, String value2) {
            addCriterion("room_tag not between", value1, value2, "roomTag");
            return (Criteria) this;
        }

        public Criteria andBadgeIsNull() {
            addCriterion("badge is null");
            return (Criteria) this;
        }

        public Criteria andBadgeIsNotNull() {
            addCriterion("badge is not null");
            return (Criteria) this;
        }

        public Criteria andBadgeEqualTo(String value) {
            addCriterion("badge =", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeNotEqualTo(String value) {
            addCriterion("badge <>", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeGreaterThan(String value) {
            addCriterion("badge >", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeGreaterThanOrEqualTo(String value) {
            addCriterion("badge >=", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeLessThan(String value) {
            addCriterion("badge <", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeLessThanOrEqualTo(String value) {
            addCriterion("badge <=", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeLike(String value) {
            addCriterion("badge like", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeNotLike(String value) {
            addCriterion("badge not like", value, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeIn(List<String> values) {
            addCriterion("badge in", values, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeNotIn(List<String> values) {
            addCriterion("badge not in", values, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeBetween(String value1, String value2) {
            addCriterion("badge between", value1, value2, "badge");
            return (Criteria) this;
        }

        public Criteria andBadgeNotBetween(String value1, String value2) {
            addCriterion("badge not between", value1, value2, "badge");
            return (Criteria) this;
        }

        public Criteria andGenderIsNull() {
            addCriterion("gender is null");
            return (Criteria) this;
        }

        public Criteria andGenderIsNotNull() {
            addCriterion("gender is not null");
            return (Criteria) this;
        }

        public Criteria andGenderEqualTo(Byte value) {
            addCriterion("gender =", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotEqualTo(Byte value) {
            addCriterion("gender <>", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderGreaterThan(Byte value) {
            addCriterion("gender >", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderGreaterThanOrEqualTo(Byte value) {
            addCriterion("gender >=", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderLessThan(Byte value) {
            addCriterion("gender <", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderLessThanOrEqualTo(Byte value) {
            addCriterion("gender <=", value, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderIn(List<Byte> values) {
            addCriterion("gender in", values, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotIn(List<Byte> values) {
            addCriterion("gender not in", values, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderBetween(Byte value1, Byte value2) {
            addCriterion("gender between", value1, value2, "gender");
            return (Criteria) this;
        }

        public Criteria andGenderNotBetween(Byte value1, Byte value2) {
            addCriterion("gender not between", value1, value2, "gender");
            return (Criteria) this;
        }

        public Criteria andNickIsNull() {
            addCriterion("nick is null");
            return (Criteria) this;
        }

        public Criteria andNickIsNotNull() {
            addCriterion("nick is not null");
            return (Criteria) this;
        }

        public Criteria andNickEqualTo(String value) {
            addCriterion("nick =", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotEqualTo(String value) {
            addCriterion("nick <>", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickGreaterThan(String value) {
            addCriterion("nick >", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickGreaterThanOrEqualTo(String value) {
            addCriterion("nick >=", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLessThan(String value) {
            addCriterion("nick <", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLessThanOrEqualTo(String value) {
            addCriterion("nick <=", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickLike(String value) {
            addCriterion("nick like", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotLike(String value) {
            addCriterion("nick not like", value, "nick");
            return (Criteria) this;
        }

        public Criteria andNickIn(List<String> values) {
            addCriterion("nick in", values, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotIn(List<String> values) {
            addCriterion("nick not in", values, "nick");
            return (Criteria) this;
        }

        public Criteria andNickBetween(String value1, String value2) {
            addCriterion("nick between", value1, value2, "nick");
            return (Criteria) this;
        }

        public Criteria andNickNotBetween(String value1, String value2) {
            addCriterion("nick not between", value1, value2, "nick");
            return (Criteria) this;
        }

        public Criteria andAvatarIsNull() {
            addCriterion("avatar is null");
            return (Criteria) this;
        }

        public Criteria andAvatarIsNotNull() {
            addCriterion("avatar is not null");
            return (Criteria) this;
        }

        public Criteria andAvatarEqualTo(String value) {
            addCriterion("avatar =", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotEqualTo(String value) {
            addCriterion("avatar <>", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarGreaterThan(String value) {
            addCriterion("avatar >", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarGreaterThanOrEqualTo(String value) {
            addCriterion("avatar >=", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLessThan(String value) {
            addCriterion("avatar <", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLessThanOrEqualTo(String value) {
            addCriterion("avatar <=", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarLike(String value) {
            addCriterion("avatar like", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotLike(String value) {
            addCriterion("avatar not like", value, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarIn(List<String> values) {
            addCriterion("avatar in", values, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotIn(List<String> values) {
            addCriterion("avatar not in", values, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarBetween(String value1, String value2) {
            addCriterion("avatar between", value1, value2, "avatar");
            return (Criteria) this;
        }

        public Criteria andAvatarNotBetween(String value1, String value2) {
            addCriterion("avatar not between", value1, value2, "avatar");
            return (Criteria) this;
        }

        public Criteria andRoomDescIsNull() {
            addCriterion("room_desc is null");
            return (Criteria) this;
        }

        public Criteria andRoomDescIsNotNull() {
            addCriterion("room_desc is not null");
            return (Criteria) this;
        }

        public Criteria andRoomDescEqualTo(String value) {
            addCriterion("room_desc =", value, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescNotEqualTo(String value) {
            addCriterion("room_desc <>", value, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescGreaterThan(String value) {
            addCriterion("room_desc >", value, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescGreaterThanOrEqualTo(String value) {
            addCriterion("room_desc >=", value, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescLessThan(String value) {
            addCriterion("room_desc <", value, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescLessThanOrEqualTo(String value) {
            addCriterion("room_desc <=", value, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescLike(String value) {
            addCriterion("room_desc like", value, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescNotLike(String value) {
            addCriterion("room_desc not like", value, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescIn(List<String> values) {
            addCriterion("room_desc in", values, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescNotIn(List<String> values) {
            addCriterion("room_desc not in", values, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescBetween(String value1, String value2) {
            addCriterion("room_desc between", value1, value2, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andRoomDescNotBetween(String value1, String value2) {
            addCriterion("room_desc not between", value1, value2, "roomDesc");
            return (Criteria) this;
        }

        public Criteria andBackPicIsNull() {
            addCriterion("back_pic is null");
            return (Criteria) this;
        }

        public Criteria andBackPicIsNotNull() {
            addCriterion("back_pic is not null");
            return (Criteria) this;
        }

        public Criteria andBackPicEqualTo(String value) {
            addCriterion("back_pic =", value, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicNotEqualTo(String value) {
            addCriterion("back_pic <>", value, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicGreaterThan(String value) {
            addCriterion("back_pic >", value, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicGreaterThanOrEqualTo(String value) {
            addCriterion("back_pic >=", value, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicLessThan(String value) {
            addCriterion("back_pic <", value, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicLessThanOrEqualTo(String value) {
            addCriterion("back_pic <=", value, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicLike(String value) {
            addCriterion("back_pic like", value, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicNotLike(String value) {
            addCriterion("back_pic not like", value, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicIn(List<String> values) {
            addCriterion("back_pic in", values, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicNotIn(List<String> values) {
            addCriterion("back_pic not in", values, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicBetween(String value1, String value2) {
            addCriterion("back_pic between", value1, value2, "backPic");
            return (Criteria) this;
        }

        public Criteria andBackPicNotBetween(String value1, String value2) {
            addCriterion("back_pic not between", value1, value2, "backPic");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusIsNull() {
            addCriterion("operator_status is null");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusIsNotNull() {
            addCriterion("operator_status is not null");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusEqualTo(Byte value) {
            addCriterion("operator_status =", value, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusNotEqualTo(Byte value) {
            addCriterion("operator_status <>", value, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusGreaterThan(Byte value) {
            addCriterion("operator_status >", value, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("operator_status >=", value, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusLessThan(Byte value) {
            addCriterion("operator_status <", value, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusLessThanOrEqualTo(Byte value) {
            addCriterion("operator_status <=", value, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusIn(List<Byte> values) {
            addCriterion("operator_status in", values, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusNotIn(List<Byte> values) {
            addCriterion("operator_status not in", values, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusBetween(Byte value1, Byte value2) {
            addCriterion("operator_status between", value1, value2, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOperatorStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("operator_status not between", value1, value2, "operatorStatus");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomIsNull() {
            addCriterion("official_room is null");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomIsNotNull() {
            addCriterion("official_room is not null");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomEqualTo(Byte value) {
            addCriterion("official_room =", value, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomNotEqualTo(Byte value) {
            addCriterion("official_room <>", value, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomGreaterThan(Byte value) {
            addCriterion("official_room >", value, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomGreaterThanOrEqualTo(Byte value) {
            addCriterion("official_room >=", value, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomLessThan(Byte value) {
            addCriterion("official_room <", value, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomLessThanOrEqualTo(Byte value) {
            addCriterion("official_room <=", value, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomIn(List<Byte> values) {
            addCriterion("official_room in", values, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomNotIn(List<Byte> values) {
            addCriterion("official_room not in", values, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomBetween(Byte value1, Byte value2) {
            addCriterion("official_room between", value1, value2, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andOfficialRoomNotBetween(Byte value1, Byte value2) {
            addCriterion("official_room not between", value1, value2, "officialRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomIsNull() {
            addCriterion("is_permit_room is null");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomIsNotNull() {
            addCriterion("is_permit_room is not null");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomEqualTo(Byte value) {
            addCriterion("is_permit_room =", value, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomNotEqualTo(Byte value) {
            addCriterion("is_permit_room <>", value, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomGreaterThan(Byte value) {
            addCriterion("is_permit_room >", value, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_permit_room >=", value, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomLessThan(Byte value) {
            addCriterion("is_permit_room <", value, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomLessThanOrEqualTo(Byte value) {
            addCriterion("is_permit_room <=", value, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomIn(List<Byte> values) {
            addCriterion("is_permit_room in", values, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomNotIn(List<Byte> values) {
            addCriterion("is_permit_room not in", values, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomBetween(Byte value1, Byte value2) {
            addCriterion("is_permit_room between", value1, value2, "isPermitRoom");
            return (Criteria) this;
        }

        public Criteria andIsPermitRoomNotBetween(Byte value1, Byte value2) {
            addCriterion("is_permit_room not between", value1, value2, "isPermitRoom");
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

        public Criteria andRecomSeqIsNull() {
            addCriterion("recom_seq is null");
            return (Criteria) this;
        }

        public Criteria andRecomSeqIsNotNull() {
            addCriterion("recom_seq is not null");
            return (Criteria) this;
        }

        public Criteria andRecomSeqEqualTo(Long value) {
            addCriterion("recom_seq =", value, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqNotEqualTo(Long value) {
            addCriterion("recom_seq <>", value, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqGreaterThan(Long value) {
            addCriterion("recom_seq >", value, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqGreaterThanOrEqualTo(Long value) {
            addCriterion("recom_seq >=", value, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqLessThan(Long value) {
            addCriterion("recom_seq <", value, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqLessThanOrEqualTo(Long value) {
            addCriterion("recom_seq <=", value, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqIn(List<Long> values) {
            addCriterion("recom_seq in", values, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqNotIn(List<Long> values) {
            addCriterion("recom_seq not in", values, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqBetween(Long value1, Long value2) {
            addCriterion("recom_seq between", value1, value2, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andRecomSeqNotBetween(Long value1, Long value2) {
            addCriterion("recom_seq not between", value1, value2, "recomSeq");
            return (Criteria) this;
        }

        public Criteria andOnlineNumIsNull() {
            addCriterion("online_num is null");
            return (Criteria) this;
        }

        public Criteria andOnlineNumIsNotNull() {
            addCriterion("online_num is not null");
            return (Criteria) this;
        }

        public Criteria andOnlineNumEqualTo(Integer value) {
            addCriterion("online_num =", value, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumNotEqualTo(Integer value) {
            addCriterion("online_num <>", value, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumGreaterThan(Integer value) {
            addCriterion("online_num >", value, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("online_num >=", value, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumLessThan(Integer value) {
            addCriterion("online_num <", value, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumLessThanOrEqualTo(Integer value) {
            addCriterion("online_num <=", value, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumIn(List<Integer> values) {
            addCriterion("online_num in", values, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumNotIn(List<Integer> values) {
            addCriterion("online_num not in", values, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumBetween(Integer value1, Integer value2) {
            addCriterion("online_num between", value1, value2, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andOnlineNumNotBetween(Integer value1, Integer value2) {
            addCriterion("online_num not between", value1, value2, "onlineNum");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalIsNull() {
            addCriterion("flow_sum_total is null");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalIsNotNull() {
            addCriterion("flow_sum_total is not null");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalEqualTo(Long value) {
            addCriterion("flow_sum_total =", value, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalNotEqualTo(Long value) {
            addCriterion("flow_sum_total <>", value, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalGreaterThan(Long value) {
            addCriterion("flow_sum_total >", value, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalGreaterThanOrEqualTo(Long value) {
            addCriterion("flow_sum_total >=", value, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalLessThan(Long value) {
            addCriterion("flow_sum_total <", value, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalLessThanOrEqualTo(Long value) {
            addCriterion("flow_sum_total <=", value, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalIn(List<Long> values) {
            addCriterion("flow_sum_total in", values, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalNotIn(List<Long> values) {
            addCriterion("flow_sum_total not in", values, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalBetween(Long value1, Long value2) {
            addCriterion("flow_sum_total between", value1, value2, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andFlowSumTotalNotBetween(Long value1, Long value2) {
            addCriterion("flow_sum_total not between", value1, value2, "flowSumTotal");
            return (Criteria) this;
        }

        public Criteria andScoreIsNull() {
            addCriterion("score is null");
            return (Criteria) this;
        }

        public Criteria andScoreIsNotNull() {
            addCriterion("score is not null");
            return (Criteria) this;
        }

        public Criteria andScoreEqualTo(Double value) {
            addCriterion("score =", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotEqualTo(Double value) {
            addCriterion("score <>", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreGreaterThan(Double value) {
            addCriterion("score >", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreGreaterThanOrEqualTo(Double value) {
            addCriterion("score >=", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreLessThan(Double value) {
            addCriterion("score <", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreLessThanOrEqualTo(Double value) {
            addCriterion("score <=", value, "score");
            return (Criteria) this;
        }

        public Criteria andScoreIn(List<Double> values) {
            addCriterion("score in", values, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotIn(List<Double> values) {
            addCriterion("score not in", values, "score");
            return (Criteria) this;
        }

        public Criteria andScoreBetween(Double value1, Double value2) {
            addCriterion("score between", value1, value2, "score");
            return (Criteria) this;
        }

        public Criteria andScoreNotBetween(Double value1, Double value2) {
            addCriterion("score not between", value1, value2, "score");
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
