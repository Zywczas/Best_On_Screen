package com.zywczas.bestonscreen

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.platform.runner.JUnitPlatform
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.SuiteDisplayName
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
@SelectPackages("com.zywczas.bestonscreen.model")
class AllTestsSuiteTest {
}