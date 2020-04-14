package com.ruoyi.project.system.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "sys_user_butt")
public class SysUserButt implements Serializable {

  @TableId
  private String id;
  private String name;
  private String password;
  private String phone;
  private String roles;
  private String avatarUrl;
  private Date createdTime;
  private int source;
  private String jobNumber;
  private String userId;
  private String status;

  @Override
  public String toString() {
    return "SysUserButt{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", password='" + password + '\'' +
            ", phone='" + phone + '\'' +
            ", roles='" + roles + '\'' +
            ", avatarUrl='" + avatarUrl + '\'' +
            ", createdTime=" + createdTime +
            ", source=" + source +
            ", jobNumber='" + jobNumber + '\'' +
            ", userId='" + userId + '\'' +
            ", status='" + status + '\'' +
            '}';
  }
}
