package com.limelanguage.ast.expressions.operations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.expressions.Expr
import com.limelanguage.ast.operators.Operator

/** This node represents all arithmetical unary operations in Lime. */
class ArithUnOp(
    unit: AnalysisUnit,
    location: SourceSection,
    op: Operator?,
    operand: Expr?,
) : UnOp(unit, location, op, operand)
