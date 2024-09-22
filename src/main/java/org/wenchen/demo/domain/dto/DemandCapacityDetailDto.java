package org.wenchen.demo.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 需量电容明细
 *
 * @author 超级管理员
 * &#064;date  2024-09-20
 */
@Data
@Accessors(chain = true)
public class DemandCapacityDetailDto {

    private Long demandCapacityId;

    @Excel(name = "房源id")
    private Long roomId;

    @Excel(name = "客户id")
    private Long customerId;

    @Excel(name = "房源编码")
    private String roomCode;

    @Excel(name = "客户名称")
    private String customerName;

    @Excel(name = "需量")
    private BigDecimal capacitance;
    private BigDecimal apportionedAmount;
    private BigDecimal billAmount;

}