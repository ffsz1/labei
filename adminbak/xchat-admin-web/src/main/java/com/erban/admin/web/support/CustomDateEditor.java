package com.erban.admin.web.support;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.util.StringUtils;

import java.beans.PropertyEditorSupport;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by PaperCut on 2018/1/20.
 */
public class CustomDateEditor extends PropertyEditorSupport{
    private final FastDateFormat dateFormat;
    private final boolean allowEmpty;

    public CustomDateEditor(String pattern, boolean allowEmpty)
    {
        this.dateFormat = FastDateFormat.getInstance(pattern);
        this.allowEmpty = allowEmpty;
    }

    public CustomDateEditor(FastDateFormat dateFormat, boolean allowEmpty) {
        this.dateFormat = dateFormat;
        this.allowEmpty = allowEmpty;
    }

    @Override
    public String getAsText() {
        Date value = (Date)this.getValue();
        return value!=null?this.dateFormat.format(value):"";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if(this.allowEmpty && !StringUtils.hasText(text))
        {
            this.setValue((Object)null);
        }
        else
        {
            try {
                this.setValue(dateFormat.parse(text));
            } catch (ParseException e) {
                throw new IllegalArgumentException("Could not parse date: " + e.getMessage(), e);
            }
        }
    }
}
