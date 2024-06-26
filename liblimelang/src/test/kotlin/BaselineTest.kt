import com.github.difflib.text.DiffRow
import com.github.difflib.text.DiffRowGenerator
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInfo
import java.io.File
import java.nio.file.Paths

/** This class is the base of all testing classes which requires a baseline support. */
abstract class BaselineTest {
    companion object {
        /** Diff output handler */
        val diffRowGenerator: DiffRowGenerator =
            DiffRowGenerator.create()
                .showInlineDiffs(false)
                .build()

        val shouldRewrite: Boolean = System.getProperty("tests.rewriteBaselines").toBoolean()

        /** Get the baseline directory file object for the given directory name. */
        fun getBaselineDir(testInfo: TestInfo): File =
            getResourceFile(
                "baselines",
                testInfo.testClass.map { c -> c.simpleName }.orElse(""),
            )

        /** Create the baseline files directory if it doesn't exist. */
        @JvmStatic
        @BeforeAll
        fun createBaselineDir(testInfo: TestInfo) {
            getBaselineDir(testInfo).mkdir()
        }

        /**
         * Compute the difference message from the diff rows. If there is no differences, this
         * function returns null.
         */
        fun computeDiffMessage(rows: List<DiffRow>): String? {
            val resBuilder = StringBuilder()
            var emptyPrevious = true
            for (row in rows) {
                when (row.tag) {
                    DiffRow.Tag.CHANGE -> {
                        resBuilder.append("-").appendLine(row.oldLine)
                        resBuilder.append("+").appendLine(row.newLine)
                        emptyPrevious = false
                    }
                    DiffRow.Tag.INSERT -> {
                        resBuilder.append("+").appendLine(row.newLine)
                        emptyPrevious = false
                    }
                    DiffRow.Tag.DELETE -> {
                        resBuilder.append("-").appendLine(row.oldLine)
                        emptyPrevious = false
                    }
                    DiffRow.Tag.EQUAL -> {
                        if (!emptyPrevious) {
                            resBuilder.appendLine()
                            emptyPrevious = true
                        }
                    }
                    null -> Unit
                }
            }
            val res = resBuilder.toString()
            return res.ifEmpty { null }
        }
    }

    // ----- Attributes -----

    /** The baseline file to read and write. */
    lateinit var baselineFile: File

    /** The baseline of the current test. It can be null if the test hadn't been initialized. */
    var baseline: List<String>? = emptyList()

    /** Output buffer for the test */
    val testOutput: StringBuilder = StringBuilder()

    // ----- Instance methods -----

    /** Load the correct baseline file before each test. */
    @BeforeEach
    fun loadBaselineFile(testInfo: TestInfo) {
        // Get the baseline file to read
        val baselineDir = getBaselineDir(testInfo)
        val testName = testInfo.displayName.substringBefore('(')
        baselineFile = Paths.get(baselineDir.absolutePath, testName).toFile()

        // If the file exists, then read it
        if (baselineFile.exists() && baselineFile.canRead()) {
            baseline = baselineFile.readText().lines()
        } else {
            baselineFile.delete()
            baselineFile.createNewFile()
        }
    }

    /** Add the given value followed by a newline to the test output for later comparison. */
    fun output(v: Any) = testOutput.append(v).append('\n')

    /**
     * Compare the output of the current test and the baseline file content. If it doesn't match
     * then the test should fail and display the differences.
     * In a second time, rewrite the baseline file with the new test output if required.
     */
    @AfterEach
    fun compareOutput() {
        // If required, write the baseline file
        if (shouldRewrite) {
            baselineFile.writeText(testOutput.toString())
        }

        // Compute the diff message
        val diffMessage =
            computeDiffMessage(diffRowGenerator.generateDiffRows(baseline, testOutput.lines()))

        // Finally clean the test output and fail if needed
        testOutput.clear()
        if (diffMessage != null) {
            val message =
                StringBuilder("Differences in test output:")
                    .appendLine()
                    .appendLine("--- expected")
                    .appendLine("+++ output")
                    .appendLine()
                    .append(diffMessage)
            throw AssertionError(message.toString())
        }
    }
}
