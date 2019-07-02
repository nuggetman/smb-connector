package org.mule.modules.smb.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.smb.automation.system.SmbValidConnectivityTest;

@RunWith(Suite.class)
@SuiteClasses({
    SmbValidConnectivityTest.class
})

public class SystemTestSuite {

}