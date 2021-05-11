package com.data.docking.service;

import com.data.docking.constant.BusinessTypeConstant;
import com.data.docking.domain.*;
import com.data.docking.mapper.primary.CarCaptureMapper;
import com.data.docking.mapper.primary.DataDockingRecordMapper;
import com.data.docking.mapper.primary.SwingCardRecordMapper;
import com.data.docking.mapper.second.FcjnCarVehicleMapper;
import com.data.docking.mapper.second.FcjnOpenDoorRecordMaper;
import com.data.docking.util.HttpClientPoolUtil;
import com.data.docking.util.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    private FcjnCarVehicleMapper fcjnCarVehicleMapper;

    @Autowired
    private FcjnOpenDoorRecordMaper fcjnOpenDoorRecordMaper;

    @Autowired
    private CarCaptureMapper carCaptureMapper;

    @Autowired
    private SwingCardRecordMapper swingCardRecordMapper;

    @Value("${car.inout.host.port}")
    private String thirdHost;

    @Value("${swing.card.device.code}")
    private String swingCardDeviceCode;

    @Value("${car.capture.in.device.code}")
    private String carCaptureInDeviceCode;

    private String carCaptureInChannelName = "翡翠江南_车闸设备";

    @Value("${car.capture.out.device.code}")
    private String carCaptureOutDeviceCode;

    @Value("${oss.ip}")
    private String ossIp;

    private String carCaptureOutChannelName = "翡翠江南_车闸设备";

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 对接第三方开门记录
     *
     * @param record 第三方开门记录
     * @return 处理结果
     */
    public void dockingOpenDoorRecord() throws Exception {
        log.info("开始同步刷卡记录");
        DataDockingRecord dataDockingRecord = queryDataDocking(BusinessTypeConstant.SWING_CARD_RECORD);
        String syncPosition = Objects.isNull(dataDockingRecord) ? "0" : dataDockingRecord.getSyncRecordPosition();
        Integer currentSynNum = 0;
        // 查询第三方数据库
        List<FcjnOpenGateRecord> fcjnOpenGateRecords = fcjnOpenDoorRecordMaper.listOpenDoorRecord(Long.parseLong(syncPosition));

        saveSwingCardRecord(fcjnOpenGateRecords);

        // 更新同步记录
        if (Objects.isNull(dataDockingRecord)) {
            dataDockingRecordMapper.insert(createDataDocking(0L, currentSynNum, BusinessTypeConstant.SWING_CARD_RECORD, syncPosition));
        } else {
            syncPosition = String.valueOf(fcjnOpenGateRecords.get(fcjnOpenGateRecords.size() - 1).getId());
            dataDockingRecordMapper.update(createDataDocking(dataDockingRecord.getId(), currentSynNum,
                    BusinessTypeConstant.SWING_CARD_RECORD, syncPosition));
        }
        log.info("刷卡记录同步完成，本次同步: {}条", fcjnOpenGateRecords.size());
    }

    /**
     * 构建刷卡记录
     *
     * @param openDoorRecord 第三方刷卡记录
     * @return
     * @throws ParseException
     */
    private void saveSwingCardRecord(List<FcjnOpenGateRecord> fcjnOpenGateRecords) throws Exception {
        for (FcjnOpenGateRecord record : fcjnOpenGateRecords) {
            SwingCardRecord swingCardRecord = new SwingCardRecord();
            swingCardRecord.setRecordNo(record.getId());
            swingCardRecord.setDeviceCode(swingCardDeviceCode);
            swingCardRecord.setOpenType(parseOpenType(record.getTcmName()));
            swingCardRecord.setSwingTime(record.getOpenDate());
            swingCardRecord.setOpenResult(1);
            swingCardRecord.setCreateTime(sdf.parse(sdf.format(new Date())));
            swingCardRecord.setUpdateTime(sdf.parse(sdf.format(new Date())));
            swingCardRecord.setUnitSeq(0L);
            swingCardRecord.setEnterOrExit(1);
            swingCardRecord.setChannelCode(swingCardDeviceCode + "$7$0$0");
            swingCardRecord.setChannelName("翡翠江南_门禁设备_通道1");
            swingCardRecord.setPersonCode(record.getStaffNo());
            swingCardRecord.setPersonName(record.getStaffName());
            swingCardRecordMapper.insert(swingCardRecord);
        }
    }

    /**
     * 根据类型查询对接记录
     *
     * @param type 业务数据类型
     * @return
     */
    public DataDockingRecord queryDataDocking(int type) {
        return dataDockingRecordMapper.queryByType(type, BusinessTypeConstant.DATA_SOURCE);
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
     * 同步车辆出场记录
     */
    public void syncCarOutCapture() throws Exception {
        log.info("开始同步过车记录");
        DataDockingRecord dataDockingRecord = queryDataDocking(BusinessTypeConstant.CAR_CAPTURE_OUT);
        String syncPosition = Objects.isNull(dataDockingRecord) ? "0" : dataDockingRecord.getSyncRecordPosition();
        Integer currentSynNum = 0;
        // 调用第三方接口
        List<FcjnCarOutVehicle> fcjnCarOutVehicles = fcjnCarVehicleMapper.listOutVehicle(Long.parseLong(syncPosition));

        saveCarOutCapture(fcjnCarOutVehicles);

        saveCarInCapture(fcjnCarOutVehicles);

        // 更新同步记录
        if (Objects.isNull(dataDockingRecord)) {
            dataDockingRecordMapper.insert(createDataDocking(0L, currentSynNum, BusinessTypeConstant.CAR_CAPTURE_OUT, syncPosition));
        } else {
            syncPosition = String.valueOf(fcjnCarOutVehicles.get(fcjnCarOutVehicles.size() - 1).getId());
            dataDockingRecordMapper.update(createDataDocking(dataDockingRecord.getId(), currentSynNum,
                    BusinessTypeConstant.CAR_CAPTURE_OUT, syncPosition));
        }
        log.info("同步过车记录结束，本次共同步: {}条", fcjnCarOutVehicles.size());
    }

    /**
     * 构建车辆进场记录
     *
     * @param records
     * @return
     */
    public void saveCarInCapture(List<FcjnCarOutVehicle> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        records.forEach(record -> {
            CarCapture carCapture = new CarCapture();
            carCapture.setDevId(carCaptureInDeviceCode);
            carCapture.setDevChnid(carCaptureInDeviceCode + "$1$0$0");
            carCapture.setDevChnnum(0);
            carCapture.setDevChnname(carCaptureInChannelName);
            carCapture.setCarNum(record.getTokenNo());
            carCapture.setCarDirect("1");
            carCapture.setCapTime(record.getInTime());
            try {
                String imgUrl = OssUtil.uploadAndGetImgUrl(record.getInPicture());
                carCapture.setCarImgUrl(imgUrl);
                String carNumImg = OssUtil.uploadAndGetImgUrl(record.getInPicture2());
                carCapture.setCarNumPic(carNumImg);
            } catch (Exception e) {
                log.error("上传车辆抓拍图片到oss异常", e);
            }
            carCapture.setParkingLotCode(record.getParkingId());
            carCapture.setVehicleHeadDirection(0);
            carCaptureMapper.insert(carCapture);
        });

    }

    /**
     * 构建车辆出场记录
     *
     * @param records
     * @return
     */
    public void saveCarOutCapture(List<FcjnCarOutVehicle> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        records.forEach(record -> {
            CarCapture carCapture = new CarCapture();
            carCapture.setDevId(carCaptureOutDeviceCode);
            carCapture.setDevChnid(carCaptureOutDeviceCode + "$1$0$0");
            carCapture.setDevChnnum(0);
            carCapture.setDevChnname(carCaptureOutChannelName);
            carCapture.setCarNum(record.getTokenNo());
            carCapture.setCarDirect("9");
            carCapture.setCapTime(record.getOutTime());
            try {
                String imgUrl = OssUtil.uploadAndGetImgUrl(record.getOutPicture());
                carCapture.setCarImgUrl(imgUrl);
                String carNumImg = OssUtil.uploadAndGetImgUrl(record.getOutPicture2());
                carCapture.setCarNumPic(carNumImg);
            } catch (Exception e) {
                log.error("上传车辆抓拍图片到oss异常", e);
            }
            carCapture.setParkingLotCode(record.getParkingId());
            carCapture.setVehicleHeadDirection(0);
            carCaptureMapper.insert(carCapture);
        });

    }

    /**
     * 构建查询参数
     *
     * @param dataDockingRecord
     * @return
     */
    private ThirdCarRecordParams buildParams(DataDockingRecord dataDockingRecord) {
        ThirdCarRecordParams params = new ThirdCarRecordParams();
        params.setPageSize(100);
        params.setCurrentPage(1);
        params.setOrderBy("id");
        if (Objects.nonNull(dataDockingRecord)) {
            params.setWhere("id > " + dataDockingRecord.getSyncRecordPosition());
        }
        return params;
    }

    /**
     * 解析开门方式
     *
     * @return
     */
    private Integer parseOpenType(String tcmName) {
        if (tcmName.contains("按钮")) {
            return 49;
        }
        return 51;
    }

}
