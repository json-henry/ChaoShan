package com.chaoshan.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.constant.ESConstant;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.entity.doc.OpenscenicDoc;
import com.chaoshan.service.impl.OpenscenicServiceImpl;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-23  18:57
 * @Version: 1.0
 */
@SpringBootTest
class ApplicationStartUtilsTest {

    @Autowired
    private RestHighLevelClient client;
    @Autowired
    private OpenscenicServiceImpl scenicService;


    @Test
    void saveScenicToEs() throws IOException {
        Openscenic openscenic = scenicService.getById(1L);
        IndexRequest request = new IndexRequest(ESConstant.SCENIC_INDEX)
                .id(openscenic.getId().toString())
                .source(JSONUtil.toJsonStr(new OpenscenicDoc(openscenic)), XContentType.JSON);
        client.index(request, RequestOptions.DEFAULT);
    }

    @Test
    void batchSaveScenic() throws IOException {
        //从数据库中获取数据后批量写入索引库中
        List<Openscenic> scenicList = scenicService.list(new LambdaQueryWrapper<>());
        BulkRequest bulkRequest = new BulkRequest();
        for (Openscenic scenic : scenicList) {
            OpenscenicDoc scenicDoc = new OpenscenicDoc(scenic);
            bulkRequest.add(new IndexRequest(ESConstant.SCENIC_INDEX)
                    .id(scenicDoc.getId().toString())
                    .source(JSON.toJSONString(scenicDoc), XContentType.JSON));
        }
        client.bulk(bulkRequest, RequestOptions.DEFAULT);
    }
}