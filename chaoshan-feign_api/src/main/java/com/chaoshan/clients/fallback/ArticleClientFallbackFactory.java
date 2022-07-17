package com.chaoshan.clients.fallback;

import com.chaoshan.clients.ArticleClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * /**
 *
 * @author 呱呱
 * @date Created in 2022/5/13 13:41
 */
public class ArticleClientFallbackFactory implements FallbackFactory<ArticleClient> {
    private static final Logger log = LoggerFactory.getLogger(ArticleClientFallbackFactory.class);

    @Override
    public ArticleClient create(Throwable throwable) {
        return null;
    }
}
