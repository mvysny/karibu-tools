package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaTest
import com.vaadin.flow.data.provider.ListDataProvider
import kotlin.test.expect

class DataProviderUtilsTest : DynaTest({
    test("fetchAll()") {
        val list = (0..10000).toList()
        expect(list) { ListDataProvider(list).fetchAll() }
    }
})
