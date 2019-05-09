package org.naruto.framework.core.web;

import lombok.extern.slf4j.Slf4j;
import org.naruto.framework.core.exception.EmServiceError;
import org.naruto.framework.core.exception.ServiceException;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultEntity methodArgumentNotValidExceptionHandler(Exception ex) {
        MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
        StringBuffer errorMsgs = new StringBuffer();
        exception.getBindingResult().getAllErrors().forEach(fieldError -> {

            errorMsgs.append(fieldError.getDefaultMessage() + ";");
        });

        ServiceException serviceException = new ServiceException(EmServiceError.PARAMETER_VALIDATION_ERROR);
        serviceException.setErrMsg(errorMsgs.toString());
        return ResultEntity.fail(serviceException);
    }

    @ResponseBody
    @ExceptionHandler(value = ServiceException.class)
    public ResultEntity serviceExceptionHandler(Exception ex) {
        return ResultEntity.fail((ServiceException) ex);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ResultEntity exceptionHandler(Exception ex) {
        return ResultEntity.fail(new ServiceException(EmServiceError.UNKNOW_ERROR));
    }
}
