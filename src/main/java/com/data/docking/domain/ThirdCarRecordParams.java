package com.data.docking.domain;

import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
    private Integer PageSize;
    /**
     * 当前页数
     */
    private Integer CurrentPage;
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
    private Integer TotalCount;

    public Map<String, String> buildMap() {
        Map<String, String> params = new HashMap<>();
        params.put("PageSize", String.valueOf(this.getPageSize()));
        params.put("CurrentPage", String.valueOf(this.getCurrentPage()));
        if (StringUtils.isNotBlank(this.getOrderBy())) {
            params.put("OrderBy", this.getOrderBy());
        }
        if (StringUtils.isNotBlank(this.getOrderType())) {
            params.put("OrderType", this.getOrderType());
        }
        if (StringUtils.isNotBlank(this.getWhere())) {
            params.put("where", this.getWhere());
        }
        if (StringUtils.isNotBlank(this.getOrderBy())) {
            params.put("Append", this.getAppend());
        }
        if (this.getTotalCount() != null) {
            params.put("TotalCount", String.valueOf(this.getTotalCount()));
        }
        return params;
    }

}
