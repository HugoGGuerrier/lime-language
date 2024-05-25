package com.limelanguage

/** This class represents a hint for a diagnostic, providing additional information to display. */
data class DiagnosticHint(val message: String, val sourceSection: SourceSection? = null)

/** This class represents a generic diagnostic, it is designed to be flexible and simple to format for output. */
data class Diagnostic(
    val message: String,
    val sourceSection: SourceSection? = null,
    val hints: List<DiagnosticHint> = emptyList(),
)
