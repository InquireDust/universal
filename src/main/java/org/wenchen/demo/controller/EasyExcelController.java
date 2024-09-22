package org.wenchen.demo.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.WriteSheet;
//import com.alibaba.excel.write.style.row.LockRowStrategy;
//import com.alibaba.excel.write.style.row.RowHeightStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenchen.demo.domain.dto.DemandCapacityDetailDto;

import java.io.IOException;
import java.util.List;

/**
 * Author: wen-chen
 * Date: 2024/9/21
 */
@RestController
@RequestMapping("/easyexcel")
@RequiredArgsConstructor
public class EasyExcelController {
//    @GetMapping("/export")
//    public void exportExcel(HttpServletResponse response) throws IOException {
//        String fileName = "example.xlsx";
//
//        // 设置响应头
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
//
//        // 创建数据列表
//        List<DemandCapacityDetailDto> dataList = fetchData(); // 替换为你的数据获取逻辑
//
//        // 创建 Excel 写入器
//        WriteSheet writeSheet = EasyExcel.writerSheet("Sheet1")
//                .registerWriteHandler(new RowHeightStyleStrategy(20)) // 设置行高
//                .registerWriteHandler(new LockRowStrategy(0)) // 锁定标题行
//                .build();
//
//        // 隐藏特定行（示例中隐藏第二和第三行）
//        writeSheet.setHiddenRows(new int[]{1, 2});
//
//        // 写入数据
//        EasyExcel.write(response.getOutputStream(), DemandCapacityDetailDto.class)
//                .sheet(writeSheet.getSheetName())
//                .doWrite(dataList);
//    }
//
//    private List<DemandCapacityDetailDto> fetchData() {
//        // TODO: 实现数据获取逻辑
//        return null; // 返回你的数据列表
//    }
}
