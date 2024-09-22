package org.wenchen.demo.domain;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ExcelTarget("person")
public class Person {

    @Excel(name = "ID", width = 10)
    private Integer id;

    @Excel(name = "姓名", width = 20)
    private String name;

    @Excel(name = "年龄", isColumnHidden = true)  // 隐藏列
    private Integer age;
}

