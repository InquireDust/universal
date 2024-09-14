package org.wenchen.demo.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * &#064;Author:  wen-chen
 * &#064;Date:  2024/9/14
 */
@Data
@TableName("address")
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Long id;
    private Long userId;
    private String city;
    private String address;
}