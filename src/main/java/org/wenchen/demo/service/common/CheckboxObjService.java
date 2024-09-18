package org.wenchen.demo.service.common;


import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.wenchen.demo.domain.common.CheckboxObj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基础模板多选属性
 *
 * @author 超级管理员
 * &#064;date  2024-09-12
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CheckboxObjService implements MultiFieldService<CheckboxObj> {
    private final CheckboxObjManager checkboxManager;


    @Override
    public void deleteByObject(Class<CheckboxObj> entityClass, CheckboxObj entity) {
        Map<SFunction<CheckboxObj, ?>, Object> params = new HashMap<>(0);
        params.put(CheckboxObj::getRefId, entity.getRefId());
        params.put(CheckboxObj::getRefObj, entity.getRefObj());
        params.put(CheckboxObj::getFieldOwner, entity.getFieldOwner());
        checkboxManager.deleteByMultFields(params);
    }

    @Override
    public void addEntity(Class<CheckboxObj> entityClass, CheckboxObj entity) {
        if (ObjectUtil.isNull(entity.getTenantId())) {
//            entity.setTenantId(SecurityUtil.getTenantId());
            entity.setTenantId(1111111L);
        }
        checkboxManager.save(entity);
    }

    @Override
    public List<CheckboxObj> findByCondition(Class<CheckboxObj> entityClass, CheckboxObj entity) {
        Map<SFunction<CheckboxObj, ?>, Object> params = new HashMap<>(0);
        params.put(CheckboxObj::getRefId, entity.getRefId());
        params.put(CheckboxObj::getRefObj, entity.getRefObj());
        params.put(CheckboxObj::getFieldOwner, entity.getFieldOwner());

        return checkboxManager.findByMultFields(params);
    }

}