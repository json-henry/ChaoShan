package com.chaoshan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.entity.doc.OpenscenicDoc;
import com.chaoshan.mapper.OpenscenicMapper;
import com.chaoshan.service.OpenscenicService;
import lombok.SneakyThrows;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.chaoshan.constant.ESConstant.SCENIC_INDEX;

/**
 * @author YCE
 * @description 针对表【cs_openscenic(开放景区表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:34:59
 */
@Service
public class OpenscenicServiceImpl extends ServiceImpl<OpenscenicMapper, Openscenic>
        implements OpenscenicService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    @SneakyThrows
    public OpenscenicDoc getScenicById(Long scenicId, String... location) {
        SearchRequest request = new SearchRequest(SCENIC_INDEX);
        SearchSourceBuilder builder = request.source()
                .query(QueryBuilders.matchQuery("id", scenicId));
        if (!ObjectUtils.isEmpty(location) && location[0] != null) {
            builder.sort(SortBuilders.geoDistanceSort("location", new GeoPoint(location[0]))
                    .order(SortOrder.ASC)
                    .unit(DistanceUnit.KILOMETERS));
        }
        builder.size(500);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        if (ObjectUtils.isEmpty(hits)) {
            return null;
        }
        SearchHit hit = hits[0];
        OpenscenicDoc openscenicDoc = JSON.parseObject(hit.getSourceAsString(), OpenscenicDoc.class);
        Object[] sortValues = hit.getSortValues();
        if (!ObjectUtils.isEmpty(sortValues)) {
            openscenicDoc.setDistance(sortValues[0]);
        }
        return openscenicDoc;
    }

    /**
     * 获取离我最近的景区
     *
     * @param size
     * @return
     */
    @Override
    @SneakyThrows
    public List<OpenscenicDoc> getScenicListByLocationAndSize(String location, int size) {
        SearchRequest request = new SearchRequest(SCENIC_INDEX);
        request.source()
                .query(QueryBuilders.matchAllQuery())
                .sort(SortBuilders.geoDistanceSort("location", new GeoPoint(location))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS))
                .size(500);
        return getOpeningDocsWithDistance(request);
    }

    /**
     * 获取所有景区，并按距离升序
     *
     * @return
     */
    @Override
    @SneakyThrows
    public List<OpenscenicDoc> getScenicListByLocation(String location) {
        SearchRequest request = new SearchRequest(SCENIC_INDEX);
        request.source()
                .query(QueryBuilders.matchAllQuery())
                .sort(SortBuilders.geoDistanceSort("location", new GeoPoint(location))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS))
                .size(500);
        return getOpeningDocsWithDistance(request);
    }


    /**
     * 获取size大小的开放景区列表
     *
     * @param size
     * @return
     */
    @Override
    @SneakyThrows
    public List<OpenscenicDoc> getScenicListBySize(int size) {
        SearchRequest request = new SearchRequest(SCENIC_INDEX);
        request.source()
                .query(QueryBuilders.termQuery("isOpen", 1));
        return getOpenscenicDocs(request);
    }

    /**
     * 直接获取整个景区列表
     *
     * @return
     */
    @Override
    @SneakyThrows
    public List<OpenscenicDoc> getScenicList() {
        SearchRequest request = new SearchRequest(SCENIC_INDEX);
        request.source()
                .query(QueryBuilders.matchAllQuery())
                .size(500);
        return getOpenscenicDocs(request);
    }

    /**
     * 通过searchKey搜索匹配的景区
     *
     * @param searchKey
     * @return
     */
    @Override
    @SneakyThrows
    public List<OpenscenicDoc> getScenicListBySearchKey(String searchKey) {
        SearchRequest request = new SearchRequest(SCENIC_INDEX);
        request.source()
                .query(QueryBuilders.matchQuery("all", searchKey))
                .size(500);
        return getOpenscenicDocs(request);
    }

    /**
     * 通过searchKey搜索匹配的景区，并按距离升序
     *
     * @param location
     * @param searchKey
     * @return
     */
    @Override
    @SneakyThrows
    public List<OpenscenicDoc> getScenicListBySearchKey(String location, String searchKey) {
        SearchRequest request = new SearchRequest(SCENIC_INDEX);
        request.source()
                .query(QueryBuilders.matchQuery("all", searchKey))
                .sort(SortBuilders.geoDistanceSort("location", new GeoPoint(location))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS))
                .size(500);
        return getOpeningDocsWithDistance(request);
    }


    private List<OpenscenicDoc> getOpeningDocsWithDistance(SearchRequest request) throws IOException {
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<OpenscenicDoc> results = new ArrayList<>();
        for (SearchHit hit : hits) {
            OpenscenicDoc openscenicDoc = JSON.parseObject(hit.getSourceAsString(), OpenscenicDoc.class);
            Object[] sortValues = hit.getSortValues();
            if (!ObjectUtils.isEmpty(sortValues)) {
                openscenicDoc.setDistance(hit.getSortValues()[0]);
            }
            results.add(openscenicDoc);
        }
        return results;
    }


    private List<OpenscenicDoc> getOpenscenicDocs(SearchRequest request) throws IOException {
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<OpenscenicDoc> results = new ArrayList<>();
        for (SearchHit hit : hits) {
            OpenscenicDoc openscenicDoc = JSON.parseObject(hit.getSourceAsString(), OpenscenicDoc.class);
            results.add(openscenicDoc);
        }
        return results;
    }


}




