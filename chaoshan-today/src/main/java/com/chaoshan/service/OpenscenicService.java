package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.entity.doc.OpenscenicDoc;

import java.util.List;

/**
 * @author YCE
 * @description 针对表【cs_openscenic(开放景区表)】的数据库操作Service
 * @createDate 2022-05-17 17:34:59
 */
public interface OpenscenicService extends IService<Openscenic> {


    /**
     * 根据景区id获取详情
     *
     * @param scenicId
     * @return
     */
    OpenscenicDoc getScenicById(Long scenicId, String... location);

    /**
     * 获取离我最近的size个景区
     *
     * @param location
     * @param size
     * @return
     */
    List<OpenscenicDoc> getScenicListByLocationAndSize(String location, int size);

    /**
     * 获取所有景区，并按距离升序
     *
     * @return
     */
    List<OpenscenicDoc> getScenicListByLocation(String location);


    /**
     * 获取size大小的开放景区列表
     *
     * @param size
     * @return
     */
    List<OpenscenicDoc> getScenicListBySize(int size);

    /**
     * 直接获取整个景区列表
     *
     * @return
     */
    List<OpenscenicDoc> getScenicList();

    /**
     * 通过searchKey搜索匹配的景区
     *
     * @param searchKey
     * @return
     */

    List<OpenscenicDoc> getScenicListBySearchKey(String searchKey);

    /**
     * 通过searchKey搜索匹配的景区，并按距离升序
     *
     * @param location
     * @param searchKey
     * @return
     */
    List<OpenscenicDoc> getScenicListBySearchKey(String location, String searchKey);
}
