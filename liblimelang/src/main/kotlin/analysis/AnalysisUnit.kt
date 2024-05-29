package com.limelanguage.analysis

import com.limelanguage.Diagnostic
import com.limelanguage.Source
import com.limelanguage.ast.LimeNode
import com.limelanguage.parser.LimeLexer
import com.limelanguage.parser.LimeParser
import com.limelanguage.parser.ParsingVisitor
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream

/** This class represents an analysed Lime [source] file. */
class AnalysisUnit(val source: Source) {
    // ----- Properties -----

    /** The root of the analysis unit, result of the source parsing. */
    val root: LimeNode? = parseLimeSource()

    /** All diagnostics collected during the source analysis. */
    val diagnostics: MutableList<Diagnostic> = ArrayList()

    // ----- Methods -----

    /**
     * Internal method to parse the unit source code and return the node extracted from it. This
     * method is just use to initialize the unit root for now.
     */
    private fun parseLimeSource(): LimeNode? {
        // Create lexer and parser
        val lexer = LimeLexer(CharStreams.fromString(source.content))
        val parser = LimeParser(CommonTokenStream(lexer))

        // Parse the lime source and visit its parsing tree to create the AST
        try {
            val sourceModuleContext = parser.file_module()
            val visitor = ParsingVisitor(this)
            return sourceModuleContext.accept(visitor)
        } catch (e: Exception) {
            // TODO: Handle the exception to register the valid diagnostics
            e.printStackTrace()
            return null
        }
    }
}
