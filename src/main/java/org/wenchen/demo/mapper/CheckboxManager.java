package org.wenchen.demo.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.wenchen.demo.domain.common.Checkbox;
import org.wenchen.demo.mapper.common.CheckboxMapper;
import org.wenchen.demo.service.common.BaseManager;

/**
 * ${comments}
 * @author 超级管理员
 * @date 2024-09-12
 */
@Repository
@RequiredArgsConstructor
public class CheckboxManager extends BaseManager<CheckboxMapper, Checkbox> {
    private final CheckboxMapper mapper;
}