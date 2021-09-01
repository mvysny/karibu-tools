package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.vaadin.flow.data.provider.ListDataProvider
import kotlin.test.expect

fun DynaNodeGroup.dataProviderUtilsTests() {
    test("fetchAll()") {
        val list = (0..10000).toList()
        expect(list) { ListDataProvider(list).fetchAll() }
    }
}
