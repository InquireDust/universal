package org.wenchen.demo.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * Author: wen-chen
 * Date: 2024/9/15
 */
@Data
@TableName("tree_table")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TreeTable {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 父级编码
     */
    private String parentCode;

    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<TreeTable> childNode;
}
