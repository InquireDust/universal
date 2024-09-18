package org.wenchen.demo.domain.common;

import lombok.Getter;
import lombok.Setter;

/**
* 错误响应类,携带链路追踪标示 trackId
* @author dejavu
* @date 2021/9/9 
*/
@Getter
@Setter
public class ErrorResult<T> extends  ResResult<T>{

    /** 链路追踪标示 */
    private String traceId;

    public ErrorResult(int code, String msg, String traceId) {
        super(code,msg);
        this.traceId = traceId;
    }
}
