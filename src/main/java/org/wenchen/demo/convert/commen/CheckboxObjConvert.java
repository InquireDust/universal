package org.wenchen.demo.convert.commen;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.wenchen.demo.domain.common.Checkbox;
import org.wenchen.demo.domain.common.CheckboxObj;
import org.wenchen.demo.domain.common.dto.CheckboxDto;
import org.wenchen.demo.domain.common.dto.CheckboxObjDto;
import org.wenchen.demo.domain.common.param.CheckboxObjParam;
import org.wenchen.demo.domain.common.param.CheckboxParam;

/**
 * ${comments}
 * @author 超级管理员
 * @date 2024-09-12
 */
@Mapper
public interface CheckboxObjConvert {
    CheckboxObjConvert CONVERT = Mappers.getMapper(CheckboxObjConvert.class);

    CheckboxObj convert(CheckboxObjParam in);

    CheckboxObjDto convert(CheckboxObj in);

}