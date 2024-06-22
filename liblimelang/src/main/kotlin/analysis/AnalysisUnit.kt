package com.limelanguage.analysis

import com.limelanguage.Diagnostic
import com.limelanguage.Source
import com.limelanguage.ast.LimeNode
import com.limelanguage.parser.ErrorStrategy
import com.limelanguage.parser.LimeLexer
import com.limelanguage.parser.LimeParser
import com.limelanguage.parser.ParsingVisitor
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

/** This class represents an analysed Lime [source] file. */
class AnalysisUnit(val source: Source) {
    // ----- Properties -----

    /** All diagnostics collected during the source analysis. */
    val diagnostics: MutableList<Diagnostic> = ArrayList()

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
        val parser = LimeParser(CommonTokenStream(lexer))
        parser.errorHandler = ErrorStrategy(this)

        // Parse the lime source and visit its parsing tree to create the AST
        return try {
            val sourceModuleContext = parser.compilation_unit()
            val visitor = ParsingVisitor(this)
            sourceModuleContext.accept(visitor)
        } catch (e: Exception) {
            e.printStackTrace()
            addDiagnostic(e)
            null
        }
    }

    /** Add a diagnostic to this analysis unit. */
    fun addDiagnostic(diagnostic: Diagnostic) {
        diagnostics.add(diagnostic)
    }

    /** Add a diagnostic to this analysis unit from an exception. */
    fun addDiagnostic(exception: Exception) {
        diagnostics.add(
            Diagnostic(exception.message ?: exception.stackTraceToString()),
        )
    }
}
