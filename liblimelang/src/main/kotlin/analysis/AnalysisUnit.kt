package com.limelanguage.analysis

import com.limelanguage.Diagnostic
import com.limelanguage.Source
import com.limelanguage.ast.LimeNode
import com.limelanguage.parser.LexingErrorListener
import com.limelanguage.parser.LimeLexer
import com.limelanguage.parser.LimeParser
import com.limelanguage.parser.ParsingErrorStrategy
import com.limelanguage.parser.ParsingVisitor
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

/**
 * This class represents an analysed Lime [source], belonging to the provided [context].
 * This class cannot be created by itself, you should use the [AnalysisContext.analyseBuffer] and
 * [AnalysisContext.analyseFile] methods to create one.
 */
class AnalysisUnit(
    val context: AnalysisContext,
    val source: Source,
) {
    // ----- Properties -----

    /** All diagnostics collected during the source parsing. */
    private val privateParsingDiagnostics: MutableList<Diagnostic> = ArrayList()
    val parsingDiagnostics: List<Diagnostic>
        get() = privateParsingDiagnostics

    /** All diagnostics collected during the lexical environment population. */
    private val privateLexEnvDiagnostics: MutableList<Diagnostic> = ArrayList()
    val lexEnvDiagnostics: List<Diagnostic>
        get() = privateLexEnvDiagnostics

    /**
     * The root node of the analysis unit, result of the source parsing. This property is lazily computed when first
     * accessed.
     */
    val rootNode: LimeNode? =
        run {
            // Create lexer and parser
            val lexer = LimeLexer(CharStreams.fromString(source.content))
            lexer.removeErrorListeners()
            lexer.addErrorListener(LexingErrorListener(this))
            val parser = LimeParser(CommonTokenStream(lexer))
            parser.errorHandler = ParsingErrorStrategy(this)

            try {
                // Parse the lime source and visit its parsing tree to create the AST
                val sourceModuleContext = parser.compilation_unit()
                val visitor = ParsingVisitor(this)
                val res = sourceModuleContext.accept(visitor)

                // Compute and populate all lexical environments in the result AST
                res?.populateLexicalEnv(LexicalEnvironment(context.preludeLexicalEnvironment))

                // Return the result node
                res
            } catch (e: Exception) {
                e.printStackTrace()
                addParsingDiagnostic(Diagnostic(e.message ?: e.stackTraceToString()))
                null
            }
        }

    // ----- Methods -----

    /** Add a diagnostic to parsing ones. */
    fun addParsingDiagnostic(diagnostic: Diagnostic) {
        privateParsingDiagnostics.add(diagnostic)
    }

    /** Add a diagnostic to the lexical environment ones */
    fun addLexEnvDiagnostic(diagnostic: Diagnostic) = privateLexEnvDiagnostics.add(diagnostic)
}
