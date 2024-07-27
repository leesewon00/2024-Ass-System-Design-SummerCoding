package org.landvibe.ass1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
public class CacheConfig {

    //https://gist.github.com/PRASANTHRAJENDRAN/cb49253d81cd393653e8223df5f33cd9
    //config, ttl 등을 map 으로 관리할 수 있다.

    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public CacheConfig(RedisConnectionFactory redisConnectionFactory) {
        this.redisConnectionFactory = redisConnectionFactory;
    }

    // basic frame
    public RedisCacheConfiguration RedisCacheConfiguration() {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        return cacheConfig;
    }

    @Bean(name = "cacheManager")
    public RedisCacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration();

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("default", cacheConfig.entryTtl(Duration.ofMinutes(1))); // TTL 1분
        cacheConfigurations.put("commonBookCache", cacheConfig.entryTtl(Duration.ofMinutes(1))); // TTL 1분
        cacheConfigurations.put("specialBookCache", cacheConfig.entryTtl(Duration.ofDays(1))); // TTL 1일

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfigurations.get("default"))
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
