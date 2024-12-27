package org.wenchen.demo;

import cn.idev.excel.FastExcel;

import java.io.File;

/**
 * @author: wen-chen
 * @date: 2024/12/27
 */
public class Excel2pdf {
    public static void main(String[] args) {
        FastExcel.convertToPdf(new File("人员信息.xls"), new File("pdf.pdf"), null, null);
    }
}
