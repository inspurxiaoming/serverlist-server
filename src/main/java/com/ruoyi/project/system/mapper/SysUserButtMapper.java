package com.ruoyi.project.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ruoyi.project.system.domain.SysUserButt;

/**
 * @author chengyongming
 */
public interface SysUserButtMapper extends BaseMapper<SysUserButt> {
     SysUserButt selectById(String id);
     int insert(SysUserButt user);

}
