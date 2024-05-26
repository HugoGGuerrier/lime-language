package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/**
 * This class is the base of all lime nodes, it contains all common information and operations require for all AST node
 * types.
 *
 * @property unit The analysis unit which owns this node.
 * @property location The section in the Lime sources corresponding to this node.
 */
abstract class LimeNode(val unit: AnalysisUnit, val location: SourceSection)
