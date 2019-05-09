package org.naruto.framework.core.exception;


public enum EmServiceError implements ICommonError{

    PARAMETER_VALIDATION_ERROR("00001","Invalid Parameter") ,
    UNKNOW_ERROR("10000","UnKnow Service Error"),
    CAPTCHA_SERVICE_ERROR("CAPTCHA_10000","Service Error")
    ;

    private String errCode;
    private String errMsg;

    @Override
    public  String getErrCode() {
        return this.errCode;
    }

    @Override
    public String getErrMsg() {
        return this.errMsg;
    }


    EmServiceError(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public EmServiceError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
