package com.ruoyi.project.common;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.project.system.domain.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;


@Component
@Slf4j
public class OwnRestTemplate<T> extends RestTemplate {
    @Autowired
    private HttpServletRequest request;

    public ResponseEntity<T> exchange(String tokenString, String url, HttpMethod method,
                                      Object params, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = generateHeaders(tokenString);
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        try{
            ResponseEntity<T> result = super.exchange(url,method,entity,responseType,uriVariables);
            return result;
        }catch (Exception e){
            log.error("ERROR","access api failure",e);
            return null;
        }
    }
    public HttpHeaders generateHeaders(String tokenString) {
        HttpHeaders httpHeaders = getHeaders();
        httpHeaders.add("Authorization", "Bearer " + tokenString);
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return httpHeaders;
    }

    public ResponseEntity<T> exchange(String url, HttpMethod method,
                                      Object params, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = getHeaders();
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        try{
            ResponseEntity<T> result = super.exchange(url,method,entity,responseType,uriVariables);
            return result;
        }catch (Exception e){
            return null;
        }
    }
    public ResponseEntity<T> exchangeWithOutAuth(String url, HttpMethod method,
                                      Object params, Class<T> responseType, Object... uriVariables) {
        HttpHeaders httpHeaders = getHeaders();
        HttpEntity entity = new HttpEntity(params, httpHeaders);
        try{
            ResponseEntity<T> result = super.exchange(url,method,entity,responseType,uriVariables);
            return result;
        }catch (Exception e){
            return null;
        }
    }
    public ResponseEntity<T> exchangeWithOutAuth(String url, HttpMethod method,
                                                 HttpEntity<?> entity, Class<T> responseType, Object... uriVariables) {
        try{
            ResponseEntity<T> result = super.exchange(url,method,entity,responseType,uriVariables);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Accept", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        httpHeaders.add("isAdmin", String.valueOf(SysUser.isAdmin(SecurityUtils.getUserId())));
        httpHeaders.add("userId", String.valueOf(SecurityUtils.getUserId()));
        httpHeaders.add("cloudCenter",String.valueOf(SecurityUtils.getDeptID()));
        return httpHeaders;
    }
}
