package com.chaoshan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.chaoshan.entity.OpenscenicSignup;
import com.chaoshan.util.api.R;

/**
 * @author YCE
 * @description 针对表【cs_openscenic_signup(开放景区报名表)】的数据库操作Service
 * @createDate 2022-05-17 17:34:59
 */
public interface OpenscenicSignupService extends IService<OpenscenicSignup> {
    /**
     * 景区报名
     *
     * @param scenicId
     * @return
     */
    R signup(Long scenicId);

    /**
     * 取消预约
     *
     * @param scenicId
     * @return
     */
    R unSignup(Long scenicId);
}
