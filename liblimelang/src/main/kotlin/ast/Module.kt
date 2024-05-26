package com.limelanguage.ast

import com.limelanguage.SourceSection
import com.limelanguage.analysis.AnalysisUnit

/**
 * This node represents a module declaration in the Lime language. A module is a collection of unique symbols bounded to
 * values and being accessible from other modules.
 */
class Module(unit: AnalysisUnit, location: SourceSection) : LimeNode(unit, location)
