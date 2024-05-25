import com.limelanguage.Source
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class SourceTest {
    @Test
    fun getSourceFile() {
        // Get the "hello_world.lime" file from the resources
        val file = getResourceFile("hello_world.lime")
        assertTrue(file.isFile)

        // Create a source and verify the source file getting
        val source = Source(file.canonicalPath, file.readText())
        assertNotNull(source.sourceFile)
        assertEquals(file.canonicalPath, source.sourceFile?.canonicalPath)

        // Create a source which doesn't correspond to a file and test this
        val otherSource = Source("<not_a_file>", "xxx")
        assertNull(otherSource.sourceFile)
    }

    @Test
    fun getLines() {
        // Get the "hello_world.lime" file from the resources
        val file = getResourceFile("hello_world.lime")
        assertTrue(file.isFile)

        // Create the source file and get the lines
        val source = Source(file.canonicalPath, file.readText())
        assertEquals(
            listOf(
                "/** Display \"Hello world!\" in Lime */",
                "fun main() {",
                "    println(\"Hello world!\")",
                "}",
                "",
            ),
            source.getLines(),
        )
    }

    @Test
    fun getSeveralLines() {
        // Get the "hello_world.lime" file from the resources
        val file = getResourceFile("hello_world.lime")
        assertTrue(file.isFile)

        // Create the source file
        val source = Source(file.canonicalPath, file.readText())
        assertEquals(
            listOf("/** Display \"Hello world!\" in Lime */"),
            source.getLines(1, 1),
        )
        assertEquals(
            listOf(
                "fun main() {",
                "    println(\"Hello world!\")",
                "}",
            ),
            source.getLines(2, 3),
        )
    }

    @Test
    fun testEquals() {
        // Create sources
        val s1 = Source("first", "xxx")
        val s2 = Source("second", "xxx")
        val otherS2 = Source("second", "yyy")

        // Check equalities
        assertNotEquals(s1, s2)
        assertNotEquals(s1, otherS2)
        assertEquals(s2, otherS2)
    }

    @Test
    fun testHashCode() {
        // Create sources
        val s1 = Source("my_source", "xxx")

        // Check hashing
        assertEquals("my_source".hashCode(), s1.hashCode())
    }
}
