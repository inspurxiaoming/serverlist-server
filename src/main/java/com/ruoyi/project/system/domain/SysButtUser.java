package com.ruoyi.project.system.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.ruoyi.framework.aspectj.lang.annotation.Excel.Type;
import com.ruoyi.framework.aspectj.lang.annotation.Excels;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * 微信用户对象 SysWXUser
 *
 * @author chengyongming
 */
@Data
public class SysButtUser extends BaseEntity {
    private String code;
    private String encryptedData;
    private String iv;
    private WXUserInfo userInfo;
    private String rawData;
    private String userId;
    private String channel;

    @Override
    public String toString() {
        return "SysButtUser{" +
                "code='" + code + '\'' +
                ", encryptedData='" + encryptedData + '\'' +
                ", iv='" + iv + '\'' +
                ", wxUserInfo=" + userInfo +
                ", rawData='" + rawData + '\'' +
                ", userId='" + userId + '\'' +
                ", channel='" + channel + '\'' +
                '}';
    }
}
