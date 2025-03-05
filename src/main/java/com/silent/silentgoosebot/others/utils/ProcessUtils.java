package com.silent.silentgoosebot.others.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * some message process utils for message process
 */
public class ProcessUtils {
    public static String[][] teacherKeys = {
            //todo 完善关键字
            new String[]{"姓名"},
            new String[]{"地点"},
            new String[]{"价格"},
            new String[]{"价格(PP)"},
            new String[]{"价格(晚上)"},
    };

    /**
     * 预处理，合并换行符，替换全角冒号
     * @param message
     * @return
     */
    public static String teacherMessagePreProcess(String message) {
        String processed = message.replaceAll("(\\n\\s*)+", "\n");
        return message.replaceAll("：", ":");
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

}
