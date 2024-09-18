package org.wenchen.demo.domain.common;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.wenchen.demo.convert.CheckboxConvert;
import org.wenchen.demo.domain.dto.PropertyCheckboxDto;
import org.wenchen.demo.domain.param.PropertyCheckboxParam;
import org.wenchen.demo.function.EntityBaseFunction;

/**
 * ${comments}
 *
 * @author 超级管理员
 * @date 2024-09-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("checkbox")
@Accessors(chain = true)
public class Checkbox extends MultiValue implements EntityBaseFunction<PropertyCheckboxDto> {

    /**
     * 创建对象
     */
    public static Checkbox init(PropertyCheckboxParam in) {
        return CheckboxConvert.CONVERT.convert(in);
    }

    /**
     * 转换成dto
     */
    @Override
    public PropertyCheckboxDto toDto() {
        return CheckboxConvert.CONVERT.convert(this);
    }
}
