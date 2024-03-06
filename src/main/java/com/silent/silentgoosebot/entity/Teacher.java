package com.silent.silentgoosebot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Date: 2023/11/6
 * Author: SilentSherlock
 * Description: describe the teacher properties
 */
@Data
@Accessors(chain = true)
public class Teacher {

    @TableId
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
