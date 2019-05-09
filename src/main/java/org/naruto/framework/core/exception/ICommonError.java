package org.naruto.framework.core.exception;

public interface ICommonError {
    public String getErrCode();
    public String getErrMsg();

    public ICommonError setErrMsg(String errMsg);
}
