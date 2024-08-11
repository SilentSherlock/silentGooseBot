package com.silent.silentgoosebot.others.base;

import java.util.Random;

public class IdGenerator {

    private static String tgAccount = "tgAcc";

    private static String getId() {
        Random rand = new Random(System.currentTimeMillis());
        StringBuffer buffer  = new StringBuffer();
        for (int i = 0; i < 10; i++) {
            buffer.append(rand.nextInt(10));
        }
        return buffer.toString();
    }

    public static String getNextTgAccountId() {
        return tgAccount.concat(getId());
    }

}
