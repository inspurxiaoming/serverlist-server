package com.ruoyi.project.cloudcenter.service;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.cloudcenter.domain.RegionVO;
import com.ruoyi.project.common.OwnRestTemplate;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import com.ruoyi.project.system.service.ISysDeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chengyongming
 */
@Slf4j
@Service
public class RegionService {

    @Autowired
    OwnRestTemplate ownRestTemplate;
    @Autowired
    ISysDeptService iSysDeptService;
    @Value("${device.domain}")
    private String domain;
    @Value("${device.config.region.insert}")
    private String insertUri;
    @Value("${device.config.region.list}")
    private String listUri;
    @Value("${device.config.region.queryOne}")
    private String queryOneUri;

    public AjaxResult insert(RegionVO regionVO) {
        ResponseEntity<RegionVO> responseEntity = ownRestTemplate.exchangeWithOutAuth(domain+insertUri, HttpMethod.POST, regionVO, AjaxResult.class, regionVO);
        return AjaxResult.success(responseEntity.getBody());
    }

    public TableDataInfo getAll(PageDomain page, RegionVO regionVO) {
        List<SysDept> sysDepts = iSysDeptService.selectAll();
        regionVO.setPageNum(page.getPageNum());
        regionVO.setPageSize(page.getPageSize());
        regionVO.setIsadmin(SysUser.isAdmin(regionVO.getSelectUser()));
        ResponseEntity<TableDataInfo<RegionVO>> responseEntity =  ownRestTemplate.exchangeWithOutAuth(domain+listUri,HttpMethod.POST,regionVO,TableDataInfo.class,regionVO);
        return responseEntity.getBody();
    }

    public AjaxResult getCloudCenter(SysDept sysDept) {
        return AjaxResult.success(getCloudCenterList(sysDept));
    }
    private List<SysDept> getCloudCenterList(SysDept sysDept){
        if(SysUser.isAdmin(SecurityUtils.getUserId())){
            return iSysDeptService.selectDeptList(sysDept);
        }else{
            List<SysDept> deptList = iSysDeptService.selectDeptByUserIdAndRole(SecurityUtils.getUserId());
            List<SysDept> resultList = new ArrayList<>();
            deptList.stream().forEach(deptObject ->{
                if(!Long.valueOf(100).equals(deptObject.getDeptId())){
                    resultList.add(deptObject);
                }
            });
            return deptList;
        }

    }

    public AjaxResult getById(String id) {
        ResponseEntity<AjaxResult> responseEntity =  ownRestTemplate.exchangeWithOutAuth(domain+String.format(queryOneUri,id),HttpMethod.GET,id,AjaxResult.class,id);
        AjaxResult ajaxResult = responseEntity.getBody();
        return ajaxResult;
    }

    public AjaxResult delete(String[] ids) {
        ResponseEntity<AjaxResult> responseEntity =  ownRestTemplate.exchangeWithOutAuth(domain+String.format(queryOneUri,ids),HttpMethod.DELETE,ids,AjaxResult.class,ids);
        AjaxResult ajaxResult = responseEntity.getBody();
        return ajaxResult;
    }

}
