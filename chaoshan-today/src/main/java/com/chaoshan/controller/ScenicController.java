package com.chaoshan.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.chaoshan.entity.doc.OpenscenicDoc;
import com.chaoshan.service.impl.OpenscenicServiceImpl;
import com.chaoshan.util.api.R;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-20  21:21
 * @Description: 景区相关接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/api/scenic")
public class ScenicController {

    @Autowired
    private OpenscenicServiceImpl service;

    @GetMapping("/morePage")
    @ApiOperation(value = "获取景区更多页内容", notes = "开放接口，如果经纬度有获取到，就返回离当前位置距离升序的景区内容")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "latitude", value = "纬度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = false, paramType = "query")
    })
    public R<List<OpenscenicDoc>> getScenicFromMorePage(@RequestParam(value = "latitude", required = false) String latitude,
                                                        @RequestParam(value = "longitude", required = false) String longitude) {
        List<OpenscenicDoc> openingDocs;
        if (!locationIsEmpty(latitude, longitude)) {
            openingDocs = service.getScenicListByLocation(latitude + "," + longitude);
        } else {
            openingDocs = service.getScenicList();
        }
        return R.data(openingDocs);
    }


    @GetMapping("/search")
    @ApiOperation(value = "搜索景区的内容", notes = "开放接口，如果经纬度有获取到，就返回离当前位置距离升序的景区内容")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "latitude", value = "纬度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "searchKey", value = "搜索关键字", required = false, paramType = "query")
    })
    public R<List<OpenscenicDoc>> getScenicBySearchKey(@RequestParam(value = "latitude", required = false) String latitude,
                                                       @RequestParam(value = "longitude", required = false) String longitude,
                                                       @RequestParam(value = "searchKey", required = false) String searchKey) {
        if (StrUtil.isEmpty(searchKey)) {
            return getScenicFromMorePage(latitude, longitude);
        }
        List<OpenscenicDoc> openscenicDocs;
        if (locationIsEmpty(latitude, longitude)) {
            openscenicDocs = service.getScenicListBySearchKey(searchKey);
        } else {
            String location = latitude + "," + longitude;
            openscenicDocs = service.getScenicListBySearchKey(location, searchKey);
        }
        return R.data(openscenicDocs);
    }

    @GetMapping("/getScenicById")
    @ApiOperation(value = "根据景区id获取详情", notes = "开放接口")

    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "scenicId", value = "景区id", required = true, paramType = "query"),
            @ApiImplicitParam(name = "latitude", value = "纬度", required = false, paramType = "query"),
            @ApiImplicitParam(name = "longitude", value = "经度", required = false, paramType = "query")
    })
    public R<OpenscenicDoc> getScenicById(
            @RequestParam(value = "scenicId", required = false) Long scenicId,
            @RequestParam(value = "latitude", required = false) String latitude,
            @RequestParam(value = "longitude", required = false) String longitude) {
        if (ObjectUtil.isEmpty(scenicId)) {
            return R.fail("景区id不存在");
        }
        String location = null;
        if (!locationIsEmpty(latitude, longitude)) {
            location = latitude + "," + longitude;
        }
        OpenscenicDoc scenic = service.getScenicById(scenicId, location);
        if (!ObjectUtil.isEmpty(scenic)) {
            return R.data(scenic);
        }
        return R.fail("景区不存在");

    }


    private boolean locationIsEmpty(String latitude, String longitude) {
        return StrUtil.isEmpty(longitude) || StrUtil.isEmpty(latitude);
    }

}
