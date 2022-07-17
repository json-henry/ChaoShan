package com.chaoshan.clients.fallback;

import com.chaoshan.clients.TodayClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 21:17
 */
public class TodayClientFallbackFactory implements FallbackFactory<TodayClient> {
    private static final Logger log = LoggerFactory.getLogger(InformationClientFallbackFactory.class);

    @Override
    public TodayClient create(Throwable throwable) {
        return null;
    }
}
