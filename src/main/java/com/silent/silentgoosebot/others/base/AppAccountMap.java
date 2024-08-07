package com.silent.silentgoosebot.others.base;

import com.silent.silentgoosebot.others.MoistLifeApp;
import lombok.Data;

import java.util.Map;

/**
 * use to keep account login in tdlight-java
 */
@Data
public class AppAccountMap {
    private Map<String, MoistLifeApp> accountMap;
}
