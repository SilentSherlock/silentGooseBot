package com.silent.silentgoosebot.others.base;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

/**
 * Date: 2021/5/8
 * Author: SilentSherlock
 * Description:操作properties文件
 */
@Slf4j
public class MyPropertiesUtil {

    private static final Properties properties;
    static {
        String fileName = "application.properties";
        properties = new Properties();
        try {
            properties.load(
                    new InputStreamReader(
                            Objects.requireNonNull(MyPropertiesUtil.class.getClassLoader().getResourceAsStream(fileName)),
                            StandardCharsets.UTF_8
                    )
            );
        } catch (IOException e) {
            log.error(fileName + "读取异常");
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        String value = properties.getProperty(key.trim());
        if(value == null || value.equals("")){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){

        String value = properties.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }
}
