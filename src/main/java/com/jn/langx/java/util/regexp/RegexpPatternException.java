package com.jn.langx.java.util.regexp;

public class RegexpPatternException extends RuntimeException {
    private static final long serialVersionUID = 3078609769667671693L;

    public RegexpPatternException(String pattern) {
        super("the specified RegExp is wrong, please check it: " + pattern);
    }
}