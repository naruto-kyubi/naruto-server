package org.naruto.framework;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.naruto.framework.captcha.CaptchaControllerTest;
import org.naruto.framework.captcha.CaptchaServiceTest;
import org.naruto.framework.captcha.CaptchaTestSuite;

@RunWith(Suite.class)
@Suite.SuiteClasses({CaptchaTestSuite.class})
public class TestSuites {

}
