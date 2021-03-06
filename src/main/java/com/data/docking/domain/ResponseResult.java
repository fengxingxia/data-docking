package com.data.docking.domain;

import lombok.Data;

import java.util.List;

/**
 * @ClassName ResponseResult
 * @Description θΏεη»ζ
 * @Author cjh
 * @Date 2021/4/20 21:29
 * @Version 1.0
 */
@Data
public class ResponseResult<T> {

    private ThirdCarRecordParams PageAttri;

    private ResponseState State;

    private List<T> Records;

}
