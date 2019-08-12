package com.jn.langx.util;

import com.jn.langx.IdGenerator;

import java.util.UUID;

public class UuidGenerator implements IdGenerator<Object> {
    @Override
    public String get(Object object) {
        return UUID.randomUUID().toString();
    }
}