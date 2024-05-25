package com.limelanguage.ast.expressions

import com.limelanguage.SourceSection
import com.limelanguage.ast.LimeNode

/** This node represents the base of all expression nodes in the Lime language. */
abstract class Expr(sourceSection: SourceSection) : LimeNode(sourceSection)
