package org.wenchen.demo.domain.common;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class MultiValue extends MpIdEntity {

    /** 属性：租户ID */
    protected Long tenantId;

    /** 属性：所有关联ID */
    protected Long refId;

    /** 属性：所属对象。*/
    protected String refObj;

    /** 属性：所属属性。 */
    protected String fieldOwner;

    /** 属性：属性取值。选择的内容，参考系统配置表 */
    protected String fieldVal;

}
