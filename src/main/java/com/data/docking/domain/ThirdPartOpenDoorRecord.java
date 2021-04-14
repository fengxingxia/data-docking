package com.data.docking.domain;

import lombok.Data;

/**
 * @ClassName ThirdPartOpenDoorRecord
 * @Description 第三方开门记录
 * @Author cjh
 * @Date 2021/4/14 23:13
 * @Version 1.0
 */
@Data
public class ThirdPartOpenDoorRecord {

    /**
     * 开门记录ID
     */
    private Long opendoorRecordId;

    /**
     * 机构ID
     */
    private Long orgId;

    /**
     * 设备ID，代表哪个设备开门
     */
    private Long equipmentId;

    /**
     * 设备唯一码
     */
    private String uniqueNumber;

    /**
     * 开门方式：1.密码，2.刷脸，3.刷卡，4.二维码，5.按钮，6.远程（中心机、室内机、移动端）
     */
    private Integer openType;

    /**
     * 开门时间
     */
    private String openTime;

    /**
     * 密码开门的参数
     */
    private PasswordParams passwordParams;

    /**
     * 刷脸开门的参数
     */
    private FaceParams faceParams;

    /**
     * 刷开开门参数
     */
    private CardParams cardParams;

    /**
     * 二维码开门参数
     */
    private QrCodeParams qrCodeParams;

    /**
     * 远程开门参数
     */
    private RemoteParams remoteParams;

    public static class PasswordParams {

        /**
         * 哪个户室的密码,为0的时候是使用通用密码
         */
        private String subOrgId;

        /**
         * 输入的密码
         */
        private String password;

        public String getSubOrgId() {
            return subOrgId;
        }

        public void setSubOrgId(String subOrgId) {
            this.subOrgId = subOrgId;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class FaceParams {

        /**
         * userId
         */
        private String personId;

        /**
         * 代表人员类型
         */
        private String relation;

        /**
         * 抓拍图片地址
         */
        private String faceUrl;

        public String getPersonId() {
            return personId;
        }

        public void setPersonId(String personId) {
            this.personId = personId;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getFaceUrl() {
            return faceUrl;
        }

        public void setFaceUrl(String faceUrl) {
            this.faceUrl = faceUrl;
        }
    }

    public static class CardParams {

        private String cardId;

        private String cardNumber;

        public String getCardId() {
            return cardId;
        }

        public void setCardId(String cardId) {
            this.cardId = cardId;
        }

        public String getCardNumber() {
            return cardNumber;
        }

        public void setCardNumber(String cardNumber) {
            this.cardNumber = cardNumber;
        }
    }

    public static class QrCodeParams {
        private String subOrgId;

        public String getSubOrgId() {
            return subOrgId;
        }

        public void setSubOrgId(String subOrgId) {
            this.subOrgId = subOrgId;
        }
    }

    public static class RemoteParams {
        /**
         * 1.中心机，2.室内机，3.移动端
         */
        private Integer remoteType;

        public Integer getRemoteType() {
            return remoteType;
        }

        public void setRemoteType(Integer remoteType) {
            this.remoteType = remoteType;
        }
    }

}
