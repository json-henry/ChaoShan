package com.chaoshan.controller.system;

import cn.hutool.core.util.ObjectUtil;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.service.impl.OpenscenicServiceImpl;
import com.chaoshan.util.api.R;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import static com.chaoshan.constant.RabbitMQConstant.*;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-20  21:21
 * @Description: 后台操作景区相关接口
 * @Version: 1.0
 */
@RestController
@RequestMapping("/system/scenic")
public class ScenicSystemController {

    @Autowired
    private OpenscenicServiceImpl service;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/add")
    @ApiOperation("管理员新增景区")
    public R addScenic(@RequestBody Openscenic scenic) {
        if (ObjectUtil.isEmpty(scenic)) {
            return R.fail("新增失败，景区内容不能为空");
        }
        if (service.save(scenic)) {
            rabbitTemplate.convertAndSend(SCENIC_EXCHANGE, SCENIC_INSERT_KEY, scenic.getId());
            return R.success("新增成功");
        }
        return R.fail("新增失败");
    }

    @DeleteMapping("/{openScenicId}")
    @ApiOperation("管理员删除景区")
    public R deleteScenic(@PathVariable Long openScenicId) {
        if (ObjectUtils.isEmpty(openScenicId)) {
            return R.fail("删除失败");
        }
        if (service.removeById(openScenicId)) {
            rabbitTemplate.convertAndSend(SCENIC_EXCHANGE, SCENIC_DELETE_KEY, openScenicId);
            return R.success("删除成功");
        }
        return R.fail("删除失败");
    }
}
