package com.data.docking.domain;

import lombok.Data;

/**
 * @ClassName ResponseState
 * @Description ResponseState
 * @Author cjh
 * @Date 2021/4/20 21:27
 * @Version 1.0
 */
@Data
public class ResponseState {

    private Boolean IsSucess;

    private Boolean IsError;

    private Integer Code;

    private Integer RecordAffected;

    private String Describe;

}
