package org.wenchen.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wenchen.demo.domain.TreeTable;
import org.wenchen.demo.domain.User;
import org.wenchen.demo.domain.dto.TableADTO;
import org.wenchen.demo.domain.dto.UserDTO;
import org.wenchen.demo.service.UserService;

import java.util.List;

/**
 * Author: wen-chen
 * Date: 2024/9/14
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class DemoController {
    private final UserService userService;

    @GetMapping("/get")
    public UserDTO get() {
        return userService.get(1L);
    }

    @GetMapping("/list")
    public List<UserDTO> get2() {
        return userService.joinList();
    }

    @GetMapping("/list2")
    public List<TableADTO> test2() {
        return userService.selectCollection();
    }

    @GetMapping("/test")
    public UserDTO test() {
        User user = new User();
        user.setId(1L);
        user.setName("wenchen");
        user.setAge(18);
        user.setEmail("wenchen@qq.com");
        return user.toDto();
    }

    @GetMapping("/getDto")
    public UserDTO selectUserWithAddress() {
        return userService.selectUserWithAddress();
    }

    @GetMapping("/test3")
    public List<TreeTable> test3() {
        return userService.noteTree();
    }

    @GetMapping("/test4")
    public UserDTO test4() {
        return new UserDTO();
    }

}
