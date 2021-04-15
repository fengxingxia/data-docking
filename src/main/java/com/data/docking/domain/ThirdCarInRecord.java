package com.data.docking.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 第三方车辆进场记录
 * @since 2021/4/15
 */
@Data
public class ThirdCarInRecord {

    /**
     * 序号
     */
    private Long ID;
    /**
     * 停车标识码
     */
    private String ParkingId;
    /**
     * 凭证号
     */
    private String TokenNo;
    /**
     * 凭证类别
     */
    private Integer tokenType;
    /**
     * 所属车场
     */
    private String ParkId;
    /**
     * 卡类Rid
     */
    private String TcmId;
    /**
     * 车类名称
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
     * 车标
     */
    private String VehicleBand;
    /**
     * 车颜色
     */
    private String VehicleColor;
    /**
     * 车类别
     */
    private String VehicleCategory;
    /**
     * 车牌颜色
     */
    private String PlateColor;
    /**
     * 入场车道
     */
    private String InLaneId;
    /**
     * 入场名称
     */
    private String InLaneName;
    /**
     * 入场时间
     */
    private Date InTime;
    /**
     * 入场图片
     */
    private String InPicture;
    /**
     * 入场图片2
     */
    private String InPicture2;
    /**
     * 入场人图
     */
    private String InPictureStaff;
    /**
     * 入场操作员编号
     */
    private String InOperatorId;
    /**
     * 入场操作员姓名
     */
    private String InOperatorName;
    /**
     * 入场类型
     */
    private Integer InType;
    /**
     * 数据标识
     */
    private Integer InFlag;
    /**
     * 车位满位说明
     */
    private String LotFullRemark;
    /**
     * 场内车状态
     */
    private Integer State;
    /**
     * 是否开通无感支付（0未开通，1开通）
     */
    private Integer PayMark;
    /**
     * 最后收费时间
     */
    private String LastChargeTime;
    /**
     * 集团车状态
     */
    private Integer GroupLotState;
    /**
     * 预约单号
     */
    private String ReservationNo;
    /**
     * 入场备注
     */
    private String InRemark;
    /**
     * 终端号
     */
    private String TerminalId;
    /**
     * 终端名
     */
    private String TerminalName;
    /**
     * 省份
     */
    private String Province;
    /**
     * 标识号
     */
    private String rid;
    /**
     * 集团标识号
     */
    private String gid;

}
