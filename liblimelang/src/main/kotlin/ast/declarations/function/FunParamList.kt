package com.limelanguage.ast.declarations.function

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.LimeListNode

/** This class represents a list of parameter in a function declaration in the Lime language. */
class FunParamList(
    unit: AnalysisUnit,
    location: SourceSection,
) : LimeListNode<FunParam>(unit, location)
