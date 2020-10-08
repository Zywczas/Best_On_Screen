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
//todo usunac te glupie activity test
//todo sprawdzic czy jak dam przy mockach @Mockk i init w kazdym before czy to mocno wydluzy czas testow
//todo Satisfying the dependency interface could take a lot of time. To reduce this pain, we could create and reuse mock objects across our multi module project.