package com.ruoyi.project.cloudcenter.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.PageDomain;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.cloudcenter.domain.RegionVO;
import com.ruoyi.project.common.OwnRestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Date;
import java.util.List;

/**
 * @author chengyongming
 */
@Slf4j
@Service
public class RegionService {

    @Autowired
    OwnRestTemplate ownRestTemplate;

    public AjaxResult insert(RegionVO regionVO) {
        ResponseEntity<RegionVO> responseEntity = ownRestTemplate.exchangeWithOutAuth("http://localhost:8021/neo4j/config/region", HttpMethod.POST, regionVO, AjaxResult.class, regionVO);
        return AjaxResult.success(responseEntity.getBody());
    }

    public TableDataInfo getAll(PageDomain page, RegionVO regionVO) {
        regionVO.setPageNum(page.getPageNum());
        regionVO.setPageSize(page.getPageSize());
//        regionVO.setIsAsc(page.getIsAsc());
//        regionVO.setOrderByColumn(page.getOrderByColumn());
//        ResponseEntity<TableDataInfo> responseEntity =  ownRestTemplate.exchangeWithOutAuth("http://localhost:8021/neo4j/config/region/list",HttpMethod.GET,regionVO,TableDataInfo.class,regionVO);
        String param = "?pageNum=" + regionVO.getPageNum() + "&pageSize=" + regionVO.getPageSize();

        ResponseEntity<TableDataInfo> responseEntity = ownRestTemplate.getForEntity("http://localhost:8021/neo4j/config/region/list" + param, TableDataInfo.class, regionVO);
        return responseEntity.getBody();
    }

//    public RegionVO getOneById(String id){
//        return regionMapper.selectById(id);
//    }
//
//    public List<RegionVO> getall(){
//        return regionMapper.selectAll();
//    }
//
//    public int update(RegionVO regionbean){
//        regionbean.setUpdateTime(new Date());
//        return regionMapper.updateById(regionbean);
//    }

//    public List<DeviceBean> getRegionDeviceByDeviceId(String deviceId){
//        return regionMapper.regionDeviceByDevice(deviceId);
//    }

}
