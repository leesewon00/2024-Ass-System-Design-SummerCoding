package org.landvibe.ass1.cache.manager;

public interface CacheManager {
    void put(String key, Object value);
    Object get(String key);
    void evict(String key);
}