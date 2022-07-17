package com.chaoshan.controller;

import com.chaoshan.clients.ArticleClient;
import com.chaoshan.entity.Article;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author 呱呱
 * @date Created in 2022/5/15 22:47
 */
@RestController
@ApiIgnore
public class DemoController {

    @Autowired
    private ArticleClient articleClient;

    @GetMapping("/test")
    public R testing() {
        Article article = articleClient.getArticleDetail(1).getData();
        return R.data(article);
    }

    /**
     * 测试接口
     */
    @Test
    public R test() {

        return R.data(LoginUser.getCurrentLoginUser());
    }


}
