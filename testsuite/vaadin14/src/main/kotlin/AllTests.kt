package com.github.mvysny.kaributools

import com.vaadin.flow.component.checkbox.Checkbox
import com.vaadin.flow.component.datepicker.DatePicker
import com.vaadin.flow.component.html.Div
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.data.provider.ListDataProvider
import com.vaadin.flow.data.provider.SortDirection
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractAllTests {
    @Nested inner class BrowserTimeZoneTests : AbstractBrowserTimeZoneTests()
    @Nested inner class ComponentUtilsTests : AbstractComponentUtilsTests()
    @Nested inner class ButtonsTests : AbstractButtonsTests()
    @Nested inner class DataProviderUtilsTests {
        @Test fun fetchAll() {
            val list = (0..10000).toList()
            expect(list) { ListDataProvider(list).fetchAll() }
        }
        @Test fun `property sort orders`() {
            expect("fullName" to SortDirection.ASCENDING) { Person::fullName.asc.let { it.sorted to it.direction } }
            expect("fullName" to SortDirection.DESCENDING) { Person::fullName.desc.let { it.sorted to it.direction } }
        }
    }
    @Nested inner class DatePickerTests {
        @Test fun prefixComponent() {
            val dp = DatePicker()
            expect(null) { dp.prefixComponent }
            val div = Div()
            dp.prefixComponent = div
            expect(div) { dp.prefixComponent }
            dp.prefixComponent = null
            expect(null) { dp.prefixComponent }
        }
    }
    @Nested inner class depthFirstTreeIteratorTests : AbstractDepthFirstTreeIteratorTests()
    @Nested inner class DialogUtilsTests : AbstractDialogUtilsTests()
    @Nested inner class ElementUtilsTests : AbstractElementUtilsTests()
    @Nested inner class GridUtilsTests : AbstractGridUtilsTests()
    @Nested inner class IconUtilsTests : AbstractIconUtilsTests()
    @Nested inner class MenuBarUtilsTests {
        @Test fun smoke() {
            MenuBar().close()
            MenuBar().addIconItem(VaadinIcon.ABACUS.create()).subMenu.addIconItem(VaadinIcon.MENU.create())
            MenuBar().addIconItem(VaadinIcon.ABACUS.create(), "Foo").subMenu.addIconItem(VaadinIcon.MENU.create(), "Bar")
        }
    }
    @Nested inner class RouterUtilsTests : AbstractRouterUtilsTests()
    @Nested inner class ShortcutsTests : AbstractShortcutsTests()
    @Nested inner class TextFieldUtilsTests : AbstractTextFieldUtilsTests()
    @Nested inner class RenderersTests : AbstractRenderersTests()
    @Nested inner class NotificationsTests : AbstractNotificationsTests()
    @Nested inner class UploadTests : AbstractUploadTests()
    @Nested inner class LoginUtilsTests : AbstractLoginUtilsTests()
    @Nested inner class RadioButtonTests : AbstractRadioButtonsTests()
    @Nested inner class HtmlSpanTests : AbstractHtmlSpanTests()
    @Nested inner class ComboBoxTests : AbstractComboBoxTests()
    @Nested inner class LabelWrapperTests : AbstractLabelWrapperTests()
    @Nested inner class SelectsTests : AbstractSelectsTests()
    @Nested inner class ListBoxTests : AbstractListBoxTests()
    @Nested inner class BadgeTests {
        @Test fun smoke() {
            Badge()
            Badge("Foo").addThemeVariants(BadgeVariant.PRIMARY, BadgeVariant.ERROR, BadgeVariant.SMALL, BadgeVariant.PILL)
        }
        @Test fun themeVariants() {
            val badge = Badge("Foo")
            badge.addThemeVariants(BadgeVariant.PRIMARY, BadgeVariant.PILL)
            expect(setOf("badge", "primary", "pill")) { badge.themeNames.toSet() }
            badge.removeThemeVariants(BadgeVariant.PRIMARY)
            expect(setOf("badge", "pill")) { badge.themeNames.toSet() }
        }
    }
    @Nested inner class CheckboxTests {
        @Test fun serverClick() {
            val cb = Checkbox()
            var clicked = 0
            cb.addClickListener { clicked++ }
            cb.serverClick()
            expect(1) { clicked }
        }
    }
    @Nested inner class ValidatorsTests : AbstractValidatorsTests()
    @Nested inner class StreamResourceTests : AbstractStreamResourceUtilsTests()
    @Nested inner class FormLayoutTests : AbstractFormLayoutTests()
}
