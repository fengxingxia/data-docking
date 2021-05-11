package com.data.docking.domain;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName FcjnCarOutVehicle
 * @Description 翡翠江南车辆出场记录
 * @Author cjh
 * @Date 2021/5/9 19:14
 * @Version 1.0
 */
@Data
public class FcjnCarOutVehicle {

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

    private Date InTime;

    /**
     * 出场识别车牌
     */
    private String OutAutoPlate;

    /**
     * 出场名称
     */
    private String OutLaneName;

    /**
     * 出场时间
     */
    private Date OutTime;

    /**
     * 出场图片
     */
    private String OutPicture;

    private String OutPicture2;

    /**
     * 出场人图
     */
    private String OutPictureStaff;

    /**
     * 0正常进出，1手动开闸，2非法开闸
     */
    private Integer OutType;



}
