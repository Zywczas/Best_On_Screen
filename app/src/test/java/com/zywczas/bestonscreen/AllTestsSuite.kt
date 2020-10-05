package com.zywczas.bestonscreen

import org.junit.platform.runner.JUnitPlatform
import org.junit.platform.suite.api.SelectPackages
import org.junit.runner.RunWith

@RunWith(JUnitPlatform::class)
@SelectPackages(
    "com.zywczas.bestonscreen.model",
    "com.zywczas.bestonscreen.viewmodels",
    "com.zywczas.bestonscreen.utilities",
    "com.zywczas.bestonscreen.views"
)
class AllTestsSuite

//todo pousuwac importy