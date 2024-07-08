package com.limelanguage.ast.operators

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/** This node represents the "!" operator. */
class NotOp(
    unit: AnalysisUnit,
    location: SourceSection,
) : Operator(unit, location)
