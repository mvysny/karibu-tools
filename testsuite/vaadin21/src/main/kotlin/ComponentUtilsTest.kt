import com.github.mvysny.kaributools.label
import com.vaadin.flow.component.Component
import com.vaadin.flow.component.HasLabel
import com.vaadin.flow.component.Tag
import com.vaadin.flow.component.textfield.TextField
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

@Tag("div")
class MyComponent : Component(), HasLabel {
    private var l: String = "default_label"
    override fun getLabel(): String = l
    override fun setLabel(label: String) {
        l = label
    }
}

abstract class AbstractComponentUtils21Tests() {
    @Nested inner class LabelTests {
        @Test fun TextFieldTests() {
            val c: Component = TextField()
            expect("") { c.label }
            c.label = "foo"
            expect("foo") { c.label }
            c.label = ""
            expect("") { c.label }
        }
        @Test fun HasLabelTests() {
            val c = MyComponent()
            expect("default_label") { c.label }
            c.label = "foo"
            expect("foo") { c.label }
            c.label = ""
            expect("") { c.label }
        }
    }
}
