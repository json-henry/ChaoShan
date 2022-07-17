package com.chaoshan.controller;

import com.chaoshan.service.impl.OpenscenicSignupServiceImpl;
import com.chaoshan.util.api.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-21  01:15
 * @Description: 景区预约相关接口
 * @Version: 1.0
 */

@RestController
@RequestMapping("/api/scenic/signup")
public class ScenicSignupController {

    @Autowired
    private OpenscenicSignupServiceImpl signupService;

    @PostMapping("/{scenicId}")
    @ApiOperation(value = "景区报名", notes = "根据景区id进行报名，用户账号后端可以自己获取当前登录的用户")
    @ApiImplicitParam(name = "scenicId", value = "景区id", required = true, paramType = "path")
    public R scenicSignup(@PathVariable("scenicId") Long scenicId) {
        return signupService.signup(scenicId);
    }

    @DeleteMapping("/{scenicId}")
    @ApiOperation(value = "取消景区报名")
    @ApiImplicitParam(name = "scenicId", value = "景区id", required = true, paramType = "path")
    public R cancelScenic(@PathVariable Long scenicId) {
        return signupService.unSignup(scenicId);
    }


}
