package org.wenchen.demo.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 电费月应收单价配置
 * @author 超级管理员
 * @date 2024-09-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class ProjectElectricPriceDto extends BaseDto {

    private Long tenantId;
    private Long projectId;
    private BigDecimal price;
    private LocalDate validDate;
    private Integer status;

}