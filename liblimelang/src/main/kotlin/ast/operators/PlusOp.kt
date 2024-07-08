package com.limelanguage.ast.operators

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/** This node represents the "+" operator. */
class PlusOp(
    unit: AnalysisUnit,
    location: SourceSection,
) : Operator(unit, location)
