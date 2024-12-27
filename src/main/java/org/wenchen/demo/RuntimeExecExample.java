package org.wenchen.demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RuntimeExecExample {
    public static void main(String[] args) {
        try {
            // 执行命令
            Process process = Runtime.getRuntime().exec("ls"); // Linux 下使用 "ls"，Windows 下可换为 "dir"

            // 获取命令输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
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
