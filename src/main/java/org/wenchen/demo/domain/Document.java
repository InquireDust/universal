package org.wenchen.demo.domain;

import lombok.Data;
import org.dromara.easyes.annotation.HighLight;
import org.dromara.easyes.annotation.IndexField;
import org.dromara.easyes.annotation.IndexId;
import org.dromara.easyes.annotation.IndexName;
import org.dromara.easyes.annotation.rely.Analyzer;
import org.dromara.easyes.annotation.rely.FieldStrategy;
import org.dromara.easyes.annotation.rely.FieldType;
import org.dromara.easyes.annotation.rely.IdType;

/**
 * Author: wen-chen
 * Date: 2024/10/7
 */
@Data
@IndexName("document_my_test")
public class Document {

    @IndexId(type = IdType.CUSTOMIZE)
//    @IndexField(fieldType = FieldType.KEYWORD)
    private String id;

    // 场景一:标记es中不存在的字段
    @IndexField(exist = false)
    private String notExistsField;

    // 场景二:更新时,此字段非空字符串才会被更新
    @IndexField(strategy = FieldStrategy.NOT_EMPTY)
    private String creator;

    // 场景三: 指定fieldData
    @IndexField(fieldType = FieldType.TEXT, fieldData = true)
    private String filedData;

    // 场景四:自定义字段名
    @IndexField("wu-la")
    private String ula;

    // 场景五:支持日期字段在es索引中的format类型
    @IndexField(fieldType = FieldType.DATE, dateFormat = "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis")
    private String gmtCreate;

    // 场景六:支持指定字段在es索引中的分词器类型
    @HighLight(preTag = "<div>", postTag = "</div>")
    @IndexField(fieldType = FieldType.TEXT, analyzer = Analyzer.IK_SMART, searchAnalyzer = Analyzer.IK_MAX_WORD)
    private String content;

    // 场景七：支持指定字段在es的索引中忽略大小写,以便在term查询时不区分大小写,仅对keyword类型字段生效,es的规则,并非框架限制.
    @HighLight(preTag = "<div>", postTag = "</div>")
    @IndexField(fieldType = FieldType.KEYWORD, ignoreCase = true)
    private String caseTest;

}
