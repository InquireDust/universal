package org.wenchen.demo.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("table_a")
public class TableA {

    @TableId
    private Integer id;

    private String name;
}
