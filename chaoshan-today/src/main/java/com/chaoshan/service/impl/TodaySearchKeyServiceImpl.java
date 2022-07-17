package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.TodaySearchKey;
import com.chaoshan.mapper.TodaySearchKeyMapper;
import com.chaoshan.service.TodaySearchKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author YCE
 * @description 针对表【cs_today_search_key】的数据库操作Service实现
 * @createDate 2022-05-21 22:58:33
 */
@Service
public class TodaySearchKeyServiceImpl extends ServiceImpl<TodaySearchKeyMapper, TodaySearchKey>
        implements TodaySearchKeyService {
    @Autowired
    TodaySearchKeyMapper mapper;

    /**
     * 根据关键字更新点击次数
     *
     * @param searchKey
     */
    @Override
    public void updateCountByKeyword(TodaySearchKey searchKey) {
        mapper.updateCountByKeyword(searchKey);
    }
}




