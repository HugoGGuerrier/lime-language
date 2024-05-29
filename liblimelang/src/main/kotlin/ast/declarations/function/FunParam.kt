package com.limelanguage.ast.declarations.function

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit
import com.limelanguage.ast.Identifier
import com.limelanguage.ast.declarations.Decl

/** This class represents a function parameter in the Lime language. */
class FunParam(
    unit: AnalysisUnit,
    location: SourceSection,
    name: Identifier,
    type: Identifier,
) : Decl(unit, location, name, type, null)
