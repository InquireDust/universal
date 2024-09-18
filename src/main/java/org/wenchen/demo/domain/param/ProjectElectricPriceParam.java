package org.wenchen.demo.domain.param;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 电费月应收单价配置
 * @author 超级管理员
 * @date 2024-09-12
 */
@Data
@Accessors(chain = true)
public class ProjectElectricPriceParam {

    private Long id;

    private Long tenantId;
    private Long projectId;
    private BigDecimal price;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate validDate;
    private Integer status;

}