package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.Seller;
import com.chaoshan.mapper.SellerMapper;
import com.chaoshan.service.AliyunOssServiceImpl;
import com.chaoshan.service.SellerService;
import com.chaoshan.util.api.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 13:22
 */
@Service
public class SellerServiceImpl extends ServiceImpl<SellerMapper, Seller> implements SellerService {

    @Autowired
    private AliyunOssServiceImpl aliyunOssService;

    @Autowired
    private SellerMapper sellerMapper;

    @Override
    public R SellerSettle(Seller seller, MultipartFile file) {
        String pic = aliyunOssService.selectExist(file);
        seller.setPictureCredential(pic)
                .setIsCheck(false);
        return R.data(sellerMapper.insert(seller));
    }
}
