package org.wenchen.demo.util;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 *  函数工具
 * @author earl
 * @date 2020-07-02
 */
public final class  FunctionUtil  {

    private  FunctionUtil(){}

    /**
     * 函数式接口执行代码
     * @param match  条件匹配，若匹配执行ExecFunction代码
     * @param function  执行的函数式代码
     */
    public static void matchExec(boolean match, ExecFunction function) {
        if (!match) {
            return  ;
        }
        function.exec();
    }

    /**
     * 函数式接口执行代码
     * @param match  条件匹配，若匹配执行ExecReturnFunction代码
     * @param function  执行的函数式代码
     * @return 返回结果
     */
    public static <T> T matchExecReturn(boolean match, ExecReturnFunction <T> function) {
        if (!match) {
            return null;
        }
        return function.exec();
    }




    /**
     * 批量执行函数代码
     * 不确保线程安全（具体看使用场景）
     * @param functions  函数数组
     */
    public static void batchExec(List<FuncBO> functions) {
        functions.stream().filter(data -> Objects.equals(true, data.getRight())).forEach(data->data.getFunc().exec());
    }


    /**
     * 函数式接口执行代码 ( Predicate利用支持泛型方式 )
     * @param predicate  Predicate接口函数
     * @param obj     Predicate 对应的泛型对象
     * @param function  执行的函数式代码
     */
    public static <T> void  matchExec(Predicate <T> predicate,T obj, ExecFunction function) {
        if (!predicate.test(obj)) {
            return  ;
        }
        function.exec();
    }



}

