package org.wenchen.demo.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.*;
import org.wenchen.demo.domain.User;
import org.wenchen.demo.domain.dto.UserDTO;

/**
 * Author: wen-chen
 * Date: 2024/9/14
 * @author wen_chen
 */
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {

    @Select("SELECT u.id AS user_id, u.name, u.age, u.email, a.city, a.address " +
            "FROM user u " +
            "LEFT JOIN address a ON u.id = a.user_id " +
            "WHERE u.id = #{userId}")
    @Results({
            @Result(column = "u.id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "age", property = "age"),
            @Result(column = "email", property = "email"),
            @Result(column = "city", property = "city"),
            @Result(column = "address", property = "address")
    })
    UserDTO selectUserWithAddress(@Param("userId") Long userId);
}
