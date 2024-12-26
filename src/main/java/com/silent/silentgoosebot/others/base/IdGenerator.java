package com.silent.silentgoosebot.others.base;

import java.util.Random;

public class IdGenerator {

    private static String getId() {
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer buffer  = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            buffer.append(rand.nextInt(10));
        }
        return buffer.toString();
    }

    public static String getNextTgAccountId() {
        String tgAccount = "tgAcc";
        return tgAccount.concat(getId());
    }

    public static String getNextMessageProcessorId() {
        String messageProcessor = "msgPro";
        return messageProcessor.concat(getId());
    }

    public static String getNextMessageStatisticsScheduleId() {
        String messageStatisticsSchedule = "msgStaSch";
        return messageStatisticsSchedule.concat(getId());
    }
}
