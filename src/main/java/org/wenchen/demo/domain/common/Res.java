package org.wenchen.demo.domain.common;


/**
 * 返回工具类
 *
 * @author dejavu
 * @date 2020/1/22 15:29
 */
public class Res {

    private final static String SUCCESS = "success";

    private final static String FAILED = "failed";

    public static <T> ResResult<T> ok() {
        return new ResResult<T>(0, SUCCESS);
    }

    public static <T> ResResult<T> okAndMsg(String message) {
        return new ResResult<T>(0, message);
    }

    public static <T> ResResult<T> ok(T data) {
        return new ResResult<T>(0, data, SUCCESS);
    }

    public static <T> ResResult<T> error() {
        return new ResResult<T>(1, FAILED);
    }

    public static <T> ResResult<T> error(String message) {
        return new ResResult<T>(1, message);
    }

    public static <T> ResResult<T> response(int code, String msg) {
        return new ResResult<T>(code, msg);
    }

    public static <T> ResResult<T> response(int code, String msg, String traceId) {
        return new ErrorResult<T>(code, msg, traceId);
    }

    public static <T> ResResult<T> response(int code, String msg, T data) {
        return new ResResult<T>(code, data, msg);
    }


}
