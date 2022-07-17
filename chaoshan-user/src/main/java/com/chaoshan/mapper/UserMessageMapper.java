package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.UserMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 12:50
 */
@Mapper
public interface UserMessageMapper extends BaseMapper<UserMessage> {
}