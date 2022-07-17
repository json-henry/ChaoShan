package com.chaoshan.clients;


import com.chaoshan.clients.fallback.UserClientFallbackFactory;
import com.chaoshan.entity.Article;
import com.chaoshan.entity.User;
import com.chaoshan.util.api.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @DATE: 2022/05/12 09:54
 * @Author: 小爽帅到拖网速
 */
@FeignClient(value = "CHAOSHAN-USER", fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
    String prefix_ = "/user";


    /**
     * 判断是否已经关注本用户
     *
     * @param accountedid
     * @return
     */
    @GetMapping(prefix_ + "/api/isFoucus/{accountid}")
    @ApiOperation(value = "查看用户是否以关注")
    boolean isFocus(@PathVariable("accountid") String accountedid);

    /**
     * 根据用户账号返回用户信息
     *
     * @param accountid
     * @return
     */
    @GetMapping(prefix_ + "/api/getUserById/{accountid}")
    R<User> getUserById(@PathVariable("accountid") String accountid);

    /**
     * 返回User
     */
    @GetMapping(prefix_ + "/api/getInfoByAccountid/{accountid}")
    User getInfoByAccountid(@PathVariable("accountid") String accountid);


    /**
     * 返回所有用户的账户id
     *
     * @return
     */
    @GetMapping(prefix_ + "/api/getAllAccountId")
    R<List<String>> getAllAccountIds();

    /**
     * 根据用户账号返回所有的粉丝，按时间降序
     * 根据accountId查出所有粉丝，后根据fans查出具体的User，最后返回一个List<User>
     *
     * @return
     */
    @GetMapping(prefix_ + "/api/fans/getFansByAccountId")
    R<List<User>> getAllFans();


    /**
     * 用户发布产品
     *
     * @param file
     * @param article
     * @param tag
     * @return
     */
    @PostMapping(prefix_ + "/postProduct")
    @ApiOperation(value = "发布产品", notes = "files是用户上传多张图片的数组信息")
    R postProduct(@RequestParam(value = "file") String file, @RequestBody Article article,
                  @RequestParam("tag") String tag);


}