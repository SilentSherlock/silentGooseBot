package com.silent.silentgoosebot.control;

import com.silent.silentgoosebot.entity.TgAccount;
import com.silent.silentgoosebot.others.MoistLifeApp;
import com.silent.silentgoosebot.others.SilentGooseBot;
import com.silent.silentgoosebot.others.base.*;
import com.silent.silentgoosebot.others.utils.ContextUtils;
import com.silent.silentgoosebot.service.TgAccountService;
import it.tdlight.jni.TdApi;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;

/**
 * Date: 2023/11/8
 * Author: SilentSherlock
 * Description: offer bot api
 */
@RestController
@Slf4j
public class BotController {

    @Resource
    private AppAccountMap appAccountMap;
    @Resource
    private TgAccountService tgAccountService;

    @RequestMapping(value = "/getAllTgAccount")
    public Result getAllTgAccount() {
        List<TgAccount> accounts = tgAccountService.getAllAccounts();
        Result result = Result.createBySuccess();
        result.getResultMap().put("tgAccounts", accounts);
        return result;
    }

    @RequestMapping(value = "/botStart", method = RequestMethod.GET)
    public void botStart() {

        DefaultBotOptions options = BotUtils.getDefaultOption();
        log.info("botToken:{}", MyPropertiesUtil.getProperty(AppConst.Tg.bot_token));
        SilentGooseBot bot = new SilentGooseBot(
                MyPropertiesUtil.getProperty(AppConst.Tg.bot_token),
                MyPropertiesUtil.getProperty(AppConst.Tg.bot_username),
                options
        );

        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 初始化tdlight-java客户端
     * @param phone state为0,1,2时必输，此时首次登录账号
     * @param waitCode state为1时必输，此时要求输入验证码
     * @param waitPassword state为2时必输，此时要求输入两步验证码
     * @param state 登录状态变换
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/appStart", method = RequestMethod.POST)
    public DeferredResult appStart(String phone, String waitCode, String waitPassword, String state) throws Exception {
        log.info("app starting phone {} waitCode {} waitPassword {} state {}", phone, waitCode, waitPassword, state);

        //构建异步返回结果
        DeferredResult<Result> deferredResult = new DeferredResult<>(50000L, "Login Timeout");

        //开始登录
        if ("0".equals(state)) {

            if (StringUtils.isEmpty(phone)) {
                log.info("phone number is blank");
                deferredResult.setResult(Result.createByFalse("phone number is blank"));
                return deferredResult;
            }

            log.info("start app with phone {}", phone);

            ForkJoinPool.commonPool().submit(() -> {

                //异步启动app
                try {

                    //启动并注册登录回调函数
                    MoistLifeApp.login(phone, update -> {
                        TdApi.AuthorizationState authorizationState = update.authorizationState;
                        log.info("MoistLifeApp authorizationState {}", authorizationState);
                        if (authorizationState instanceof TdApi.AuthorizationStateReady) {
                            log.info("user logged in, put moistLifeApp into context");
                            Result result = Result.createBySuccess("login success");
                            result.getResultMap().put("state", "3");
                            deferredResult.setResult(result);
//                        context.setMoistLifeApp(moistLifeApp);
                            // todo 添加登录完成逻辑
                        } else if (authorizationState instanceof TdApi.AuthorizationStateClosing) {
                            log.info("user closing");
                        } else if (authorizationState instanceof TdApi.AuthorizationStateClosed) {
                            log.info("user close");
                        } else if (authorizationState instanceof TdApi.AuthorizationStateLoggingOut) {
                            log.info("user logged out");
                        } else if (authorizationState instanceof TdApi.AuthorizationStateWaitCode) {
                            Result result = Result.createBySuccess("need wait code");
                            Map<String, Object> resultMap = new HashMap<>();;
                            resultMap.put("state", "1");
                            result.setResultMap(resultMap);
                            deferredResult.setResult(result);

                        } else if (authorizationState instanceof TdApi.AuthorizationStateWaitPassword) {
                            // 当状态为 AuthorizationStateWaitPassword 时，提示用户输入两步验证密码
                            Result result = Result.createBySuccess("need wait password");
                            Map<String, Object> resultMap = new HashMap<>();;
                            resultMap.put("state", "2");
                            result.setResultMap(resultMap);
                            deferredResult.setResult(result);
                        }
                    });

                    // 休眠10秒保证注册的回调函数完成
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        } else if ("1".equals(state)) {

            if (StringUtils.isEmpty(waitCode)) {
                log.info("wait code is blank");
                deferredResult.setResult(Result.createByFalse("wait code is blank"));
                return deferredResult;
            }

            //开始发送wait code, 此时tdlight-java已经初始化并放在context中
            ForkJoinPool.commonPool().submit(() ->  {
                try {
                    log.info("wait code start flying");
                    MoistLifeApp moistLifeApp = appAccountMap.getAccountMap().get(phone);
                    moistLifeApp.getClient().send(new TdApi.CheckAuthenticationCode(waitCode), result -> {
                        if (result.isError()) {
                            log.info("check authentication code failed");
                            deferredResult.setResult(Result.createByFalse("check authentication code failed"));
                        } else {
                            Result result1 = Result.createBySuccess("wait code check success");
                            result1.getResultMap().put("state", "3");
                            deferredResult.setResult(result1);
                        }
                    });
                    // 休眠2秒保证回调完成
                    log.info("wait code start sleeping");
                    Thread.sleep(5000);
                    log.info("wait code wake up");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

        } else if ("2".equals(state)) {
            if (StringUtils.isEmpty(waitPassword)) {
                log.info("wait password is blank");
                deferredResult.setResult(Result.createByFalse("wait password is blank"));
                return deferredResult;
            }

            //开始发送wait password, 此时tdlight-java已经初始化并放在context中
            ForkJoinPool.commonPool().submit(() ->  {
                try {

                    appAccountMap.getAccountMap().get(phone).getClient().send(new TdApi.CheckAuthenticationPassword(waitPassword), result -> {
                        if (result.isError()) {
                            log.info("check authentication password failed");
                            deferredResult.setResult(Result.createByFalse("check authentication password failed"));
                        } else {
                            Result result1 = Result.createBySuccess("wait password check success");
                            result1.getResultMap().put("state", "3");
                            deferredResult.setResult(result1);
                        }
                    });
                    // 休眠2秒保证回调完成
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return deferredResult;

    }
}
