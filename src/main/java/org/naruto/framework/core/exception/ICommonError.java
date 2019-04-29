package org.naruto.framework.core.exception;

public interface ICommonError {
    public int getErrCode();
    public String getErrMsg();

    public ICommonError setErrMsg(String errMsg);
}
