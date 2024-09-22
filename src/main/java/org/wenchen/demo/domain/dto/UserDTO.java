package org.wenchen.demo.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author:  wen-chen
 * &#064;Date:  2024/9/14
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private Integer age;
    private String email;

    private String city;
    private String address;

    public String getStudent() {
        StringBuilder stu = new StringBuilder("student---");
        for (int i = 0; i < 10; i++) {
            stu.append("*");
        }
        return stu.toString();
    }
}
