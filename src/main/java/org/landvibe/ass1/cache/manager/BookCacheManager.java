package org.landvibe.ass1.cache.manager;

import org.landvibe.ass1.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

import static org.landvibe.ass1.cache.RedisKey.FIND_ALL_KEY;

@Component("bookCacheManager")
public class BookCacheManager implements CacheManager {

    private final RedisTemplate<String, Book> bookRedisTemplate;

    @Autowired
    public BookCacheManager(RedisTemplate<String, Book> bookRedisTemplate) {
        this.bookRedisTemplate = bookRedisTemplate;
    }

    @Override
    public void put(String key, Object object) {

        if (key.equals(FIND_ALL_KEY.getKey())) {
            for (Object o : (List) object) {
                bookRedisTemplate.opsForList().rightPush(key, (Book) o);
                bookRedisTemplate.expire(key, Duration.ofMinutes(60 * 24));
            }
            return;
        }
        bookRedisTemplate.opsForValue().set(key, (Book) object, Duration.ofSeconds(180));
    }


    @Override
    public Object get(String key) {

        if (key.equals(FIND_ALL_KEY.getKey())) {
            List<Book> bookList = bookRedisTemplate.opsForList().range(key, 0, -1);
            if (bookList.isEmpty()) {
                return null;
            }
            return bookList;
        }
        return bookRedisTemplate.opsForValue().get(key);
    }

    @Override
    public void evict(String key) {
        bookRedisTemplate.delete(key);
    }
}
