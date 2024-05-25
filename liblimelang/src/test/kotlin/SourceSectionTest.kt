import com.limelanguage.Source
import com.limelanguage.SourceLocation
import com.limelanguage.SourceSection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SourceSectionTest {
    @Test
    fun getLines() {
        // Create a source
        val file = getResourceFile("hello_world.lime")
        assertTrue(file.isFile)
        val source = Source(file.canonicalPath, file.readText())

        // Create the source section
        val section = SourceSection(source, SourceLocation(2, 1), SourceLocation(4, 2))
        assertEquals(
            listOf(
                "fun main() {",
                "    println(\"Hello world!\")",
                "}",
            ),
            section.getLines(),
        )
    }
}
