package com.chaoshan.service;


import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.chaoshan.util.api.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Service
public class AliyunOssServiceImpl {
    @Value("${aliyun.oss.endpoint}")
    private String endPoint;

    @Value("${aliyun.oss.access-key-id}")
    private String accessKeyId;

    @Value("${aliyun.oss.access-key-secret}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucket-name}")
    private String bucketName;

    @Value("${aliyun.oss.bucket-domain}")
    private String bucketDomain;

    @Value("${aliyun.oss.folder}")
    private String folder;

    /**
     * 上传图片
     * @param file
     * @return oss中的图片路径
     */
//    public String uploadImg(MultipartFile file){
//        //创建oss实例
//        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
//
//        try{
//            //获取上传文件输入流
//            InputStream inputStream = file.getInputStream();
//            //在文件名里添加随机唯一的值，避免文件覆盖
//            String uuid = UUID.randomUUID().toString().replace("-","");
//            //获取原文件名，提取文件格式名
//            String fileName = file.getOriginalFilename();
//            String suffixName = fileName.substring(fileName.lastIndexOf("."));
//            String imgName = folder + uuid + suffixName;
//            //调用oss方法实现上传
//            ossClient.putObject(bucketName, imgName, inputStream);
//            String url = "https://" + bucketDomain  + "/" + imgName;
//            return url;
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            ossClient.shutdown();
//        }
//        return null;
//    }

    /**
     * 删除图片功能
     */
    public R<String> deleteFile(String url) {
//        解析获得的url为oss文件路径
        for (int i = 0; i < 3; i++) {
            url = url.substring(url.indexOf("/") + 1);
        }
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        // 判断当前文件url 是否存在
        boolean exist = ossClient.doesObjectExist(bucketName, url);
        if (!exist) {
            return R.data("文件不存在，可能已删除");
        } else {
            // 删除文件。
            ossClient.deleteObject(bucketName, url);
            // 关闭OSSClient。
            ossClient.shutdown();
            return R.data("文件删除成功！");
        }
    }

    /**
     * 上传文件后查看oss中是否拥有该文件
     */
    public String selectExist(MultipartFile file) {
        //创建oss实例
        OSS ossClient = new OSSClientBuilder().build(endPoint, accessKeyId, accessKeySecret);
        try {
            //获取上传文件输入流
            InputStream inputStream = file.getInputStream();
            //在文件名里添加随机唯一的值，避免文件覆盖
            String uuid = UUID.randomUUID().toString().replace("-", "");
            //获取原文件名，提取文件格式名
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            String imgName = folder + uuid + suffixName;
            String url = "https://" + bucketDomain + "/" + imgName;
            // 判断当前文件url 是否存在
            boolean exist = ossClient.doesObjectExist(bucketName, url);
//            存在则不上传，直接返回oss现有的该文件路径
//            不存在则调用oss方法上传图片
            if (!exist) {
                ossClient.putObject(bucketName, imgName, inputStream);
            }
            return url;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
        return null;
    }
}