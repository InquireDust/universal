package org.wenchen.demo;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.pdf.PdfWriter;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;

import java.io.*;
import java.util.List;

public class MarkdownToPdfConverter {

    public static void main(String[] args) {
        // Markdown 文件路径
        String markdownFilePath = "test1.md";
        // 输出 PDF 文件路径
        String pdfOutputPath = "example.pdf";

        try {
            // 1. 读取 Markdown 文件内容
            String markdownContent = readFile(markdownFilePath);
            System.out.println("markdownContent::" + markdownContent);
            // 2. 转换 Markdown 为 HTML
            String htmlContent = convertMarkdownToHtml(markdownContent);
            System.out.println("htmlContent::" + htmlContent);
            // 3. 将 HTML 转换为 PDF
            convertHtmlToPdf(htmlContent, pdfOutputPath);

            System.out.println("PDF 文件生成成功：" + pdfOutputPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     */
    private static String readFile(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    /**
     * 将 Markdown 转换为 HTML
     */
    private static String convertMarkdownToHtml(String markdown) {
        // 配置 Flexmark
        MutableDataSet options = new MutableDataSet();
        Parser parser = Parser.builder(options).build();
        HtmlRenderer renderer = HtmlRenderer.builder(options).build();

        // 解析并渲染 Markdown 为 HTML
        return renderer.render(parser.parse(markdown));
    }

    /**
     * 将 HTML 转换为 PDF
     */
    private static void convertHtmlToPdf(String htmlContent, String outputPath) throws DocumentException, IOException {
        // 创建 PDF 文档对象
        Document document = new Document();

        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
            // 创建 PdfWriter 实例
            PdfWriter.getInstance(document, fos);
            // 打开文档
            document.open();
            // 使用 HTMLWorker 将 HTML 添加到文档中
            List<Element> elements = HTMLWorker.parseToList(new StringReader(htmlContent), null);
            for (Element element : elements) {
                document.add(element);
            }
        }
    }
}
