package com.limelanguage

/**
 * This class represents a generic diagnostic, it is designed to be flexible and simple to format for output.
 *
 * @property kind The type of the diagnostic. This will influence the way it is output.
 * @property message The main message for the user.
 * @property location The source section the diagnostic is about.
 * @property hints A list of additional pieces of information about the diagnostic.
 */
data class Diagnostic(
    val message: String,
    val kind: DiagnosticKind = DiagnosticKind.ERROR,
    val location: SourceSection? = null,
    val hints: List<DiagnosticHint> = emptyList(),
)

/** This enum contains all possible diagnostic kinds. */
enum class DiagnosticKind {
    ERROR,
    WARNING,
    INFO,
}

/**
 * This class represents a hint for a diagnostic, providing additional information to display.
 *
 * @property message Additional information to display to the user.
 * @property sourceSection Source context where the hint makes sense.
 */
data class DiagnosticHint(val message: String, val sourceSection: SourceSection? = null)
