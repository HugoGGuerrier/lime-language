package com.limelanguage.parser

import com.limelanguage.Source
import com.limelanguage.SourceLocation
import com.limelanguage.SourceSection
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.ast.Point
import org.antlr.v4.kotlinruntime.ast.Position

// ===== Extension functions =====

/** Create a source section from an ANTLR [position]. */
internal fun SourceSection.Companion.fromPosition(
    source: Source,
    position: Position,
): SourceSection =
    SourceSection(
        source,
        SourceLocation.fromPoint(position.start),
        SourceLocation.fromPoint(position.end),
    )

/** Create a new source section from an ANTLR [token]. */
internal fun SourceSection.Companion.fromToken(
    source: Source,
    token: Token,
): SourceSection =
    SourceSection(
        source,
        SourceLocation.fromPoint(token.startPoint()),
        SourceLocation.fromPoint(token.endPoint() ?: token.startPoint()),
    )

/**
 * Create a new source section from two ANTLR tokens. The resulting section goes from the start of [startToken]
 * to the end of [stopToken], this method handles cases where [startToken] is after or included in [stopToken]
 * (or the contrary), and returns the largest computable section.
 */
internal fun SourceSection.Companion.fromTokens(
    source: Source,
    startToken: Token,
    stopToken: Token,
): SourceSection {
    // Compute the position bounds
    val startPoint =
        if (startToken.startIndex <= stopToken.startIndex) {
            startToken.startPoint()
        } else {
            stopToken.startPoint()
        }
    val endPoint =
        if (stopToken.stopIndex >= startToken.stopIndex) {
            stopToken.endPoint()
        } else {
            startToken.endPoint()
        }

    // Return the result
    return SourceSection(
        source,
        SourceLocation.fromPoint(startPoint),
        SourceLocation.fromPoint(endPoint ?: startPoint),
    )
}

/** Create a source location object from an ANTLR [point]. */
internal fun SourceLocation.Companion.fromPoint(point: Point) = SourceLocation(point.line, point.column)
