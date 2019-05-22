package org.naruto.framework.core.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.naruto.framework.core.exception.ServiceException;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultEntity<T> {

    private String status;
    private T data;
    private Object meta;

    public static ResultEntity ok(Object data){
        return new ResultEntity("ok",data,null);
    }

    public static ResultEntity ok(Object data,Object meta){
        return new ResultEntity("ok",data,meta);
    }

    public static ResultEntity fail(ServiceException serviceException){
        Map<String,Object> data = new HashMap<String,Object>();
        data.put("errCode",serviceException.getErrCode());
        data.put("errMsg",serviceException.getErrMsg());
        return new ResultEntity("fail",data,null);
    }
}
