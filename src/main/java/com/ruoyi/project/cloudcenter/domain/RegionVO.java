package com.ruoyi.project.cloudcenter.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.framework.web.domain.BaseEntity;
import com.ruoyi.framework.web.page.PageDomain;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RegionVO extends PageDomain implements Serializable {

  private String id;
  private Long cloudCenter;
  private String cloudCenterName;
  private String name;
  private boolean status;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updateTime;
  private Long createBy;
  private Long updateBy;
  private Long selectUser;
  private boolean isadmin;


}
