package org.landvibe.ass1.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.landvibe.ass1.cache.annotation.CacheOutLandVibe;
import org.landvibe.ass1.cache.annotation.CachingLandVibe;
import org.landvibe.ass1.cache.manager.CacheManager;
import org.landvibe.ass1.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import static org.landvibe.ass1.cache.RedisKey.FIND_ALL_KEY;


@Aspect // aspectj 어노테이션으로 어드바이저 생성
@Component
@Slf4j
public class CachingAspect {

    @Autowired
    private ApplicationContext applicationContext;


    @Around("@annotation(cachingLandvibe)") // 어드바이스 + 포인트컷 설정
    public Object cache(ProceedingJoinPoint joinPoint, CachingLandVibe cachingLandvibe) throws Throwable {

        String cacheManagerName = cachingLandvibe.cacheManager();
        CacheManager cacheManager = (CacheManager) applicationContext.getBean(cacheManagerName);

        if (cachingLandvibe.method().equals("POST") || cachingLandvibe.method().equals("PATCH")) {
            Object result = joinPoint.proceed(); // 타깃 메소드 호출

            if (result instanceof Book) {
                // list cache out
                cacheManager.evict(FIND_ALL_KEY.getKey());

                String key = "book:" + ((Book) result).getId();
                cacheManager.put(key, result);
                return result;
            }
        }
        if (cachingLandvibe.method().equals("GET")) {
            String key = generateKey(cachingLandvibe.key(), joinPoint.getArgs());

            // cache first
            Object cachedValue = cacheManager.get(key); // 적절한 타입을 잘 반환할 수 있어야 한다.
            if (cachedValue != null) {
                // cache hit case
                log.info("cache hit for key: " + key);
                return cachedValue;
            }

            // cache miss then go to mysql
            Object result = joinPoint.proceed(); // 타깃 메소드 호출
            cacheManager.put(key, result); // 적절한 타입을 잘 넣을 수 있어야 한다.
            log.info("cache miss for key: " + key + ", caching result: " + result);

            return result;
        }

        throw new RuntimeException("error in cache");
    }

    @Around("@annotation(cacheoutLandvibe)")
    public Object cacheEvict(ProceedingJoinPoint joinPoint, CacheOutLandVibe cacheoutLandvibe) throws Throwable {

        String cacheManagerName = cacheoutLandvibe.cacheManager();
        CacheManager cacheManager = (CacheManager) applicationContext.getBean(cacheManagerName);

        String key = generateKey(cacheoutLandvibe.key(), joinPoint.getArgs());
        cacheManager.evict(key);
        log.info("evict key: " + key);

        return joinPoint.proceed();
    }

    private String generateKey(String keyPattern, Object[] args) {
        for (int i = 0; i < args.length; i++) {
            keyPattern = keyPattern.replace("{" + i + "}", args[i].toString());
        }
        return keyPattern;
    }
}