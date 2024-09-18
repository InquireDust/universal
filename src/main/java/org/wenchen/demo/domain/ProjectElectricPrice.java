package org.wenchen.demo.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.wenchen.demo.convert.ProjectElectricPriceConvert;
import org.wenchen.demo.domain.dto.ProjectElectricPriceDto;
import org.wenchen.demo.domain.param.*;
import org.wenchen.demo.function.EntityBaseFunction;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 电费月应收单价配置
 *
 * @author 超级管理员
 * &#064;date  2024-09-12
 */
@Data
@TableName("property_project_electric_price")
@Accessors(chain = true)
public class ProjectElectricPrice implements EntityBaseFunction<ProjectElectricPriceDto> {

    /**
     * 租户id
     */
    private Long tenantId;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 单价
     */
    private BigDecimal price;
    /**
     * 生效时间
     */
    private LocalDate validDate;
    /**
     * 状态(0:草稿,1:已生效)
     */
    private Integer status;

    /**
     * 创建对象
     */
    public static ProjectElectricPrice init(ProjectElectricPriceParam in) {
        return ProjectElectricPriceConvert.CONVERT.convert(in);
    }

    /**
     * 转换成dto
     */
    @Override
    public ProjectElectricPriceDto toDto() {
        return ProjectElectricPriceConvert.CONVERT.convert(this);
    }
}
