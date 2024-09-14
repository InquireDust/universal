package org.wenchen.demo.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.wenchen.demo.domain.User;

/**
 * Author: wen-chen
 * Date: 2024/9/14
 * @author wen_chen
 */
@Mapper
public interface UserMapper extends MPJBaseMapper<User> {
}
