package org.wenchen.demo;

import com.github.yulichang.toolkit.JoinWrappers;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.wenchen.demo.domain.Address;
import org.wenchen.demo.domain.User;
import org.wenchen.demo.domain.dto.UserDTO;
import org.wenchen.demo.mapper.mpMapper.UserMapper;

import java.util.List;

@SpringBootTest
@RequiredArgsConstructor
class DemoApplicationTests {

    private final UserMapper userMapper;

    @Test
    void contextLoads() {
        List<User> list = JoinWrappers.lambda(User.class)
                .selectAll(User.class)
                .selectAs(Address::getAddress, UserDTO::getAddress)
                .selectAs(Address::getCity, UserDTO::getCity)
                .leftJoin(Address.class, Address::getUserId, User::getId)
                .eq(User::getId, 1L).list();
        System.out.println(list);
        userMapper.selectList(null);
    }

    @Test
    void test() {
        User user = new User();
        user.setId(1L);
        user.setName("wenchen");
        user.setAge(18);
        user.setEmail("wenchen@qq.com");
        System.out.println(user.toDto());
    }
}
