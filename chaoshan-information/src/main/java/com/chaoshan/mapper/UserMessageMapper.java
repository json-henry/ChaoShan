package com.chaoshan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaoshan.entity.UserMessage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 用户消息表 Mapper 接口
 * </p>
 *
 * @author Hyx
 * @since 2022-05-13
 */
public interface UserMessageMapper extends BaseMapper<UserMessage> {

    List<UserMessage> getUserMessageListByType(@Param("types") int[] types, @Param("accountId") String accountId);

}
