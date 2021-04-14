package com.data.docking.service;

import com.alibaba.fastjson.JSONObject;
import com.data.docking.constant.BusinessTypeConstant;
import com.data.docking.domain.DataDockingRecord;
import com.data.docking.domain.Response;
import com.data.docking.domain.SwingCardRecord;
import com.data.docking.domain.ThirdPartOpenDoorRecord;
import com.data.docking.mapper.CarCaptureMapper;
import com.data.docking.mapper.DataDockingRecordMapper;
import com.data.docking.mapper.SwingCardRecordMapper;
import com.data.docking.util.HttpClientPoolUtil;
import com.data.docking.util.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
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

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 对接第三方开门记录
     *
     * @param record 第三方开门记录
     * @return 处理结果
     */
    public Response dockingOpenDoorRecord(String record) throws Exception {
        if (null == record || record.length() == 0) {
            log.error("接收到的第三方开门记录为空");
            return Response.buildSuccess();
        }
        ThirdPartOpenDoorRecord openDoorRecord = JSONObject.parseObject(record, ThirdPartOpenDoorRecord.class);
        SwingCardRecord swingCardRecord = buildSwingCardRecord(openDoorRecord);
        swingCardRecordMapper.insert(swingCardRecord);

        return Response.buildSuccess();
    }

    /**
     * 构建刷卡记录
     *
     * @param openDoorRecord 第三方刷卡记录
     * @return
     * @throws ParseException
     */
    private SwingCardRecord buildSwingCardRecord(ThirdPartOpenDoorRecord openDoorRecord) throws Exception {
        SwingCardRecord swingCardRecord = new SwingCardRecord();
        swingCardRecord.setRecordNo(openDoorRecord.getOpendoorRecordId());
        swingCardRecord.setDeviceCode(openDoorRecord.getUniqueNumber());
        swingCardRecord.setOpenType(parseOpenType(openDoorRecord.getOpenType()));
        swingCardRecord.setSwingTime(sdf.parse(openDoorRecord.getOpenTime()));
        swingCardRecord.setCardNumber(openDoorRecord.getCardParams().getCardNumber());
        String picture1 = openDoorRecord.getFaceParams().getFaceUrl();
        byte[] imageBytes = HttpClientPoolUtil.getResponseBytes(picture1, null);
        if (imageBytes.length > 0) {
            picture1 = OssUtil.getImgUrl(Base64.encodeBase64String(imageBytes));
        }
        swingCardRecord.setPicutre1(picture1);
        // TODO 开门结果待确认
        swingCardRecord.setOpenResult(1);
        swingCardRecord.setCreateTime(sdf.parse(sdf.format(new Date())));
        swingCardRecord.setUpdateTime(sdf.parse(sdf.format(new Date())));
        return swingCardRecord;
    }

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
            dataDockingRecordMapper.update(createDataDocking(dataDockingRecord.getId(), currentSynNum,
                    BusinessTypeConstant.CAR_CAPTURE, syncPosition));
        }
    }

    /**
     * 解析开门方式
     *
     * @return
     */
    private Integer parseOpenType(Integer thirdOpenType) {
        if (thirdOpenType == null) {
            return 1;
        }
        // TODO 开门方式待完善
        return thirdOpenType;
    }

}
