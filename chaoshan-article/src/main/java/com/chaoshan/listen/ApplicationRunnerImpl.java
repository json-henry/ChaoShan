package com.chaoshan.listen;

import com.chaoshan.service.search.ArticleIndexService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @DATE: 2022/05/19 21:58
 * @Author: 小爽帅到拖网速
 */
@Component
@Slf4j
public class ApplicationRunnerImpl implements ApplicationRunner {

  @Autowired
  private ArticleIndexService articleIndexService;


  @Override
  public void run(ApplicationArguments args) throws Exception {
    // 启动时判断索引是否存在
    log.info("判断是否生成当前索引");
    articleIndexService.createArticleIndex();
  }

}
