package com.silent.silentgoosebot.others.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * some message process utils for message process
 */
public class ProcessUtils {
    public static String[][] teacherKeys = {
            //todo 完善关键字
            new String[]{"姓名", "名字"},
            new String[]{"地点", "地区"},
            new String[]{"一次价格"},
            new String[]{"两次价格", "二次价格"},
            new String[]{"包夜价格"},
            new String[]{"电报号"},
    };

    /**
     * 预处理，合并换行符，替换全角冒号，替换所有空白字符为半角空格
     * @param message
     * @return
     */
    public static String teacherMessagePreProcess(String message) {
        String processed = message.replaceAll("(\\n\\s*)+", "\n");
        return processed.replaceAll("：", ":").replaceAll("\\p{Z}\\s&&[^\\n\\r]+", " ");
    }

    /**
     * 根据预处理的结果，提取关键字和信息列表
     * @param message
     * @return
     */
    public static Map<String, String> extractKeyValue(String message) {
        Map<String, String> result = new LinkedHashMap<>();
        // 按换行符分割
        String[] lines = message.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (StringUtils.isBlank(line)) {
                continue;
            }
            String key;
            String value;
            if (line.contains(":")) {
                // 以第一个冒号为分割点，注意只拆分一次
                String[] parts = StringUtils.splitByWholeSeparatorPreserveAllTokens(line, ":", 2);
                key = parts[0].trim();
                value = parts.length > 1 ? parts[1].trim() : "";
            } else {
                // 如果不包含冒号，则尝试用第一个空格拆分
                int idx = StringUtils.indexOf(line, " ");
                if (idx > 0) {
                    key = StringUtils.substring(line, 0, idx).trim();
                    value = StringUtils.substring(line, idx).trim();
                } else {
                    key = line;
                    value = "";
                }
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 给定一个字符串，字符串中存在两个数字，两个数字不相连，编写函数提取这两个数字，并以BigDecimal[]返回
     */
    public static BigDecimal[] extractPriceNumber(String message) {
        BigDecimal[] result = new BigDecimal[2];
        // 正则表达式：匹配连续数字
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(message);

        int count = 0;
        while (matcher.find() && count < 2) {
            result[count] = new BigDecimal(matcher.group());
            count++;
        }
        return result;
    }

    /**
     * 提取包含用户名字符串中的用户名，不包含@符号
     */
    public static String extractUserId(String message) {
        // 正则表达式：匹配@符号后的所有非空白字符
        Pattern pattern = Pattern.compile("(?<=^|\\\\s)@?([a-zA-Z0-9_]{5,32})(?=$|\\\\s)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

}
