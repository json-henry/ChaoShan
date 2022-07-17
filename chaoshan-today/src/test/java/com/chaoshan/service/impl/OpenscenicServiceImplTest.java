package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.mapper.OpenscenicMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-18  15:51
 * @Version: 1.0
 */
@SpringBootTest
class OpenscenicServiceImplTest {

    @Autowired
    private OpenscenicMapper mapper;

    @Test
    void test1() {
        LambdaQueryWrapper<Openscenic> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Openscenic::getId, Openscenic::getPictureLink, Openscenic::getAccessibleNumber,
                        Openscenic::getOpenTime, Openscenic::getEndTime, Openscenic::getIsOpen,
                        Openscenic::getLongitude,
                        Openscenic::getLatitude)
                .last("limit 0,10");
        List<Openscenic> openscenics = mapper.selectList(wrapper);
        for (Openscenic openscenic : openscenics) {
            System.out.println(openscenic);
        }
    }

    @Test
    void selectTest() {
        Openscenic openscenic = mapper.selectById(1);
        System.out.println(openscenic);
        System.out.println(mapper.selectOne(new LambdaQueryWrapper<Openscenic>().eq(Openscenic::getId, 1)));

    }
}