package com.limelanguage.analysis

import com.limelanguage.Diagnostic
import com.limelanguage.Source
import com.limelanguage.ast.LimeNode
import com.limelanguage.parser.parseLimeSource

/** This class represents an analysed Lime [source] file. */
class AnalysisUnit(val source: Source) {
    // ----- Properties -----

    /** The root of the analysis unit, result of the source parsing. */
    val root: LimeNode? = parseLimeSource(this.source)

    /** All diagnostics collected during the source analysis. */
    val diagnostics: MutableList<Diagnostic> = ArrayList()
}
