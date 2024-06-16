package com.limelanguage

import org.antlr.v4.kotlinruntime.ast.Point
import org.antlr.v4.kotlinruntime.ast.Position

/**
 * This class represents a specific section from a source.
 *
 * @property source The concerned Lime source.
 * @property start Start of the section.
 * @property end End of the section.
 */
data class SourceSection(val source: Source, val start: SourceLocation, val end: SourceLocation) {
    companion object {
        /** Create a new source location from an ANTLR [position] with [source]. */
        fun fromPosition(
            source: Source,
            position: Position,
        ): SourceSection =
            SourceSection(
                source,
                SourceLocation.fromPoint(position.start),
                SourceLocation.fromPoint(position.end),
            )
    }

    // ----- Methods -----

    /** Get the lines corresponding to the source section. */
    fun getLines(): List<String> = this.source.getLines(start.line, end.line + 1 - start.line)
}

/** This class represents a location in a source, defined by its [line] and [column]. */
data class SourceLocation(val line: Int, val column: Int) {
    companion object {
        val FIRST: SourceLocation = SourceLocation(1, 0)

        fun fromPoint(point: Point) = SourceLocation(point.line, point.column)
    }
}
