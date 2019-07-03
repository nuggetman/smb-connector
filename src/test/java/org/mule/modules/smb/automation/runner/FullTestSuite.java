package org.mule.modules.smb.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	UnitTestSuite.class,
	SystemTestSuite.class,
	FunctionalTestSuite.class,
	FunctionalAnonymousTestSuite.class,
	FunctionalGuestTestSuite.class
})

public class FullTestSuite {

}