package com.chaoshan.util.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @DATE: 2022/05/05 23:58
 * @Author: 小爽帅到拖网速
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rpage implements Serializable {
    /**
     * 总条数
     */
    private Long total;

    /**
     * 数据List
     */
    private List<?> data;
}
