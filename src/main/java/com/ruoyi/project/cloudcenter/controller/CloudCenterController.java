package com.ruoyi.project.cloudcenter.controller;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.framework.web.page.TableDataInfo;
import com.ruoyi.project.cloudcenter.domain.RegionVO;
import com.ruoyi.project.cloudcenter.service.RegionService;
import com.ruoyi.project.system.domain.SysDept;
import com.ruoyi.project.system.domain.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.swing.plaf.synth.Region;

@RestController
@RequestMapping("/config")
public class CloudCenterController extends BaseController {
    @Autowired
    RegionService regionService;


//    @PreAuthorize("@ss.hasPermi('project:region:add')")
//    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping("/region")
    public AjaxResult insertRegion(@RequestBody RegionVO regionVO){
        regionVO.setCreateBy(SecurityUtils.getUserId());
        return regionService.insert(regionVO);
    }
    @PreAuthorize("@ss.hasPermi('cloudcenter:region:list')")
    @GetMapping("/region/list")
    public TableDataInfo getall( RegionVO regionVO){
        regionVO.setSelectUser(SecurityUtils.getUserId());
        return regionService.getAll(pageMessage(),regionVO);
    }
    @PreAuthorize("@ss.hasPermi('cloudcenter:region:list')")
    @GetMapping("/region/{id}")
    public AjaxResult getById(@PathVariable String id){
        return regionService.getById(id);
    }
    @PreAuthorize("@ss.hasPermi('cloudcenter:region:list')")
    @GetMapping("/region/cloudcenter/list")
    public AjaxResult getCloudCenter( SysDept dept){
        return regionService.getCloudCenter(dept);
    }

//    @GetMapping("/region/list")
//    public AjaxResult getAll(){
//        return AjaxResult.success(regionService.getall());
//    }
//    @GetMapping("/region/{id}")
//    public AjaxResult getOneById(@PathParam("id") String id){
//        return AjaxResult.success(regionService.getOneById(id));
//    }
//    @PatchMapping("/region/update")
//    public AjaxResult update(RegionBean regionBean){
//        return AjaxResult.success(regionService.update(regionBean));
//    }
//
//    @GetMapping("/device/dict-type/list")
//    public AjaxResult getAllType(){
//        return dictTypeService.getAll();
//    }
//    @PostMapping("/device/dict-type")
//    public AjaxResult insertDictType(DictTypeBean dictTypeBean){
//        return dictTypeService.insert(dictTypeBean);
//    }
//    @GetMapping("/device/dict-type/{typeId}}")
//    public AjaxResult getOneTypeById(@PathParam("typeId")String typeId){
//        return dictTypeService.getTypeById(typeId);
//    }
//
//    @GetMapping("/device/dict-data/list")
//    public AjaxResult getAllDictData(String typeId){
//        return AjaxResult.success(dictDataService.getAllByType(typeId));
//    }
//    @PostMapping("/device/dict-data")
//    public AjaxResult insertDictData(DictDataVo dictDataVo){
//        return AjaxResult.success(dictDataService.insert(dictDataBean));
//    }

}
