package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/**
 * This node represents an arbitrary identifier in the lime sources. This node DOESN'T represent the symbol accesses
 * in the language, see the [com.limelanguage.ast.expressions.literals.SymbolLiteral] for this purpose.
 */
class Identifier(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child val text: String,
) : LimeNode(unit, location)
