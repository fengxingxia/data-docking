package com.data.docking.mapper.second;

import com.data.docking.domain.FcjnCarInVehicle;
import com.data.docking.domain.FcjnCarOutVehicle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName FcjnCarOutVehicleMapper
 * @Description 翡翠江南车辆出场记录
 * @Author cjh
 * @Date 2021/5/9 19:54
 * @Version 1.0
 */
@Mapper
@Repository
public interface FcjnCarVehicleMapper {

    /**
     * 查询车辆出场记录
     *
     * @param id
     * @return
     */
    List<FcjnCarOutVehicle> listOutVehicle(@Param("id") Long id);

    /**
     * 查询车辆进场记录
     *
     * @param id
     * @return
     */
    List<FcjnCarInVehicle> listInVehicle(@Param("id") Long id);

}
