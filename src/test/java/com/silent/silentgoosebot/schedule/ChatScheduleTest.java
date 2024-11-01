package com.silent.silentgoosebot.schedule;

import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.base.AppAccountMap;
import it.tdlight.jni.TdApi;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

@SpringBootTest
public class ChatScheduleTest {

    private static final Logger log = LoggerFactory.getLogger(ChatScheduleTest.class);
    @Resource
    private ChatSchedule chatSchedule;
    @Resource
    private AppAccountMap appAccountMap;

    /**
     * 控制台交互启动MoistLife App
     */
    @Test
    public void startMoistLifeApp() {

        Scanner scanner = new Scanner(System.in);
        AtomicBoolean loginFlag = new AtomicBoolean(false);

        System.out.println("请输入要登录的手机号！");
        String phone = scanner.nextLine();
        MoistLifeApp.login(phone.strip(), updateAuthorizationState -> {
            TdApi.AuthorizationState state = updateAuthorizationState.authorizationState;
            if (state instanceof TdApi.AuthorizationStateReady) {
                System.out.println("登录成功！");
                loginFlag.set(true);
            } else if (state instanceof TdApi.AuthorizationStateWaitCode) {
                System.out.println("请输入验证码");
                String code = scanner.nextLine();
                appAccountMap.getAccountMap().get(phone).getClient().send(
                        new TdApi.CheckAuthenticationCode(code)
                );
            } else if (state instanceof TdApi.AuthorizationStateWaitPassword) {
                System.out.println("请输入两步验证码");
                String password = scanner.nextLine();
                appAccountMap.getAccountMap().get(phone).getClient().send(
                        new TdApi.CheckAuthenticationPassword(password)
                );
            }
        });

        while (!loginFlag.get()) {
            // 循环保证登录完成
        }
        scanner.close();
    }

    @Test
    public void JobScheduleType0Test() {
        chatSchedule.JobScheduleType0();
    }

}
