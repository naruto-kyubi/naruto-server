package org.naruto.framework.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultEntity<T> {

    //返回状态码
    private String status;
    //返回消息；
    private String message;
    //返回数据；
    private T data;

    public static ResultEntity ok(Object data){
        return new ResultEntity("ok","success",data);
    }

    public static ResultEntity error(String status,String message,Object data){
        return new ResultEntity(status,message,data);
    }
}
