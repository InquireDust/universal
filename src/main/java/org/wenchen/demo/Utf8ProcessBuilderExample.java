package org.wenchen.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Utf8ProcessBuilderExample {
    public static void main(String[] args) {
        try {
            // 创建 ProcessBuilder 实例并设置命令
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("ping", "www.google.com");

            // 启动进程
            Process process = processBuilder.start();

            // 获取命令输出并指定为 UTF-8 编码
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待进程完成
            int exitCode = process.waitFor();
            System.out.println("Exit code: " + exitCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
