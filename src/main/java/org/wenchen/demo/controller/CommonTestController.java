package org.wenchen.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenchen.demo.service.CommonTestServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: wen-chen
 * Date: 2024/9/18
 *
 * @author wen_chen
 */
@RestController
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonTestController {
    private final CommonTestServer commonTestServer;

    @GetMapping("/set")
    public String set() {
        ArrayList<Long> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add((long) i);
        }
        commonTestServer.saveCheckbox(list, 10L);
        return "ok";
    }

    @GetMapping("/get")
    public List<Long> get() {
        return commonTestServer.queryCheckbox(10L);
    }
}
