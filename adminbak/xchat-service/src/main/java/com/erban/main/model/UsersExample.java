package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class UsersExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public UsersExample() {
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
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

        public Criteria andErbanNoIsNull() {
            addCriterion("erban_no is null");
            return (Criteria) this;
        }

        public Criteria andErbanNoIsNotNull() {
            addCriterion("erban_no is not null");
            return (Criteria) this;
        }

        public Criteria andErbanNoEqualTo(Long value) {
            addCriterion("erban_no =", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotEqualTo(Long value) {
            addCriterion("erban_no <>", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThan(Long value) {
            addCriterion("erban_no >", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoGreaterThanOrEqualTo(Long value) {
            addCriterion("erban_no >=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThan(Long value) {
            addCriterion("erban_no <", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoLessThanOrEqualTo(Long value) {
            addCriterion("erban_no <=", value, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoIn(List<Long> values) {
            addCriterion("erban_no in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotIn(List<Long> values) {
            addCriterion("erban_no not in", values, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoBetween(Long value1, Long value2) {
            addCriterion("erban_no between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andErbanNoNotBetween(Long value1, Long value2) {
            addCriterion("erban_no not between", value1, value2, "erbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoIsNull() {
            addCriterion("has_pretty_erban_no is null");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoIsNotNull() {
            addCriterion("has_pretty_erban_no is not null");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoEqualTo(Boolean value) {
            addCriterion("has_pretty_erban_no =", value, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoNotEqualTo(Boolean value) {
            addCriterion("has_pretty_erban_no <>", value, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoGreaterThan(Boolean value) {
            addCriterion("has_pretty_erban_no >", value, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_pretty_erban_no >=", value, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoLessThan(Boolean value) {
            addCriterion("has_pretty_erban_no <", value, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoLessThanOrEqualTo(Boolean value) {
            addCriterion("has_pretty_erban_no <=", value, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoIn(List<Boolean> values) {
            addCriterion("has_pretty_erban_no in", values, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoNotIn(List<Boolean> values) {
            addCriterion("has_pretty_erban_no not in", values, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoBetween(Boolean value1, Boolean value2) {
            addCriterion("has_pretty_erban_no between", value1, value2, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andHasPrettyErbanNoNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_pretty_erban_no not between", value1, value2, "hasPrettyErbanNo");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNull() {
            addCriterion("phone is null");
            return (Criteria) this;
        }

        public Criteria andPhoneIsNotNull() {
            addCriterion("phone is not null");
            return (Criteria) this;
        }

        public Criteria andPhoneEqualTo(String value) {
            addCriterion("phone =", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotEqualTo(String value) {
            addCriterion("phone <>", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThan(String value) {
            addCriterion("phone >", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneGreaterThanOrEqualTo(String value) {
            addCriterion("phone >=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThan(String value) {
            addCriterion("phone <", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLessThanOrEqualTo(String value) {
            addCriterion("phone <=", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneLike(String value) {
            addCriterion("phone like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotLike(String value) {
            addCriterion("phone not like", value, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneIn(List<String> values) {
            addCriterion("phone in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotIn(List<String> values) {
            addCriterion("phone not in", values, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneBetween(String value1, String value2) {
            addCriterion("phone between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andPhoneNotBetween(String value1, String value2) {
            addCriterion("phone not between", value1, value2, "phone");
            return (Criteria) this;
        }

        public Criteria andBirthIsNull() {
            addCriterion("birth is null");
            return (Criteria) this;
        }

        public Criteria andBirthIsNotNull() {
            addCriterion("birth is not null");
            return (Criteria) this;
        }

        public Criteria andBirthEqualTo(Date value) {
            addCriterionForJDBCDate("birth =", value, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthNotEqualTo(Date value) {
            addCriterionForJDBCDate("birth <>", value, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthGreaterThan(Date value) {
            addCriterionForJDBCDate("birth >", value, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("birth >=", value, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthLessThan(Date value) {
            addCriterionForJDBCDate("birth <", value, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("birth <=", value, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthIn(List<Date> values) {
            addCriterionForJDBCDate("birth in", values, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthNotIn(List<Date> values) {
            addCriterionForJDBCDate("birth not in", values, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("birth between", value1, value2, "birth");
            return (Criteria) this;
        }

        public Criteria andBirthNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("birth not between", value1, value2, "birth");
            return (Criteria) this;
        }

        public Criteria andStarIsNull() {
            addCriterion("star is null");
            return (Criteria) this;
        }

        public Criteria andStarIsNotNull() {
            addCriterion("star is not null");
            return (Criteria) this;
        }

        public Criteria andStarEqualTo(Byte value) {
            addCriterion("star =", value, "star");
            return (Criteria) this;
        }

        public Criteria andStarNotEqualTo(Byte value) {
            addCriterion("star <>", value, "star");
            return (Criteria) this;
        }

        public Criteria andStarGreaterThan(Byte value) {
            addCriterion("star >", value, "star");
            return (Criteria) this;
        }

        public Criteria andStarGreaterThanOrEqualTo(Byte value) {
            addCriterion("star >=", value, "star");
            return (Criteria) this;
        }

        public Criteria andStarLessThan(Byte value) {
            addCriterion("star <", value, "star");
            return (Criteria) this;
        }

        public Criteria andStarLessThanOrEqualTo(Byte value) {
            addCriterion("star <=", value, "star");
            return (Criteria) this;
        }

        public Criteria andStarIn(List<Byte> values) {
            addCriterion("star in", values, "star");
            return (Criteria) this;
        }

        public Criteria andStarNotIn(List<Byte> values) {
            addCriterion("star not in", values, "star");
            return (Criteria) this;
        }

        public Criteria andStarBetween(Byte value1, Byte value2) {
            addCriterion("star between", value1, value2, "star");
            return (Criteria) this;
        }

        public Criteria andStarNotBetween(Byte value1, Byte value2) {
            addCriterion("star not between", value1, value2, "star");
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

        public Criteria andEmailIsNull() {
            addCriterion("email is null");
            return (Criteria) this;
        }

        public Criteria andEmailIsNotNull() {
            addCriterion("email is not null");
            return (Criteria) this;
        }

        public Criteria andEmailEqualTo(String value) {
            addCriterion("email =", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotEqualTo(String value) {
            addCriterion("email <>", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThan(String value) {
            addCriterion("email >", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailGreaterThanOrEqualTo(String value) {
            addCriterion("email >=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThan(String value) {
            addCriterion("email <", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLessThanOrEqualTo(String value) {
            addCriterion("email <=", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailLike(String value) {
            addCriterion("email like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotLike(String value) {
            addCriterion("email not like", value, "email");
            return (Criteria) this;
        }

        public Criteria andEmailIn(List<String> values) {
            addCriterion("email in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotIn(List<String> values) {
            addCriterion("email not in", values, "email");
            return (Criteria) this;
        }

        public Criteria andEmailBetween(String value1, String value2) {
            addCriterion("email between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andEmailNotBetween(String value1, String value2) {
            addCriterion("email not between", value1, value2, "email");
            return (Criteria) this;
        }

        public Criteria andSigntureIsNull() {
            addCriterion("signture is null");
            return (Criteria) this;
        }

        public Criteria andSigntureIsNotNull() {
            addCriterion("signture is not null");
            return (Criteria) this;
        }

        public Criteria andSigntureEqualTo(String value) {
            addCriterion("signture =", value, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureNotEqualTo(String value) {
            addCriterion("signture <>", value, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureGreaterThan(String value) {
            addCriterion("signture >", value, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureGreaterThanOrEqualTo(String value) {
            addCriterion("signture >=", value, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureLessThan(String value) {
            addCriterion("signture <", value, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureLessThanOrEqualTo(String value) {
            addCriterion("signture <=", value, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureLike(String value) {
            addCriterion("signture like", value, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureNotLike(String value) {
            addCriterion("signture not like", value, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureIn(List<String> values) {
            addCriterion("signture in", values, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureNotIn(List<String> values) {
            addCriterion("signture not in", values, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureBetween(String value1, String value2) {
            addCriterion("signture between", value1, value2, "signture");
            return (Criteria) this;
        }

        public Criteria andSigntureNotBetween(String value1, String value2) {
            addCriterion("signture not between", value1, value2, "signture");
            return (Criteria) this;
        }

        public Criteria andUserVoiceIsNull() {
            addCriterion("user_voice is null");
            return (Criteria) this;
        }

        public Criteria andUserVoiceIsNotNull() {
            addCriterion("user_voice is not null");
            return (Criteria) this;
        }

        public Criteria andUserVoiceEqualTo(String value) {
            addCriterion("user_voice =", value, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceNotEqualTo(String value) {
            addCriterion("user_voice <>", value, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceGreaterThan(String value) {
            addCriterion("user_voice >", value, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceGreaterThanOrEqualTo(String value) {
            addCriterion("user_voice >=", value, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceLessThan(String value) {
            addCriterion("user_voice <", value, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceLessThanOrEqualTo(String value) {
            addCriterion("user_voice <=", value, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceLike(String value) {
            addCriterion("user_voice like", value, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceNotLike(String value) {
            addCriterion("user_voice not like", value, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceIn(List<String> values) {
            addCriterion("user_voice in", values, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceNotIn(List<String> values) {
            addCriterion("user_voice not in", values, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceBetween(String value1, String value2) {
            addCriterion("user_voice between", value1, value2, "userVoice");
            return (Criteria) this;
        }

        public Criteria andUserVoiceNotBetween(String value1, String value2) {
            addCriterion("user_voice not between", value1, value2, "userVoice");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraIsNull() {
            addCriterion("voice_dura is null");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraIsNotNull() {
            addCriterion("voice_dura is not null");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraEqualTo(Integer value) {
            addCriterion("voice_dura =", value, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraNotEqualTo(Integer value) {
            addCriterion("voice_dura <>", value, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraGreaterThan(Integer value) {
            addCriterion("voice_dura >", value, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraGreaterThanOrEqualTo(Integer value) {
            addCriterion("voice_dura >=", value, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraLessThan(Integer value) {
            addCriterion("voice_dura <", value, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraLessThanOrEqualTo(Integer value) {
            addCriterion("voice_dura <=", value, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraIn(List<Integer> values) {
            addCriterion("voice_dura in", values, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraNotIn(List<Integer> values) {
            addCriterion("voice_dura not in", values, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraBetween(Integer value1, Integer value2) {
            addCriterion("voice_dura between", value1, value2, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andVoiceDuraNotBetween(Integer value1, Integer value2) {
            addCriterion("voice_dura not between", value1, value2, "voiceDura");
            return (Criteria) this;
        }

        public Criteria andFollowNumIsNull() {
            addCriterion("follow_num is null");
            return (Criteria) this;
        }

        public Criteria andFollowNumIsNotNull() {
            addCriterion("follow_num is not null");
            return (Criteria) this;
        }

        public Criteria andFollowNumEqualTo(Integer value) {
            addCriterion("follow_num =", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumNotEqualTo(Integer value) {
            addCriterion("follow_num <>", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumGreaterThan(Integer value) {
            addCriterion("follow_num >", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("follow_num >=", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumLessThan(Integer value) {
            addCriterion("follow_num <", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumLessThanOrEqualTo(Integer value) {
            addCriterion("follow_num <=", value, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumIn(List<Integer> values) {
            addCriterion("follow_num in", values, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumNotIn(List<Integer> values) {
            addCriterion("follow_num not in", values, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumBetween(Integer value1, Integer value2) {
            addCriterion("follow_num between", value1, value2, "followNum");
            return (Criteria) this;
        }

        public Criteria andFollowNumNotBetween(Integer value1, Integer value2) {
            addCriterion("follow_num not between", value1, value2, "followNum");
            return (Criteria) this;
        }

        public Criteria andFansNumIsNull() {
            addCriterion("fans_num is null");
            return (Criteria) this;
        }

        public Criteria andFansNumIsNotNull() {
            addCriterion("fans_num is not null");
            return (Criteria) this;
        }

        public Criteria andFansNumEqualTo(Integer value) {
            addCriterion("fans_num =", value, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumNotEqualTo(Integer value) {
            addCriterion("fans_num <>", value, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumGreaterThan(Integer value) {
            addCriterion("fans_num >", value, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumGreaterThanOrEqualTo(Integer value) {
            addCriterion("fans_num >=", value, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumLessThan(Integer value) {
            addCriterion("fans_num <", value, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumLessThanOrEqualTo(Integer value) {
            addCriterion("fans_num <=", value, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumIn(List<Integer> values) {
            addCriterion("fans_num in", values, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumNotIn(List<Integer> values) {
            addCriterion("fans_num not in", values, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumBetween(Integer value1, Integer value2) {
            addCriterion("fans_num between", value1, value2, "fansNum");
            return (Criteria) this;
        }

        public Criteria andFansNumNotBetween(Integer value1, Integer value2) {
            addCriterion("fans_num not between", value1, value2, "fansNum");
            return (Criteria) this;
        }

        public Criteria andDefUserIsNull() {
            addCriterion("def_user is null");
            return (Criteria) this;
        }

        public Criteria andDefUserIsNotNull() {
            addCriterion("def_user is not null");
            return (Criteria) this;
        }

        public Criteria andDefUserEqualTo(Byte value) {
            addCriterion("def_user =", value, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserNotEqualTo(Byte value) {
            addCriterion("def_user <>", value, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserGreaterThan(Byte value) {
            addCriterion("def_user >", value, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserGreaterThanOrEqualTo(Byte value) {
            addCriterion("def_user >=", value, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserLessThan(Byte value) {
            addCriterion("def_user <", value, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserLessThanOrEqualTo(Byte value) {
            addCriterion("def_user <=", value, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserIn(List<Byte> values) {
            addCriterion("def_user in", values, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserNotIn(List<Byte> values) {
            addCriterion("def_user not in", values, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserBetween(Byte value1, Byte value2) {
            addCriterion("def_user between", value1, value2, "defUser");
            return (Criteria) this;
        }

        public Criteria andDefUserNotBetween(Byte value1, Byte value2) {
            addCriterion("def_user not between", value1, value2, "defUser");
            return (Criteria) this;
        }

        public Criteria andFortuneIsNull() {
            addCriterion("fortune is null");
            return (Criteria) this;
        }

        public Criteria andFortuneIsNotNull() {
            addCriterion("fortune is not null");
            return (Criteria) this;
        }

        public Criteria andFortuneEqualTo(Long value) {
            addCriterion("fortune =", value, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneNotEqualTo(Long value) {
            addCriterion("fortune <>", value, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneGreaterThan(Long value) {
            addCriterion("fortune >", value, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneGreaterThanOrEqualTo(Long value) {
            addCriterion("fortune >=", value, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneLessThan(Long value) {
            addCriterion("fortune <", value, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneLessThanOrEqualTo(Long value) {
            addCriterion("fortune <=", value, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneIn(List<Long> values) {
            addCriterion("fortune in", values, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneNotIn(List<Long> values) {
            addCriterion("fortune not in", values, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneBetween(Long value1, Long value2) {
            addCriterion("fortune between", value1, value2, "fortune");
            return (Criteria) this;
        }

        public Criteria andFortuneNotBetween(Long value1, Long value2) {
            addCriterion("fortune not between", value1, value2, "fortune");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNull() {
            addCriterion("channel_type is null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIsNotNull() {
            addCriterion("channel_type is not null");
            return (Criteria) this;
        }

        public Criteria andChannelTypeEqualTo(Byte value) {
            addCriterion("channel_type =", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotEqualTo(Byte value) {
            addCriterion("channel_type <>", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThan(Byte value) {
            addCriterion("channel_type >", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("channel_type >=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThan(Byte value) {
            addCriterion("channel_type <", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeLessThanOrEqualTo(Byte value) {
            addCriterion("channel_type <=", value, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeIn(List<Byte> values) {
            addCriterion("channel_type in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotIn(List<Byte> values) {
            addCriterion("channel_type not in", values, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeBetween(Byte value1, Byte value2) {
            addCriterion("channel_type between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andChannelTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("channel_type not between", value1, value2, "channelType");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNull() {
            addCriterion("last_login_time is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIsNotNull() {
            addCriterion("last_login_time is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeEqualTo(Date value) {
            addCriterion("last_login_time =", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotEqualTo(Date value) {
            addCriterion("last_login_time <>", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThan(Date value) {
            addCriterion("last_login_time >", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("last_login_time >=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThan(Date value) {
            addCriterion("last_login_time <", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeLessThanOrEqualTo(Date value) {
            addCriterion("last_login_time <=", value, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeIn(List<Date> values) {
            addCriterion("last_login_time in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotIn(List<Date> values) {
            addCriterion("last_login_time not in", values, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeBetween(Date value1, Date value2) {
            addCriterion("last_login_time between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginTimeNotBetween(Date value1, Date value2) {
            addCriterion("last_login_time not between", value1, value2, "lastLoginTime");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIsNull() {
            addCriterion("last_login_ip is null");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIsNotNull() {
            addCriterion("last_login_ip is not null");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpEqualTo(String value) {
            addCriterion("last_login_ip =", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotEqualTo(String value) {
            addCriterion("last_login_ip <>", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpGreaterThan(String value) {
            addCriterion("last_login_ip >", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpGreaterThanOrEqualTo(String value) {
            addCriterion("last_login_ip >=", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLessThan(String value) {
            addCriterion("last_login_ip <", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLessThanOrEqualTo(String value) {
            addCriterion("last_login_ip <=", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpLike(String value) {
            addCriterion("last_login_ip like", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotLike(String value) {
            addCriterion("last_login_ip not like", value, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpIn(List<String> values) {
            addCriterion("last_login_ip in", values, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotIn(List<String> values) {
            addCriterion("last_login_ip not in", values, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpBetween(String value1, String value2) {
            addCriterion("last_login_ip between", value1, value2, "lastLoginIp");
            return (Criteria) this;
        }

        public Criteria andLastLoginIpNotBetween(String value1, String value2) {
            addCriterion("last_login_ip not between", value1, value2, "lastLoginIp");
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

        public Criteria andRegionIsNull() {
            addCriterion("region is null");
            return (Criteria) this;
        }

        public Criteria andRegionIsNotNull() {
            addCriterion("region is not null");
            return (Criteria) this;
        }

        public Criteria andRegionEqualTo(String value) {
            addCriterion("region =", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotEqualTo(String value) {
            addCriterion("region <>", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionGreaterThan(String value) {
            addCriterion("region >", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionGreaterThanOrEqualTo(String value) {
            addCriterion("region >=", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLessThan(String value) {
            addCriterion("region <", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLessThanOrEqualTo(String value) {
            addCriterion("region <=", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionLike(String value) {
            addCriterion("region like", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotLike(String value) {
            addCriterion("region not like", value, "region");
            return (Criteria) this;
        }

        public Criteria andRegionIn(List<String> values) {
            addCriterion("region in", values, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotIn(List<String> values) {
            addCriterion("region not in", values, "region");
            return (Criteria) this;
        }

        public Criteria andRegionBetween(String value1, String value2) {
            addCriterion("region between", value1, value2, "region");
            return (Criteria) this;
        }

        public Criteria andRegionNotBetween(String value1, String value2) {
            addCriterion("region not between", value1, value2, "region");
            return (Criteria) this;
        }

        public Criteria andUserDescIsNull() {
            addCriterion("user_desc is null");
            return (Criteria) this;
        }

        public Criteria andUserDescIsNotNull() {
            addCriterion("user_desc is not null");
            return (Criteria) this;
        }

        public Criteria andUserDescEqualTo(String value) {
            addCriterion("user_desc =", value, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescNotEqualTo(String value) {
            addCriterion("user_desc <>", value, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescGreaterThan(String value) {
            addCriterion("user_desc >", value, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescGreaterThanOrEqualTo(String value) {
            addCriterion("user_desc >=", value, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescLessThan(String value) {
            addCriterion("user_desc <", value, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescLessThanOrEqualTo(String value) {
            addCriterion("user_desc <=", value, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescLike(String value) {
            addCriterion("user_desc like", value, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescNotLike(String value) {
            addCriterion("user_desc not like", value, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescIn(List<String> values) {
            addCriterion("user_desc in", values, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescNotIn(List<String> values) {
            addCriterion("user_desc not in", values, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescBetween(String value1, String value2) {
            addCriterion("user_desc between", value1, value2, "userDesc");
            return (Criteria) this;
        }

        public Criteria andUserDescNotBetween(String value1, String value2) {
            addCriterion("user_desc not between", value1, value2, "userDesc");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountIsNull() {
            addCriterion("alipay_account is null");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountIsNotNull() {
            addCriterion("alipay_account is not null");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountEqualTo(String value) {
            addCriterion("alipay_account =", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNotEqualTo(String value) {
            addCriterion("alipay_account <>", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountGreaterThan(String value) {
            addCriterion("alipay_account >", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountGreaterThanOrEqualTo(String value) {
            addCriterion("alipay_account >=", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountLessThan(String value) {
            addCriterion("alipay_account <", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountLessThanOrEqualTo(String value) {
            addCriterion("alipay_account <=", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountLike(String value) {
            addCriterion("alipay_account like", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNotLike(String value) {
            addCriterion("alipay_account not like", value, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountIn(List<String> values) {
            addCriterion("alipay_account in", values, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNotIn(List<String> values) {
            addCriterion("alipay_account not in", values, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountBetween(String value1, String value2) {
            addCriterion("alipay_account between", value1, value2, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNotBetween(String value1, String value2) {
            addCriterion("alipay_account not between", value1, value2, "alipayAccount");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameIsNull() {
            addCriterion("alipay_account_name is null");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameIsNotNull() {
            addCriterion("alipay_account_name is not null");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameEqualTo(String value) {
            addCriterion("alipay_account_name =", value, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameNotEqualTo(String value) {
            addCriterion("alipay_account_name <>", value, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameGreaterThan(String value) {
            addCriterion("alipay_account_name >", value, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameGreaterThanOrEqualTo(String value) {
            addCriterion("alipay_account_name >=", value, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameLessThan(String value) {
            addCriterion("alipay_account_name <", value, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameLessThanOrEqualTo(String value) {
            addCriterion("alipay_account_name <=", value, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameLike(String value) {
            addCriterion("alipay_account_name like", value, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameNotLike(String value) {
            addCriterion("alipay_account_name not like", value, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameIn(List<String> values) {
            addCriterion("alipay_account_name in", values, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameNotIn(List<String> values) {
            addCriterion("alipay_account_name not in", values, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameBetween(String value1, String value2) {
            addCriterion("alipay_account_name between", value1, value2, "alipayAccountName");
            return (Criteria) this;
        }

        public Criteria andAlipayAccountNameNotBetween(String value1, String value2) {
            addCriterion("alipay_account_name not between", value1, value2, "alipayAccountName");
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

        public Criteria andWxPubFansOpenidIsNull() {
            addCriterion("wx_pub_fans_openid is null");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidIsNotNull() {
            addCriterion("wx_pub_fans_openid is not null");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidEqualTo(String value) {
            addCriterion("wx_pub_fans_openid =", value, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidNotEqualTo(String value) {
            addCriterion("wx_pub_fans_openid <>", value, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidGreaterThan(String value) {
            addCriterion("wx_pub_fans_openid >", value, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidGreaterThanOrEqualTo(String value) {
            addCriterion("wx_pub_fans_openid >=", value, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidLessThan(String value) {
            addCriterion("wx_pub_fans_openid <", value, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidLessThanOrEqualTo(String value) {
            addCriterion("wx_pub_fans_openid <=", value, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidLike(String value) {
            addCriterion("wx_pub_fans_openid like", value, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidNotLike(String value) {
            addCriterion("wx_pub_fans_openid not like", value, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidIn(List<String> values) {
            addCriterion("wx_pub_fans_openid in", values, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidNotIn(List<String> values) {
            addCriterion("wx_pub_fans_openid not in", values, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidBetween(String value1, String value2) {
            addCriterion("wx_pub_fans_openid between", value1, value2, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansOpenidNotBetween(String value1, String value2) {
            addCriterion("wx_pub_fans_openid not between", value1, value2, "wxPubFansOpenid");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderIsNull() {
            addCriterion("wx_pub_fans_gender is null");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderIsNotNull() {
            addCriterion("wx_pub_fans_gender is not null");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderEqualTo(Byte value) {
            addCriterion("wx_pub_fans_gender =", value, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderNotEqualTo(Byte value) {
            addCriterion("wx_pub_fans_gender <>", value, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderGreaterThan(Byte value) {
            addCriterion("wx_pub_fans_gender >", value, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderGreaterThanOrEqualTo(Byte value) {
            addCriterion("wx_pub_fans_gender >=", value, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderLessThan(Byte value) {
            addCriterion("wx_pub_fans_gender <", value, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderLessThanOrEqualTo(Byte value) {
            addCriterion("wx_pub_fans_gender <=", value, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderIn(List<Byte> values) {
            addCriterion("wx_pub_fans_gender in", values, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderNotIn(List<Byte> values) {
            addCriterion("wx_pub_fans_gender not in", values, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderBetween(Byte value1, Byte value2) {
            addCriterion("wx_pub_fans_gender between", value1, value2, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andWxPubFansGenderNotBetween(Byte value1, Byte value2) {
            addCriterion("wx_pub_fans_gender not between", value1, value2, "wxPubFansGender");
            return (Criteria) this;
        }

        public Criteria andRoomUidIsNull() {
            addCriterion("room_uid is null");
            return (Criteria) this;
        }

        public Criteria andRoomUidIsNotNull() {
            addCriterion("room_uid is not null");
            return (Criteria) this;
        }

        public Criteria andRoomUidEqualTo(Long value) {
            addCriterion("room_uid =", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotEqualTo(Long value) {
            addCriterion("room_uid <>", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidGreaterThan(Long value) {
            addCriterion("room_uid >", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidGreaterThanOrEqualTo(Long value) {
            addCriterion("room_uid >=", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidLessThan(Long value) {
            addCriterion("room_uid <", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidLessThanOrEqualTo(Long value) {
            addCriterion("room_uid <=", value, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidIn(List<Long> values) {
            addCriterion("room_uid in", values, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotIn(List<Long> values) {
            addCriterion("room_uid not in", values, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidBetween(Long value1, Long value2) {
            addCriterion("room_uid between", value1, value2, "roomUid");
            return (Criteria) this;
        }

        public Criteria andRoomUidNotBetween(Long value1, Long value2) {
            addCriterion("room_uid not between", value1, value2, "roomUid");
            return (Criteria) this;
        }

        public Criteria andShareUidIsNull() {
            addCriterion("share_uid is null");
            return (Criteria) this;
        }

        public Criteria andShareUidIsNotNull() {
            addCriterion("share_uid is not null");
            return (Criteria) this;
        }

        public Criteria andShareUidEqualTo(Long value) {
            addCriterion("share_uid =", value, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidNotEqualTo(Long value) {
            addCriterion("share_uid <>", value, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidGreaterThan(Long value) {
            addCriterion("share_uid >", value, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidGreaterThanOrEqualTo(Long value) {
            addCriterion("share_uid >=", value, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidLessThan(Long value) {
            addCriterion("share_uid <", value, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidLessThanOrEqualTo(Long value) {
            addCriterion("share_uid <=", value, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidIn(List<Long> values) {
            addCriterion("share_uid in", values, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidNotIn(List<Long> values) {
            addCriterion("share_uid not in", values, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidBetween(Long value1, Long value2) {
            addCriterion("share_uid between", value1, value2, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareUidNotBetween(Long value1, Long value2) {
            addCriterion("share_uid not between", value1, value2, "shareUid");
            return (Criteria) this;
        }

        public Criteria andShareChannelIsNull() {
            addCriterion("share_channel is null");
            return (Criteria) this;
        }

        public Criteria andShareChannelIsNotNull() {
            addCriterion("share_channel is not null");
            return (Criteria) this;
        }

        public Criteria andShareChannelEqualTo(Byte value) {
            addCriterion("share_channel =", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelNotEqualTo(Byte value) {
            addCriterion("share_channel <>", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelGreaterThan(Byte value) {
            addCriterion("share_channel >", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelGreaterThanOrEqualTo(Byte value) {
            addCriterion("share_channel >=", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelLessThan(Byte value) {
            addCriterion("share_channel <", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelLessThanOrEqualTo(Byte value) {
            addCriterion("share_channel <=", value, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelIn(List<Byte> values) {
            addCriterion("share_channel in", values, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelNotIn(List<Byte> values) {
            addCriterion("share_channel not in", values, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelBetween(Byte value1, Byte value2) {
            addCriterion("share_channel between", value1, value2, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andShareChannelNotBetween(Byte value1, Byte value2) {
            addCriterion("share_channel not between", value1, value2, "shareChannel");
            return (Criteria) this;
        }

        public Criteria andWxOpenidIsNull() {
            addCriterion("wx_openId is null");
            return (Criteria) this;
        }

        public Criteria andWxOpenidIsNotNull() {
            addCriterion("wx_openId is not null");
            return (Criteria) this;
        }

        public Criteria andWxOpenidEqualTo(String value) {
            addCriterion("wx_openId =", value, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidNotEqualTo(String value) {
            addCriterion("wx_openId <>", value, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidGreaterThan(String value) {
            addCriterion("wx_openId >", value, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidGreaterThanOrEqualTo(String value) {
            addCriterion("wx_openId >=", value, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidLessThan(String value) {
            addCriterion("wx_openId <", value, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidLessThanOrEqualTo(String value) {
            addCriterion("wx_openId <=", value, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidLike(String value) {
            addCriterion("wx_openId like", value, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidNotLike(String value) {
            addCriterion("wx_openId not like", value, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidIn(List<String> values) {
            addCriterion("wx_openId in", values, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidNotIn(List<String> values) {
            addCriterion("wx_openId not in", values, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidBetween(String value1, String value2) {
            addCriterion("wx_openId between", value1, value2, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andWxOpenidNotBetween(String value1, String value2) {
            addCriterion("wx_openId not between", value1, value2, "wxOpenid");
            return (Criteria) this;
        }

        public Criteria andOsIsNull() {
            addCriterion("os is null");
            return (Criteria) this;
        }

        public Criteria andOsIsNotNull() {
            addCriterion("os is not null");
            return (Criteria) this;
        }

        public Criteria andOsEqualTo(String value) {
            addCriterion("os =", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotEqualTo(String value) {
            addCriterion("os <>", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsGreaterThan(String value) {
            addCriterion("os >", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsGreaterThanOrEqualTo(String value) {
            addCriterion("os >=", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLessThan(String value) {
            addCriterion("os <", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLessThanOrEqualTo(String value) {
            addCriterion("os <=", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsLike(String value) {
            addCriterion("os like", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotLike(String value) {
            addCriterion("os not like", value, "os");
            return (Criteria) this;
        }

        public Criteria andOsIn(List<String> values) {
            addCriterion("os in", values, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotIn(List<String> values) {
            addCriterion("os not in", values, "os");
            return (Criteria) this;
        }

        public Criteria andOsBetween(String value1, String value2) {
            addCriterion("os between", value1, value2, "os");
            return (Criteria) this;
        }

        public Criteria andOsNotBetween(String value1, String value2) {
            addCriterion("os not between", value1, value2, "os");
            return (Criteria) this;
        }

        public Criteria andOsversionIsNull() {
            addCriterion("osVersion is null");
            return (Criteria) this;
        }

        public Criteria andOsversionIsNotNull() {
            addCriterion("osVersion is not null");
            return (Criteria) this;
        }

        public Criteria andOsversionEqualTo(String value) {
            addCriterion("osVersion =", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionNotEqualTo(String value) {
            addCriterion("osVersion <>", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionGreaterThan(String value) {
            addCriterion("osVersion >", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionGreaterThanOrEqualTo(String value) {
            addCriterion("osVersion >=", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionLessThan(String value) {
            addCriterion("osVersion <", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionLessThanOrEqualTo(String value) {
            addCriterion("osVersion <=", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionLike(String value) {
            addCriterion("osVersion like", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionNotLike(String value) {
            addCriterion("osVersion not like", value, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionIn(List<String> values) {
            addCriterion("osVersion in", values, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionNotIn(List<String> values) {
            addCriterion("osVersion not in", values, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionBetween(String value1, String value2) {
            addCriterion("osVersion between", value1, value2, "osversion");
            return (Criteria) this;
        }

        public Criteria andOsversionNotBetween(String value1, String value2) {
            addCriterion("osVersion not between", value1, value2, "osversion");
            return (Criteria) this;
        }

        public Criteria andAppIsNull() {
            addCriterion("app is null");
            return (Criteria) this;
        }

        public Criteria andAppIsNotNull() {
            addCriterion("app is not null");
            return (Criteria) this;
        }

        public Criteria andAppEqualTo(String value) {
            addCriterion("app =", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppNotEqualTo(String value) {
            addCriterion("app <>", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppGreaterThan(String value) {
            addCriterion("app >", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppGreaterThanOrEqualTo(String value) {
            addCriterion("app >=", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppLessThan(String value) {
            addCriterion("app <", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppLessThanOrEqualTo(String value) {
            addCriterion("app <=", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppLike(String value) {
            addCriterion("app like", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppNotLike(String value) {
            addCriterion("app not like", value, "app");
            return (Criteria) this;
        }

        public Criteria andAppIn(List<String> values) {
            addCriterion("app in", values, "app");
            return (Criteria) this;
        }

        public Criteria andAppNotIn(List<String> values) {
            addCriterion("app not in", values, "app");
            return (Criteria) this;
        }

        public Criteria andAppBetween(String value1, String value2) {
            addCriterion("app between", value1, value2, "app");
            return (Criteria) this;
        }

        public Criteria andAppNotBetween(String value1, String value2) {
            addCriterion("app not between", value1, value2, "app");
            return (Criteria) this;
        }

        public Criteria andImeiIsNull() {
            addCriterion("imei is null");
            return (Criteria) this;
        }

        public Criteria andImeiIsNotNull() {
            addCriterion("imei is not null");
            return (Criteria) this;
        }

        public Criteria andImeiEqualTo(String value) {
            addCriterion("imei =", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotEqualTo(String value) {
            addCriterion("imei <>", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiGreaterThan(String value) {
            addCriterion("imei >", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiGreaterThanOrEqualTo(String value) {
            addCriterion("imei >=", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLessThan(String value) {
            addCriterion("imei <", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLessThanOrEqualTo(String value) {
            addCriterion("imei <=", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiLike(String value) {
            addCriterion("imei like", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotLike(String value) {
            addCriterion("imei not like", value, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiIn(List<String> values) {
            addCriterion("imei in", values, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotIn(List<String> values) {
            addCriterion("imei not in", values, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiBetween(String value1, String value2) {
            addCriterion("imei between", value1, value2, "imei");
            return (Criteria) this;
        }

        public Criteria andImeiNotBetween(String value1, String value2) {
            addCriterion("imei not between", value1, value2, "imei");
            return (Criteria) this;
        }

        public Criteria andChannelIsNull() {
            addCriterion("channel is null");
            return (Criteria) this;
        }

        public Criteria andChannelIsNotNull() {
            addCriterion("channel is not null");
            return (Criteria) this;
        }

        public Criteria andChannelEqualTo(String value) {
            addCriterion("channel =", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotEqualTo(String value) {
            addCriterion("channel <>", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThan(String value) {
            addCriterion("channel >", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelGreaterThanOrEqualTo(String value) {
            addCriterion("channel >=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThan(String value) {
            addCriterion("channel <", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLessThanOrEqualTo(String value) {
            addCriterion("channel <=", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelLike(String value) {
            addCriterion("channel like", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotLike(String value) {
            addCriterion("channel not like", value, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelIn(List<String> values) {
            addCriterion("channel in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotIn(List<String> values) {
            addCriterion("channel not in", values, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelBetween(String value1, String value2) {
            addCriterion("channel between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andChannelNotBetween(String value1, String value2) {
            addCriterion("channel not between", value1, value2, "channel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelIsNull() {
            addCriterion("linkedme_channel is null");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelIsNotNull() {
            addCriterion("linkedme_channel is not null");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelEqualTo(String value) {
            addCriterion("linkedme_channel =", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelNotEqualTo(String value) {
            addCriterion("linkedme_channel <>", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelGreaterThan(String value) {
            addCriterion("linkedme_channel >", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelGreaterThanOrEqualTo(String value) {
            addCriterion("linkedme_channel >=", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelLessThan(String value) {
            addCriterion("linkedme_channel <", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelLessThanOrEqualTo(String value) {
            addCriterion("linkedme_channel <=", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelLike(String value) {
            addCriterion("linkedme_channel like", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelNotLike(String value) {
            addCriterion("linkedme_channel not like", value, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelIn(List<String> values) {
            addCriterion("linkedme_channel in", values, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelNotIn(List<String> values) {
            addCriterion("linkedme_channel not in", values, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelBetween(String value1, String value2) {
            addCriterion("linkedme_channel between", value1, value2, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andLinkedmeChannelNotBetween(String value1, String value2) {
            addCriterion("linkedme_channel not between", value1, value2, "linkedmeChannel");
            return (Criteria) this;
        }

        public Criteria andIspTypeIsNull() {
            addCriterion("isp_type is null");
            return (Criteria) this;
        }

        public Criteria andIspTypeIsNotNull() {
            addCriterion("isp_type is not null");
            return (Criteria) this;
        }

        public Criteria andIspTypeEqualTo(String value) {
            addCriterion("isp_type =", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeNotEqualTo(String value) {
            addCriterion("isp_type <>", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeGreaterThan(String value) {
            addCriterion("isp_type >", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeGreaterThanOrEqualTo(String value) {
            addCriterion("isp_type >=", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeLessThan(String value) {
            addCriterion("isp_type <", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeLessThanOrEqualTo(String value) {
            addCriterion("isp_type <=", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeLike(String value) {
            addCriterion("isp_type like", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeNotLike(String value) {
            addCriterion("isp_type not like", value, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeIn(List<String> values) {
            addCriterion("isp_type in", values, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeNotIn(List<String> values) {
            addCriterion("isp_type not in", values, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeBetween(String value1, String value2) {
            addCriterion("isp_type between", value1, value2, "ispType");
            return (Criteria) this;
        }

        public Criteria andIspTypeNotBetween(String value1, String value2) {
            addCriterion("isp_type not between", value1, value2, "ispType");
            return (Criteria) this;
        }

        public Criteria andNetTypeIsNull() {
            addCriterion("net_type is null");
            return (Criteria) this;
        }

        public Criteria andNetTypeIsNotNull() {
            addCriterion("net_type is not null");
            return (Criteria) this;
        }

        public Criteria andNetTypeEqualTo(String value) {
            addCriterion("net_type =", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeNotEqualTo(String value) {
            addCriterion("net_type <>", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeGreaterThan(String value) {
            addCriterion("net_type >", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeGreaterThanOrEqualTo(String value) {
            addCriterion("net_type >=", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeLessThan(String value) {
            addCriterion("net_type <", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeLessThanOrEqualTo(String value) {
            addCriterion("net_type <=", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeLike(String value) {
            addCriterion("net_type like", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeNotLike(String value) {
            addCriterion("net_type not like", value, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeIn(List<String> values) {
            addCriterion("net_type in", values, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeNotIn(List<String> values) {
            addCriterion("net_type not in", values, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeBetween(String value1, String value2) {
            addCriterion("net_type between", value1, value2, "netType");
            return (Criteria) this;
        }

        public Criteria andNetTypeNotBetween(String value1, String value2) {
            addCriterion("net_type not between", value1, value2, "netType");
            return (Criteria) this;
        }

        public Criteria andModelIsNull() {
            addCriterion("model is null");
            return (Criteria) this;
        }

        public Criteria andModelIsNotNull() {
            addCriterion("model is not null");
            return (Criteria) this;
        }

        public Criteria andModelEqualTo(String value) {
            addCriterion("model =", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotEqualTo(String value) {
            addCriterion("model <>", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThan(String value) {
            addCriterion("model >", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelGreaterThanOrEqualTo(String value) {
            addCriterion("model >=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThan(String value) {
            addCriterion("model <", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLessThanOrEqualTo(String value) {
            addCriterion("model <=", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelLike(String value) {
            addCriterion("model like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotLike(String value) {
            addCriterion("model not like", value, "model");
            return (Criteria) this;
        }

        public Criteria andModelIn(List<String> values) {
            addCriterion("model in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotIn(List<String> values) {
            addCriterion("model not in", values, "model");
            return (Criteria) this;
        }

        public Criteria andModelBetween(String value1, String value2) {
            addCriterion("model between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andModelNotBetween(String value1, String value2) {
            addCriterion("model not between", value1, value2, "model");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNull() {
            addCriterion("device_id is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNotNull() {
            addCriterion("device_id is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdEqualTo(String value) {
            addCriterion("device_id =", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotEqualTo(String value) {
            addCriterion("device_id <>", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThan(String value) {
            addCriterion("device_id >", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThanOrEqualTo(String value) {
            addCriterion("device_id >=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThan(String value) {
            addCriterion("device_id <", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThanOrEqualTo(String value) {
            addCriterion("device_id <=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLike(String value) {
            addCriterion("device_id like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotLike(String value) {
            addCriterion("device_id not like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIn(List<String> values) {
            addCriterion("device_id in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotIn(List<String> values) {
            addCriterion("device_id not in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdBetween(String value1, String value2) {
            addCriterion("device_id between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotBetween(String value1, String value2) {
            addCriterion("device_id not between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andAppVersionIsNull() {
            addCriterion("app_version is null");
            return (Criteria) this;
        }

        public Criteria andAppVersionIsNotNull() {
            addCriterion("app_version is not null");
            return (Criteria) this;
        }

        public Criteria andAppVersionEqualTo(String value) {
            addCriterion("app_version =", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionNotEqualTo(String value) {
            addCriterion("app_version <>", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionGreaterThan(String value) {
            addCriterion("app_version >", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionGreaterThanOrEqualTo(String value) {
            addCriterion("app_version >=", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionLessThan(String value) {
            addCriterion("app_version <", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionLessThanOrEqualTo(String value) {
            addCriterion("app_version <=", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionLike(String value) {
            addCriterion("app_version like", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionNotLike(String value) {
            addCriterion("app_version not like", value, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionIn(List<String> values) {
            addCriterion("app_version in", values, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionNotIn(List<String> values) {
            addCriterion("app_version not in", values, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionBetween(String value1, String value2) {
            addCriterion("app_version between", value1, value2, "appVersion");
            return (Criteria) this;
        }

        public Criteria andAppVersionNotBetween(String value1, String value2) {
            addCriterion("app_version not between", value1, value2, "appVersion");
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

        public Criteria andWithdrawStatusIsNull() {
            addCriterion("withdraw_status is null");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusIsNotNull() {
            addCriterion("withdraw_status is not null");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusEqualTo(Byte value) {
            addCriterion("withdraw_status =", value, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusNotEqualTo(Byte value) {
            addCriterion("withdraw_status <>", value, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusGreaterThan(Byte value) {
            addCriterion("withdraw_status >", value, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("withdraw_status >=", value, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusLessThan(Byte value) {
            addCriterion("withdraw_status <", value, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusLessThanOrEqualTo(Byte value) {
            addCriterion("withdraw_status <=", value, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusIn(List<Byte> values) {
            addCriterion("withdraw_status in", values, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusNotIn(List<Byte> values) {
            addCriterion("withdraw_status not in", values, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusBetween(Byte value1, Byte value2) {
            addCriterion("withdraw_status between", value1, value2, "withdrawStatus");
            return (Criteria) this;
        }

        public Criteria andWithdrawStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("withdraw_status not between", value1, value2, "withdrawStatus");
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
