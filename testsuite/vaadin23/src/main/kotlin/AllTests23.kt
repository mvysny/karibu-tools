import com.github.mvysny.dynatest.DynaNodeGroup
import com.github.mvysny.dynatest.DynaTestDsl
import java.io.File
import java.util.*

@DynaTestDsl
fun DynaNodeGroup.allTests23() {
    group("TabSheet") {
        tabSheetTest()
    }
}
