package com.ruoyi.framework.security.service;

import com.ruoyi.common.enums.UserStatus;
import com.ruoyi.common.exception.BaseException;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.SysButtUser;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service
public class ButtUserDetailsServiceImpl {
    private static final Logger log = LoggerFactory.getLogger(ButtUserDetailsServiceImpl.class);

    @Autowired
    private ISysUserService userService;

    @Autowired
    private SysPermissionService permissionService;

    public UserDetails loadUser(SysButtUser sysButtUser) throws UsernameNotFoundException {
        SysUser user = userService.selectUserByUserName(sysButtUser.getUserId());
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", sysButtUser.getUserId());
            throw new UsernameNotFoundException("登录用户：" + sysButtUser.getUserId() + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", sysButtUser.getUserId());
            throw new BaseException("对不起，您的账号：" + sysButtUser.getUserId() + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", sysButtUser.getUserId());
            throw new BaseException("对不起，您的账号：" + sysButtUser.getUserId() + " 已停用");
        }


        return createLoginUser(user);
    }

    public UserDetails createLoginUser(SysUser user) {
        return new LoginUser(user, permissionService.getMenuPermission(user));
    }
}
