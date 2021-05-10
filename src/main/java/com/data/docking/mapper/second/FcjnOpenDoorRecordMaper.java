package com.data.docking.mapper.second;

import com.data.docking.domain.FcjnOpenGateRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 翡翠江南开门记录
 * @since 2021/5/10
 */
@Mapper
@Repository
public interface FcjnOpenDoorRecordMaper {

    /**
     * 查询开门记录
     *
     * @param id
     * @return
     */
    List<FcjnOpenGateRecord> listOpenDoorRecord(@Param("id") Long id);

}
