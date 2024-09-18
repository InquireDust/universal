package org.wenchen.demo.convert;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wenchen.demo.domain.common.Checkbox;
import org.wenchen.demo.domain.dto.PropertyCheckboxDto;
import org.wenchen.demo.domain.param.PropertyCheckboxParam;

/**
 * ${comments}
 * @author 超级管理员
 * @date 2024-09-12
 */
@Mapper
public interface CheckboxConvert {
    CheckboxConvert CONVERT = Mappers.getMapper(CheckboxConvert.class);

    Checkbox convert(PropertyCheckboxParam in);

    PropertyCheckboxDto convert(Checkbox in);

}