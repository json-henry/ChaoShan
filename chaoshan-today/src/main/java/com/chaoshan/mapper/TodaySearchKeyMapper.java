package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.TodaySearchKey;

/**
 * @author YCE
 * @description 针对表【cs_today_search_key】的数据库操作Mapper
 * @createDate 2022-05-21 22:58:33
 * @Entity com.chaoshan.entity.TodaySearchKey
 */
public interface TodaySearchKeyMapper extends BaseMapper<TodaySearchKey> {

    void updateCountByKeyword(TodaySearchKey searchKey);
}




