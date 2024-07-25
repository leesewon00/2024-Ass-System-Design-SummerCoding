package org.landvibe.ass1.cache;

import lombok.Getter;

@Getter
public enum RedisKey {
    FIND_ALL_KEY("book");

    private final String key;

    RedisKey(String key) {
        this.key = key;
    }
}

