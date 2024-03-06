package com.silent.silentgoosebot.others.message;

import com.silent.silentgoosebot.entity.AutoMessageCreator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Date: 2024/2/2
 * Author: SilentSherlock
 * Description: create format message, classified by chatId
 */
@Slf4j
public class AutoMessageCreateFactory {

    /**
     * create message by reflect
     * @param creator class and method will be reflected
     * @param dataMap method params
     */
    public static Object createMessage(AutoMessageCreator creator, Map<String, Object> dataMap) {

        log.info("Create message : " + creator.toString());
        String classpath = creator.getMessageClassPath();
        String methodName = creator.getMethodName();
        Class c = null;
        try {
            c = Class.forName(classpath.trim());
        } catch (ClassNotFoundException e) {
            log.error("Class: {} not found", classpath);
            e.printStackTrace();
        }

        log.info("begin reflect method");
        Method m = null;
        if (null != c) {
            try {
                m = c.getDeclaredMethod(methodName.trim(), Map.class);
            } catch (NoSuchMethodException e) {
                log.error("Method: {} not found", methodName);
                e.printStackTrace();
            }
        }

        log.info("begin invoke");
        if (null != m) {
            try {
                return m.invoke(c.getDeclaredConstructor().newInstance(), dataMap);
            } catch (IllegalAccessException
                    | InvocationTargetException
                    | InstantiationException
                    | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * used in groups, send drink water tips
     * @return
     */
    public SendPhoto drinkWaterMessage(Map<String, Object> dataMap) {
        SendPhoto sendPhoto = new SendPhoto();
        String text = "<b><u>本群提醒喝水小助手：</u></b>" + Arrays.toString(Character.toChars(0x1F375)) + "\n"
                + "看到消息的人可以和我一起来一杯水\n"
                + "一小时后我将继续提醒大家喝水\n"
                + "和我一起成为一天8杯水的人吧！" + Arrays.toString(Character.toChars(0x1F64B));
        sendPhoto.setCaption(text);
        File photo;
        try {
            photo = ResourceUtils.getFile("classpath:static/image/drinkWater.jpg");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        sendPhoto.setPhoto(new InputFile(photo));
        sendPhoto.setParseMode(ParseMode.HTML);
        return sendPhoto;
    }

}
