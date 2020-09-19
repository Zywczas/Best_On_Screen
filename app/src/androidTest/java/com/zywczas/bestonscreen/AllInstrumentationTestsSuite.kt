package com.zywczas.bestonscreen

import com.zywczas.bestonscreen.model.db.MovieDaoTest
import com.zywczas.bestonscreen.views.DBActivityTest
import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    DBActivityTest::class,
    MovieDaoTest::class
)
class AllInstrumentationTestsSuite