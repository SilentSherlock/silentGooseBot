package com.silent.silentgoosebot.others;

import com.silent.silentgoosebot.others.base.AppConst;
import com.silent.silentgoosebot.others.base.MyPropertiesUtil;
import it.tdlight.Init;
import it.tdlight.client.*;
import it.tdlight.jni.TdApi;
import it.tdlight.util.UnsupportedNativeLibraryException;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Date: 2023/12/17
 * Author: SilentSherlock
 * Description: describe the file
 */
@Slf4j
public class MoistLifeAppThread implements Runnable{
    private volatile boolean runFlag;
    private MoistLifeApp app;

    public MoistLifeAppThread() {
        this.runFlag = true;
    }

    public boolean isRunFlag() {
        return runFlag;
    }

    public void setRunFlag(boolean runFlag) {
        this.runFlag = runFlag;
    }

    @Override
    public void run() {
        log.info("app start");
        try {
            Init.init();
        } catch (UnsupportedNativeLibraryException e) {
            throw new RuntimeException(e);
        }

        try (SimpleTelegramClientFactory clientFactory = new SimpleTelegramClientFactory()){
            APIToken apiToken = new APIToken(Integer.parseInt(Objects.requireNonNull(MyPropertiesUtil.getProperty(AppConst.Tg.app_api_id))),
                    MyPropertiesUtil.getProperty(AppConst.Tg.app_api_hash));

            TDLibSettings settings = TDLibSettings.create(apiToken);
            //configure session
            Path sessionPath = Paths.get("tdlib-session-57066");
            settings.setDatabaseDirectoryPath(sessionPath.resolve("data"));
            settings.setDownloadedFilesDirectoryPath(sessionPath.resolve("downloads"));

            //prepare a client builder
            TdApi.AddProxy proxy = new TdApi.AddProxy(
                    AppConst.Proxy.proxy_host,
                    AppConst.Proxy.proxy_port,
                    true,
                    new TdApi.ProxyTypeHttp()
            );

            SimpleTelegramClientBuilder builder = clientFactory.builder(settings);


            //configure authentication
            SimpleAuthenticationSupplier<?> supplier = AuthenticationSupplier.user(MyPropertiesUtil.getProperty(AppConst.Tg.user_phone_number));
//            settings.setUseTestDatacenter(true);
            try {
                app = new MoistLifeApp(builder, supplier);
                SimpleTelegramClient appClient = app.getClient();
                log.info("build proxy");
                appClient.send(proxy, result -> System.out.println("result:" + result.toString()));
                log.info("App start success");
                while (runFlag) {
                    Thread.onSpinWait();
                }
                /*TdApi.User me = appClient.getMeAsync().get(1, TimeUnit.MINUTES);
                TdApi.SendMessage req = new TdApi.SendMessage();*/
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
