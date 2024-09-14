package org.wenchen.demo.domain.dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class TableBDTO {

    private Integer id;

    private Integer aid;

    private String name;

    private List<TableCDTO> cList;

    private TableCDTO c;
}
