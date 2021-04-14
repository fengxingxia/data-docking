package com.data.docking.controller;

import com.alibaba.fastjson.JSONObject;
import com.data.docking.domain.Response;
import com.data.docking.service.DataDockingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;

/**
 * @ClassName DockOpenRecordController
 * @Description 对接第三方推送开门记录的controller
 * @Author cjh
 * @Date 2021/4/14 23:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/opendoor")
@Slf4j
public class DockOpenRecordController {

    @Autowired
    private DataDockingService dockingService;

    /**
     * 接收第三方传递过来的开门记录
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/record")
    public String roomList(HttpServletRequest request ) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            if (log.isDebugEnabled()) {
                log.debug("接收到的第三方开门记录为: {}", sb.toString());
            }
            dockingService.dockingOpenDoorRecord(sb.toString());
            return JSONObject.toJSONString(Response.buildSuccess());
        } catch (Exception e) {
            log.error("接收第三方参数异常", e);
        }
        return JSONObject.toJSONString(Response.buildFail());
    }

}
