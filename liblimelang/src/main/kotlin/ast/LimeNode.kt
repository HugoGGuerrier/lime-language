package com.limelanguage.ast

import com.limelanguage.SourceSection

/**
 * This class is the base of all lime nodes, it contains all common information and operations require for all AST node
 * types.
 */
abstract class LimeNode(val sourceSection: SourceSection)
