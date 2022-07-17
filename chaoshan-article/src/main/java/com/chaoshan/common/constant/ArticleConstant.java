package com.chaoshan.common.constant;

/**
 * @DATE: 2022/05/15 21:46
 * @Author: 小爽帅到拖网速
 */
public class ArticleConstant {

    /**
     * 文章类型
     */
    public final static String ARTICLE_TYPE = "0";

    /**
     * 点赞类型
     */
    public final static String ARTICLE_STAR_TYPE = "1";

    /**
     * 收藏类型
     */
    public final static String ARTICLE_COLLECTION_TYPE = "2";

    /**
     * article索引文档
     */
    public static final String ARTICLE_MAPPING = "{\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"title\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"introduction\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"createTime\":{\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"details\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "         \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"collectionCount\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"pageView\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "       \"starCount\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"location\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      },\n" +
            "      \"pictureLink\":{\n" +
            "        \"type\":\"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"tag\":{\n" +
            "        \"type\":\"keyword\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"type\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"recommendMouth\":{\n" +
            "        \"type\":\"integer\"\n" +
            "      },\n" +
            "      \"accoutid\":{\n" +
            "        \"type\":\"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"sum\":{\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }";
}
