package org.naruto.framework.captcha;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.naruto.framework.captcha.CaptchaServiceTest;
import org.naruto.framework.captcha.CaptchaControllerTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({CaptchaControllerTest.class, CaptchaServiceTest.class})
public class CaptchaTestSuite {

}
