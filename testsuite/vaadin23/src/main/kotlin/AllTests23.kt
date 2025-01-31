import com.github.mvysny.kaributesting.v10.MockVaadin
import com.github.mvysny.kaributools.template
import com.vaadin.flow.data.renderer.LitRenderer
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.expect

abstract class AbstractAllTests23 {
    @Nested inner class TabSheetTests : AbstractTabSheetTests()
    @Nested inner class LitRendererTests {
        @BeforeEach fun fakeVaadin() { MockVaadin.setup() }
        @AfterEach fun tearDownVaadin() { MockVaadin.tearDown() }
        @Test fun testRenderer() {
            data class Person(val name: String)
            val renderer = LitRenderer.of<Person>("<div>Name: \${item.name}</div>")
                .withProperty("name", Person::name)
            expect("<div>Name: \${item.name}</div>") { renderer.template }
        }
    }
}
