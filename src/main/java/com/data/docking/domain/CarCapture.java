package com.data.docking.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 过车记录
 * @since 2021/4/1
 */
@Data
public class CarCapture {

    /**
     * 主键
     */
    private Long id;

    /**
     * 设备id
     */
    private String devId;

    /**
     * 设备通道id
     */
    private String devChnid;

    /**
     * 通道序号
     */
    private Integer devChnnum;

    /**
     * 设备名称
     */
    private String devName;

    /**
     * 通道名称
     */
    private String devChnname;

    /**
     * 车牌号
     */
    private String carNum;

    /**
     * 车牌类型
     */
    private Integer carNumtype;

    /**
     * 车牌颜色
     */
    private Integer carNumcolor;

    /**
     * 车速
     */
    private Integer carSpeed;

    /**
     * 车型
     */
    private Integer carType;

    /**
     * 车身颜色
     */
    private Integer carColor;

    /**
     * 车长
     */
    private Integer carLength;

    /**
     * 行车方向
     */
    private String carDirect;

    /**
     * 车道编号
     */
    private String carWayCode;

    /**
     * 车辆商标
     */
    private Integer carBrand;

    /**
     * 抓图时间
     */
    private Date capTime;

    /**
     * 备注信息
     */
    private String infNote;

    /**
     * 最高限制速度
     */
    private Integer maxSpeed;

    /**
     * 最低限制速度
     */
    private Integer minSpeed;

    /**
     * 图片存放路径
     */
    private String carImgUrl;

    /**
     * 车牌小图片url
     */
    private String carNumPic;

    /**
     * 记录状态
     */
    private Integer recStat;

    /**
     * 记录图片是否要永久保存
     */
    private Integer saveFlag;

    /**
     * dataclean程序使用表示，是否已清除图片
     */
    private Integer dcCleanflag;

    /**
     * 车牌在图片中的坐标，上右下左
     */
    private String imgPlate;

    /**
     * 车辆抓拍信息校验时的信息
     */
    private String verifyMemo;

    /**
     * 车辆进出类型(见字典)
     */
    private String carInnerCategory;

    /**
     * 场区的code
     */
    private String parkingLotCode;

    /**
     * 入场记录id
     */
    private Long enterId;

    /**
     * 具体的车辆类型公交车、面包车等
     */
    private String parkingCarType;

    /**
     * 车系:迪a6l，由于车系较多，sdk实现时透传此字段，设备如实填写
     */
    private String parkingCarSubType;

    /**
     * 车辆特征值
     */
    private String parkingCarCbirfeature;

    /**
     * 车头方向 0:未知 1:左 2:中 3:右
     */
    private int vehicleHeadDirection;

    /**
     * 车身颜色 white ， black ， red ， yellow ， gray ， blue ， green
     */
    private String parkingCarColor;

    /**
     * 车标
     */
    private String parkingCarLogo;

    /**
     * 子系统ip
     */
    private String subSystemIp;

    /**
     * 子系统id
     */
    private Long subSystemId;

    /**
     * 道闸状态：0-未开闸等待命令开闸，1-白名单已开闸
     */
    private int strobeState;

}
