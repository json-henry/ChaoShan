package com.chaoshan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.User;
import com.chaoshan.service.FansService;
import com.chaoshan.service.UserService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @author 呱呱
 * @date Created in 2022/5/16 16:32
 */
@RestController
@RequestMapping("/api")
@ApiIgnore
public class FeignUserController {


    @Autowired
    private UserService userService;
    @Autowired
    private FansService fansService;

    @GetMapping("/isFoucus/{accountid}")
    @ApiOperation(value = "查看用户是否以关注,传被关注的用户账号")
    public boolean isFocus(@PathVariable("accountid") String accountid) {
//        获取当前用户账号
        String accountedid = LoginUser.getCurrentLoginUser().getAccountid();
        return fansService.checkFocus(accountedid, accountid);
    }

    /**
     * 根据用户账号返回用户信息
     *
     * @param accountid
     * @return
     */
    @GetMapping("/getInfoByAccountid/{accountid}")
    @ApiOperation(value = "返回User类型的用户信息")
    @ApiIgnore
    public User getInfoByAccountid(@PathVariable("accountid") String accountid) {
        return userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccountid, accountid));
    }

    /**
     * 根据accountid返回用户信息
     */
    @GetMapping("/getUserById/{accountid}")
    @ApiOperation(value = "根据accountid返回用户信息")
    @ApiIgnore
    public R<User> getUserById(@PathVariable String accountid) {
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccountid, accountid));
        return R.data(user);
    }

    /**
     * 获取所有用户id
     *
     * @return
     */
    @GetMapping("/getAllAccountId")
    @ApiOperation(value = "获取所有的用户id")
    public R<List<String>> getAllAccountIds() {
        return R.data(userService.getAllAccountId());
    }

    /**
     * 根据用户账号返回所有的粉丝，按时间降序
     * 根据accountId查出所有粉丝，后根据fans查出具体的User，最后返回一个List<User>
     *
     * @return
     */
    @GetMapping("/fans/getFansByAccountId")
    public R<List<User>> getAllFans() {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        return fansService.getFansInfoByDesc(accountId);
    }

}
