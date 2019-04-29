package org.naruto.framework.core.web;

import org.naruto.framework.core.exception.EmServiceError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultEntity exceptionHandler(Exception ex){
        if(ex instanceof ServiceException) {
            return ResultEntity.fail((EmServiceError)((ServiceException) ex).getCommonError());
        }else{
            return ResultEntity.fail(EmServiceError.UNKNOW_ERROR);
        }
    }
}
