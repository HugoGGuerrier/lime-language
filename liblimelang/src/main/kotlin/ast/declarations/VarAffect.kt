package com.limelanguage.ast.declarations

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Child
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.expressions.Expr

/** This class represents a variable affectation in the Lime language. This node belongs to the declaration family. */
class VarAffect(
    unit: AnalysisUnit,
    location: SourceSection,
    @Child(0) val name: Identifier?,
    @Child(1) val value: Expr?,
) : Decl(unit, location)
