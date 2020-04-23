package com.ruoyi.project.cloudcenter.service;

import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.cloudcenter.domain.DictDataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chengyongming
 */
@Slf4j
@Service
public class DictDataService {
    @Autowired
    DictTypeService dictTypeService;

    @Transactional
    public AjaxResult insert(DictDataVO dictDataBean) {

        return AjaxResult.success(dictDataBean);
    }

    public List<DictDataVO> getAllByType(String typeId){

//        return dictDataMapper.selectAll(typeId);
        return null;
    }
}
