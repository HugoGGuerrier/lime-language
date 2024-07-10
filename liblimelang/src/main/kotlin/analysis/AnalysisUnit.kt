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

/** This class represents an analysed Lime [source] file. */
class AnalysisUnit(val source: Source) {
    // ----- Properties -----

    /** All diagnostics collected during the source parsing. */
    val parsingDiagnostics: MutableList<Diagnostic> = ArrayList()

    /** The root of the analysis unit, result of the source parsing. */
    val root: LimeNode? = parseLimeSource()

    // ----- Methods -----

    /**
     * Internal method to parse the unit source code and return the node extracted from it. This
     * method is just use to initialize the unit root for now.
     */
    private fun parseLimeSource(): LimeNode? {
        // Create lexer and parser
        val lexer = LimeLexer(CharStreams.fromString(source.content))
        lexer.removeErrorListeners()
        lexer.addErrorListener(LexingErrorListener(this))
        val parser = LimeParser(CommonTokenStream(lexer))
        parser.errorHandler = ParsingErrorStrategy(this)

        // Parse the lime source and visit its parsing tree to create the AST
        return try {
            val sourceModuleContext = parser.compilation_unit()
            val visitor = ParsingVisitor(this)
            sourceModuleContext.accept(visitor)
        } catch (e: Exception) {
            e.printStackTrace()
            addParsingDiagnostic(Diagnostic(e.message ?: e.stackTraceToString()))
            null
        }
    }

    /** Add a diagnostic to this analysis unit. */
    fun addParsingDiagnostic(diagnostic: Diagnostic) {
        parsingDiagnostics.add(diagnostic)
    }
}
