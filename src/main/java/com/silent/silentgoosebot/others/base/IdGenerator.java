package com.silent.silentgoosebot.others.base;

import java.security.SecureRandom;

public class IdGenerator {

    private static String getId() {
        SecureRandom rand = new SecureRandom();
        StringBuffer buffer  = new StringBuffer();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 10; i++) {
            buffer.append(chars.charAt(rand.nextInt(chars.length())));
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

    public static String getNextTeacherMessageSequenceId() {
        String teacherMessageSequence = "teaMsgSeq";
        return teacherMessageSequence.concat(getId());
    }

    public static String getNextTeacherId() {
        String teacher = "tea";
        return teacher.concat(getId());
    }
}
