package com.data.docking.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DataDockingRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
    * 主键
    */
    private Long id;

    /**
    * 数据类型，1.刷卡，2.过车
    */
    private int type;

    /**
    * 同步到哪里标记
    */
    private String syncRecordPosition;

    /**
    * 最新一次同步时间
    */
    private Date latestSyncTime;

    /**
    * 本次同步数量
    */
    private Integer currentSyncNum;

    /**
    * 同步总量
    */
    private Long syncTotal;

    /**
     * 当前查询到哪个页面
     */
    private Integer currentPage;

    private String areaName = "翡翠江南";

}