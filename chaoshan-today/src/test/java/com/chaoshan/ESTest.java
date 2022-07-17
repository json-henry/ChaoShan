package com.chaoshan;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.chaoshan.constant.ESConstant;
import com.chaoshan.entity.Activity;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.entity.Topic;
import com.chaoshan.entity.doc.OpenscenicDoc;
import com.chaoshan.mapper.ActivityMapper;
import com.chaoshan.mapper.OpenscenicMapper;
import com.chaoshan.mapper.TopicMapper;
import lombok.SneakyThrows;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-20  15:04
 * @Description: ESTest
 * @Version: 1.0
 */

@SpringBootTest
public class ESTest {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private OpenscenicMapper mapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private TopicMapper topicMapper;

    @Test
    void test1() throws IOException {
        Openscenic openscenic = mapper.selectById(2L);
        OpenscenicDoc openscenicDoc = new OpenscenicDoc(openscenic);
        String s = JSONUtil.toJsonStr(openscenicDoc);
        IndexRequest request = new IndexRequest("openscenic").id(openscenic.getId().toString());
        request.source(s, XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    void test2() throws IOException {
        Activity activity = activityMapper.selectById(1L);
        IndexRequest request = new IndexRequest("activity").id(activity.getId().toString());
        request.source(JSONUtil.toJsonStr(activity), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    void test3() throws IOException {
        Topic topic = topicMapper.selectById(1L);
        IndexRequest request = new IndexRequest("topic").id(topic.getId().toString());
        request.source(JSONUtil.toJsonStr(topic), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    @SneakyThrows
    void testGetScenicBySize() {
        SearchRequest request = new SearchRequest("openscenic");
        request.source()
                .query(QueryBuilders.matchAllQuery())
                .size(1)
                .sort(SortBuilders.geoDistanceSort("location", new GeoPoint("23.27,113.29"))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<OpenscenicDoc> results = new ArrayList<>();
        for (SearchHit hit : hits) {
            OpenscenicDoc openscenicDoc = JSON.parseObject(hit.getSourceAsString(), OpenscenicDoc.class);
            openscenicDoc.setDistance(hit.getSortValues()[0]);
            results.add(openscenicDoc);
        }
        for (OpenscenicDoc result : results) {
            System.out.println(JSONUtil.toJsonStr(result));
        }
    }

    @Test
    @SneakyThrows
    void testGetScenic() {
        SearchRequest request = new SearchRequest("openscenic");
        request.source()
                .query(QueryBuilders.termQuery("isOpen", 1))
                .size(1);
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<OpenscenicDoc> results = new ArrayList<>();
        for (SearchHit hit : hits) {
            OpenscenicDoc openscenicDoc = JSON.parseObject(hit.getSourceAsString(), OpenscenicDoc.class);
            results.add(openscenicDoc);
        }
        for (OpenscenicDoc result : results) {
            System.out.println(JSONUtil.toJsonStr(result));
        }
    }

    @Test
    @SneakyThrows
    void testGetScenicBySearchKey() {
        SearchRequest request = new SearchRequest("openscenic");
        request.source()
                .query(QueryBuilders.matchQuery("all", "字塔"))
                .sort(SortBuilders.geoDistanceSort("location", new GeoPoint("23.27,113.29"))
                        .order(SortOrder.ASC)
                        .unit(DistanceUnit.KILOMETERS));
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);
        SearchHit[] hits = response.getHits().getHits();
        ArrayList<OpenscenicDoc> results = new ArrayList<>();
        for (SearchHit hit : hits) {
            OpenscenicDoc openscenicDoc = JSON.parseObject(hit.getSourceAsString(), OpenscenicDoc.class);
            openscenicDoc.setDistance(hit.getSortValues()[0]);
            results.add(openscenicDoc);
        }
        for (OpenscenicDoc result : results) {
            System.out.println(JSONUtil.toJsonStr(result));
        }
    }


    @Test
    @SneakyThrows
    void deleteAllEsIndex() {
        // 1.创建Request对象
        DeleteIndexRequest r1 = new DeleteIndexRequest(ESConstant.TOPIC_INDEX);
        DeleteIndexRequest r2 = new DeleteIndexRequest(ESConstant.ACTIVITY_INDEX);
        DeleteIndexRequest r3 = new DeleteIndexRequest(ESConstant.SCENIC_INDEX);
// 2.发送请求
        client.indices().delete(r1, RequestOptions.DEFAULT);
        client.indices().delete(r2, RequestOptions.DEFAULT);
        client.indices().delete(r3, RequestOptions.DEFAULT);


        // 1.创建Request对象
        GetIndexRequest request1 = new GetIndexRequest(ESConstant.SCENIC_INDEX);
        GetIndexRequest request2 = new GetIndexRequest(ESConstant.ACTIVITY_INDEX);
        GetIndexRequest request3 = new GetIndexRequest(ESConstant.TOPIC_INDEX);
// 2.发送请求
        boolean e1 = client.indices().exists(request1,
                RequestOptions.DEFAULT);
        boolean e2 = client.indices().exists(request2,
                RequestOptions.DEFAULT);
        boolean e3 = client.indices().exists(request3,
                RequestOptions.DEFAULT);
// 3.输出
        System.err.println(e1 ? "索引库已经存在！" : "索引库不存在！");
        System.err.println(e2 ? "索引库已经存在！" : "索引库不存在！");
        System.err.println(e3 ? "索引库已经存在！" : "索引库不存在！");

    }

}
