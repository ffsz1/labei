package com.xchat.oauth2.service.domain;

import com.xchat.oauth2.service.infrastructure.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author liuguofu
 */
public abstract class AbstractDomain implements Serializable {

    /**
     * Database id
     */
    protected Long uid;

    protected boolean archived;

    /**
     * The domain create time.
     */
    protected Date createTime = DateUtils.now();

    public AbstractDomain() {
    }

    public Long uid() {
        return uid;
    }

    public void id(Long uid) {
        this.uid = uid;
    }

    public boolean archived() {
        return archived;
    }

    public AbstractDomain archived(boolean archived) {
        this.archived = archived;
        return this;
    }

    public Date createTime() {
        return createTime;
    }

    public boolean isNewly() {
        return uid == 0l;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractDomain)) {
            return false;
        }
        AbstractDomain that = (AbstractDomain) o;
        return uid == that.uid();
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    //For subclass override it
    public void saveOrUpdate() {
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{uid=").append(uid);
        sb.append(", archived=").append(archived);
        sb.append(", createTime=").append(createTime);
        sb.append('}');
        return sb.toString();
    }
}
