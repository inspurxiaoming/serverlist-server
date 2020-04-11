package com.ruoyi.common.exception.user;

/**
 * 对接用户不存在或对接存在故障
 * 
 * @author ruoyi
 */
public class ButtUserNotFoundException extends UserException
{
    private static final long serialVersionUID = 1L;

    public ButtUserNotFoundException()
    {
        super("user.not.exists", null);
    }
}
