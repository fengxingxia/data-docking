package com.data.docking.domain;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName FcjnOpenGateRecord
 * @Description 翡翠江南开门记录
 * @Author cjh
 * @Date 2021/5/9 21:03
 * @Version 1.0
 */
@Data
public class FcjnOpenGateRecord {

    /**
     * ID
     */
    private Long Id;

    private Date OpenDate;

    private String StaffNo;

    private String StaffName;

    private String OrganizationName;

    private String DevNo;

    private String DevName;

    private String TcmName;

    private Integer InOrOut;

}
