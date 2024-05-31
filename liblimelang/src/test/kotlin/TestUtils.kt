import java.io.File
import kotlin.io.path.Path

/** Get given [filepath] from the test resource directory. */
fun getResourceFile(vararg filepath: String): File =
    Path("src", "test", "resources", *filepath).toFile()
