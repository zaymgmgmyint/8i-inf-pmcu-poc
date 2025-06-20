package com._i.dahua.face.scan.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class VisitorMessageDTO {
    private String method;
    private VisitorInfo info;
    private String id;

    @Data
    public static class VisitorInfo {
        private String visitorId;
        private String status; // 0 = Appointed, 1 = In visit
        private String source; // 1 = Client, 2 = Appointment
        private String registerType;
        private String registerDetail;
        private String createTime;
        private String visitorName;
        private String visitorOrgName;
        private String visitedName;
        private String visitedOrgName;
        private String idType;
        private String idNum;
        private String tel;
        private String email;
        private String expectArrivalTime;
        private String arrivalTime;
        private String expectLeaveTime;
        private String leaveTime;
        private String plateNo;
        private String reason;
        private String remark;
        private String personId;
        private AuthInfo authInfo;
    }

    @Data
    public static class AuthInfo {
        private String sipId;
        private String cardNo;
        private List<String> facePictures;
        private String passportCardNo;
        private String idPicture;
        private String qrcode;
        private RightInfo rightInfo;
    }

    @Data
    public static class RightInfo {
        private List<String> acsChannelIds;
        private List<String> vtoChannelIds;
        private List<String> positionIds;
        private List<LiftChannel> liftChannels;
    }

    @Data
    public static class LiftChannel {
        @JsonProperty("channelId")
        private String channelId;
        @JsonProperty("floors")
        private String floors;
    }
}
