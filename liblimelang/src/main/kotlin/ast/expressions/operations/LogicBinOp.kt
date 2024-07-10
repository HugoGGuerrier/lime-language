package com.limelanguage.ast.expressions.operations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.expressions.Expr
import com.limelanguage.ast.operators.Operator

/** This node represents all logical binary operations in Lime. */
class LogicBinOp(
    unit: AnalysisUnit,
    location: SourceSection,
    left: Expr?,
    op: Operator?,
    right: Expr?,
) : BinOp(unit, location, left, op, right)
