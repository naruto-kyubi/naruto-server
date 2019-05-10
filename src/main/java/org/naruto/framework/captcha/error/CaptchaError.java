package org.naruto.framework.captcha.error;

import lombok.NoArgsConstructor;
import org.naruto.framework.core.exception.CommonError;
import org.naruto.framework.core.exception.ICommonError;

public class CaptchaError extends CommonError {
    public static CommonError CAPTCHA_SERVICE_ERROR = new CaptchaError("captcha.unknown.error","Service Error");
    public static CommonError CAPTCHA_INCORRECT_ERROR = new CaptchaError("captcha.incorrect.error","incorrect captcha code.");
    public static CommonError CAPTCHA_TIMEOUT_ERROR = new CaptchaError("captcha.timeout.error","captcha timeout.");

    private CaptchaError(String errCode, String errMsg) {
        super(errCode,errMsg);
    }
}
