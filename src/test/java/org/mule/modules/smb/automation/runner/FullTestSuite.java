package org.mule.modules.smb.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    FunctionalTestSuite.class,
    FunctionalTestSuiteAnonymous.class
})

public class FullTestSuite {

}