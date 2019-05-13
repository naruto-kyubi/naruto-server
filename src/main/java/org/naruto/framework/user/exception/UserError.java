package org.naruto.framework.user.exception;

import org.naruto.framework.captcha.error.CaptchaError;
import org.naruto.framework.core.exception.CommonError;

public class UserError extends CommonError {
    public static CommonError USER_EXIST_ERROR = new UserError("user.exist.error","User aleady exist");
    private UserError(String errCode, String errMsg) {
        super(errCode,errMsg);
    }
}
