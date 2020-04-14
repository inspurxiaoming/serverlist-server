package com.ruoyi.framework.security.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.exception.CustomException;
import com.ruoyi.common.exception.user.ButtUserNotFoundException;
import com.ruoyi.common.exception.user.CaptchaException;
import com.ruoyi.common.exception.user.CaptchaExpireException;
import com.ruoyi.common.exception.user.UserPasswordNotMatchException;
import com.ruoyi.common.utils.AesCbcUtil;
import com.ruoyi.common.utils.MessageUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.TimeUtils;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.redis.RedisCache;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.common.HttpClientUtil;
import com.ruoyi.project.common.JsonUtils;
import com.ruoyi.project.system.domain.*;
import com.ruoyi.project.system.mapper.SysUserButtMapper;
import com.ruoyi.project.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 登录校验方法
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    SysUserButtMapper sysUserButtMapper;
    @Autowired
    ISysUserService iSysUserService;


    @Autowired
    private RedisCache redisCache;
    @Value("${wechat.appid}")
    String appid="wx8099953f3ba7d072";
    @Value("${wechat.secret}")
    String secret="5da2451cc68d32f35bd364389ccd52a1";

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);
        if (captcha == null) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
            throw new CaptchaException();
        }
        // 用户验证
        Authentication authentication = null;
        try {
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new CustomException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 登录验证
     *
     * @param sysButtUser 微信用户登录信息
     * @return 结果
     */
    public String buttLogin(SysButtUser sysButtUser) {
        WXSessionModel model = new WXSessionModel();
        //首先验证用户登录微信登录信息，
        if(sysButtUser==null){
            throw new CustomException("错误的请求");
        }
        if(StringUtils.isNotEmpty(sysButtUser.getChannel())&&"wechat".equals(sysButtUser.getChannel())){
            try {
                model = getWXSessionModel(sysButtUser);
            }catch(Exception e){
                throw e;
            }
        }
        //查询用户是否存在，有用户则查询用户的信息；没有用户则为用户注册账号，并生成token。
//        LoginUser loginUser = checkUserforWX(model,sysButtUser);
        // 生成token
        return tokenService.createToken(checkUserforWX(model,sysButtUser));
    }
    private WXSessionModel getWXSessionModel(SysButtUser sysButtUser){
        String url = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String, String> param = new HashMap<>();
//        param.put("appid", "wx8c0e94d2e3a3d4d4");
        param.put("appid", appid);
//        param.put("secret", "10f8d287ee2c99c4b07b03b266515a36");
        param.put("secret", secret);
        param.put("js_code", sysButtUser.getCode());
        param.put("grant_type", "authorization_code");

        String wxResult = HttpClientUtil.doGet(url, param);
        if(StringUtils.isEmpty(wxResult)){
            throw new ButtUserNotFoundException();
        }
        log.info("++==userWxResult:{}",wxResult);
        WXSessionModel model = JsonUtils.jsonToPojo(wxResult, WXSessionModel.class);
        log.info("++##userModel-session_key:{}", model != null ? model.getSession_key() : "");
        log.info("++##userModel-openid:{}", model != null ? model.getOpenid() : "");
        return model;
    }

    /**
     * 查询用户信息，判断用户是否存在，用户存在的情况下，查询用户信息，用户信息不存在的情况下，注册账号，生成用户信息。
     * @param model
     * @param sysButtUser
     * @return
     */
    private  LoginUser checkUserforWX(WXSessionModel model, SysButtUser sysButtUser){
        //根据sysButtUser判断用户类型，确定查询哪些表，然后
        SysUserButt sysUserButt = sysUserButtMapper.selectById(model.getOpenid());
        SysUser sysUser = new SysUser();
        if(sysUserButt!=null){
            sysUser = iSysUserService.selectUserById(sysUserButt.getUserId());
        }else{
            try{
                sysUserButt = getUserInfo(sysButtUser,model,sysUserButt);
            } catch (Exception e) {
                log.error("error {}",e);
//                e.printStackTrace();
            }
            sysUser.setUserName(sysButtUser.getChannel()+model.getOpenid());
            log.info("sysUserButt  {}",sysUserButt.toString());
            sysUser.setCreateBy("user-wechat");
            sysUser.setNickName(sysUserButt.getName());
            int row = iSysUserService.insertUser(sysUser);
            if(row>0){
                sysUserButt.setId(model.getOpenid());
                sysUserButt.setUserId(sysUser.getUserId());
                log.info("start insert sysUserButt from wechat");
                sysUserButtMapper.insert(sysUserButt);
            }
        }
        LoginUser loginUser = new LoginUser();
        loginUser.setUser(sysUser);
        return loginUser;

    }
    private SysUserButt getUserInfo(SysButtUser sysButtUser, WXSessionModel model,SysUserButt sysUserButt) throws Exception {
        String decodeEncryptedData = decode(sysButtUser.getEncryptedData());
        String userInfo = null;
        String userOpenId = model.getOpenid();
        String sessionKey = model.getSession_key();
        decodeEncryptedData = decodeEncryptedData.replaceAll(" ","\\+");
        userInfo = AesCbcUtil.decrypt(decodeEncryptedData, sessionKey, sysButtUser.getIv(), "UTF-8");
        log.info("uuuuuserInfo{}",userInfo);
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(userInfo)) {
            Map<String, String> userInfoMap = (new ObjectMapper()).readValue(userInfo, Map.class);
            sysUserButt = getUserInfo(userOpenId,userInfoMap,sysButtUser);

        }
        return sysUserButt;
    }
    private String decode(String url){
        try {
            String prevURL="";
            String decodeURL=url;
            while(!prevURL.equals(decodeURL))
            {
                prevURL=decodeURL;
                decodeURL= URLDecoder.decode( decodeURL, "UTF-8" );
            }
            return decodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while decoding" +e.getMessage();
        }
    }
    public SysUserButt getUserInfo(String userOpenId, Map<String, String> userInfoMap, SysButtUser sysButtUser){
        log.info("start package SysUserButt");
        SysUserButt sysUserButt = new SysUserButt();
        sysUserButt.setId(userOpenId);
        sysUserButt.setPhone(userInfoMap.get("phoneNumber"));
        sysUserButt.setName(sysButtUser.getUserInfo().getNickName());
        sysUserButt.setUserId(sysButtUser.getUserId());
        //正常环境用户角色设置
        sysUserButt.setRoles("user");
        //审核环境用户角色设置
//            user.setRoles(CommonConstants.USER_ROLES_ADMIN);
        sysUserButt.setCreatedTime(TimeUtils.longToDate(System.currentTimeMillis()));
        if(org.apache.commons.lang3.StringUtils.isNoneEmpty(sysButtUser.getRawData())){
            sysButtUser.setUserInfo(getUserFromRawData(sysButtUser.getRawData()));
        }
        sysUserButt.setAvatarUrl(sysButtUser.getUserInfo().getAvatarUrl());
        sysUserButt.setSource(1);
        return sysUserButt;
    }
    public WXUserInfo getUserFromRawData(String rawData){
        JSONObject jsonObj = (JSONObject) JSON.parse(rawData);
        WXUserInfo userInfo= JSONObject.toJavaObject(jsonObj,WXUserInfo.class);
        return userInfo;
    }
}
