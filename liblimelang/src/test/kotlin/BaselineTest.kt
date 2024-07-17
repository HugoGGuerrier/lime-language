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
            val res = StringBuilder()
            val linesToDisplay = HashSet<Int>()
            for ((i, row) in rows.withIndex()) {
                when (row.tag) {
                    DiffRow.Tag.CHANGE, DiffRow.Tag.INSERT, DiffRow.Tag.DELETE -> {
                        linesToDisplay.addAll((i - 3)..(i + 3))
                    }
                    else -> Unit
                }
            }

            val sortedLines = linesToDisplay.filter { it >= 0 && it < rows.size }.sorted()
            if (sortedLines.isNotEmpty()) {
                var prev = sortedLines.first()
                for (i in sortedLines) {
                    if ((i - prev) > 1) {
                        res.appendLine()
                    }
                    val row = rows[i]
                    when (row.tag) {
                        DiffRow.Tag.CHANGE -> {
                            res.append("-").appendLine(row.oldLine)
                            res.append("+").appendLine(row.newLine)
                        }
                        DiffRow.Tag.INSERT -> {
                            res.append("+").appendLine(row.newLine)
                        }
                        DiffRow.Tag.DELETE -> {
                            res.append("-").appendLine(row.oldLine)
                        }
                        DiffRow.Tag.EQUAL -> {
                            res.append(" ").appendLine(row.oldLine)
                        }
                        else -> Unit
                    }
                    prev = i
                }
            }
            return res.toString().ifEmpty { null }
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
    fun output(
        v: Any?,
        newLine: Boolean = true,
    ) {
        if (newLine) {
            testOutput.appendLine(v)
        } else {
            testOutput.append(v)
        }
    }

    /**
     * Compare the output of the current test and the baseline file content. If it doesn't match
     * then the test should fail and display the differences.
     * In a second time, rewrite the baseline file with the new test output if required.
     */
    @AfterEach
    fun compareOutput() {
        // Compute the diff message
        val diffMessage =
            computeDiffMessage(diffRowGenerator.generateDiffRows(baseline, testOutput.lines()))

        if (diffMessage != null) {
            // If required, write the baseline file
            if (shouldRewrite) {
                baselineFile.writeText(testOutput.toString())
            }

            // Finally clean the test output and fail if needed
            testOutput.clear()
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
