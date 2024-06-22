package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.expressions.Expr

/**
 * This class is the base for all declaration nodes. Those nodes are the ones who introduce new
 * symbols in the current lexical environment.
 * Note that all declarations are also expression.
 *
 * @property name The name of the introduced symbol.
 * @property type The type of the declared symbol.
 * @property value The expression representing the bounded value.
 */
abstract class Decl(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Identifier?,
    @Child(2) val type: Identifier?,
    @Child(3) val value: Expr?,
) : Expr(unit, location)
