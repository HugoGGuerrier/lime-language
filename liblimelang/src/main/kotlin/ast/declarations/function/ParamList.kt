package com.limelanguage.ast.declarations.function

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.LimeListNode

/** This node represents a list of parameter in a function declaration in the Lime language. */
class ParamList(
    unit: AnalysisUnit,
    location: SourceSection,
) : LimeListNode<Param>(unit, location)
