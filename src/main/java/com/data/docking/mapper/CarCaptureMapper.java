package com.data.docking.mapper;

import com.data.docking.domain.CarCapture;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CarCaptureMapper {

    /**
     * 新增
     *
     * @author BEJSON
     * @date 2021/04/01
     **/
    int insert(CarCapture pCarCapture);

    /**
     * 刪除
     *
     * @author BEJSON
     * @date 2021/04/01
     **/
    int delete(int id);

    /**
     * 更新
     *
     * @author BEJSON
     * @date 2021/04/01
     **/
    int update(CarCapture pCarCapture);

    /**
     * 查询 根据主键 id 查询
     *
     * @author BEJSON
     * @date 2021/04/01
     **/
    CarCapture load(int id);

    /**
     * 查询 分页查询
     *
     * @author BEJSON
     * @date 2021/04/01
     **/
    List<CarCapture> pageList(int offset, int pagesize);

    /**
     * 查询 分页查询 count
     *
     * @author BEJSON
     * @date 2021/04/01
     **/
    int pageListCount(int offset, int pagesize);

}