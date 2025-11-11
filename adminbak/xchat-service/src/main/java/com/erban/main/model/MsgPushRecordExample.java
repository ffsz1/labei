package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MsgPushRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public MsgPushRecordExample() {
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

        public Criteria andFromAccidIsNull() {
            addCriterion("from_accid is null");
            return (Criteria) this;
        }

        public Criteria andFromAccidIsNotNull() {
            addCriterion("from_accid is not null");
            return (Criteria) this;
        }

        public Criteria andFromAccidEqualTo(Long value) {
            addCriterion("from_accid =", value, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidNotEqualTo(Long value) {
            addCriterion("from_accid <>", value, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidGreaterThan(Long value) {
            addCriterion("from_accid >", value, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidGreaterThanOrEqualTo(Long value) {
            addCriterion("from_accid >=", value, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidLessThan(Long value) {
            addCriterion("from_accid <", value, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidLessThanOrEqualTo(Long value) {
            addCriterion("from_accid <=", value, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidIn(List<Long> values) {
            addCriterion("from_accid in", values, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidNotIn(List<Long> values) {
            addCriterion("from_accid not in", values, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidBetween(Long value1, Long value2) {
            addCriterion("from_accid between", value1, value2, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andFromAccidNotBetween(Long value1, Long value2) {
            addCriterion("from_accid not between", value1, value2, "fromAccid");
            return (Criteria) this;
        }

        public Criteria andToObjTypeIsNull() {
            addCriterion("to_obj_type is null");
            return (Criteria) this;
        }

        public Criteria andToObjTypeIsNotNull() {
            addCriterion("to_obj_type is not null");
            return (Criteria) this;
        }

        public Criteria andToObjTypeEqualTo(Byte value) {
            addCriterion("to_obj_type =", value, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeNotEqualTo(Byte value) {
            addCriterion("to_obj_type <>", value, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeGreaterThan(Byte value) {
            addCriterion("to_obj_type >", value, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("to_obj_type >=", value, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeLessThan(Byte value) {
            addCriterion("to_obj_type <", value, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeLessThanOrEqualTo(Byte value) {
            addCriterion("to_obj_type <=", value, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeIn(List<Byte> values) {
            addCriterion("to_obj_type in", values, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeNotIn(List<Byte> values) {
            addCriterion("to_obj_type not in", values, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeBetween(Byte value1, Byte value2) {
            addCriterion("to_obj_type between", value1, value2, "toObjType");
            return (Criteria) this;
        }

        public Criteria andToObjTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("to_obj_type not between", value1, value2, "toObjType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNull() {
            addCriterion("msg_type is null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIsNotNull() {
            addCriterion("msg_type is not null");
            return (Criteria) this;
        }

        public Criteria andMsgTypeEqualTo(Byte value) {
            addCriterion("msg_type =", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotEqualTo(Byte value) {
            addCriterion("msg_type <>", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThan(Byte value) {
            addCriterion("msg_type >", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("msg_type >=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThan(Byte value) {
            addCriterion("msg_type <", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeLessThanOrEqualTo(Byte value) {
            addCriterion("msg_type <=", value, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeIn(List<Byte> values) {
            addCriterion("msg_type in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotIn(List<Byte> values) {
            addCriterion("msg_type not in", values, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeBetween(Byte value1, Byte value2) {
            addCriterion("msg_type between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andMsgTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("msg_type not between", value1, value2, "msgType");
            return (Criteria) this;
        }

        public Criteria andToAccidsIsNull() {
            addCriterion("to_accids is null");
            return (Criteria) this;
        }

        public Criteria andToAccidsIsNotNull() {
            addCriterion("to_accids is not null");
            return (Criteria) this;
        }

        public Criteria andToAccidsEqualTo(String value) {
            addCriterion("to_accids =", value, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsNotEqualTo(String value) {
            addCriterion("to_accids <>", value, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsGreaterThan(String value) {
            addCriterion("to_accids >", value, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsGreaterThanOrEqualTo(String value) {
            addCriterion("to_accids >=", value, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsLessThan(String value) {
            addCriterion("to_accids <", value, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsLessThanOrEqualTo(String value) {
            addCriterion("to_accids <=", value, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsLike(String value) {
            addCriterion("to_accids like", value, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsNotLike(String value) {
            addCriterion("to_accids not like", value, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsIn(List<String> values) {
            addCriterion("to_accids in", values, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsNotIn(List<String> values) {
            addCriterion("to_accids not in", values, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsBetween(String value1, String value2) {
            addCriterion("to_accids between", value1, value2, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToAccidsNotBetween(String value1, String value2) {
            addCriterion("to_accids not between", value1, value2, "toAccids");
            return (Criteria) this;
        }

        public Criteria andToErbanNosIsNull() {
            addCriterion("to_erban_nos is null");
            return (Criteria) this;
        }

        public Criteria andToErbanNosIsNotNull() {
            addCriterion("to_erban_nos is not null");
            return (Criteria) this;
        }

        public Criteria andToErbanNosEqualTo(String value) {
            addCriterion("to_erban_nos =", value, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosNotEqualTo(String value) {
            addCriterion("to_erban_nos <>", value, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosGreaterThan(String value) {
            addCriterion("to_erban_nos >", value, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosGreaterThanOrEqualTo(String value) {
            addCriterion("to_erban_nos >=", value, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosLessThan(String value) {
            addCriterion("to_erban_nos <", value, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosLessThanOrEqualTo(String value) {
            addCriterion("to_erban_nos <=", value, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosLike(String value) {
            addCriterion("to_erban_nos like", value, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosNotLike(String value) {
            addCriterion("to_erban_nos not like", value, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosIn(List<String> values) {
            addCriterion("to_erban_nos in", values, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosNotIn(List<String> values) {
            addCriterion("to_erban_nos not in", values, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosBetween(String value1, String value2) {
            addCriterion("to_erban_nos between", value1, value2, "toErbanNos");
            return (Criteria) this;
        }

        public Criteria andToErbanNosNotBetween(String value1, String value2) {
            addCriterion("to_erban_nos not between", value1, value2, "toErbanNos");
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

        public Criteria andWebUrlIsNull() {
            addCriterion("web_url is null");
            return (Criteria) this;
        }

        public Criteria andWebUrlIsNotNull() {
            addCriterion("web_url is not null");
            return (Criteria) this;
        }

        public Criteria andWebUrlEqualTo(String value) {
            addCriterion("web_url =", value, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlNotEqualTo(String value) {
            addCriterion("web_url <>", value, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlGreaterThan(String value) {
            addCriterion("web_url >", value, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlGreaterThanOrEqualTo(String value) {
            addCriterion("web_url >=", value, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlLessThan(String value) {
            addCriterion("web_url <", value, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlLessThanOrEqualTo(String value) {
            addCriterion("web_url <=", value, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlLike(String value) {
            addCriterion("web_url like", value, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlNotLike(String value) {
            addCriterion("web_url not like", value, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlIn(List<String> values) {
            addCriterion("web_url in", values, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlNotIn(List<String> values) {
            addCriterion("web_url not in", values, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlBetween(String value1, String value2) {
            addCriterion("web_url between", value1, value2, "webUrl");
            return (Criteria) this;
        }

        public Criteria andWebUrlNotBetween(String value1, String value2) {
            addCriterion("web_url not between", value1, value2, "webUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNull() {
            addCriterion("pic_url is null");
            return (Criteria) this;
        }

        public Criteria andPicUrlIsNotNull() {
            addCriterion("pic_url is not null");
            return (Criteria) this;
        }

        public Criteria andPicUrlEqualTo(String value) {
            addCriterion("pic_url =", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotEqualTo(String value) {
            addCriterion("pic_url <>", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThan(String value) {
            addCriterion("pic_url >", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlGreaterThanOrEqualTo(String value) {
            addCriterion("pic_url >=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThan(String value) {
            addCriterion("pic_url <", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLessThanOrEqualTo(String value) {
            addCriterion("pic_url <=", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlLike(String value) {
            addCriterion("pic_url like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotLike(String value) {
            addCriterion("pic_url not like", value, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlIn(List<String> values) {
            addCriterion("pic_url in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotIn(List<String> values) {
            addCriterion("pic_url not in", values, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlBetween(String value1, String value2) {
            addCriterion("pic_url between", value1, value2, "picUrl");
            return (Criteria) this;
        }

        public Criteria andPicUrlNotBetween(String value1, String value2) {
            addCriterion("pic_url not between", value1, value2, "picUrl");
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

        public Criteria andMsgDescIsNull() {
            addCriterion("msg_desc is null");
            return (Criteria) this;
        }

        public Criteria andMsgDescIsNotNull() {
            addCriterion("msg_desc is not null");
            return (Criteria) this;
        }

        public Criteria andMsgDescEqualTo(String value) {
            addCriterion("msg_desc =", value, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescNotEqualTo(String value) {
            addCriterion("msg_desc <>", value, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescGreaterThan(String value) {
            addCriterion("msg_desc >", value, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescGreaterThanOrEqualTo(String value) {
            addCriterion("msg_desc >=", value, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescLessThan(String value) {
            addCriterion("msg_desc <", value, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescLessThanOrEqualTo(String value) {
            addCriterion("msg_desc <=", value, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescLike(String value) {
            addCriterion("msg_desc like", value, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescNotLike(String value) {
            addCriterion("msg_desc not like", value, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescIn(List<String> values) {
            addCriterion("msg_desc in", values, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescNotIn(List<String> values) {
            addCriterion("msg_desc not in", values, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescBetween(String value1, String value2) {
            addCriterion("msg_desc between", value1, value2, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andMsgDescNotBetween(String value1, String value2) {
            addCriterion("msg_desc not between", value1, value2, "msgDesc");
            return (Criteria) this;
        }

        public Criteria andAdminIdIsNull() {
            addCriterion("admin_id is null");
            return (Criteria) this;
        }

        public Criteria andAdminIdIsNotNull() {
            addCriterion("admin_id is not null");
            return (Criteria) this;
        }

        public Criteria andAdminIdEqualTo(String value) {
            addCriterion("admin_id =", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdNotEqualTo(String value) {
            addCriterion("admin_id <>", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdGreaterThan(String value) {
            addCriterion("admin_id >", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdGreaterThanOrEqualTo(String value) {
            addCriterion("admin_id >=", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdLessThan(String value) {
            addCriterion("admin_id <", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdLessThanOrEqualTo(String value) {
            addCriterion("admin_id <=", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdLike(String value) {
            addCriterion("admin_id like", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdNotLike(String value) {
            addCriterion("admin_id not like", value, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdIn(List<String> values) {
            addCriterion("admin_id in", values, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdNotIn(List<String> values) {
            addCriterion("admin_id not in", values, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdBetween(String value1, String value2) {
            addCriterion("admin_id between", value1, value2, "adminId");
            return (Criteria) this;
        }

        public Criteria andAdminIdNotBetween(String value1, String value2) {
            addCriterion("admin_id not between", value1, value2, "adminId");
            return (Criteria) this;
        }

        public Criteria andCrateTimeIsNull() {
            addCriterion("crate_time is null");
            return (Criteria) this;
        }

        public Criteria andCrateTimeIsNotNull() {
            addCriterion("crate_time is not null");
            return (Criteria) this;
        }

        public Criteria andCrateTimeEqualTo(Date value) {
            addCriterion("crate_time =", value, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeNotEqualTo(Date value) {
            addCriterion("crate_time <>", value, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeGreaterThan(Date value) {
            addCriterion("crate_time >", value, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("crate_time >=", value, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeLessThan(Date value) {
            addCriterion("crate_time <", value, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeLessThanOrEqualTo(Date value) {
            addCriterion("crate_time <=", value, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeIn(List<Date> values) {
            addCriterion("crate_time in", values, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeNotIn(List<Date> values) {
            addCriterion("crate_time not in", values, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeBetween(Date value1, Date value2) {
            addCriterion("crate_time between", value1, value2, "crateTime");
            return (Criteria) this;
        }

        public Criteria andCrateTimeNotBetween(Date value1, Date value2) {
            addCriterion("crate_time not between", value1, value2, "crateTime");
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
