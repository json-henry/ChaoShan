package com.chaoshan.util.api;

import java.io.Serializable;

/**
 * 业务代码接口
 *
 * @DATE: 2022/05/04 13:15
 * @Author: 小爽帅到拖网速
 */
public interface IResultCode extends Serializable {

    /**
     * 消息
     *
     * @return String
     */
    String getMessage();

    /**
     * 状态码
     *
     * @return int
     */
    int getCode();

}
