package com.data.docking.mapper.primary;

import com.data.docking.domain.DataDockingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DataDockingRecordMapper {

    /**
     * 新增
     *
     * @author BEJSON
     * @date 2021/04/02
     **/
    int insert(DataDockingRecord dataDockingRecord);

    /**
     * 刪除
     *
     * @author BEJSON
     * @date 2021/04/02
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author BEJSON
     * @date 2021/04/02
     **/
    int update(DataDockingRecord dataDockingRecord);

    /**
     * 查询 根据主键 id 查询
     *
     * @author BEJSON
     * @date 2021/04/02
     **/
    DataDockingRecord load(int id);

    /**
     * 查询 分页查询
     *
     * @author BEJSON
     * @date 2021/04/02
     **/
    List<DataDockingRecord> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author BEJSON
     * @date 2021/04/02
     **/
    int pageListCount(int offset, int pagesize);

    /**
     * 根据类型查询同步记录
     *
     * @param type
     * @return
     */
    DataDockingRecord queryByType(@Param("type") int type, @Param("areaName") String areaName);

}