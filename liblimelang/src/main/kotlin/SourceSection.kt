package com.limelanguage

/** This class represents a location in a source, defined by its [line] and [column]. */
data class SourceLocation(val line: Int, val column: Short)

/**
 * This class represents a specific section from a source.
 *
 * @property source The concerned Lime source.
 * @property start Start of the section.
 * @property end End of the section.
 */
data class SourceSection(val source: Source, val start: SourceLocation, val end: SourceLocation) {
    // ----- Properties -----

    /** Get the lines corresponding to the source section. */
    fun getLines(): List<String> = this.source.getLines(start.line, end.line + 1 - start.line)
}
