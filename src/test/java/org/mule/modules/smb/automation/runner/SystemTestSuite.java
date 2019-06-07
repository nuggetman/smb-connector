package org.mule.modules.smb.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.mule.modules.smb.automation.system.SmbValidConnectivityTest;
import org.mule.modules.smb.automation.system.SmbInvalidConnectivityTest;

@RunWith(Suite.class)
@SuiteClasses({
    SmbValidConnectivityTest.class,
    SmbInvalidConnectivityTest.class
})

public class SystemTestSuite {

}