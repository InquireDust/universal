package org.wenchen.demo.controller;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.wenchen.demo.domain.Person;
import org.wenchen.demo.domain.dto.ElectricSignDto;
import org.wenchen.demo.service.CommonTestServer;
import org.wenchen.demo.util.EasyPoiUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Author: wen-chen
 * Date: 2024/9/21
 */
@RestController
@RequestMapping("/poi")
@RequiredArgsConstructor
public class PoiController {
    private final CommonTestServer commonTestServer;

    @GetMapping("/export")
    public ResponseEntity<byte[]> export() {
        return commonTestServer.export();
    }

    @PostMapping("/inportfile")
    public void exportCheckbox(@RequestParam("file") MultipartFile file) throws Exception {
//        List<ElectricSignDto> signList = EasyPoiUtils.importExcel(file, ElectricSignDto.class);
        // 设置导入参数
        ImportParams params = new ImportParams();
        // 使用 EasyPoi 进行导入，将 Excel 转换为对象列表
        List<Object> importExcel = ExcelImportUtil.importExcel(file.getInputStream(), ElectricSignDto.class, null);
        System.out.println(importExcel);
    }

    @GetMapping("/easypoi/out")
    public void out(HttpServletResponse response) {
        try {
            commonTestServer.exportPersonList(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/easypoi/out/look")
    public void outLook(HttpServletResponse response) {
        try {
            commonTestServer.exportPersonListLook(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/easypoi/testutil/out")
    public void testUtilOut(HttpServletResponse response) throws IOException {
        commonTestServer.exportPersonListLookList(response);
    }


    @GetMapping("/exportPersonList")
    public ResponseEntity<byte[]> exportPersonList(HttpServletResponse response) throws IOException {
        // 模拟数据
        List<Person> personList = new ArrayList<>();
        personList.add(new Person(1, "张三", 25));
        personList.add(new Person(2, "李四", 30));
        personList.add(new Person(3, "王五", 28));
        return EasyPoiUtils.exportDefaultExcel(personList, "测试", null, Person.class, "xlsx");
    }

    @GetMapping("/easypoi/in")
    public void in() {
        try {
            commonTestServer.importPersonList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @GetMapping("/easypoi/word")
    public void pdf() throws FileNotFoundException {
        String oldText = "{oldText}";
        String newText = "--------newText------------";
        File file = ResourceUtils.getFile("classpath:test.docx");
        try (XWPFDocument document = new XWPFDocument(new FileInputStream(file))) {
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                for (XWPFRun run : paragraph.getRuns()) {
                    String text = run.getText(0);
                    if (text != null && text.contains(oldText)) {
                        text = text.replace(oldText, newText);
                        run.setText(text, 0);
                    }
                }
            }
            // 保存文档
            try (FileOutputStream out = new FileOutputStream("./newTest.docx")) {
                document.write(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    @GetMapping("/png")
//    public void png() {
//        String filePath = "path/to/your/document.docx";
//        String newImagePath = "path/to/your/new_image.png";
//
//        try (XWPFDocument document = new XWPFDocument(new FileInputStream(filePath))) {
//            // 删除现有图片
//            for (XWPFPictureData pictureData : document.getAllPictures()) {
//                document.removePicture(pictureData);
//            }
//
//            // 添加新图片
//            try (FileInputStream imageStream = new FileInputStream(newImagePath)) {
//                document.createPicture(imageStream, XWPFDocument.PICTURE_TYPE_PNG, imageStream.available(), 100, 100); // 调整宽高
//            }
//
//            // 保存文档
//            try (FileOutputStream out = new FileOutputStream("path/to/your/modified_document.docx")) {
//                document.write(out);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
