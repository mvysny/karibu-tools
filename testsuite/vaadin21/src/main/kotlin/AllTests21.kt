import com.github.mvysny.dynatest.DynaNodeGroup
import java.io.File
import java.util.*

fun DynaNodeGroup.allTests21() {
    group("Component Utils") {
        componentUtils21Tests()
    }
}
