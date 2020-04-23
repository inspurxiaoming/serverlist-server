package com.ruoyi.project.cloudcenter.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.cloudcenter.domain.DictTypeVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author chengyongming
 */
@Slf4j
@Service
public class DictTypeService {

    @Transactional
    public AjaxResult insert(DictTypeVO dictTypeBean){
        return AjaxResult.success(dictTypeBean);
    }
    public AjaxResult getTypeById(String typeId){
        return null;
//        return AjaxResult.success(dictTypeMapper.selectById(typeId));
    }

    public AjaxResult getAll(){
//        return AjaxResult.success(dictTypeMapper.selectAll());
        return null;
    }

}
