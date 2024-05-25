package com.limelanguage.ast.expressions.literals

import com.limelanguage.SourceSection
import com.limelanguage.ast.expressions.Expr

/** This class represents an arbitrary integer literal Lime sources. */
class IntLiteral(sourceSection: SourceSection) : Expr(sourceSection)
