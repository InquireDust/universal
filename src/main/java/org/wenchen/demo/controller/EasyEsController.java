package org.wenchen.demo.controller;

import lombok.RequiredArgsConstructor;
import org.dromara.easyes.core.biz.EsPageInfo;
import org.dromara.easyes.core.conditions.select.LambdaEsQueryWrapper;
import org.dromara.easyes.core.conditions.update.LambdaEsUpdateWrapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenchen.demo.domain.Document;
import org.wenchen.demo.mapper.esMapper.DocumentEsMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Author: wen-chen
 * Date: 2024/10/7
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class EasyEsController {
    private final DocumentEsMapper documentEsMapper;

    @RequestMapping("/setEs")
    public Integer es() {
        Document document = new Document();
//        document.setId(String.valueOf((int) (Math.random() * 100)));
        document.setId(String.valueOf(1));
        // 此字段在ES中不存在
        document.setNotExistsField(null);
        // 非空字符串会被更新
        document.setCreator("Alice");
        document.setFiledData("This is some field data.");
        document.setUla("Wu-La");
        // 格式化当前时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);
        document.setGmtCreate(formattedDate);
        document.setContent("This is a test content for the document.");
        document.setCaseTest("TestCase");
        return documentEsMapper.insert(document);
    }

    @RequestMapping("/getEs")
    public Document getEs() {
        return documentEsMapper.selectById(1L);
    }

    @RequestMapping("/updateEs")
    public Integer updateEs() {
        Document document = new Document();
        document.setId("1");
        document.setContent("666");
        return documentEsMapper.updateById(document);
    }

    @RequestMapping("/deleteEs")
    public Integer deleteEs() {
        return documentEsMapper.delete(new LambdaEsUpdateWrapper<>());
//        return documentEsMapper.deleteById(1L);
    }

    @RequestMapping("/esList")
    public EsPageInfo<Document> deleteEsByField() {
        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
//        wrapper.like(Document::getCaseTest,"Test");
        wrapper.orderByAsc(Document::getId);
        wrapper.like(Document::getContent, "content");
//        return documentEsMapper.selectList(wrapper);
        return documentEsMapper.pageQuery(wrapper, 1, 100);
    }

    @RequestMapping("/{id}")
    public Document selectById(@PathVariable("id") Long id) {
        LambdaEsQueryWrapper<Document> wrapper = new LambdaEsQueryWrapper<>();
        wrapper.eq(Document::getId, id.toString());
        return documentEsMapper.selectOne(wrapper);
    }

}
