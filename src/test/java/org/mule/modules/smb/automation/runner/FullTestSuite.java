/**
 * Copyright 2018-2020 (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */
package org.mule.modules.smb.automation.runner;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ UnitTestSuite.class, FunctionalTestSuite.class, FunctionalAnonymousTestSuite.class,
        FunctionalGuestTestSuite.class, SystemTestSuite.class })

public class FullTestSuite {

}