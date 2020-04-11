package com.ruoyi.project.system.domain;

import lombok.Data;

/**
 *
 * 微信用户详情
 *
 */
@Data
public class WXUserInfo {
    private String nickName;
    private String gender;
    private String language;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;

    @Override
    public String toString() {
        return "WXUserInfo{" +
                "nickName='" + nickName + '\'' +
                ", gender='" + gender + '\'' +
                ", language='" + language + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                '}';
    }
}
