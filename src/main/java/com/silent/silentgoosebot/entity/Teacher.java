package com.silent.silentgoosebot.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Date: 2023/11/6
 * Author: SilentSherlock
 * Description: describe the teacher properties
 */
@Data
public class Teacher {

    private String teacherTableId;
    private String channelId;
    private String teacherId;
    private String teacherName;
    private String location;
    private BigDecimal priceP;
    private BigDecimal pricePp;
    private BigDecimal priceNight;
    private LocalDateTime insertTime;
    private LocalDateTime updateTime;
}
