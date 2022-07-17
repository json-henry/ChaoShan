package com.chaoshan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chaoshan.entity.Openscenic;
import com.chaoshan.entity.OpenscenicSignup;
import com.chaoshan.mapper.OpenscenicMapper;
import com.chaoshan.mapper.OpenscenicSignupMapper;
import com.chaoshan.service.OpenscenicSignupService;
import com.chaoshan.util.api.R;
import com.chaoshan.util.entity.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

/**
 * @author YCE
 * @description 针对表【cs_openscenic_signup(开放景区报名表)】的数据库操作Service实现
 * @createDate 2022-05-17 17:34:59
 */
@Service
public class OpenscenicSignupServiceImpl extends ServiceImpl<OpenscenicSignupMapper, OpenscenicSignup>
        implements OpenscenicSignupService {

    @Autowired
    private OpenscenicMapper scenicMapper;
    @Autowired
    private OpenscenicSignupMapper signupMapper;

    /**
     * 景区报名
     *
     * @param scenicId
     * @return
     */
    @Override
    @Transactional
    public R signup(Long scenicId) {
        Openscenic scenic = scenicMapper.selectById(scenicId);
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        if (ObjectUtils.isEmpty(scenic)) {
            return R.fail("景区不存在！");
        }
        if (scenic.getAccessibleNumber() <= 0) {
            return R.fail("景区人数已满,无法再进行预约！");
        }
        //判断是否已经预约
        OpenscenicSignup signup = signupMapper.selectOne(new LambdaQueryWrapper<OpenscenicSignup>()
                .eq(OpenscenicSignup::getAccountid, accountId)
                .eq(OpenscenicSignup::getOpenscenicid, scenicId)
        );
        if (!ObjectUtils.isEmpty(signup)) {
            return R.fail("该活动已经预约");
        }
        scenicMapper.updateById(scenic.setAccessibleNumber(scenic.getAccessibleNumber() - 1));
        signupMapper.insert(new OpenscenicSignup(scenicId, accountId));
        return R.success("景区预约成功！");
    }

    @Override
    public R unSignup(Long scenicId) {
        //判断是否为空
        if (ObjectUtils.isEmpty(scenicId)) {
            return R.fail("景区id不能为空");
        }
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        //查询报名记录并判断是否存在
        OpenscenicSignup signup = signupMapper.selectOne(new LambdaQueryWrapper<OpenscenicSignup>()
                .eq(OpenscenicSignup::getOpenscenicid, scenicId)
                .eq(OpenscenicSignup::getAccountid, accountId)
        );
        if (ObjectUtils.isEmpty(signup)) {
            //不存在则直接提示没有报名过该活动
            return R.fail("没有报名过该景区");
        }
        //存在则进行删除
        if (signupMapper.deleteById(signup.getId()) > 0) {
            return R.success("取消报名成功");
        }
        return R.fail("取消报名失败");
    }
}




