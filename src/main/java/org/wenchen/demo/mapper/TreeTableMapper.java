package org.wenchen.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.wenchen.demo.domain.TreeTable;

import java.util.List;

@Mapper
public interface TreeTableMapper extends BaseMapper<TreeTable> {
    /**
     * 获取树形结构数据
     *
     * @return 树形结构
     */
    public List<TreeTable> noteTree();
}
