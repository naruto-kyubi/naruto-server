package org.naruto.framework.core.exception;

public class CommonError implements ICommonError{
    public static CommonError PARAMETER_VALIDATION_ERROR = new CommonError("sys.invalid-parameter.error","Invalid Parameter");
    public static CommonError UNKNOWN_ERROR = new CommonError("sys.unkown.error","unknow error");

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


    public CommonError(String errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public CommonError setErrMsg(String errMsg) {
        this.errMsg = errMsg;
        return this;
    }
}
