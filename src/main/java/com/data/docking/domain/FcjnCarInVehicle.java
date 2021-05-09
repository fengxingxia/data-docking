package com.data.docking.domain;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName FcjnCarOutVehicle
 * @Description 翡翠江南车辆出场记录
 * @Author cjh
 * @Date 2021/5/8 22:20
 * @Version 1.0
 */
@Data
public class FcjnCarInVehicle {

    /**
     * ID 自增
     */
    private Long Id;

    /**
     * 停车场标识码
     */
    private String ParkingId;

    /**
     * 凭证号
     */
    private String TokenNo;

    /**
     * 0 车牌 1 卡片 2 ETC
     */
    private Integer TokenType;

    /**
     * 所属车场
     */
    private Long ParkId;

    /**
     * 业务类型名
     */
    private String TcmName;

    /**
     * 人员编号
     */
    private String StaffNo;

    /**
     * 人员姓名
     */
    private String StaffName;

    /**
     * 车牌号码
     */
    private String RegPlate;

    /**
     * 自动识别车牌
     */
    private String InAutoPlate;

    /**
     * 入场名称
     */
    private String InLaneName;

    /**
     * 入场人图
     */
    private String InPicture;

    /**
     * 入场人图
     */
    private String InPicture2;

    /**
     * 入场人图
     */
    private String InPictureStaff;

    /**
     * 进场时间
     */
    private Date InTime;

}
