package com.data.docking.mapper.primary;

import com.data.docking.domain.SwingCardRecord;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 刷卡记录mapper
 * @since 2021/4/1
 */
@Mapper
@Repository
public interface SwingCardRecordMapper {

    /**
     * 新增
     * @author BEJSON
     * @date 2021/04/01
     **/
    int insert(SwingCardRecord acsSwingCardRecord);

    /**
     * 刪除
     * @author BEJSON
     * @date 2021/04/01
     **/
    int delete(int id);

    /**
     * 更新
     * @author BEJSON
     * @date 2021/04/01
     **/
    int update(SwingCardRecord acsSwingCardRecord);

    /**
     * 查询 根据主键 id 查询
     * @author BEJSON
     * @date 2021/04/01
     **/
    SwingCardRecord load(int id);

    /**
     * 查询 分页查询
     * @author BEJSON
     * @date 2021/04/01
     **/
    List<SwingCardRecord> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     * @author BEJSON
     * @date 2021/04/01
     **/
    int pageListCount(int offset,int pagesize);

}
