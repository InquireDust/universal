package org.wenchen.demo.domain.param;

import cn.hutool.core.util.PageUtil;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 分页查询参数
 */
@Getter
@Setter
public class PageParam implements Serializable {

    private static final long serialVersionUID = 7489222986629492487L;
    /**
     * 当前页
     */
    private int current = 1;

    /**
     * 每页显示条数，默认 10
     */
    private int size = 10;

    /**
     * 开始条数
     */
    public int start(){
        // hutool 的下标从0开始
        return PageUtil.transToStartEnd(current-1,size)[0];
    }

    /**
     * 结束条数
     */
    public int end(){
        // hutool 的下标从0开始
        return PageUtil.transToStartEnd(current-1,size)[1];
    }

}
