package com.limelanguage.ast.expressions.call

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.LimeListNode

/** This node represents a list of arguments. This is used, for example, in function call nodes. */
class ArgList(
    unit: AnalysisUnit,
    location: SourceSection,
) : LimeListNode<Arg>(unit, location)
