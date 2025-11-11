package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BannerExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public BannerExample() {
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

        public Criteria andBannerIdIsNull() {
            addCriterion("banner_id is null");
            return (Criteria) this;
        }

        public Criteria andBannerIdIsNotNull() {
            addCriterion("banner_id is not null");
            return (Criteria) this;
        }

        public Criteria andBannerIdEqualTo(Integer value) {
            addCriterion("banner_id =", value, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdNotEqualTo(Integer value) {
            addCriterion("banner_id <>", value, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdGreaterThan(Integer value) {
            addCriterion("banner_id >", value, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("banner_id >=", value, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdLessThan(Integer value) {
            addCriterion("banner_id <", value, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdLessThanOrEqualTo(Integer value) {
            addCriterion("banner_id <=", value, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdIn(List<Integer> values) {
            addCriterion("banner_id in", values, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdNotIn(List<Integer> values) {
            addCriterion("banner_id not in", values, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdBetween(Integer value1, Integer value2) {
            addCriterion("banner_id between", value1, value2, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerIdNotBetween(Integer value1, Integer value2) {
            addCriterion("banner_id not between", value1, value2, "bannerId");
            return (Criteria) this;
        }

        public Criteria andBannerNameIsNull() {
            addCriterion("banner_name is null");
            return (Criteria) this;
        }

        public Criteria andBannerNameIsNotNull() {
            addCriterion("banner_name is not null");
            return (Criteria) this;
        }

        public Criteria andBannerNameEqualTo(String value) {
            addCriterion("banner_name =", value, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameNotEqualTo(String value) {
            addCriterion("banner_name <>", value, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameGreaterThan(String value) {
            addCriterion("banner_name >", value, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameGreaterThanOrEqualTo(String value) {
            addCriterion("banner_name >=", value, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameLessThan(String value) {
            addCriterion("banner_name <", value, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameLessThanOrEqualTo(String value) {
            addCriterion("banner_name <=", value, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameLike(String value) {
            addCriterion("banner_name like", value, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameNotLike(String value) {
            addCriterion("banner_name not like", value, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameIn(List<String> values) {
            addCriterion("banner_name in", values, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameNotIn(List<String> values) {
            addCriterion("banner_name not in", values, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameBetween(String value1, String value2) {
            addCriterion("banner_name between", value1, value2, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerNameNotBetween(String value1, String value2) {
            addCriterion("banner_name not between", value1, value2, "bannerName");
            return (Criteria) this;
        }

        public Criteria andBannerPicIsNull() {
            addCriterion("banner_pic is null");
            return (Criteria) this;
        }

        public Criteria andBannerPicIsNotNull() {
            addCriterion("banner_pic is not null");
            return (Criteria) this;
        }

        public Criteria andBannerPicEqualTo(String value) {
            addCriterion("banner_pic =", value, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicNotEqualTo(String value) {
            addCriterion("banner_pic <>", value, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicGreaterThan(String value) {
            addCriterion("banner_pic >", value, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicGreaterThanOrEqualTo(String value) {
            addCriterion("banner_pic >=", value, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicLessThan(String value) {
            addCriterion("banner_pic <", value, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicLessThanOrEqualTo(String value) {
            addCriterion("banner_pic <=", value, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicLike(String value) {
            addCriterion("banner_pic like", value, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicNotLike(String value) {
            addCriterion("banner_pic not like", value, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicIn(List<String> values) {
            addCriterion("banner_pic in", values, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicNotIn(List<String> values) {
            addCriterion("banner_pic not in", values, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicBetween(String value1, String value2) {
            addCriterion("banner_pic between", value1, value2, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andBannerPicNotBetween(String value1, String value2) {
            addCriterion("banner_pic not between", value1, value2, "bannerPic");
            return (Criteria) this;
        }

        public Criteria andSkipTypeIsNull() {
            addCriterion("skip_type is null");
            return (Criteria) this;
        }

        public Criteria andSkipTypeIsNotNull() {
            addCriterion("skip_type is not null");
            return (Criteria) this;
        }

        public Criteria andSkipTypeEqualTo(Byte value) {
            addCriterion("skip_type =", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeNotEqualTo(Byte value) {
            addCriterion("skip_type <>", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeGreaterThan(Byte value) {
            addCriterion("skip_type >", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("skip_type >=", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeLessThan(Byte value) {
            addCriterion("skip_type <", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeLessThanOrEqualTo(Byte value) {
            addCriterion("skip_type <=", value, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeIn(List<Byte> values) {
            addCriterion("skip_type in", values, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeNotIn(List<Byte> values) {
            addCriterion("skip_type not in", values, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeBetween(Byte value1, Byte value2) {
            addCriterion("skip_type between", value1, value2, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("skip_type not between", value1, value2, "skipType");
            return (Criteria) this;
        }

        public Criteria andSkipUriIsNull() {
            addCriterion("skip_uri is null");
            return (Criteria) this;
        }

        public Criteria andSkipUriIsNotNull() {
            addCriterion("skip_uri is not null");
            return (Criteria) this;
        }

        public Criteria andSkipUriEqualTo(String value) {
            addCriterion("skip_uri =", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriNotEqualTo(String value) {
            addCriterion("skip_uri <>", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriGreaterThan(String value) {
            addCriterion("skip_uri >", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriGreaterThanOrEqualTo(String value) {
            addCriterion("skip_uri >=", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriLessThan(String value) {
            addCriterion("skip_uri <", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriLessThanOrEqualTo(String value) {
            addCriterion("skip_uri <=", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriLike(String value) {
            addCriterion("skip_uri like", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriNotLike(String value) {
            addCriterion("skip_uri not like", value, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriIn(List<String> values) {
            addCriterion("skip_uri in", values, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriNotIn(List<String> values) {
            addCriterion("skip_uri not in", values, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriBetween(String value1, String value2) {
            addCriterion("skip_uri between", value1, value2, "skipUri");
            return (Criteria) this;
        }

        public Criteria andSkipUriNotBetween(String value1, String value2) {
            addCriterion("skip_uri not between", value1, value2, "skipUri");
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

        public Criteria andOsTypeIsNull() {
            addCriterion("os_type is null");
            return (Criteria) this;
        }

        public Criteria andOsTypeIsNotNull() {
            addCriterion("os_type is not null");
            return (Criteria) this;
        }

        public Criteria andOsTypeEqualTo(Byte value) {
            addCriterion("os_type =", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotEqualTo(Byte value) {
            addCriterion("os_type <>", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeGreaterThan(Byte value) {
            addCriterion("os_type >", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("os_type >=", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeLessThan(Byte value) {
            addCriterion("os_type <", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeLessThanOrEqualTo(Byte value) {
            addCriterion("os_type <=", value, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeIn(List<Byte> values) {
            addCriterion("os_type in", values, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotIn(List<Byte> values) {
            addCriterion("os_type not in", values, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeBetween(Byte value1, Byte value2) {
            addCriterion("os_type between", value1, value2, "osType");
            return (Criteria) this;
        }

        public Criteria andOsTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("os_type not between", value1, value2, "osType");
            return (Criteria) this;
        }

        public Criteria andIsNewUserIsNull() {
            addCriterion("is_new_user is null");
            return (Criteria) this;
        }

        public Criteria andIsNewUserIsNotNull() {
            addCriterion("is_new_user is not null");
            return (Criteria) this;
        }

        public Criteria andIsNewUserEqualTo(Byte value) {
            addCriterion("is_new_user =", value, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserNotEqualTo(Byte value) {
            addCriterion("is_new_user <>", value, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserGreaterThan(Byte value) {
            addCriterion("is_new_user >", value, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserGreaterThanOrEqualTo(Byte value) {
            addCriterion("is_new_user >=", value, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserLessThan(Byte value) {
            addCriterion("is_new_user <", value, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserLessThanOrEqualTo(Byte value) {
            addCriterion("is_new_user <=", value, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserIn(List<Byte> values) {
            addCriterion("is_new_user in", values, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserNotIn(List<Byte> values) {
            addCriterion("is_new_user not in", values, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserBetween(Byte value1, Byte value2) {
            addCriterion("is_new_user between", value1, value2, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andIsNewUserNotBetween(Byte value1, Byte value2) {
            addCriterion("is_new_user not between", value1, value2, "isNewUser");
            return (Criteria) this;
        }

        public Criteria andAppTypeIsNull() {
            addCriterion("app_type is null");
            return (Criteria) this;
        }

        public Criteria andAppTypeIsNotNull() {
            addCriterion("app_type is not null");
            return (Criteria) this;
        }

        public Criteria andAppTypeEqualTo(Byte value) {
            addCriterion("app_type =", value, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeNotEqualTo(Byte value) {
            addCriterion("app_type <>", value, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeGreaterThan(Byte value) {
            addCriterion("app_type >", value, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("app_type >=", value, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeLessThan(Byte value) {
            addCriterion("app_type <", value, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeLessThanOrEqualTo(Byte value) {
            addCriterion("app_type <=", value, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeIn(List<Byte> values) {
            addCriterion("app_type in", values, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeNotIn(List<Byte> values) {
            addCriterion("app_type not in", values, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeBetween(Byte value1, Byte value2) {
            addCriterion("app_type between", value1, value2, "appType");
            return (Criteria) this;
        }

        public Criteria andAppTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("app_type not between", value1, value2, "appType");
            return (Criteria) this;
        }

        public Criteria andBannerStatusIsNull() {
            addCriterion("banner_status is null");
            return (Criteria) this;
        }

        public Criteria andBannerStatusIsNotNull() {
            addCriterion("banner_status is not null");
            return (Criteria) this;
        }

        public Criteria andBannerStatusEqualTo(Byte value) {
            addCriterion("banner_status =", value, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusNotEqualTo(Byte value) {
            addCriterion("banner_status <>", value, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusGreaterThan(Byte value) {
            addCriterion("banner_status >", value, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("banner_status >=", value, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusLessThan(Byte value) {
            addCriterion("banner_status <", value, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusLessThanOrEqualTo(Byte value) {
            addCriterion("banner_status <=", value, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusIn(List<Byte> values) {
            addCriterion("banner_status in", values, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusNotIn(List<Byte> values) {
            addCriterion("banner_status not in", values, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusBetween(Byte value1, Byte value2) {
            addCriterion("banner_status between", value1, value2, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andBannerStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("banner_status not between", value1, value2, "bannerStatus");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNull() {
            addCriterion("start_time is null");
            return (Criteria) this;
        }

        public Criteria andStartTimeIsNotNull() {
            addCriterion("start_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartTimeEqualTo(Date value) {
            addCriterion("start_time =", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotEqualTo(Date value) {
            addCriterion("start_time <>", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThan(Date value) {
            addCriterion("start_time >", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_time >=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThan(Date value) {
            addCriterion("start_time <", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_time <=", value, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeIn(List<Date> values) {
            addCriterion("start_time in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotIn(List<Date> values) {
            addCriterion("start_time not in", values, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeBetween(Date value1, Date value2) {
            addCriterion("start_time between", value1, value2, "startTime");
            return (Criteria) this;
        }

        public Criteria andStartTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_time not between", value1, value2, "startTime");
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

        public Criteria andCreateTiemIsNull() {
            addCriterion("create_tiem is null");
            return (Criteria) this;
        }

        public Criteria andCreateTiemIsNotNull() {
            addCriterion("create_tiem is not null");
            return (Criteria) this;
        }

        public Criteria andCreateTiemEqualTo(Date value) {
            addCriterion("create_tiem =", value, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemNotEqualTo(Date value) {
            addCriterion("create_tiem <>", value, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemGreaterThan(Date value) {
            addCriterion("create_tiem >", value, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemGreaterThanOrEqualTo(Date value) {
            addCriterion("create_tiem >=", value, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemLessThan(Date value) {
            addCriterion("create_tiem <", value, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemLessThanOrEqualTo(Date value) {
            addCriterion("create_tiem <=", value, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemIn(List<Date> values) {
            addCriterion("create_tiem in", values, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemNotIn(List<Date> values) {
            addCriterion("create_tiem not in", values, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemBetween(Date value1, Date value2) {
            addCriterion("create_tiem between", value1, value2, "createTiem");
            return (Criteria) this;
        }

        public Criteria andCreateTiemNotBetween(Date value1, Date value2) {
            addCriterion("create_tiem not between", value1, value2, "createTiem");
            return (Criteria) this;
        }

        public Criteria andViewTypeIsNull() {
            addCriterion("view_type is null");
            return (Criteria) this;
        }

        public Criteria andViewTypeIsNotNull() {
            addCriterion("view_type is not null");
            return (Criteria) this;
        }

        public Criteria andViewTypeEqualTo(Byte value) {
            addCriterion("view_type =", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeNotEqualTo(Byte value) {
            addCriterion("view_type <>", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeGreaterThan(Byte value) {
            addCriterion("view_type >", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("view_type >=", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeLessThan(Byte value) {
            addCriterion("view_type <", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeLessThanOrEqualTo(Byte value) {
            addCriterion("view_type <=", value, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeIn(List<Byte> values) {
            addCriterion("view_type in", values, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeNotIn(List<Byte> values) {
            addCriterion("view_type not in", values, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeBetween(Byte value1, Byte value2) {
            addCriterion("view_type between", value1, value2, "viewType");
            return (Criteria) this;
        }

        public Criteria andViewTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("view_type not between", value1, value2, "viewType");
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
