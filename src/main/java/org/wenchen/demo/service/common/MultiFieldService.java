package org.wenchen.demo.service.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.wenchen.demo.domain.common.MultiValue;

import java.util.List;

public interface MultiFieldService<T extends MultiValue> {
    
    void deleteByObject(Class<T> entityClass, T entity);
    
    void addEntity(Class<T> entityClass, T entity);

    List<T> findByCondition(Class<T> entityClass, T entity);

    default void addMultiFields(Class<T> entityClass, String refObj, String fieldOwner, Long refId,
                                   Object... fieldVals) throws RuntimeException {
        try {
            if (StrUtil.isEmpty(refObj) || StrUtil.isEmpty(fieldOwner) || ObjectUtil.isNull(refId)) {
                return;
            }

            T entity = entityClass.newInstance();
            BeanUtil.setFieldValue(entity, "refObj", refObj);
            BeanUtil.setFieldValue(entity, "fieldOwner", fieldOwner);
            BeanUtil.setFieldValue(entity, "refId", refId);
            //先删除
            deleteByObject(entityClass, entity);
            //再插件
            if (fieldVals != null && fieldVals.length > 0) {
                for (Object fieldVal : fieldVals) {
                    entity = entityClass.newInstance();
                    BeanUtil.setFieldValue(entity, "refObj", refObj);
                    BeanUtil.setFieldValue(entity, "fieldOwner", fieldOwner);
                    BeanUtil.setFieldValue(entity, "refId", refId);
                    BeanUtil.setFieldValue(entity, "fieldVal", ObjectUtil.toString(fieldVal));
                    addEntity(entityClass, entity);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    default List<String> queryMultiFields(Class<T> entityClass, String refObj, String fieldOwner, Long refId)
            throws RuntimeException {
        List<String> lists = CollectionUtil.newArrayList();
        try {
            if (StrUtil.isNotEmpty(refObj) && StrUtil.isNotEmpty(fieldOwner) && ObjectUtil.isNotNull(refId)) {
                T entity = entityClass.newInstance();
                BeanUtil.setFieldValue(entity, "refObj", refObj);
                BeanUtil.setFieldValue(entity, "fieldOwner", fieldOwner);
                BeanUtil.setFieldValue(entity, "refId", refId);

                List<T> objects = findByCondition(entityClass, entity);
                if (CollectionUtil.isNotEmpty(objects)) {
                    lists = CollectionUtil.getFieldValues(objects, "fieldVal", String.class);
                }
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return lists;
    }
}
