package org.landvibe.ass1.config;

import io.github.bucket4j.*;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import static java.time.Duration.ofMinutes;

@Component
@Slf4j
public class RateLimitFilter implements Filter {

    /**
     * Reference
     * https://bucket4j.com/8.9.0/toc.html#bucket4j-redis
     */

    private ProxyManager<String> proxyManager;

    @Autowired
    public RateLimitFilter(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String key = httpRequest.getRemoteAddr(); // ip 기반 처리율 제한, 키에 따라서 기반이 달라진다.
        Supplier<BucketConfiguration> bucketConfigurationSupplier = getConfigSupplier();
        Bucket bucket = proxyManager.builder().build(key, bucketConfigurationSupplier); // 버킷 생성
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1); // 이 메소드는 요청을 1개 소비 시도하고, 소비가 성공했는지 및 남아 있는 토큰 수를 반환합니다.

        if (probe.isConsumed()) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            // fail case
            log.info("수용 불가능한 처리율" + "\trequest ip : " + key + "\turi : " + httpRequest.getRequestURI() + "\tremainingToken : " + probe.getRemainingTokens());
            HttpServletResponse httpServletResponse = makeRateLimitResponse(servletResponse, probe);
        }
    }

    // 공식문서상 여기 위치가 올바름
    // 파라미터로 인자 받아서 커스텀 가능 ex) id
    public Supplier<BucketConfiguration> getConfigSupplier() {

        // 토큰 개수 상황에 맞게 custom
        return () ->
                BucketConfiguration.builder()
                        .addLimit(limit -> limit.capacity(60).refillGreedy(60, ofMinutes(1))) // 토큰 리필 전략, 필요에 따라 메소드 선택
                        .build();
    }

    private HttpServletResponse makeRateLimitResponse(ServletResponse servletResponse, ConsumptionProbe probe) throws IOException {

        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        httpResponse.setContentType("text/plain");
        httpResponse.setHeader("X-Rate-Limit-Retry-After-Seconds", "" +
                TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill()));
        httpResponse.setStatus(429);
        httpResponse.getWriter().append("Too many requests");

        return httpResponse;
    }

}
