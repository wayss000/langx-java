package com.jn.langx.test.util.enums;

import com.jn.langx.Delegatable;
import com.jn.langx.util.enums.base.CommonEnum;
import com.jn.langx.util.enums.base.EnumDelegate;

public enum Period implements Delegatable<EnumDelegate>, CommonEnum {
    MINUTES(0, "minutes", "minutes"),
    HOURS(1, "hours", "hours"),
    DAY(2, "day", "day"),
    MONTH(3, "month", "month");

    public static final long serialVersionUID = 1L;

    private EnumDelegate delegate;

    Period(int code, String name, String displayText) {
        setDelegate(new EnumDelegate(code, name, displayText));
    }

    public int getCode() {
        return delegate.getCode();
    }

    @Override
    public void setCode(int code) {
        delegate.setCode(code);
    }

    @Override
    public void setName(String name) {
        delegate.setName(name);
    }

    public String getName() {
        return delegate.getName();
    }

    public String getDisplayText() {
        return delegate.getDisplayText();
    }

    @Override
    public void setDisplayText(String displayText) {
        delegate.setDisplayText(displayText);
    }


    public EnumDelegate getDelegate() {
        return delegate;
    }

    public void setDelegate(EnumDelegate delegate) {
        this.delegate = delegate;
    }
}