package com.ruoyi.project.cloudcenter.domain;


import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DictDataVO {

  private String dictCode;
  private int dictSort;
  private String dictLabel;
  private String dictValue;
  private String dictType;
  private String cssClass;
  private String listClass;
  private String isDefault;
  private boolean status;
  private String createBy;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date createTime;
  private String updateBy;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
  private Date updateTime;
  private String remark;
  private boolean hasSubdivision;

}
