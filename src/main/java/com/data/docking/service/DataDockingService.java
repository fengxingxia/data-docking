package com.data.docking.service;

import com.data.docking.constant.BusinessTypeConstant;
import com.data.docking.domain.DataDockingRecord;
import com.data.docking.mapper.CarCaptureMapper;
import com.data.docking.mapper.DataDockingRecordMapper;
import com.data.docking.mapper.SwingCardRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @author chenjianhui
 * @version V1.0
 * @description 数据对接service
 * @since 2021/4/2
 */
@Service
@Slf4j
public class DataDockingService {

    @Autowired
    private DataDockingRecordMapper dataDockingRecordMapper;

    @Autowired
    private CarCaptureMapper carCaptureMapper;

    @Autowired
    private SwingCardRecordMapper swingCardRecordMapper;

    /**
     * 根据类型查询对接记录
     *
     * @param type 业务数据类型
     * @return
     */
    public DataDockingRecord queryDataDocking(int type) {
        return dataDockingRecordMapper.queryByType(type);
    }

    /**
     * 构建对接记录
     *
     * @return
     */
    private DataDockingRecord createDataDocking(Long id, Integer currentSynNum, Integer type, String syncRecordPosition) {
        DataDockingRecord dataDockingRecord = new DataDockingRecord();
        dataDockingRecord.setId(id);
        dataDockingRecord.setCurrentSyncNum(currentSynNum);
        dataDockingRecord.setSyncRecordPosition(syncRecordPosition);
        dataDockingRecord.setType(type);
        dataDockingRecord.setLatestSyncTime(new Date());
        return dataDockingRecord;
    }

    /**
     * 同步过车记录
     */
    public void syncCarCapture() {
        DataDockingRecord dataDockingRecord = queryDataDocking(BusinessTypeConstant.CAR_CAPTURE);
        String syncPosition = Objects.isNull(dataDockingRecord) ? "0" : dataDockingRecord.getSyncRecordPosition();
        Integer currentSynNum = 0;
        // 查询第三方数据库

        // 更新同步记录
        if (Objects.isNull(dataDockingRecord)) {
            dataDockingRecordMapper.insert(createDataDocking(0L, currentSynNum, BusinessTypeConstant.CAR_CAPTURE, syncPosition));
        } else {
            dataDockingRecordMapper.update(createDataDocking(0L, currentSynNum, BusinessTypeConstant.CAR_CAPTURE, syncPosition));
        }
    }

}
