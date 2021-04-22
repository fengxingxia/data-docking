package com.data.docking.schedule;

import com.data.docking.service.DataDockingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 定时任务
 * @since 2021/4/21
 */
@Component
@Slf4j
public class TimerTask {

    @Autowired
    private DataDockingService dockingService;

    @Scheduled(cron = "${car.capture.data.docking.cron}")
    public void syncCarCapture() {
        log.info("开始同步过车记录数据");
        try {
            dockingService.syncCarInCapture();
            dockingService.syncCarOutCapture();
        } catch (Exception e) {
            log.error("同步车辆过车记录异常", e);
        }
    }

}
