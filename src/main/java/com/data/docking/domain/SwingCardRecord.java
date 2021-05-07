package com.data.docking.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 刷卡记录
 * @since 2021/4/1
 */
@Data
public class SwingCardRecord {

    /**
     * 自增id
     */
    private Long id;

    /**
     * record_no
     */
    private Long recordNo;

    /**
     * 设备编码
     */
    private String deviceCode;

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 通道编码
     */
    private String channelCode;

    /**
     * 通道名称
     */
    private String channelName;

    /**
     * 读卡器编码
     */
    private String readerCode;

    /**
     * 读卡器名称
     */
    private String readerName;

    /**
     * 开门方式，有刷卡，指纹，远程开门等
     */
    private Integer openType;

    /**
     * 读卡器业务类型，进门或者出门
     */
    private int businessType;

    /**
     * 通道所属组织编码
     */
    private String orgCode;

    /**
     * 通道所属组织名称
     */
    private String orgName;

    /**
     * 卡号
     */
    private String cardNumber;

    /**
     * 卡片类型
     */
    private int cardType;

    /**
     * 人员编码
     */
    private String personCode;

    /**
     * 人员姓名
     */
    private String personName;

    /**
     * 刷卡时间
     */
    private Date swingTime;

    /**
     * 开门结果
     */
    private int openResult;

    /**
     * 抓图1
     */
    private String picutre1;

    /**
     * 抓图2
     */
    private String picutre2;

    /**
     * 抓图3
     */
    private String picutre3;

    /**
     * picutre4
     */
    private String picutre4;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * card_status
     */
    private int cardStatus;

    /**
     * unit_seq
     */
    private Long unitSeq;

    /**
     * enter_or_exit
     */
    private int enterOrExit;

    /**
     * prev
     */
    private Long prev;

    /**
     * next
     */
    private Long next;

    /**
     * 巡检记录处理人
     */
    private String operator;

    /**
     * 巡检结果（0：正常；1：异常）
     */
    private int routeResult;

    /**
     * 备注
     */
    private String remark;

    /**
     * 证件类型，查询单个刷卡记录用到
     */
    private String paperType;

    /**
     * 证件号码，查询单个刷卡记录用到
     */
    private String paperNumber;

    /**
     * 设备型号
     */
    private String deviceModel;

    private String recordImage;

}
