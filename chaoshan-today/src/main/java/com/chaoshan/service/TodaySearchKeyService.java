package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.TodaySearchKey;

/**
 * @author YCE
 * @description 针对表【cs_today_search_key】的数据库操作Service
 * @createDate 2022-05-21 22:58:33
 */
public interface TodaySearchKeyService extends IService<TodaySearchKey> {
    /**
     * 根据关键字更新点击次数
     *
     * @param searchKey
     */
    void updateCountByKeyword(TodaySearchKey searchKey);
}
