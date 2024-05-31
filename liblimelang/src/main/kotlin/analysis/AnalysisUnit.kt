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
        try {
            val sourceModuleContext = parser.file_module()
            val visitor = ParsingVisitor(this)
            return sourceModuleContext.accept(visitor)
        } catch (e: Exception) {
            addDiagnostic(e)
            return null
        }
    }

    /** Add a diagnostic to this analysis unit. */
    fun addDiagnostic(diagnostic: Diagnostic) {
        this.diagnostics.add(diagnostic)
    }

    /** Add a diagnostic to this analysis unit from an exception. */
    fun addDiagnostic(exception: Exception) {
        this.diagnostics.add(
            Diagnostic(exception.message ?: exception.toString())
        )
    }
}
