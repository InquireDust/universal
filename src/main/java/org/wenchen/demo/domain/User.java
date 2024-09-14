package org.wenchen.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.wenchen.demo.convert.UserConvert;
import org.wenchen.demo.domain.dto.UserDTO;
import org.wenchen.demo.function.EntityBaseFunction;

/**
 * &#064;Author:  wen-chen
 * &#064;date:  2024/9/14
 */
@Data
@TableName("user")
@AllArgsConstructor
@NoArgsConstructor
public class User implements EntityBaseFunction<UserDTO> {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    @Override
    public UserDTO toDto() {
        return UserConvert.CONVERT.convert(this);
    }
}