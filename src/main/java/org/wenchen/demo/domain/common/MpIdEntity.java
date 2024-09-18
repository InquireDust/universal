package org.wenchen.demo.domain.common;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;

/**   
* mybatis plus id实体
* @author dejavu
* @date 2021/8/17 
*/
@Getter
@Setter
@FieldNameConstants
public class MpIdEntity implements Serializable {

    private static final long serialVersionUID = 3982181843202226124L;

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
}
