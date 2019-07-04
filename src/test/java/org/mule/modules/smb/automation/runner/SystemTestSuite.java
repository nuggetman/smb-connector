package org.mule.modules.smb.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.smb.automation.system.InvalidValuesConnectivityTest;
import org.mule.modules.smb.automation.system.ValidValuesConnectivityTest;
import org.mule.modules.smb.automation.system.ZeroFileageConnectivityTest;

@RunWith(Suite.class)
@SuiteClasses({
	InvalidValuesConnectivityTest.class,
    ValidValuesConnectivityTest.class,
    ZeroFileageConnectivityTest.class
})

public class SystemTestSuite {

}