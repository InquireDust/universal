package org.wenchen.demo.domain.dto;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 签约电容对象
 *
 * @author 超级管理员
 * @date 2024-09-12
 */
@Data
@Accessors(chain = true)
public class ElectricSignDto {


    @Excel(name = "项目名称", width = 20)
    private String projectName;

    @Excel(name = "客户名称", width = 20)
    private String customerName;

    @Excel(name = "客户社会信用码/身份证", width = 30)
    private String creditNo;

    private Long tenantId;
    private Long projectId;
    private Long contractId;
    private Long customerId;
    private Integer signType;
    private String signCode;
    @Excel(name = "交房日期(日期格式要以-隔开)", width = 20)
    private LocalDate receiveDate;

    @Excel(name = "是否生成历史账单(是/否)", replace = {"是_1", "否_0"}, width = 25, dict = "historyDict")
    private String genHistoryStr;

    @Excel(name = "空间编号(多个空间用中文；隔开）", width = 20)
    private String roomNumber;
    private LocalDate startDate;

    private LocalDate overDate;

    private Integer genHistory;


    @Excel(name = "签约电容量")
    private BigDecimal capacitance;


    private Integer status;
    //    , replace = {"已启用_1", "未启用_0"}
    @Excel(name = "状态(已启用/未启用)", dict = "statusDict")
    private String statusStr;

    private String contractNo;
    //todo
    private String contractName = "后期添加";
    private List<String> roomNameList;
    private List<Long> roomIds;

}