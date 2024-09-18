package org.wenchen.demo.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wenchen.demo.domain.ProjectElectricPrice;
import org.wenchen.demo.domain.dto.ProjectElectricPriceDto;
import org.wenchen.demo.domain.param.*;

/**
 * 电费月应收单价配置
 *
 * @author 超级管理员
 * &#064;date  2024-09-12
 */
@Mapper
public interface ProjectElectricPriceConvert {
    ProjectElectricPriceConvert CONVERT = Mappers.getMapper(ProjectElectricPriceConvert.class);

    ProjectElectricPrice convert(ProjectElectricPriceParam in);

    ProjectElectricPriceDto convert(ProjectElectricPrice in);

}