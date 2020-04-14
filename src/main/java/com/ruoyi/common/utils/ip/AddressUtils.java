package com.ruoyi.common.utils.ip;

import com.sun.javafx.binding.StringFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSONObject;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.http.HttpUtils;
import com.ruoyi.framework.config.RuoYiConfig;

/**
 * 获取地址类
 *
 * @author ruoyi
 */
public class AddressUtils {
    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static final String IP_URL = "http://ip.taobao.com/service/getIpInfo.php";
    public static final String IP_API_URL="http://ip-api.com/json/%s";

    public static String getRealAddressByIP(String ip) {
        String address = "XX XX XX";
        // 内网不查询
        if (IpUtils.internalIp(ip)) {
            return "内网IP";
        }
        if (RuoYiConfig.isAddressEnabled()) {
//            try {
//                String rspStr = HttpUtils.sendPost(IP_URL, "ip=" + ip);
//                if (StringUtils.isEmpty(rspStr)) {
//                    log.error("获取地理位置异常 {}", ip);
//                    return address;
//                }
//                JSONObject obj = JSONObject.parseObject(rspStr);
//                JSONObject data = obj.getObject("data", JSONObject.class);
//                String region = data.getString("region");
//                String city = data.getString("city");
//                address = region + " " + city;
//            }catch (Exception e) {
                String rspStr = HttpUtils.sendGet(String.format(IP_API_URL,ip), null);
                if (StringUtils.isEmpty(rspStr)) {
                    log.error("获取地理位置异常 {}", ip);
                    return address;
                }
                JSONObject obj = JSONObject.parseObject(rspStr);
                String country = obj.getString("country");
                String region = obj.getString("region");
                String city = obj.getString("city");
                address = country +" "+ region + " " + city;
                log.info(address);
//            }
        }
        return address;
    }
}
