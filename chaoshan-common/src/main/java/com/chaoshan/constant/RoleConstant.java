package com.chaoshan.constant;

/**
 * @DATE: 2022/05/04 13:45
 * @Author: 小爽帅到拖网速
 */
public class RoleConstant {
    /**
     * 系统管理员，开放所有权限
     */
    private static final String ADMIN = "admin";

    /**
     * 普通用户，拥有对文章进行点赞删除修改操作，修改个人信息等
     */
    private static final String BASIC_USER = "basic_user";

    /**
     * 商家用户，完成了商家的资质认证，可以发布商家产品
     */
    private static final String SELLER_USER = "seller_user";

    /**
     * 测试角色，拥有普通用户，商家的权限
     */
    private static final String TEST_USER = "test_user";

    public static final String HAS_ROLE_ADMIN = "hasAuthority('" + ADMIN + "')";
    public static final String HAS_ROLE_BASIC_USER = "hasAuthority('" + BASIC_USER + "')";
    public static final String Has_ROLE_SELLER_USER = "hasAuthority('" + SELLER_USER + "')";
    public static final String Has_ROLE_TEST_USER = "hasAuthority('" + TEST_USER + "')";


}
