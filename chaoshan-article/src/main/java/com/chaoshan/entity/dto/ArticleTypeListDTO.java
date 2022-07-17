package com.chaoshan.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @DATE: 2022/05/14 14:38
 * @Author: 小爽帅到拖网速
 */
@Data
@ApiModel
public class ArticleTypeListDTO {

    @ApiModelProperty(value = "文章列表", notes = "按照文章类别分类的文章列表,类型：1(饮食文化) 2(名人景点) 3(民间艺术) 4(潮玩攻略)")
    private List<ArticleTypeDTO> articles;

    @ApiModelProperty(value = "处理饮食文化，潮玩攻略的特殊类型")
    private Map<String, List<ArticleTypeDTO>> map;
}
