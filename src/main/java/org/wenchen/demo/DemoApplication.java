package org.wenchen.demo;

import org.dromara.easyes.starter.register.EsMapperScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Locale;

@SpringBootApplication
@MapperScan("org.wenchen.demo.mapper.mpMapper")
@EsMapperScan("org.wenchen.demo.mapper.esMapper")
public class DemoApplication {

    public static void main(String[] args) {
        String[] newArgs = args.clone();
        int defaultPort = 6789;
        boolean needChangePort = false;
        int newPort = defaultPort;

        // 判断8080端口是否占用
        if (isPortInUse(defaultPort)) {
            System.out.println("端口8080被占用，正在寻找可用端口...");
            newPort = findAvailablePort(defaultPort + 1); // 动态查找可用端口
            newArgs = new String[args.length + 1];
            System.arraycopy(args, 0, newArgs, 0, args.length);
            newArgs[args.length] = "--server.port=" + newPort;
            needChangePort = true;
        }

        ConfigurableApplicationContext run = SpringApplication.run(DemoApplication.class, newArgs);

        // 如果端口8080被占用并且当前使用的是其他端口，则释放8080并切换回8080端口
        if (needChangePort) {
            System.out.println("已切换到端口 " + newPort);

            try {
                if (isWindows()) {
                    killPortOnWindows(defaultPort);
                } else {
                    killPortOnUnix(defaultPort);
                }

                // 等待端口释放
                while (isPortInUse(defaultPort)) {
                    Thread.sleep(500);
                }

                System.out.println("8080端口释放成功");

                // 切换回8080端口
                switchPort(run, defaultPort);

            } catch (Exception e) {
                System.out.println("发生错误：" + e.getMessage());
            }
        }
    }

    private static boolean isPortInUse(int port) {
        try (Socket socket = new Socket("127.0.0.1", port)) {
            return true; // 端口被占用
        } catch (Exception e) {
            return false; // 端口未占用
        }
    }

    private static int findAvailablePort(int startPort) {
        int port = startPort;
        while (isPortInUse(port)) {
            port++;
        }
        return port;
    }

    private static void killPortOnUnix(int port) throws Exception {
        String command = String.format("lsof -i :%d | grep LISTEN | awk '{print $2}' | xargs kill -9", port);
        Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", command});
        process.waitFor();
        printProcessOutput(process);
    }

    private static void killPortOnWindows(int port) throws Exception {
        String findCommand = String.format("netstat -ano | findstr :%d", port);
        Process findProcess = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", findCommand});
        BufferedReader reader = new BufferedReader(new InputStreamReader(findProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] tokens = line.trim().split("\\s+");
            if (tokens.length > 4) {
                String pid = tokens[4];
                String killCommand = "taskkill /F /PID " + pid;
                Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", killCommand}).waitFor();
            }
        }
    }

    private static void switchPort(ConfigurableApplicationContext run, int newPort) {
        TomcatServletWebServerFactory tomcatFactory = run.getBean(TomcatServletWebServerFactory.class);
        tomcatFactory.setPort(newPort);

        System.out.println("切换到端口 " + newPort);

        try {
            run.close();
            SpringApplication.run(DemoApplication.class, new String[]{"--server.port=" + newPort});
        } catch (Exception e) {
            System.out.println("重新启动失败：" + e.getMessage());
        }
    }

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase(Locale.ROOT).contains("win");
    }

    private static void printProcessOutput(Process process) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    }
}
