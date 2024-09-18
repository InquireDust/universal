package org.wenchen.demo.domain.common.param;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 基础模板多选属性
 * @author 超级管理员
 * @date 2024-09-12
 */
@Data
@Accessors(chain = true)
public class CheckboxParam {

    private Long id;

    private Long tenantId;
    private Long refId;
    private String refObj;
    private String fieldOwner;
    private String fieldVal;

}