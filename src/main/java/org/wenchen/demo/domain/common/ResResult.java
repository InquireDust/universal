package org.wenchen.demo.domain.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 响应包装类
 *
 * @author dejavu
 * @date 2020/1/22 15:26
 */
@Getter
@Setter
public class ResResult<T> implements Serializable {
    private static final long serialVersionUID = -3041700282408360384L;
    private String msg = "success";
    private int code = 0;
    private T data;

    public ResResult() {
        super();
    }

    public ResResult(int code) {
        this.code = code;
    }

    public ResResult(int code, T data) {
        this(code);
        this.data = data;
    }

    public ResResult(int code, String msg) {
        this(code);
        this.msg = msg;
    }

    public ResResult(int code, T data, String msg) {
        this(code, msg);
        this.data = data;
    }
}