package com.data.docking.domain;

import lombok.Data;

/**
 * @ClassName Response
 * @Description 返回对象
 * @Author cjh
 * @Date 2021/4/14 23:30
 * @Version 1.0
 */
@Data
public class Response {

    private String result;

    private String message;

    public static Response buildSuccess() {
        Response response = new Response();
        response.setMessage("SUCCESS");
        response.setResult("0");
        return response;
    }

    public static Response buildFail() {
        Response response = new Response();
        response.setMessage("业务处理失败");
        response.setResult("1");
        return response;
    }

}
