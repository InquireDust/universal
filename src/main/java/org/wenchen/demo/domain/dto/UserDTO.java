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
}
