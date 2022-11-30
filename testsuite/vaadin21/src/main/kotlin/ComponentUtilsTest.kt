import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributools.label
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasLabel
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.textfield.TextField
import kotlin.test.expect

@Tag("div")
class MyComponent : Component(), HasLabel {
    private var l: String = "default_label"
    override fun getLabel(): String = l
    override fun setLabel(label: String) {
        l = label
    }
}

@DynaTestDsl
fun DynaNodeGroup.componentUtils21Tests() {
    group("label") {
        test("TextField") {
            val c: Component = TextField()
            expect("") { c.label }
            c.label = "foo"
            expect("foo") { c.label }
            c.label = ""
            expect("") { c.label }
        }
        test("HasLabel") {
            val c = MyComponent()
            expect("default_label") { c.label }
            c.label = "foo"
            expect("foo") { c.label }
            c.label = ""
            expect("") { c.label }
        }
    }
}
