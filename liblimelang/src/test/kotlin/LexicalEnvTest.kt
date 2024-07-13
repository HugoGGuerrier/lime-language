import com.limelanguage.analysis.AnalysisContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class LexicalEnvTest : BaselineTest() {
    /** Context to parse lime sources. */
    val context: AnalysisContext = AnalysisContext()

    @Test
    fun funDeclLexEnv() {
        expectOk("fun main() {}")
        expectOk("fun main(x: int) {}")
        expectOk("fun main(x: int) { const x = 0 }")
        expectOk("fun main(main: int) { const x = 0 }")
        expectOk("fun main() { main() }")

        expectErr("fun main() {} fun main() {}")
        expectErr("fun main(x: int, x: int) {}")
        expectErr("fun main(x: int) { const y = z }")
    }

    @Test
    fun constDeclLexEnv() {
        expectOk("const x = 0")
        expectOk("const x = 0 const y = x")

        expectErr("const x = x")
        expectErr("const x = 0 const x = 0")
        expectErr("const x = y")
    }

    @Test
    fun varDeclLexEnv() {
        expectOk("fun f() { var x = 0 }")
        expectOk("fun f() { var x = 0; var y = 0 }")
        expectOk("fun f() { var x = 0; var y = x }")

        expectErr("fun f() { var x = x }")
        expectErr("fun f() { var x = 0; var x = 0 }")
        expectErr("fun f() { var x = 0; var x = z }")
    }

    @Test
    fun blockExprLexEnv() {
        expectOk("const x = { var x; { var x; x } { var x } }")
    }

    // ----- Util functions -----

    /** Parse the given string and display its lexical environment. Fails if there is any diagnostics. */
    private fun expectOk(source: String) {
        output("==========")
        output(source)
        output("==========")
        val unit = context.analyseBuffer("testOk", source, reparse = true)
        if (unit.lexEnvDiagnostics.isNotEmpty()) {
            val message = StringBuilder("=== Unexpected lexical env error:").appendLine()
            unit.lexEnvDiagnostics.forEach { d -> message.appendLine(d) }
            fail(message.toString())
        } else {
            output(unit.rootNode!!.nodeLexicalEnvironment)
            output("")
        }
    }

    private fun expectErr(source: String) {
        output("==========")
        output(source)
        output("==========")
        val unit = context.analyseBuffer("testOk", source, reparse = true)
        if (unit.lexEnvDiagnostics.isNotEmpty()) {
            output("=== Expected parsing error(s):")
            unit.lexEnvDiagnostics.forEach {
                output(
                    """
                    ${it.message}
                    ${it.hints}
                    ${if (it.location != null) {
                        "at ${it.location!!.start.line}:${it.location!!.start.column}"
                    } else {
                        "(no location)"
                    }}
                    """.trimIndent(),
                )
            }
            output("=== Fallback lexical environment")
            output(unit.rootNode!!.nodeLexicalEnvironment)
            output("")
        } else {
            fail("Expecting a lexical environment error, got the lexenv: ${unit.rootNode!!.nodeLexicalEnvironment}")
        }
    }
}
