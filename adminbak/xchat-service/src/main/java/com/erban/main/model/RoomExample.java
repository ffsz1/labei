package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoomExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public RoomExample() {
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

        public Criteria andMeetingNameIsNull() {
            addCriterion("meeting_name is null");
            return (Criteria) this;
        }

        public Criteria andMeetingNameIsNotNull() {
            addCriterion("meeting_name is not null");
            return (Criteria) this;
        }

        public Criteria andMeetingNameEqualTo(String value) {
            addCriterion("meeting_name =", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameNotEqualTo(String value) {
            addCriterion("meeting_name <>", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameGreaterThan(String value) {
            addCriterion("meeting_name >", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameGreaterThanOrEqualTo(String value) {
            addCriterion("meeting_name >=", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameLessThan(String value) {
            addCriterion("meeting_name <", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameLessThanOrEqualTo(String value) {
            addCriterion("meeting_name <=", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameLike(String value) {
            addCriterion("meeting_name like", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameNotLike(String value) {
            addCriterion("meeting_name not like", value, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameIn(List<String> values) {
            addCriterion("meeting_name in", values, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameNotIn(List<String> values) {
            addCriterion("meeting_name not in", values, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameBetween(String value1, String value2) {
            addCriterion("meeting_name between", value1, value2, "meetingName");
            return (Criteria) this;
        }

        public Criteria andMeetingNameNotBetween(String value1, String value2) {
            addCriterion("meeting_name not between", value1, value2, "meetingName");
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

        public Criteria andAbChannelTypeIsNull() {
            addCriterion("ab_channel_type is null");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeIsNotNull() {
            addCriterion("ab_channel_type is not null");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeEqualTo(Byte value) {
            addCriterion("ab_channel_type =", value, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeNotEqualTo(Byte value) {
            addCriterion("ab_channel_type <>", value, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeGreaterThan(Byte value) {
            addCriterion("ab_channel_type >", value, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("ab_channel_type >=", value, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeLessThan(Byte value) {
            addCriterion("ab_channel_type <", value, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeLessThanOrEqualTo(Byte value) {
            addCriterion("ab_channel_type <=", value, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeIn(List<Byte> values) {
            addCriterion("ab_channel_type in", values, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeNotIn(List<Byte> values) {
            addCriterion("ab_channel_type not in", values, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeBetween(Byte value1, Byte value2) {
            addCriterion("ab_channel_type between", value1, value2, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andAbChannelTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("ab_channel_type not between", value1, value2, "abChannelType");
            return (Criteria) this;
        }

        public Criteria andRewardIdIsNull() {
            addCriterion("reward_id is null");
            return (Criteria) this;
        }

        public Criteria andRewardIdIsNotNull() {
            addCriterion("reward_id is not null");
            return (Criteria) this;
        }

        public Criteria andRewardIdEqualTo(Long value) {
            addCriterion("reward_id =", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdNotEqualTo(Long value) {
            addCriterion("reward_id <>", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdGreaterThan(Long value) {
            addCriterion("reward_id >", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdGreaterThanOrEqualTo(Long value) {
            addCriterion("reward_id >=", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdLessThan(Long value) {
            addCriterion("reward_id <", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdLessThanOrEqualTo(Long value) {
            addCriterion("reward_id <=", value, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdIn(List<Long> values) {
            addCriterion("reward_id in", values, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdNotIn(List<Long> values) {
            addCriterion("reward_id not in", values, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdBetween(Long value1, Long value2) {
            addCriterion("reward_id between", value1, value2, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardIdNotBetween(Long value1, Long value2) {
            addCriterion("reward_id not between", value1, value2, "rewardId");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyIsNull() {
            addCriterion("reward_money is null");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyIsNotNull() {
            addCriterion("reward_money is not null");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyEqualTo(Long value) {
            addCriterion("reward_money =", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotEqualTo(Long value) {
            addCriterion("reward_money <>", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyGreaterThan(Long value) {
            addCriterion("reward_money >", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyGreaterThanOrEqualTo(Long value) {
            addCriterion("reward_money >=", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyLessThan(Long value) {
            addCriterion("reward_money <", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyLessThanOrEqualTo(Long value) {
            addCriterion("reward_money <=", value, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyIn(List<Long> values) {
            addCriterion("reward_money in", values, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotIn(List<Long> values) {
            addCriterion("reward_money not in", values, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyBetween(Long value1, Long value2) {
            addCriterion("reward_money between", value1, value2, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andRewardMoneyNotBetween(Long value1, Long value2) {
            addCriterion("reward_money not between", value1, value2, "rewardMoney");
            return (Criteria) this;
        }

        public Criteria andServDuraIsNull() {
            addCriterion("serv_dura is null");
            return (Criteria) this;
        }

        public Criteria andServDuraIsNotNull() {
            addCriterion("serv_dura is not null");
            return (Criteria) this;
        }

        public Criteria andServDuraEqualTo(Integer value) {
            addCriterion("serv_dura =", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotEqualTo(Integer value) {
            addCriterion("serv_dura <>", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraGreaterThan(Integer value) {
            addCriterion("serv_dura >", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraGreaterThanOrEqualTo(Integer value) {
            addCriterion("serv_dura >=", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraLessThan(Integer value) {
            addCriterion("serv_dura <", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraLessThanOrEqualTo(Integer value) {
            addCriterion("serv_dura <=", value, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraIn(List<Integer> values) {
            addCriterion("serv_dura in", values, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotIn(List<Integer> values) {
            addCriterion("serv_dura not in", values, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraBetween(Integer value1, Integer value2) {
            addCriterion("serv_dura between", value1, value2, "servDura");
            return (Criteria) this;
        }

        public Criteria andServDuraNotBetween(Integer value1, Integer value2) {
            addCriterion("serv_dura not between", value1, value2, "servDura");
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

        public Criteria andRoomNoticeIsNull() {
            addCriterion("room_notice is null");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeIsNotNull() {
            addCriterion("room_notice is not null");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeEqualTo(String value) {
            addCriterion("room_notice =", value, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeNotEqualTo(String value) {
            addCriterion("room_notice <>", value, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeGreaterThan(String value) {
            addCriterion("room_notice >", value, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeGreaterThanOrEqualTo(String value) {
            addCriterion("room_notice >=", value, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeLessThan(String value) {
            addCriterion("room_notice <", value, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeLessThanOrEqualTo(String value) {
            addCriterion("room_notice <=", value, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeLike(String value) {
            addCriterion("room_notice like", value, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeNotLike(String value) {
            addCriterion("room_notice not like", value, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeIn(List<String> values) {
            addCriterion("room_notice in", values, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeNotIn(List<String> values) {
            addCriterion("room_notice not in", values, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeBetween(String value1, String value2) {
            addCriterion("room_notice between", value1, value2, "roomNotice");
            return (Criteria) this;
        }

        public Criteria andRoomNoticeNotBetween(String value1, String value2) {
            addCriterion("room_notice not between", value1, value2, "roomNotice");
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

        public Criteria andOpenTimeIsNull() {
            addCriterion("open_time is null");
            return (Criteria) this;
        }

        public Criteria andOpenTimeIsNotNull() {
            addCriterion("open_time is not null");
            return (Criteria) this;
        }

        public Criteria andOpenTimeEqualTo(Date value) {
            addCriterion("open_time =", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotEqualTo(Date value) {
            addCriterion("open_time <>", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeGreaterThan(Date value) {
            addCriterion("open_time >", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("open_time >=", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeLessThan(Date value) {
            addCriterion("open_time <", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeLessThanOrEqualTo(Date value) {
            addCriterion("open_time <=", value, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeIn(List<Date> values) {
            addCriterion("open_time in", values, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotIn(List<Date> values) {
            addCriterion("open_time not in", values, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeBetween(Date value1, Date value2) {
            addCriterion("open_time between", value1, value2, "openTime");
            return (Criteria) this;
        }

        public Criteria andOpenTimeNotBetween(Date value1, Date value2) {
            addCriterion("open_time not between", value1, value2, "openTime");
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

        public Criteria andIsExceptionCloseIsNull() {
            addCriterion("is_exception_close is null");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseIsNotNull() {
            addCriterion("is_exception_close is not null");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseEqualTo(Boolean value) {
            addCriterion("is_exception_close =", value, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseNotEqualTo(Boolean value) {
            addCriterion("is_exception_close <>", value, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseGreaterThan(Boolean value) {
            addCriterion("is_exception_close >", value, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_exception_close >=", value, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseLessThan(Boolean value) {
            addCriterion("is_exception_close <", value, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseLessThanOrEqualTo(Boolean value) {
            addCriterion("is_exception_close <=", value, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseIn(List<Boolean> values) {
            addCriterion("is_exception_close in", values, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseNotIn(List<Boolean> values) {
            addCriterion("is_exception_close not in", values, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseBetween(Boolean value1, Boolean value2) {
            addCriterion("is_exception_close between", value1, value2, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andIsExceptionCloseNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_exception_close not between", value1, value2, "isExceptionClose");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeIsNull() {
            addCriterion("exception_close_time is null");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeIsNotNull() {
            addCriterion("exception_close_time is not null");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeEqualTo(Date value) {
            addCriterion("exception_close_time =", value, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeNotEqualTo(Date value) {
            addCriterion("exception_close_time <>", value, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeGreaterThan(Date value) {
            addCriterion("exception_close_time >", value, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("exception_close_time >=", value, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeLessThan(Date value) {
            addCriterion("exception_close_time <", value, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeLessThanOrEqualTo(Date value) {
            addCriterion("exception_close_time <=", value, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeIn(List<Date> values) {
            addCriterion("exception_close_time in", values, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeNotIn(List<Date> values) {
            addCriterion("exception_close_time not in", values, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeBetween(Date value1, Date value2) {
            addCriterion("exception_close_time between", value1, value2, "exceptionCloseTime");
            return (Criteria) this;
        }

        public Criteria andExceptionCloseTimeNotBetween(Date value1, Date value2) {
            addCriterion("exception_close_time not between", value1, value2, "exceptionCloseTime");
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

        public Criteria andCanShowIsNull() {
            addCriterion("can_show is null");
            return (Criteria) this;
        }

        public Criteria andCanShowIsNotNull() {
            addCriterion("can_show is not null");
            return (Criteria) this;
        }

        public Criteria andCanShowEqualTo(Byte value) {
            addCriterion("can_show =", value, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowNotEqualTo(Byte value) {
            addCriterion("can_show <>", value, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowGreaterThan(Byte value) {
            addCriterion("can_show >", value, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowGreaterThanOrEqualTo(Byte value) {
            addCriterion("can_show >=", value, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowLessThan(Byte value) {
            addCriterion("can_show <", value, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowLessThanOrEqualTo(Byte value) {
            addCriterion("can_show <=", value, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowIn(List<Byte> values) {
            addCriterion("can_show in", values, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowNotIn(List<Byte> values) {
            addCriterion("can_show not in", values, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowBetween(Byte value1, Byte value2) {
            addCriterion("can_show between", value1, value2, "canShow");
            return (Criteria) this;
        }

        public Criteria andCanShowNotBetween(Byte value1, Byte value2) {
            addCriterion("can_show not between", value1, value2, "canShow");
            return (Criteria) this;
        }

        public Criteria andDefBackpicIsNull() {
            addCriterion("def_backpic is null");
            return (Criteria) this;
        }

        public Criteria andDefBackpicIsNotNull() {
            addCriterion("def_backpic is not null");
            return (Criteria) this;
        }

        public Criteria andDefBackpicEqualTo(String value) {
            addCriterion("def_backpic =", value, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicNotEqualTo(String value) {
            addCriterion("def_backpic <>", value, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicGreaterThan(String value) {
            addCriterion("def_backpic >", value, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicGreaterThanOrEqualTo(String value) {
            addCriterion("def_backpic >=", value, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicLessThan(String value) {
            addCriterion("def_backpic <", value, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicLessThanOrEqualTo(String value) {
            addCriterion("def_backpic <=", value, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicLike(String value) {
            addCriterion("def_backpic like", value, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicNotLike(String value) {
            addCriterion("def_backpic not like", value, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicIn(List<String> values) {
            addCriterion("def_backpic in", values, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicNotIn(List<String> values) {
            addCriterion("def_backpic not in", values, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicBetween(String value1, String value2) {
            addCriterion("def_backpic between", value1, value2, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andDefBackpicNotBetween(String value1, String value2) {
            addCriterion("def_backpic not between", value1, value2, "defBackpic");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchIsNull() {
            addCriterion("gift_effect_switch is null");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchIsNotNull() {
            addCriterion("gift_effect_switch is not null");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchEqualTo(Boolean value) {
            addCriterion("gift_effect_switch =", value, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchNotEqualTo(Boolean value) {
            addCriterion("gift_effect_switch <>", value, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchGreaterThan(Boolean value) {
            addCriterion("gift_effect_switch >", value, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchGreaterThanOrEqualTo(Boolean value) {
            addCriterion("gift_effect_switch >=", value, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchLessThan(Boolean value) {
            addCriterion("gift_effect_switch <", value, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchLessThanOrEqualTo(Boolean value) {
            addCriterion("gift_effect_switch <=", value, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchIn(List<Boolean> values) {
            addCriterion("gift_effect_switch in", values, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchNotIn(List<Boolean> values) {
            addCriterion("gift_effect_switch not in", values, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchBetween(Boolean value1, Boolean value2) {
            addCriterion("gift_effect_switch between", value1, value2, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andGiftEffectSwitchNotBetween(Boolean value1, Boolean value2) {
            addCriterion("gift_effect_switch not between", value1, value2, "giftEffectSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchIsNull() {
            addCriterion("public_chat_switch is null");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchIsNotNull() {
            addCriterion("public_chat_switch is not null");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchEqualTo(Boolean value) {
            addCriterion("public_chat_switch =", value, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchNotEqualTo(Boolean value) {
            addCriterion("public_chat_switch <>", value, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchGreaterThan(Boolean value) {
            addCriterion("public_chat_switch >", value, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchGreaterThanOrEqualTo(Boolean value) {
            addCriterion("public_chat_switch >=", value, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchLessThan(Boolean value) {
            addCriterion("public_chat_switch <", value, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchLessThanOrEqualTo(Boolean value) {
            addCriterion("public_chat_switch <=", value, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchIn(List<Boolean> values) {
            addCriterion("public_chat_switch in", values, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchNotIn(List<Boolean> values) {
            addCriterion("public_chat_switch not in", values, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchBetween(Boolean value1, Boolean value2) {
            addCriterion("public_chat_switch between", value1, value2, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andPublicChatSwitchNotBetween(Boolean value1, Boolean value2) {
            addCriterion("public_chat_switch not between", value1, value2, "publicChatSwitch");
            return (Criteria) this;
        }

        public Criteria andAudioLevelIsNull() {
            addCriterion("audio_level is null");
            return (Criteria) this;
        }

        public Criteria andAudioLevelIsNotNull() {
            addCriterion("audio_level is not null");
            return (Criteria) this;
        }

        public Criteria andAudioLevelEqualTo(Integer value) {
            addCriterion("audio_level =", value, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelNotEqualTo(Integer value) {
            addCriterion("audio_level <>", value, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelGreaterThan(Integer value) {
            addCriterion("audio_level >", value, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelGreaterThanOrEqualTo(Integer value) {
            addCriterion("audio_level >=", value, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelLessThan(Integer value) {
            addCriterion("audio_level <", value, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelLessThanOrEqualTo(Integer value) {
            addCriterion("audio_level <=", value, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelIn(List<Integer> values) {
            addCriterion("audio_level in", values, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelNotIn(List<Integer> values) {
            addCriterion("audio_level not in", values, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelBetween(Integer value1, Integer value2) {
            addCriterion("audio_level between", value1, value2, "audioLevel");
            return (Criteria) this;
        }

        public Criteria andAudioLevelNotBetween(Integer value1, Integer value2) {
            addCriterion("audio_level not between", value1, value2, "audioLevel");
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
