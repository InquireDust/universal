package org.wenchen.demo.util;

/**
 * 执行代码的并返回结果的函数式接口
 * @author earl
 * @date 2020-07-15
 */
@FunctionalInterface
public interface ExecReturnFunction <T> {

     T exec( );

}
