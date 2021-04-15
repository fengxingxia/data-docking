package com.data.docking.domain;

import lombok.Data;

/**
 * @ClassName ThirdCarRecordParams
 * @Description 车辆记录查询参数
 * @Author cjh
 * @Date 2021/4/15 20:47
 * @Version 1.0
 */
@Data
public class ThirdCarRecordParams {

    /**
     * 分页条数
     */
    private String PageSize;
    /**
     * 当前页数
     */
    private String CurrentPage;
    /**
     * 排序
     */
    private String OrderBy;
    /**
     * 排序类型
     */
    private String OrderType;
    /**
     * 查询逻辑条件
     */
    private String where;
    /**
     * 附加
     */
    private String Append;
    /**
     * 总条数
     */
    private String TotalCount;

}
