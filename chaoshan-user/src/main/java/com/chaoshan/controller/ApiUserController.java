package com.chaoshan.controller;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaoshan.clients.ArticleClient;
import com.chaoshan.clients.TodayClient;
import com.chaoshan.entity.*;
import com.chaoshan.entity.dto.UserDTO;
import com.chaoshan.service.*;
import com.chaoshan.util.api.R;
import com.chaoshan.util.api.ResultCode;
import com.chaoshan.util.entity.LoginUser;
import com.chaoshan.utils.RandomNum;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 呱呱
 * @since 2022/5/12 14:10
 */
@RestController
@Slf4j
@RequestMapping("/api")
public class ApiUserController {

    @Autowired
    private UserService userService;
    @Autowired
    private FansService fansService;
    @Autowired
    private AliyunOssServiceImpl aliyunOssService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private ArticleClient articleClient;
    @Autowired
    private UserInvitationService invitationService;
    @Autowired
    private TodayClient todayClient;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * 被邀请用户首次登录时是否提供了邀请码
     */
    @GetMapping("/firstLogin/{code}")
    @ApiOperation(value = "用户首次登录是否使用了邀请码", notes = "开放接口")
    @ApiImplicitParam(name = "code", value = "验证码", required = false, paramType = "path")
    public R firstLogin(@PathVariable String code) {
//    获取被邀请用户的账号id
        String accountided = LoginUser.getCurrentLoginUser().getAccountid();
        if (ObjectUtil.isEmpty(code)) {
//       1. 判断code是否为空 || code不存在
            return R.data(ResultCode.PARAM_MISS);
        }
//   3. code不为空则查找对应code的accountid,添加邀请数据在UserInvitation中
        return R.data(invitationService.insertInvite(code, accountided));
    }

    /**
     * 根据用户账号返回用户信息
     *
     * @return 用户信息
     * test ok
     */
    @GetMapping("/getUserByAccountId")
    @ApiOperation(value = "根据用户账号返回用户信息")
    @ApiIgnore
    public R<User> getUserByAccountId() {
        User user = LoginUser.getCurrentLoginUser();
        return R.data(userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccountid, user.getAccountid())));
    }


    /**
     * 获取用户信息
     *
     * @return 用户名、头像、简介及关注数、预约报名、所有收藏和点赞数及邀请用户数
     * test ok
     */
    @GetMapping("/userInfo")
    @ApiOperation(value = "获取用户信息,用户名、头像、简介及关注数、粉丝数、预约报名数及邀请用户数")
    public R<UserDTO> getUserInfo() {
        User user = LoginUser.getCurrentLoginUser();
        return userService.getUserInfo(user.getAccountid());
    }

    /**
     * 获取用户关注的用户信息
     *
     * @return 用户信息列表
     * test ok
     */
    @GetMapping("/getFollows")
    @ApiOperation(value = "获取用户关注的用户信息")
    public R<List<User>> getFollows() {
        // 1. 通过accountid查找当前用户关注的用户信息
        User user = LoginUser.getCurrentLoginUser();
        // 根据accountid获取用户关注的用户列表
        return fansService.getFocusInfo(user.getAccountid());
    }


    /**
     * 获取用户的粉丝信息
     *
     * @return 用户信息列表
     * test ok
     */
    @GetMapping("/getFans")
    @ApiOperation(value = "获取用户的粉丝信息")
    public R<List<User>> getFans() {
        User user = LoginUser.getCurrentLoginUser();
//  获取用户的粉丝用户信息列表
        return fansService.getFansInfo(user.getAccountid());
    }

    /**
     * 商家入驻
     *
     * @return test ok
     */
    @PostMapping("/beSeller")
    @ApiOperation(value = "商家入驻", notes = "用户的is_business为0才能触发该接口，为1则提示‘已成为商家’")
    public R beSeller(MultipartFile file) {
        if (ObjectUtil.isEmpty(file)) {
            return R.fail(ResultCode.PARAM_MISS);
        }
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        Seller seller = new Seller().setAccountid(accountId);
        return sellerService.SellerSettle(seller, file);
    }

    /**
     * 单个图片视频上传
     *
     * @param file
     * @return
     */
    @PostMapping("/postPhoto")
    @ApiOperation(value = "上传单个图片视频文件", notes = "开放接口")
    public R upload(MultipartFile file) {
        if (file != null) {
            Map<Object, String> map = new HashMap<>();
            map.put("picture", aliyunOssService.selectExist(file));
            return R.data(map);
        }
        return R.fail("文件为空");
    }

    /**
     * 多个图片视频文件上传
     */
    @ApiOperation(value = "上传多个图片文件", notes = "开放接口")
    @PostMapping(value = "/postManyFile")
    public R uploadMany(@RequestParam("files") MultipartFile[] files) {
        Map<Object, String> pictures = new HashMap<>();
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                pictures.put("manyPic", aliyunOssService.selectExist(file));
            }
        }
        return R.data(pictures);
    }

    /**
     * 修改个人信息
     *
     * @param user
     * @return
     */
    @PostMapping("/updateUserInfo")
    @ApiOperation(value = "修改个人信息")
    public R updateUserInfo(@RequestBody User user) {
        user.setAccountid(LoginUser.getCurrentLoginUser().getAccountid());
        if (!ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return userService.updateUserInfo(user);
    }


    /**
     * 生成邀请码
     *
     * @return 返回生成的邀请码
     * 如果存在被邀请的用户，返回被邀请用户信息
     * test ok
     */
    @GetMapping("/createCode")
    @ApiOperation(value = "生成用户邀请码")
    public R<Map> createCode() {
        String accountid = LoginUser.getCurrentLoginUser().getAccountid();
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getAccountid, accountid));
        Map<String, Object> users = new HashMap<>();
//   1. 查找当前用户是否已有邀请码
        if (ObjectUtil.isEmpty(user.getInvitationCode())) {
//       2.根据accountid生成固定的邀请码
            user.setInvitationCode(RandomNum.genRandomNum());
//       3.将邀请码保存在user表中并返回邀请码
            userService.updateById(user);
        }
        users.put("invitationCode", user.getInvitationCode());
//    返回被邀请用户的信息列表
        List<User> userList = userService.getInvitedUser(user.getAccountid());
        users.put("userInfos", userList);
        return R.data(users);
    }


    /**
     * 获取文章列表
     *
     * @return 返回个人页文章展示的列表数据
     */
    @GetMapping("/getSelfArticleList")
    @ApiOperation(value = "获取文章列表")
    public R<List<Article>> getSelfArticleList(@RequestParam(value = "content", required = false) String content) {
        User user = LoginUser.getCurrentLoginUser();
//    使用feignClient调用Article服务
        return articleClient.vagueSelect(user.getAccountid(), content, "0");
    }

    /**
     * 获取文章内容
     *
     * @return 文章数据
     */
    @GetMapping("/getArticleInfo/{articleid}")
    @ApiOperation(value = "获取文章内容", notes = "开放接口")
    @ApiImplicitParam(name = "articleid", value = "文章id", required = true, paramType = "path")
    public R<Article> getArticleInfo(@PathVariable Integer articleid) {
        return articleClient.getArticleDetail(articleid);
    }

    /**
     * 获取收藏文章的列表
     *
     * @return 个人页收藏展示的列表数据
     */
    @GetMapping("/getUserCollectionList")
    @ApiOperation(value = "获取收藏列表")
    public R<List<Article>> getUserCollectionList(@RequestParam(value = "content", required = false) String content) {
        User user = LoginUser.getCurrentLoginUser();
        return articleClient.vagueSelect(user.getAccountid(), content, "2");
    }

    /**
     * 获取点赞文章的列表
     *
     * @return 个人页点赞展示的列表数据
     */
    @GetMapping("/getUserStarList")
    @ApiOperation(value = "获取点赞文章的列表")
    public R<List<Article>> getUserStarList(@RequestParam(value = "content", required = false) String content) {
        User user = LoginUser.getCurrentLoginUser();
        return articleClient.vagueSelect(user.getAccountid(), content, "1");
    }

    /**
     * 获取当前用户的草稿箱文章列表
     *
     * @return 文章列表
     */
    @GetMapping("/getUserDrafts")
    @ApiOperation(value = "获取用户草稿箱文章")
    public R<List<Article>> getUserDrafts(@RequestParam(value = "keyword", required = false) String keyword) {
        User user = LoginUser.getCurrentLoginUser();
        return articleClient.getDrafts(user.getAccountid(), keyword);
    }

    /**
     * 模糊搜索文章
     *
     * @param content
     * @param label   值为"文章/点赞/收藏“ --> 0/1/2
     * @return
     */
    @GetMapping("/vagueSelect")
    @ApiIgnore
    @ApiOperation(value = "模糊搜索文章", notes = "content是模糊查询的内容，label值文章/点赞/收藏 --> 0/1/2")
    @ApiImplicitParam(name = "content", value = "文章字段", required = true, paramType = "path")
    public R<List<Article>> vagueSelect(@RequestParam(value = "content", required = false) String content,
                                        @RequestParam(value = "label") String label) {
        User user = LoginUser.getCurrentLoginUser();
        return articleClient.vagueSelect(user.getAccountid(), content, label);
    }

    /**
     * 用户待审核文章
     *
     * @return 待审核文章列表
     */
    @GetMapping("/waitPublishArticle")
    @ApiOperation(value = "待审核文章")
    public R<List<Article>> waitPublishArticle(@RequestParam(value = "keyword", required = false) String keyword) {
        String accountid = LoginUser.getCurrentLoginUser().getAccountid();
        return articleClient.waitPublish(accountid, keyword);
    }

    /**
     * 关注操作
     *
     * @param accountid 被关注用户的账号
     * @return
     */
    @GetMapping("/focuson/{accountid}")
    @ApiOperation(value = "关注操作", notes = "返回1则取消关注")
    @ApiImplicitParam(name = "accountid", value = "被关注用户的账号", required = true, paramType = "path")
    public R focuson(@PathVariable String accountid) throws InterruptedException {
        User user = LoginUser.getCurrentLoginUser();
//  进行关注 or 取关操作
        return fansService.focus(accountid, user.getAccountid());
    }

    /**
     * 点赞文章
     *
     * @param articleid
     * @return
     */
    @GetMapping("/starArticle/{articleid}")
    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "articleid", value = "文章id", required = true, paramType = "path")
    public R starArticle(@PathVariable Long articleid) {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        return articleClient.starArticleOper(new ArticleStar().setAccountid(accountId).setArticleid(articleid));
    }

    /**
     * 收藏文章
     *
     * @param articleid
     * @return
     */
    @GetMapping("/collectionArticle/{articleid}")
    @ApiOperation(value = "收藏文章")
    @ApiImplicitParam(name = "articleid", value = "文章id", required = true, paramType = "path")
    public R collectionArticle(@PathVariable Long articleid) {
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        return articleClient.collectionUserArticle(new ArticleCollection().setArticleid(articleid).setAccountid(accountId));
    }

    /**
     * 用户发布文章
     *
     * @param article
     * @return
     */
    @PostMapping("/postArticle")
    @ApiOperation(value = "发布文章", notes = "article实体必传字段：isPublish[0未发布 1 已发布 default 1],title")
    public R postArticle(@RequestBody Article article) {
        if (ObjectUtil.isEmpty(article.getTitle())) {
            return R.data(ResultCode.PARAM_MISS);
        }
        if (article.getPictureLink() == null) {
            article.setPictureLink("");
        }
        if (article.getTag() == null) {
            article.setTag("");
        }
        String[] files = article.getPictureLink().split(";");
        String[] tags = article.getTag().split(";");
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        return articleClient.postArticle(files, article, tags, accountId);

    }

    /**
     * 用户发布产品
     *
     * @param article
     * @return
     */
    @PostMapping("/postProduct")
    @ApiOperation(value = "发布产品", notes = "files是用户上传多张图片的数组信息")
    public R postProduct(@RequestBody Article article) {
        if (ObjectUtil.isEmpty(article.getTitle())) {
            return R.data(ResultCode.PARAM_MISS);
        }
        if (article.getPictureLink() == null) {
            article.setPictureLink("");
        }
        if (article.getTag() == null) {
            article.setTag("");
        }
        String[] files = article.getPictureLink().split(";");
        String[] tags = article.getTag().split(";");
        String accountId = LoginUser.getCurrentLoginUser().getAccountid();
        return articleClient.postArticle(files, article, tags, accountId);


    }

    /**
     * 用户更新文章
     */
    @PostMapping("/updateArticle")
    @ApiOperation("用户更新文章")
    public R updateUserArticle(@RequestBody Article article) {
        if (article.getPictureLink() == null) {
            article.setPictureLink("");
        }
        if (article.getTag() == null) {
            article.setTag("");
        }
        String[] files = article.getPictureLink().split(";");
        String[] tags = article.getTag().split(";");
        return articleClient.updateArticle(files, article, tags);
    }

    /**
     * 话题文章发布
     */
    @PostMapping("/publishTopicArticle")
    @ApiOperation(value = "话题文章发布", notes = "需要先上传图片，将图片链接放入实体中")
    public R publishTopicArticle(@RequestBody TopicArticle topicArticle) {
        return todayClient.publishTopicArticle(topicArticle);
    }

//  /**
//   * 当前登录用户修改密码
//   */
//  @PostMapping("/modifyPassword")
//  @ApiOperation(value = "当前登录用户修改密码",notes = "需要传递原密码(rawPassword)和新密码(newPassword)")
//  public R modifyPassword(@RequestBody UserPasswordDTO userPasswordDTO){
//    if (ObjectUtil.hasEmpty(userPasswordDTO.getRawPassword(),userPasswordDTO.getNewPassword())) {
//      return R.fail(ResultCode.PARAM_VALID_ERROR,"原密码或者新密码不能为空");
//    }
//    return userService.modifyPassword(userPasswordDTO);
//  }

}
