package org.wenchen.demo;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.PageSize;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfWriter;

import java.io.*;
import java.util.List;

public class HtmlToPdfConverter {

    public static void main(String[] args) {
        // HTML 文件路径
        String htmlFilePath = "test.html";
        // 输出 PDF 文件路径
        String pdfOutputPath = "output.pdf";

        try {
            // 1. 读取 HTML 文件内容
            String htmlContent = readFile(htmlFilePath);

            // 2. 将 HTML 内容转换为 PDF
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
     * 将 HTML 转换为 PDF
     */
    private static void convertHtmlToPdf(String htmlContent, String outputPath) throws DocumentException, IOException {
        // 创建 PDF 文档对象
        Document document = new Document(PageSize.A4);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(outputPath);
            PdfWriter writer = PdfWriter.getInstance(document, fos);
            document.open();

            // 使用 HTMLWorker 将 HTML 转换为 PDF
            StyleSheet styles = new StyleSheet();
            List<Element> elements = HTMLWorker.parseToList(new StringReader(htmlContent), styles);

            for (Element element : elements) {
                document.add(element);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (document != null) {
                document.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
    }
}
