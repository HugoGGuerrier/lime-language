package com.limelanguage.ast.expressions.literals

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.expressions.Expr

/** This class represents an arbitrary integer literal Lime sources. */
class IntLiteral(unit: AnalysisUnit, location: SourceSection) : Expr(unit, location)
