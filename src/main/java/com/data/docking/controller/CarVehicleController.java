package com.data.docking.controller;

import com.data.docking.service.DataDockingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chenjianhui
 * @version V1.0
 * @description TODO
 * @since 2021/5/10
 */
@RestController
@RequestMapping("/")
public class CarVehicleController {

    @Autowired
    private DataDockingService dockingService;

    @RequestMapping("/out/vehicle")
    public String carVehicle() throws Exception {
        dockingService.syncCarOutCapture();
        return "success";
    }

}
