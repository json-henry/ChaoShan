package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.Seller;
import com.chaoshan.util.api.R;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 呱呱
 * @date Created in 2022/5/13 13:22
 */
public interface SellerService extends IService<Seller> {


    /**
     * 商家入驻
     */
    R SellerSettle(Seller seller, MultipartFile file);
}
