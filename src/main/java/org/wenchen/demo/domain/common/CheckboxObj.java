package org.wenchen.demo.domain.common;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.wenchen.demo.convert.commen.CheckboxObjConvert;
import org.wenchen.demo.domain.common.dto.CheckboxObjDto;
import org.wenchen.demo.domain.common.param.CheckboxObjParam;
import org.wenchen.demo.function.EntityBaseFunction;

/**
 * ${comments}
 *
 * @author 超级管理员
 * &#064;date  2024-09-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("checkbox_obj")
@Accessors(chain = true)
public class CheckboxObj extends MultiValue implements EntityBaseFunction<CheckboxObjDto> {

    /**
     * 创建对象
     */
    public static CheckboxObj init(CheckboxObjParam in) {
        return CheckboxObjConvert.CONVERT.convert(in);
    }

    /**
     * 转换成dto
     */
    @Override
    public CheckboxObjDto toDto() {
        return CheckboxObjConvert.CONVERT.convert(this);
    }
}
