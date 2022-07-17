package com.chaoshan.clients;

import com.baomidou.mybatisplus.extension.api.R;
import com.chaoshan.clients.fallback.InformationClientFallbackFactory;
import com.chaoshan.entity.UserMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 21:15
 */
@FeignClient(value = "CHAOSHAN-INFORMATION", fallbackFactory = InformationClientFallbackFactory.class)
public interface InformationClient {

    String prefix_ = "/information/api/user-message";


    @PostMapping(prefix_ + "/add")
    R addMessage(@RequestBody UserMessage userMessage);
}
