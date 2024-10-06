package org.wenchen.demo.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.wenchen.demo.code.CheckboxConstants;
import org.wenchen.demo.domain.Person;
import org.wenchen.demo.domain.common.Checkbox;
import org.wenchen.demo.domain.dto.DemandCapacityDetailDto;
import org.wenchen.demo.service.common.CheckboxService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Author: wen-chen
 * Date: 2024/9/18
 */
@Service
@RequiredArgsConstructor
public class CommonTestServer {
    private final CheckboxService checkboxService;

    public void saveCheckbox(List<Long> listIds, Long id) {
        List<String> roomIdList = listIds.stream().map(Object::toString).collect(Collectors.toList());
        checkboxService.addMultiFields(Checkbox.class, CheckboxConstants.OBJECT_NAME, CheckboxConstants.FIELD_NAME, id,
                CollUtil.isNotEmpty(roomIdList) ? roomIdList.toArray(new String[0]) : null);
    }

    public List<Long> queryCheckbox(Long id) {
        List<String> strings = checkboxService.queryMultiFields(Checkbox.class, CheckboxConstants.OBJECT_NAME, CheckboxConstants.FIELD_NAME, id);
        return strings.stream().map(Long::valueOf).collect(Collectors.toList());
    }


    private static void look(Sheet sheet, int ageColumnIndex, Workbook workbook) {
        //todo 隐藏age列
        sheet.setColumnHidden(ageColumnIndex, true);
//        sheet.setColumnHidden(ageColumnIndex, false);

        // 遍历每一行，锁定 "age" 列
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                Cell ageCell = row.getCell(ageColumnIndex);
                if (ageCell != null) {
                    CellStyle lockedCellStyle = workbook.createCellStyle();
                    lockedCellStyle.setLocked(true); // 锁定单元格
                    ageCell.setCellStyle(lockedCellStyle);
                }

                // 解锁其他列
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    if (j != ageColumnIndex) {
                        Cell otherCell = row.getCell(j);
                        if (otherCell != null) {
                            CellStyle unlockedCellStyle = workbook.createCellStyle();
                            unlockedCellStyle.setLocked(false); // 其他单元格可编辑
                            otherCell.setCellStyle(unlockedCellStyle);
                        }
                    }
                }
            }
        }
    }

    private static void hideAndLockColumns(Sheet sheet, List<Integer> list, List<Integer> columnHiddenList, Workbook workbook) {
        // 锁定所有列
        for (int i = 0; i < sheet.getRow(0).getLastCellNum(); i++) {
            CellStyle lockedCellStyle = workbook.createCellStyle();
            lockedCellStyle.setLocked(true); // 锁定单元格

            // 遍历每一行，锁定所有列
            for (int j = 0; j <= sheet.getLastRowNum(); j++) {
                Row row = sheet.getRow(j);
                if (row != null) {
                    Cell cell = row.getCell(i);
                    if (cell != null) {
                        cell.setCellStyle(lockedCellStyle);
                    }
                }
            }
        }

        // 解锁不在列表中的列
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    if (!list.contains(j)) {
                        Cell otherCell = row.getCell(j);
                        if (otherCell != null) {
                            CellStyle unlockedCellStyle = workbook.createCellStyle();
                            unlockedCellStyle.setLocked(false); // 解锁单元格
                            otherCell.setCellStyle(unlockedCellStyle);
                        }
                    }
                }
            }
        }

        // 隐藏列
        for (Integer columnIndex : columnHiddenList) {
            sheet.setColumnHidden(columnIndex, true);
        }
    }

    /**
     * 为指定索引的 Sheet 设置保护密码
     *
     * @param workbook 工作簿对象，包含多个 Sheet
     * @param index    Sheet 的索引位置
     * @return 返回设置密码保护后的 Sheet 对象
     * <p>
     * 该方法首先根据索引从工作簿中获取对应的 Sheet，然后为该 Sheet 设置密码保护，
     * 密码为“password”。保护 Sheet 后，方法返回设置过密码保护的 Sheet 对象。
     */
    private static Sheet setPassword(Workbook workbook, int index) {
        // 获取第一个 Sheet
        Sheet sheet = workbook.getSheetAt(index);
        // 设置 Sheet 保护密码
        sheet.protectSheet("password");
        return sheet;
    }

    public void exportPersonList(HttpServletResponse response) throws IOException {
        // 模拟数据
        List<Person> personList = new ArrayList<>();
        personList.add(new Person(1, "张三", 25));
        personList.add(new Person(2, "李四", 30));
        personList.add(new Person(3, "王五", 28));

        // 导出参数
        ExportParams exportParams = new ExportParams("人员信息", "导出");

        // 导出 Excel
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, Person.class, personList);

        // 设置响应头
        String fileName = URLEncoder.encode("人员信息.xlsx", "UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("UTF-8");

        // 输出到客户端
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    public List<Person> importPersonList() throws FileNotFoundException {
        // 从 resources 目录下读取人员信息.xlsx 文件
        File file = ResourceUtils.getFile("classpath:人员信息.xlsx");

        // 设置导入参数
        ImportParams params = new ImportParams();
        params.setHeadRows(1); // 表头行数

        // 使用 EasyPoi 进行导入，将 Excel 转换为对象列表
        List<Person> personList = ExcelImportUtil.importExcel(file, Person.class, params);
        personList.forEach(System.out::println);
        return personList;
    }

    public void exportPersonListLook(HttpServletResponse response) throws IOException {
        // 模拟数据
        List<DemandCapacityDetailDto> personList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemandCapacityDetailDto detailDto = new DemandCapacityDetailDto();
            detailDto.setRoomCode("roomCode" + i);
            detailDto.setCustomerName("customerName" + i);
            detailDto.setRoomId(i + 100L);
            detailDto.setCustomerId(i + 200L);
            personList.add(detailDto);
        }

        // 导出参数
        ExportParams exportParams = new ExportParams(null, "导出");

        // 导出 Excel
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, DemandCapacityDetailDto.class, personList);

        // 获取第一个 Sheet
        Sheet sheet = workbook.getSheetAt(0);

        // 设置 Sheet 保护密码
        sheet.protectSheet("password");

        // 锁定 "age" 列，并隐藏该列
        int ageColumnIndex = 2; // 假设 age 列是第3列，索引为2

        look(sheet, ageColumnIndex, workbook);

        // 设置响应头
        String fileName = URLEncoder.encode("人员信息.xlsx", "UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("UTF-8");

        // 输出到客户端
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    /**
     * 导出人员列表为带密码保护和列锁定的Excel文件
     *
     * @param response HttpServletResponse对象，用于设置响应头并输出Excel文件
     * @throws IOException 如果文件写入或关闭时发生IO错误
     */
    public void exportPersonListLookList(HttpServletResponse response) throws IOException {
        // 模拟数据
        List<DemandCapacityDetailDto> personList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            DemandCapacityDetailDto detailDto = new DemandCapacityDetailDto();
            detailDto.setRoomCode("roomCode" + i);
            detailDto.setCustomerName("customerName" + i);
            detailDto.setRoomId(i + 100L);
            detailDto.setCustomerId(i + 200L);
            personList.add(detailDto);
        }

        // 导出参数
        ExportParams exportParams = new ExportParams(null, "导出");

        // 导出 Excel
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, DemandCapacityDetailDto.class, personList);

        // 设置密码
        Sheet sheet = setPassword(workbook, 0);

        // 锁定列表列
        ArrayList<Integer> list = new ArrayList<>();
        ArrayList<Integer> columnList = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            list.add(i);
        }
        columnList.add(0);
        hideAndLockColumns(sheet, list, columnList, workbook);

        // 设置响应头
        String fileName = URLEncoder.encode("人员信息.xlsx", "UTF-8");
        response.setHeader("content-Type", "application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        response.setCharacterEncoding("UTF-8");

        // 输出到客户端
        workbook.write(response.getOutputStream());
        workbook.close();
    }


    public ResponseEntity<byte[]> export() {
        try {
            // 获取文件,并返回给浏览器
            File file = ResourceUtils.getFile("classpath:签约电动导出模板.xlsx");
            byte[] fileContent = FileUtil.readBytes(file);
            // 设置响应头，告诉浏览器这是一个附件文件
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(fileContent.length);
            headers.setContentDispositionFormData("attachment", new String((file.getName()).getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            // 返回文件字节数组
            return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
