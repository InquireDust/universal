package org.wenchen.demo.service.common;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wenchen.demo.domain.common.CheckboxObj;
import org.wenchen.demo.mapper.common.CheckboxObjMapper;

/**
 * ${comments}
 *
 * @author 超级管理员
 * @date 2024-09-12
 */
@Repository
@RequiredArgsConstructor
public class CheckboxObjManager extends BaseManager<CheckboxObjMapper, CheckboxObj> {
    private final CheckboxObjMapper mapper;
}