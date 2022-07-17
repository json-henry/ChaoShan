package com.chaoshan.clients.fallback;

import com.chaoshan.clients.InformationClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 21:16
 */
public class InformationClientFallbackFactory implements FallbackFactory<InformationClient> {
    private static final Logger log = LoggerFactory.getLogger(InformationClientFallbackFactory.class);

    @Override
    public InformationClient create(Throwable throwable) {
        return null;
    }
}

