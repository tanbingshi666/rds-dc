package com.skysec.soc.rds.dc;

import cn.hutool.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String originalString = "{\n" +
                "    \"query\": {\n" +
                "        \"range\": {\n" +
                "            \"storagetime\": {\n" +
                "                \"gte\": #{startTime},\n" +
                "                \"lte\": #{endTime}\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        // 替换规则：将 "[count]" 替换为数字 42，"[value]" 替换为数字 123
        originalString = replaceWithNumber(originalString, "startTime", 42);
        originalString = replaceWithNumber(originalString, "endTime", "123");

        System.out.println(originalString);

        String temp = "{\n" +
                "    \"query\": {\n" +
                "        \"range\": {\n" +
                "            \"storagetime\": {\n" +
                "                \"gte\": #{startTime},\n" +
                "                \"lte\": #{endTime}\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        JSONObject jsonObject = new JSONObject();
        jsonObject.set("dataSource", "ElasticSearch");
        jsonObject.set("config", temp);
        System.out.println(jsonObject.toJSONString(1));
    }

    private static String replaceWithNumber(String input, String placeholder, Object replacementNumber) {
        String pattern = "\\#\\{" + placeholder + "\\}";
        String replacementString = String.valueOf(replacementNumber);

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(input);
        return matcher.replaceAll(replacementString);
    }
}
