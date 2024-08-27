package com.silent.silentgoosebot.others.base;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

/**
 * Date: 2023/12/13
 * Author: SilentSherlock
 * Description: config some beans outside the frame
 */
@Configuration
@Slf4j
public class BeanConfig {

    @Bean
    public AppAccountMap getAppAccountMap() {
        AppAccountMap appAccountMap = new AppAccountMap();
        appAccountMap.setAccountMap(new HashMap<>());
        return appAccountMap;
    }

}
