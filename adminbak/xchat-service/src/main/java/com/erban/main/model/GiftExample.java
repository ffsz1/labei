package com.erban.main.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GiftExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    protected String likeName;

    public String getLikeName() {
        return likeName;
    }

    public void setLikeName(String likeName) {
        this.likeName = likeName;
    }

    public GiftExample() {
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

        public Criteria andGiftIdIsNull() {
            addCriterion("gift_id is null");
            return (Criteria) this;
        }

        public Criteria andGiftIdIsNotNull() {
            addCriterion("gift_id is not null");
            return (Criteria) this;
        }

        public Criteria andGiftIdEqualTo(Integer value) {
            addCriterion("gift_id =", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdNotEqualTo(Integer value) {
            addCriterion("gift_id <>", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdGreaterThan(Integer value) {
            addCriterion("gift_id >", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("gift_id >=", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdLessThan(Integer value) {
            addCriterion("gift_id <", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdLessThanOrEqualTo(Integer value) {
            addCriterion("gift_id <=", value, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdIn(List<Integer> values) {
            addCriterion("gift_id in", values, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdNotIn(List<Integer> values) {
            addCriterion("gift_id not in", values, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdBetween(Integer value1, Integer value2) {
            addCriterion("gift_id between", value1, value2, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftIdNotBetween(Integer value1, Integer value2) {
            addCriterion("gift_id not between", value1, value2, "giftId");
            return (Criteria) this;
        }

        public Criteria andGiftNameIsNull() {
            addCriterion("gift_name is null");
            return (Criteria) this;
        }

        public Criteria andGiftNameIsNotNull() {
            addCriterion("gift_name is not null");
            return (Criteria) this;
        }

        public Criteria andGiftNameEqualTo(String value) {
            addCriterion("gift_name =", value, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameNotEqualTo(String value) {
            addCriterion("gift_name <>", value, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameGreaterThan(String value) {
            addCriterion("gift_name >", value, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameGreaterThanOrEqualTo(String value) {
            addCriterion("gift_name >=", value, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameLessThan(String value) {
            addCriterion("gift_name <", value, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameLessThanOrEqualTo(String value) {
            addCriterion("gift_name <=", value, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameLike(String value) {
            addCriterion("gift_name like", value, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameNotLike(String value) {
            addCriterion("gift_name not like", value, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameIn(List<String> values) {
            addCriterion("gift_name in", values, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameNotIn(List<String> values) {
            addCriterion("gift_name not in", values, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameBetween(String value1, String value2) {
            addCriterion("gift_name between", value1, value2, "giftName");
            return (Criteria) this;
        }

        public Criteria andGiftNameNotBetween(String value1, String value2) {
            addCriterion("gift_name not between", value1, value2, "giftName");
            return (Criteria) this;
        }

        public Criteria andGoldPriceIsNull() {
            addCriterion("gold_price is null");
            return (Criteria) this;
        }

        public Criteria andGoldPriceIsNotNull() {
            addCriterion("gold_price is not null");
            return (Criteria) this;
        }

        public Criteria andGoldPriceEqualTo(Long value) {
            addCriterion("gold_price =", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceNotEqualTo(Long value) {
            addCriterion("gold_price <>", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceGreaterThan(Long value) {
            addCriterion("gold_price >", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceGreaterThanOrEqualTo(Long value) {
            addCriterion("gold_price >=", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceLessThan(Long value) {
            addCriterion("gold_price <", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceLessThanOrEqualTo(Long value) {
            addCriterion("gold_price <=", value, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceIn(List<Long> values) {
            addCriterion("gold_price in", values, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceNotIn(List<Long> values) {
            addCriterion("gold_price not in", values, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceBetween(Long value1, Long value2) {
            addCriterion("gold_price between", value1, value2, "goldPrice");
            return (Criteria) this;
        }

        public Criteria andGoldPriceNotBetween(Long value1, Long value2) {
            addCriterion("gold_price not between", value1, value2, "goldPrice");
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

        public Criteria andIsNobleGiftIsNull() {
            addCriterion("is_noble_gift is null");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftIsNotNull() {
            addCriterion("is_noble_gift is not null");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftEqualTo(Boolean value) {
            addCriterion("is_noble_gift =", value, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftNotEqualTo(Boolean value) {
            addCriterion("is_noble_gift <>", value, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftGreaterThan(Boolean value) {
            addCriterion("is_noble_gift >", value, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_noble_gift >=", value, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftLessThan(Boolean value) {
            addCriterion("is_noble_gift <", value, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftLessThanOrEqualTo(Boolean value) {
            addCriterion("is_noble_gift <=", value, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftIn(List<Boolean> values) {
            addCriterion("is_noble_gift in", values, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftNotIn(List<Boolean> values) {
            addCriterion("is_noble_gift not in", values, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftBetween(Boolean value1, Boolean value2) {
            addCriterion("is_noble_gift between", value1, value2, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andIsNobleGiftNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_noble_gift not between", value1, value2, "isNobleGift");
            return (Criteria) this;
        }

        public Criteria andGiftTypeIsNull() {
            addCriterion("gift_type is null");
            return (Criteria) this;
        }

        public Criteria andGiftTypeIsNotNull() {
            addCriterion("gift_type is not null");
            return (Criteria) this;
        }

        public Criteria andGiftTypeEqualTo(Byte value) {
            addCriterion("gift_type =", value, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeNotEqualTo(Byte value) {
            addCriterion("gift_type <>", value, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeGreaterThan(Byte value) {
            addCriterion("gift_type >", value, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeGreaterThanOrEqualTo(Byte value) {
            addCriterion("gift_type >=", value, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeLessThan(Byte value) {
            addCriterion("gift_type <", value, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeLessThanOrEqualTo(Byte value) {
            addCriterion("gift_type <=", value, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeIn(List<Byte> values) {
            addCriterion("gift_type in", values, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeNotIn(List<Byte> values) {
            addCriterion("gift_type not in", values, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeBetween(Byte value1, Byte value2) {
            addCriterion("gift_type between", value1, value2, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftTypeNotBetween(Byte value1, Byte value2) {
            addCriterion("gift_type not between", value1, value2, "giftType");
            return (Criteria) this;
        }

        public Criteria andGiftStatusIsNull() {
            addCriterion("gift_status is null");
            return (Criteria) this;
        }

        public Criteria andGiftStatusIsNotNull() {
            addCriterion("gift_status is not null");
            return (Criteria) this;
        }

        public Criteria andGiftStatusEqualTo(Byte value) {
            addCriterion("gift_status =", value, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusNotEqualTo(Byte value) {
            addCriterion("gift_status <>", value, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusGreaterThan(Byte value) {
            addCriterion("gift_status >", value, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusGreaterThanOrEqualTo(Byte value) {
            addCriterion("gift_status >=", value, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusLessThan(Byte value) {
            addCriterion("gift_status <", value, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusLessThanOrEqualTo(Byte value) {
            addCriterion("gift_status <=", value, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusIn(List<Byte> values) {
            addCriterion("gift_status in", values, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusNotIn(List<Byte> values) {
            addCriterion("gift_status not in", values, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusBetween(Byte value1, Byte value2) {
            addCriterion("gift_status between", value1, value2, "giftStatus");
            return (Criteria) this;
        }

        public Criteria andGiftStatusNotBetween(Byte value1, Byte value2) {
            addCriterion("gift_status not between", value1, value2, "giftStatus");
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

        public Criteria andHasGifPicIsNull() {
            addCriterion("has_gif_pic is null");
            return (Criteria) this;
        }

        public Criteria andHasGifPicIsNotNull() {
            addCriterion("has_gif_pic is not null");
            return (Criteria) this;
        }

        public Criteria andHasGifPicEqualTo(Boolean value) {
            addCriterion("has_gif_pic =", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicNotEqualTo(Boolean value) {
            addCriterion("has_gif_pic <>", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicGreaterThan(Boolean value) {
            addCriterion("has_gif_pic >", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_gif_pic >=", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicLessThan(Boolean value) {
            addCriterion("has_gif_pic <", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicLessThanOrEqualTo(Boolean value) {
            addCriterion("has_gif_pic <=", value, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicIn(List<Boolean> values) {
            addCriterion("has_gif_pic in", values, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicNotIn(List<Boolean> values) {
            addCriterion("has_gif_pic not in", values, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicBetween(Boolean value1, Boolean value2) {
            addCriterion("has_gif_pic between", value1, value2, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andHasGifPicNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_gif_pic not between", value1, value2, "hasGifPic");
            return (Criteria) this;
        }

        public Criteria andGifUrlIsNull() {
            addCriterion("gif_url is null");
            return (Criteria) this;
        }

        public Criteria andGifUrlIsNotNull() {
            addCriterion("gif_url is not null");
            return (Criteria) this;
        }

        public Criteria andGifUrlEqualTo(String value) {
            addCriterion("gif_url =", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlNotEqualTo(String value) {
            addCriterion("gif_url <>", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlGreaterThan(String value) {
            addCriterion("gif_url >", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlGreaterThanOrEqualTo(String value) {
            addCriterion("gif_url >=", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlLessThan(String value) {
            addCriterion("gif_url <", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlLessThanOrEqualTo(String value) {
            addCriterion("gif_url <=", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlLike(String value) {
            addCriterion("gif_url like", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlNotLike(String value) {
            addCriterion("gif_url not like", value, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlIn(List<String> values) {
            addCriterion("gif_url in", values, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlNotIn(List<String> values) {
            addCriterion("gif_url not in", values, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlBetween(String value1, String value2) {
            addCriterion("gif_url between", value1, value2, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andGifUrlNotBetween(String value1, String value2) {
            addCriterion("gif_url not between", value1, value2, "gifUrl");
            return (Criteria) this;
        }

        public Criteria andHasVggPicIsNull() {
            addCriterion("has_vgg_pic is null");
            return (Criteria) this;
        }

        public Criteria andHasVggPicIsNotNull() {
            addCriterion("has_vgg_pic is not null");
            return (Criteria) this;
        }

        public Criteria andHasVggPicEqualTo(Boolean value) {
            addCriterion("has_vgg_pic =", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicNotEqualTo(Boolean value) {
            addCriterion("has_vgg_pic <>", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicGreaterThan(Boolean value) {
            addCriterion("has_vgg_pic >", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_vgg_pic >=", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicLessThan(Boolean value) {
            addCriterion("has_vgg_pic <", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicLessThanOrEqualTo(Boolean value) {
            addCriterion("has_vgg_pic <=", value, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicIn(List<Boolean> values) {
            addCriterion("has_vgg_pic in", values, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicNotIn(List<Boolean> values) {
            addCriterion("has_vgg_pic not in", values, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicBetween(Boolean value1, Boolean value2) {
            addCriterion("has_vgg_pic between", value1, value2, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andHasVggPicNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_vgg_pic not between", value1, value2, "hasVggPic");
            return (Criteria) this;
        }

        public Criteria andVggUrlIsNull() {
            addCriterion("vgg_url is null");
            return (Criteria) this;
        }

        public Criteria andVggUrlIsNotNull() {
            addCriterion("vgg_url is not null");
            return (Criteria) this;
        }

        public Criteria andVggUrlEqualTo(String value) {
            addCriterion("vgg_url =", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlNotEqualTo(String value) {
            addCriterion("vgg_url <>", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlGreaterThan(String value) {
            addCriterion("vgg_url >", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlGreaterThanOrEqualTo(String value) {
            addCriterion("vgg_url >=", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlLessThan(String value) {
            addCriterion("vgg_url <", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlLessThanOrEqualTo(String value) {
            addCriterion("vgg_url <=", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlLike(String value) {
            addCriterion("vgg_url like", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlNotLike(String value) {
            addCriterion("vgg_url not like", value, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlIn(List<String> values) {
            addCriterion("vgg_url in", values, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlNotIn(List<String> values) {
            addCriterion("vgg_url not in", values, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlBetween(String value1, String value2) {
            addCriterion("vgg_url between", value1, value2, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andVggUrlNotBetween(String value1, String value2) {
            addCriterion("vgg_url not between", value1, value2, "vggUrl");
            return (Criteria) this;
        }

        public Criteria andIsLatestIsNull() {
            addCriterion("is_latest is null");
            return (Criteria) this;
        }

        public Criteria andIsLatestIsNotNull() {
            addCriterion("is_latest is not null");
            return (Criteria) this;
        }

        public Criteria andIsLatestEqualTo(Boolean value) {
            addCriterion("is_latest =", value, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestNotEqualTo(Boolean value) {
            addCriterion("is_latest <>", value, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestGreaterThan(Boolean value) {
            addCriterion("is_latest >", value, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_latest >=", value, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestLessThan(Boolean value) {
            addCriterion("is_latest <", value, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestLessThanOrEqualTo(Boolean value) {
            addCriterion("is_latest <=", value, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestIn(List<Boolean> values) {
            addCriterion("is_latest in", values, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestNotIn(List<Boolean> values) {
            addCriterion("is_latest not in", values, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestBetween(Boolean value1, Boolean value2) {
            addCriterion("is_latest between", value1, value2, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsLatestNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_latest not between", value1, value2, "isLatest");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitIsNull() {
            addCriterion("is_time_limit is null");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitIsNotNull() {
            addCriterion("is_time_limit is not null");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitEqualTo(Boolean value) {
            addCriterion("is_time_limit =", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitNotEqualTo(Boolean value) {
            addCriterion("is_time_limit <>", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitGreaterThan(Boolean value) {
            addCriterion("is_time_limit >", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_time_limit >=", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitLessThan(Boolean value) {
            addCriterion("is_time_limit <", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitLessThanOrEqualTo(Boolean value) {
            addCriterion("is_time_limit <=", value, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitIn(List<Boolean> values) {
            addCriterion("is_time_limit in", values, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitNotIn(List<Boolean> values) {
            addCriterion("is_time_limit not in", values, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitBetween(Boolean value1, Boolean value2) {
            addCriterion("is_time_limit between", value1, value2, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andIsTimeLimitNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_time_limit not between", value1, value2, "isTimeLimit");
            return (Criteria) this;
        }

        public Criteria andHasEffectIsNull() {
            addCriterion("has_effect is null");
            return (Criteria) this;
        }

        public Criteria andHasEffectIsNotNull() {
            addCriterion("has_effect is not null");
            return (Criteria) this;
        }

        public Criteria andHasEffectEqualTo(Boolean value) {
            addCriterion("has_effect =", value, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectNotEqualTo(Boolean value) {
            addCriterion("has_effect <>", value, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectGreaterThan(Boolean value) {
            addCriterion("has_effect >", value, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectGreaterThanOrEqualTo(Boolean value) {
            addCriterion("has_effect >=", value, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectLessThan(Boolean value) {
            addCriterion("has_effect <", value, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectLessThanOrEqualTo(Boolean value) {
            addCriterion("has_effect <=", value, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectIn(List<Boolean> values) {
            addCriterion("has_effect in", values, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectNotIn(List<Boolean> values) {
            addCriterion("has_effect not in", values, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectBetween(Boolean value1, Boolean value2) {
            addCriterion("has_effect between", value1, value2, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andHasEffectNotBetween(Boolean value1, Boolean value2) {
            addCriterion("has_effect not between", value1, value2, "hasEffect");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIsNull() {
            addCriterion("start_valid_time is null");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIsNotNull() {
            addCriterion("start_valid_time is not null");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeEqualTo(Date value) {
            addCriterion("start_valid_time =", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotEqualTo(Date value) {
            addCriterion("start_valid_time <>", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeGreaterThan(Date value) {
            addCriterion("start_valid_time >", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("start_valid_time >=", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeLessThan(Date value) {
            addCriterion("start_valid_time <", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeLessThanOrEqualTo(Date value) {
            addCriterion("start_valid_time <=", value, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeIn(List<Date> values) {
            addCriterion("start_valid_time in", values, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotIn(List<Date> values) {
            addCriterion("start_valid_time not in", values, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeBetween(Date value1, Date value2) {
            addCriterion("start_valid_time between", value1, value2, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andStartValidTimeNotBetween(Date value1, Date value2) {
            addCriterion("start_valid_time not between", value1, value2, "startValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIsNull() {
            addCriterion("end_valid_time is null");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIsNotNull() {
            addCriterion("end_valid_time is not null");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeEqualTo(Date value) {
            addCriterion("end_valid_time =", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotEqualTo(Date value) {
            addCriterion("end_valid_time <>", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeGreaterThan(Date value) {
            addCriterion("end_valid_time >", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeGreaterThanOrEqualTo(Date value) {
            addCriterion("end_valid_time >=", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeLessThan(Date value) {
            addCriterion("end_valid_time <", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeLessThanOrEqualTo(Date value) {
            addCriterion("end_valid_time <=", value, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeIn(List<Date> values) {
            addCriterion("end_valid_time in", values, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotIn(List<Date> values) {
            addCriterion("end_valid_time not in", values, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeBetween(Date value1, Date value2) {
            addCriterion("end_valid_time between", value1, value2, "endValidTime");
            return (Criteria) this;
        }

        public Criteria andEndValidTimeNotBetween(Date value1, Date value2) {
            addCriterion("end_valid_time not between", value1, value2, "endValidTime");
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

        public Criteria andIsExpressGiftIsNull() {
            addCriterion("is_express_gift is null");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftIsNotNull() {
            addCriterion("is_express_gift is not null");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftEqualTo(Boolean value) {
            addCriterion("is_express_gift =", value, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftNotEqualTo(Boolean value) {
            addCriterion("is_express_gift <>", value, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftGreaterThan(Boolean value) {
            addCriterion("is_express_gift >", value, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_express_gift >=", value, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftLessThan(Boolean value) {
            addCriterion("is_express_gift <", value, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftLessThanOrEqualTo(Boolean value) {
            addCriterion("is_express_gift <=", value, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftIn(List<Boolean> values) {
            addCriterion("is_express_gift in", values, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftNotIn(List<Boolean> values) {
            addCriterion("is_express_gift not in", values, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftBetween(Boolean value1, Boolean value2) {
            addCriterion("is_express_gift between", value1, value2, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsExpressGiftNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_express_gift not between", value1, value2, "isExpressGift");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveIsNull() {
            addCriterion("is_can_give is null");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveIsNotNull() {
            addCriterion("is_can_give is not null");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveEqualTo(Boolean value) {
            addCriterion("is_can_give =", value, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveNotEqualTo(Boolean value) {
            addCriterion("is_can_give <>", value, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveGreaterThan(Boolean value) {
            addCriterion("is_can_give >", value, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveGreaterThanOrEqualTo(Boolean value) {
            addCriterion("is_can_give >=", value, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveLessThan(Boolean value) {
            addCriterion("is_can_give <", value, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveLessThanOrEqualTo(Boolean value) {
            addCriterion("is_can_give <=", value, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveIn(List<Boolean> values) {
            addCriterion("is_can_give in", values, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveNotIn(List<Boolean> values) {
            addCriterion("is_can_give not in", values, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveBetween(Boolean value1, Boolean value2) {
            addCriterion("is_can_give between", value1, value2, "isCanGive");
            return (Criteria) this;
        }

        public Criteria andIsCanGiveNotBetween(Boolean value1, Boolean value2) {
            addCriterion("is_can_give not between", value1, value2, "isCanGive");
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
