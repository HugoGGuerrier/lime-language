import com.limelanguage.analysis.AnalysisContext
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail

class ParserTest : BaselineTest() {
    /** Context to parse lime sources. */
    val context: AnalysisContext = AnalysisContext()

    @Test
    fun moduleParsing() {
        expectOk("")
        expectOk("const x = 0")
        expectOk(
            """
            const x = 0
            const y = 0
            """.trimIndent(),
        )
        expectOk(
            """
            fun test() {
                0
            }
            """.trimIndent(),
        )

        expectErr("0")
        expectErr(
            """
            const x = 0
            var z = 0
            """.trimIndent(),
        )
    }

    @Test
    fun funDeclParsing() {
        expectOk("fun test() { 0 }")
        expectOk("fun test(x: int) { 0 }")
        expectOk("fun test(x: int, y: int) { 0 }")
        expectOk("fun test(x: int, y: int,) { 0 }")
        expectOk("fun test(x: int, y: int = 0) { 0 }")
        expectOk("fun test() -> int { 0 }")

        expectErr("fun ()")
        expectErr("fun test")
        expectErr("fun test()")
        expectErr("fun test(x: int, y: int { 0 }")
    }

    @Test
    fun constDeclParsing() {
        expectOk("const x = 0")
        expectOk("const x: int = 0")

        expectErr("const = 0")
        expectErr("const x")
        expectErr("const x: = 0")
    }

    @Test
    fun varDeclParsing() {
        expectOk("fun main() { var x = 0 }")
        expectOk("fun main() { var x }")
        expectOk("fun main() { var x: int = 0 }")

        expectErr("fun main() { var = 0 }")
        expectErr("fun main() { var }")
        expectErr("fun main() { var x = }")
    }

    @Test
    fun varAffectParsing() {
        expectOk("fun main() { x = 0 }")

        expectErr("fun main() { x = }")
        expectErr("fun main() { x: int = 0 }")
    }

    @Test
    fun conditionalExprParsing() {
        expectOk("const x = if true {0} else {1}")
        expectOk("const x = if true {0}")

        expectErr("const x = if true else {1}")
        expectErr("const x = if true {0} else ")
        expectErr("const x = if {0} else {1}")
    }

    @Test
    fun blockExprParsing() {
        expectOk("const x = {}")
        expectOk("const x = { var x = 0; 0 }")
        expectOk("const x = { var x = 0; 0; }")
        expectOk(
            """
            fun main() {
                x;
                10;
                false;
                true;
                ();
                
                if true {
                    0
                } else {
                    0
                }
                test(arg1, arg2);
                {
                    nested_block
                }
                ((0));
                
                x = 0;
                var x;
                const y = 0;
                fun x () { 0 }                
            }
            """.trimIndent(),
        )

        expectErr("const x = { true; const x = 0;")
        expectErr("const x = { call( }")
        expectErr("const x = { error(; 0 }")
    }

    @Test
    fun logicalOperationParsing() {
        expectOk("const x = a && b")
        expectOk("const x = a || b")
        expectOk("const x = !a")
        expectOk("const x = a || b && c")
        expectOk("const x = a && b || c")
        expectOk("const x = a && !b || c && !d")

        expectErr("const x = a &&")
        expectErr("const x = !!a")
    }

    @Test
    fun comparisonOperationParsing() {
        expectOk("const x = a == b")
        expectOk("const x = a != b")
        expectOk("const x = a < b")
        expectOk("const x = a > b")
        expectOk("const x = a <= b")
        expectOk("const x = a >= b")

        expectErr("const x = a ==")
    }

    @Test
    fun arithmeticalOperationParsing() {
        expectOk("const x = 1 + 2")
        expectOk("const x = 1 - 2")
        expectOk("const x = 1 * 2")
        expectOk("const x = 1 / 2")
        expectOk("const x = 1 + 2 - 3")
        expectOk("const x = 1 * 2 + 3")
        expectOk("const x = 1 + 2 * 3")
        expectOk("const x = -1")
        expectOk("const x = +1")
        expectOk("const x = 1 * -2")

        expectErr("const x = 1 +")
        expectErr("const x = --1")
    }

    @Test
    fun callExprParsing() {
        expectOk("const x = test()")
        expectOk("const x = test(y)")
        expectOk("const x = test(y, z)")
        expectOk("const x = test(y, z,)")
        expectOk("const x = test(a=y, b=z,)")

        expectErr("const x = test(")
        expectErr("const x = test(a=)")
        expectErr("const x = test(x,,)")
    }

    @Test
    fun bracketExprParsing() {
        expectOk("const x = (0)")
        expectOk("const x = (((0)))")

        expectErr("const x = (0")
        expectErr("const x = (0))")
        expectErr("const x = ((0)")
    }

    @Test
    fun literalExprParsing() {
        // Unit literal
        expectOk("const x = ()")
        expectOk("const x = (      )")
        expectErr("const x = (")

        // Booleans
        expectOk("const x = true")
        expectOk("const x = false")

        // Integers
        expectOk("const x = 1")
        expectOk("const x = +10000000000000000000000")
        expectOk("const x = -30")
        expectOk("const x = -30")
        expectErr("const x = -+0")

        // Symbolic
        expectOk("const x = hello")
        expectOk("const x = my_symbol")
        expectOk("const x = _a_symbol")
        expectOk("const x = var_1")
        expectErr("const x = 1_var")
    }

    @Test
    fun typeExprParsing() {
        // Symbol type
        expectOk("const x: int = 0")
        expectOk("const x: whatever = 0")

        // Functional type
        expectOk("const x: () -> int = 0")
        expectOk("const x: (int) -> int = 0")
        expectOk("const x: (int, bool) -> int = 0")
        expectOk("const x: (int, bool,) -> int = 0")
        expectOk("fun test(x: bool) -> (int) -> bool { 0 }")
        expectErr("const x: (int) -> = 0")
        expectErr("const x: (int -> bool = 0")
    }

    // ----- Util functions -----

    /**
     * Util function to parse the given source and display the result AST. If a parsing error happens, this method
     * raises a test failure.
     */
    private fun expectOk(source: String) {
        output("==========")
        output(source)
        output("==========")
        val unit = context.analyseBuffer("testOk", source, reparse = true)
        if (unit.parsingDiagnostics.size > 0) {
            val message = StringBuilder("=== Unexpected parsing error:").appendLine()
            unit.parsingDiagnostics.forEach { d -> message.appendLine(d) }
            fail(message.toString())
        } else {
            output(unit.root!!.treeString())
            output("")
        }
    }

    /** Same as [expectOk], but fail if the parsing succeeds. */
    private fun expectErr(source: String) {
        output("==========")
        output(source)
        output("==========")
        val unit = context.analyseBuffer("testErr", source, reparse = true)
        if (unit.parsingDiagnostics.isNotEmpty()) {
            output("=== Expected parsing error(s):")
            unit.parsingDiagnostics.forEach {
                output(
                    """
                    ${it.message} 
                    ${if (it.location != null) {
                        "at ${it.location!!.start.line}:${it.location!!.start.column}"
                    } else {
                        "(no location)"
                    }}
                    """.trimIndent(),
                )
            }
            output("=== Fallback tree:")
            output(unit.root?.treeString())
            output("")
        } else {
            fail("Expecting a parsing error, got the tree:\n${unit.root?.treeString()}")
        }
    }
}
