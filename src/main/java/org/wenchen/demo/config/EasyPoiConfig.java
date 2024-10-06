package org.wenchen.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: wen-chen
 * Date: 2024/9/23
 */
@Configuration
public class EasyPoiConfig {
    @Bean
    public Map<String, String> statusDict() {
        Map<String, String> dict = new HashMap<>();
        dict.put("已启用", "1");
        dict.put("未启用", "0");
        return dict;
    }

    @Bean
    public Map<String, String> historyDict() {
        Map<String, String> dict = new HashMap<>();
        dict.put("是", "1");
        dict.put("否", "0");
        return dict;
    }
}
