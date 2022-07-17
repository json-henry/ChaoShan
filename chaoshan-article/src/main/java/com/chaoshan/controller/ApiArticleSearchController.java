package com.chaoshan.controller;

import cn.hutool.core.util.ObjectUtil;
import com.chaoshan.service.search.ArticleIndexService;
import com.chaoshan.util.SensitiveFilter;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @DATE: 2022/05/15 15:57
 * @Author: 小爽帅到拖网速
 */
@RestController
@RequestMapping("/api")
@Api(value = "搜索模块")
public class ApiArticleSearchController {

    @Autowired
    private ArticleIndexService articleIndexService;


    @GetMapping("/articleSearch")
    @SneakyThrows
    @ApiOperation(value = "文章搜索", notes = "开放接口:根据关键字以及类型进行搜索,并将搜素结果中的关键字高亮返回")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "keyword", value = "模糊查询关键字", paramType = "query"),
            @ApiImplicitParam(name = "type", value = "不传代表首页,文章类型(1:民间艺术，2:饮食文化，3:名人景点，4:潮玩攻略)", paramType = "query")
    })
    public R<Map<String, Object>> articleSearch(@RequestParam(value = "keyword", required = false) String keyword,
                                                @RequestParam(value = "type", required = false) Integer type) {

        if (ObjectUtil.isNotEmpty(keyword)) {
            // 检测关键字是否包含敏感词汇
            if (checkKeyword(keyword)) {
                return R.fail(ResultCode.PARAM_VALID_ERROR, "包含敏感词汇，请合理输入搜索关键词");
            }
        }
        return articleIndexService.articleSearch(keyword, type);
    }

    /**
     * 检测关键字是否包含敏感词汇
     *
     * @param keyword
     * @return
     * @throws IOException
     */
    private boolean checkKeyword(String keyword) throws IOException {
        String s = SensitiveFilter.getInstance().replaceSensitiveWord(keyword, 1, "*");
        String[] texts = s.split("");
        AtomicInteger num = new AtomicInteger();
        int i = 0;
        for (String text : texts) {
            if (text.equals("*")) {
                i = num.incrementAndGet();
            }
            if (i >= 2) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/articleHotSearch")
    @ApiOperation(value = "热搜", notes = "开放接口")
    public R<List<Map<String, Object>>> articleHotSearch() {
        return articleIndexService.articleHotSearch();
    }


}
