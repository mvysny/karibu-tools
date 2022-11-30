package com.github.mvysny.kaributools.v22

import allTests21
import com.github.mvysny.dynatest.DynaTest
import com.github.mvysny.dynatest.jvmVersion
import com.github.mvysny.kaributesting.v10.VaadinMeta
import com.github.mvysny.kaributools.*
import java.io.File
import java.util.*
import kotlin.test.expect

class AllTests : DynaTest({
    // Vaadin 24+ requires JDK 17+
    if (jvmVersion >= 17) {
        test("vaadin version") {
            expect(24) { VaadinVersion.get.major }
            VaadinVersion.flow // smoke test that the call doesn't fail
        }

        test("vaadin version 2") {
            val gradleProps: Properties = File("../../gradle.properties").loadAsProperties()
            val expectedVaadinVersion: String = gradleProps["vaadin24_version"] as String
            expect(expectedVaadinVersion) { VaadinVersion.get.toString().replace('-', '.') }
        }

        group("vaadin14") {
            // todo skip tests which depend on Karibu-Testing, it doesn't support Vaadin 24 yet.
//            group("Browser Time Zone") {
//                browserTimeZoneTests()
//            }
//            group("Component Utils") {
//                componentUtilsTests()
//            }
            group("Buttons") {
                buttonsTests()
            }
            group("Data Provider Utils") {
                dataProviderUtilsTests()
            }
            group("Depth First Tree Iterator") {
                depthFirstTreeIteratorTests()
            }
//            group("Dialog Utils") {
//                dialogUtilsTests()
//            }
//            group("Element Utils") {
//                elementUtilsTests()
//            }
            group("Grid Utils") {
                gridUtilsTests()
            }
            group("Icon Utils") {
                iconUtilsTests()
            }
            group("Menu Bar Utils") {
                menuBarUtilsTests()
            }
//            group("Router Utils") {
//                routerUtilsTests()
//            }
            group("Shortcuts") {
                shortcutsTests()
            }
            group("Text Field Utils") {
                textFieldUtilsTests()
            }
            group("Renderers") {
                renderersTests()
            }
//            group("Notifications") {
//                notificationsTests()
//            }
//            group("Upload") {
//                uploadTests()
//            }
            group("AbstractLogin") {
                abstractLoginUtilsTests()
            }
            group("radio button") {
                radioButtonsTests()
            }
//            group("HtmlSpan") {
//                htmlSpanTests()
//            }
            group("Combobox") {
                comboboxesTests()
            }
//            group("LabelWrapper") {
//                labelWrapperTests()
//            }
            group("Select") {
                selectsTests()
            }
            group("ListBox") {
                listBoxTests()
            }
        }
        group("vaadin21+") {
            allTests21()
        }
    }
})
