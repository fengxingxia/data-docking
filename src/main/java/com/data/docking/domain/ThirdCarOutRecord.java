package com.data.docking.domain;

import lombok.Data;

import java.util.Date;

/**
 * @ClassName ThirdCarOutRecord
 * @Description 第三方车辆出场记录
 * @Author cjh
 * @Date 2021/4/15 23:16
 * @Version 1.0
 */
@Data
public class ThirdCarOutRecord {

    /**
     * 编号（数据库自增）
     */
    private String ID;
    /**
     * 主键，记录唯一标识码
     */
    private String ParkingId;
    /**
     * 凭证号
     */
    private String TokenNo;
    /**
     * 凭证类别
     */
    private String TokenType;
    /**
     * 车场标识码
     */
    private String ParkId;
    /**
     * 业务rid
     */
    private String TcmId;
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
     * 入场识别车牌
     */
    private String InAutoPlate;
    /**
     * 入场名称
     */
    private String InLaneName;
    /**
     * 入场车道标识号
     */
    private String InLaneId;
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
     * 组车状态
     */
    private Integer GroupLotState;
    /**
     * 预约单号
     */
    private String ReservationNo;
    /**
     * 车位满位说明
     */
    private String LotFullRemark;
    /**
     * 入场终端号
     */
    private String InTerminalId;
    /**
     * 入场终端名称
     */
    private String InTerminalName;
    /**
     * 入场备注
     */
    private String InRemark;
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
    /**
     * 出场图片2
     */
    private String OutPicture2;
    /**
     * 出场人图
     */
    private String OutPictureStaff;
    /**
     * 出场操作员编号
     */
    private String OutOperatorId;
    /**
     * 出场操作员姓名
     */
    private String OutOperatorName;
    /**
     * 出场类型
     */
    private Integer OutType;
    /**
     * 出场备注
     */
    private String OutRemark;
    /**
     * 数据标识
     */
    private Integer OutFlag;
    /**
     * 出场车道
     */
    private String OutLaneId;
    /**
     * 停车时长
     */
    private String StayLasts;
    /**
     * 终端号
     */
    private String TerminalId;
    /**
     * 终端名称
     */
    private String TerminalName;
    /**
     * 应收金额
     */
    private String TranAmount;
    /**
     * 实收金额
     */
    private String CashAmount;
    /**
     * 人工免费
     */
    private String FreeAmount;
    /**
     * 金额抵扣
     */
    private String DeductedAmount;
    /**
     * 小时金额免费
     */
    private String DeductedHoursAmount;
    /**
     * 抵扣小时
     */
    private String DeductedHours;
    /**
     * 省份
     */
    private String Province;
    /**
     * 标识号
     */
    private String Rid;
    /**
     * 集团标识号
     */
    private String Gid;

}
