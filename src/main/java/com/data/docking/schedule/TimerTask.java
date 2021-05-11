package com.data.docking.schedule;

import com.data.docking.service.DataDockingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 定时任务
 * @since 2021/4/21
 */
@Component
@Slf4j
@PropertySource(value = "classpath:application.properties")
public class TimerTask {

    @Autowired
    private DataDockingService dockingService;

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Scheduled(cron = "${car.capture.data.docking.cron}")
    public void syncCarCapture() {
        executorService.execute(() -> {
            try {
                dockingService.syncCarOutCapture();
            } catch (Exception e) {
                log.error("同步车辆过车记录异常", e);
            }
        });

    }

    @Scheduled(cron = "${swing.card.data.docking.cron}")
    public void syncSwingCardRecord() {
        executorService.execute(() -> {
            try {
                dockingService.dockingOpenDoorRecord();
            } catch (Exception e) {
                log.error("同步刷卡记录异常", e);
            }
        });

    }

}
