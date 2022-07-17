package com.chaoshan.constant;

/**
 * @Author: HYX
 * @CreateTime: 2022-05-20  20:02
 * @Description: ES相关的常量
 * @Version: 1.0
 */
public class ESConstant {

    public static final String SCENIC_INDEX = "openscenic";
    public static final String TOPIC_INDEX = "topic";
    public static final String ACTIVITY_INDEX = "activity";

    public static final String SCENIC_MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\": {\n" +
            "        \"type\": \"long\"\n" +
            "      },\n" +
            "      \"name\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"picture_link\": {\n" +
            "        \"type\": \"keyword\",\n" +
            "        \"index\": false\n" +
            "      },\n" +
            "      \"contents\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"accessible_number\": {\n" +
            "        \"type\": \"integer\"\n" +
            "      },\n" +
            "      \"is_open\": {\n" +
            "        \"type\": \"boolean\"\n" +
            "      },\n" +
            "      \"location\": {\n" +
            "        \"type\": \"geo_point\"\n" +
            "      },\n" +
            "      \"create_time\": {\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"create_by\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"update_time\": {\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"update_by\": {\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"all\": {\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static final String TOPIC_MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"long\"\n" +
            "      },\n" +
            "      \"activityid\":{\n" +
            "        \"type\": \"long\"\n" +
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
            "      \"view_count\":{\n" +
            "        \"type\": \"long\"\n" +
            "      },\n" +
            "      \"is_delete\":{\n" +
            "        \"type\": \"boolean\"\n" +
            "      },\n" +
            "      \"create_time\":{\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"create_by\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"update_time\":{\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"update_by\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";
    public static final String ACTIVITY_MAPPING_TEMPLATE = "{\n" +
            "  \"mappings\": {\n" +
            "    \"properties\": {\n" +
            "      \"id\":{\n" +
            "        \"type\": \"long\"\n" +
            "      },\n" +
            "      \"title\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"picture_link\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"details\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\",\n" +
            "        \"copy_to\": \"all\"\n" +
            "      },\n" +
            "      \"is_delete\":{\n" +
            "        \"type\": \"boolean\"\n" +
            "      },\n" +
            "      \"start_time\":{\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"end_time\":{\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"create_time\":{\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"create_by\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"update_time\":{\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"update_by\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"calendar\":{\n" +
            "        \"type\": \"date\"\n" +
            "      },\n" +
            "      \"lunar\":{\n" +
            "        \"type\": \"keyword\"\n" +
            "      },\n" +
            "      \"all\":{\n" +
            "        \"type\": \"text\",\n" +
            "        \"analyzer\": \"ik_max_word\"\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

}
