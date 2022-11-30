package com.github.mvysny.kaributools

import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.vaadin.flow.data.provider.ListDataProvider
import kotlin.test.expect

@DynaTestDsl
fun DynaNodeGroup.dataProviderUtilsTests() {
    test("fetchAll()") {
        val list = (0..10000).toList()
        expect(list) { ListDataProvider(list).fetchAll() }
    }
}
