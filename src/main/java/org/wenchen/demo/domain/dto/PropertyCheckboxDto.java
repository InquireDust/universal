package org.wenchen.demo.domain.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ${comments}
 * @author 超级管理员
 * @date 2024-09-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class PropertyCheckboxDto extends BaseDto {

    private Long tenantId;
    private Long refId;
    private String refObj;
    private String fieldOwner;
    private String fieldVal;

}