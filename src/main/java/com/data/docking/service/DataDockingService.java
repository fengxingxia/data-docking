package com.data.docking.service;

import com.alibaba.fastjson.JSONObject;
import com.data.docking.constant.BusinessTypeConstant;
import com.data.docking.domain.*;
import com.data.docking.mapper.CarCaptureMapper;
import com.data.docking.mapper.DataDockingRecordMapper;
import com.data.docking.mapper.SwingCardRecordMapper;
import com.data.docking.util.HttpClientPoolUtil;
import com.data.docking.util.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
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
    private CarCaptureMapper carCaptureMapper;

    @Autowired
    private SwingCardRecordMapper swingCardRecordMapper;

    @Value("${car.inout.host.port}")
    private String thirdHost;

    @Value("${swing.card.device.code}")
    private String swingCardDeviceCode;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private static final String IN_CAR_URL = "/InVehicle/GetByCustom";

    private static final String OUT_CAR_URL = "/OutVehicle/GetByFunc";

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
        swingCardRecord.setDeviceCode(swingCardDeviceCode);
        swingCardRecord.setOpenType(parseOpenType(openDoorRecord.getOpenType()));
        swingCardRecord.setSwingTime(sdf.parse(openDoorRecord.getOpenTime()));
        swingCardRecord.setCardNumber(Objects.nonNull(openDoorRecord.getCardParams())
                ? openDoorRecord.getCardParams().getCardNumber() : "");
        String picture1 = openDoorRecord.getFaceParams().getFaceUrl();
        byte[] imageBytes = HttpClientPoolUtil.getResponseBytes(picture1, null);
        if (imageBytes.length > 0) {
            picture1 = OssUtil.getImgUrl(Base64.encodeBase64String(imageBytes));
        }
        swingCardRecord.setPicutre1(picture1);
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
     * 同步车辆进场记录
     */
    public void syncCarInCapture() throws Exception {
        DataDockingRecord dataDockingRecord = queryDataDocking(BusinessTypeConstant.CAR_CAPTURE_IN);
        String syncPosition = Objects.isNull(dataDockingRecord) ? "0" : dataDockingRecord.getSyncRecordPosition();
        Integer currentSynNum = 0;
        // 调用第三方接口
        String url = thirdHost + IN_CAR_URL;
        ThirdCarRecordParams params = buildParams(dataDockingRecord);
        String result = HttpClientPoolUtil.post(url, JSONObject.toJSONString(params), new HashMap<>());
        if (StringUtils.isNotBlank(result)) {
            ResponseResult<ThirdCarInRecord> responseResult = JSONObject.parseObject(result, ResponseResult.class);
            if (Objects.nonNull(responseResult.getState()) && responseResult.getState().getIsSucess()) {
                // 解析入场记录
                List<ThirdCarInRecord> records = responseResult.getRecords();
                saveCarInCapture(records);
                ThirdCarInRecord lastRecord = records.get(records.size() - 1);
                if (Objects.nonNull(lastRecord.getID())) {
                    syncPosition = String.valueOf(lastRecord.getID());
                }
            }
        }
        // 更新同步记录
        if (Objects.isNull(dataDockingRecord)) {
            dataDockingRecordMapper.insert(createDataDocking(0L, currentSynNum, BusinessTypeConstant.CAR_CAPTURE_IN, syncPosition));
        } else {
            dataDockingRecordMapper.update(createDataDocking(dataDockingRecord.getId(), currentSynNum,
                    BusinessTypeConstant.CAR_CAPTURE_IN, syncPosition));
        }
    }

    /**
     * 同步车辆进场记录
     */
    public void syncCarOutCapture() throws Exception {
        DataDockingRecord dataDockingRecord = queryDataDocking(BusinessTypeConstant.CAR_CAPTURE_OUT);
        String syncPosition = Objects.isNull(dataDockingRecord) ? "0" : dataDockingRecord.getSyncRecordPosition();
        Integer currentSynNum = 0;
        // 调用第三方接口
        String url = thirdHost + OUT_CAR_URL;
        ThirdCarRecordParams params = buildParams(dataDockingRecord);
        String result = HttpClientPoolUtil.post(url, JSONObject.toJSONString(params), new HashMap<>());
        if (StringUtils.isNotBlank(result)) {
            ResponseResult<ThirdCarOutRecord> responseResult = JSONObject.parseObject(result, ResponseResult.class);
            if (Objects.nonNull(responseResult.getState()) && responseResult.getState().getIsSucess()) {
                // 保存入场记录
                List<ThirdCarOutRecord> records = responseResult.getRecords();
                saveCarOutCapture(records);
                ThirdCarOutRecord lastRecord = records.get(records.size() - 1);
                if (Objects.nonNull(lastRecord.getID())) {
                    syncPosition = String.valueOf(lastRecord.getID());
                }
            }
        }
        // 更新同步记录
        if (Objects.isNull(dataDockingRecord)) {
            dataDockingRecordMapper.insert(createDataDocking(0L, currentSynNum, BusinessTypeConstant.CAR_CAPTURE_OUT, syncPosition));
        } else {
            dataDockingRecordMapper.update(createDataDocking(dataDockingRecord.getId(), currentSynNum,
                    BusinessTypeConstant.CAR_CAPTURE_IN, syncPosition));
        }
    }

    /**
     * 构建车辆进场记录
     *
     * @param records
     * @return
     */
    public void saveCarInCapture(List<ThirdCarInRecord> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        records.forEach(record -> {
            CarCapture carCapture = new CarCapture();
            carCapture.setDevId(record.getTerminalId());
            carCapture.setDevChnid(record.getInLaneId());
            carCapture.setDevChnnum(0);
            carCapture.setDevChnname(record.getTerminalName());
            carCapture.setCarNum(record.getRegPlate());
            // TODO 车牌类型
            carCapture.setCarNumtype(null);
            // TODO 车牌颜色
//            carCapture.setCarNumcolor(record.getPlateColor());
            carCapture.setCarDirect("1");
            carCapture.setCarWayCode(record.getInLaneId());
            carCapture.setCapTime(record.getInTime());
            try {
                carCapture.setCarImgUrl(OssUtil.uploadAndGetImgUrl(record.getInPicture()));
            } catch (Exception e) {
                log.error("上传车辆抓拍图片到oss异常", e);
            }
            carCapture.setParkingLotCode(record.getParkingId());
            carCapture.setVehicleHeadDirection(0);
            carCapture.setParkingCarColor(record.getVehicleColor());
            carCaptureMapper.insert(carCapture);
        });

    }

    /**
     * 构建车辆出场记录
     *
     * @param records
     * @return
     */
    public void saveCarOutCapture(List<ThirdCarOutRecord> records) {
        if (CollectionUtils.isEmpty(records)) {
            return;
        }
        records.forEach(record -> {
            CarCapture carCapture = new CarCapture();
            carCapture.setDevId(record.getTerminalId());
            carCapture.setDevChnid(record.getInLaneId());
            carCapture.setDevChnnum(0);
            carCapture.setDevChnname(record.getTerminalName());
            carCapture.setCarNum(record.getRegPlate());
            // TODO 车牌类型
            carCapture.setCarNumtype(null);
            // TODO 车牌颜色
//            carCapture.setCarNumcolor(record.getPlateColor());
            carCapture.setCarDirect("1");
            carCapture.setCarWayCode(record.getInLaneId());
            carCapture.setCapTime(record.getInTime());
            try {
                carCapture.setCarImgUrl(OssUtil.uploadAndGetImgUrl(record.getInPicture()));
            } catch (Exception e) {
                log.error("上传车辆抓拍图片到oss异常", e);
            }
            carCapture.setParkingLotCode(record.getParkingId());
            carCapture.setVehicleHeadDirection(0);
            carCapture.setParkingCarColor(record.getVehicleColor());
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
    private Integer parseOpenType(Integer thirdOpenType) {
        return 1;
    }

}
