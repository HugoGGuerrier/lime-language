package com.limelanguage

import java.io.File

/** A source identifier is just a string. */
typealias SourceId = String

/**
 * This class represents a Lime source, this can be a buffer or an existing file, this class doesn't care. A source is
 * identified by a string and all equality operations will depend on this identifier.
 *
 * @property identifier The identifier of the source.
 * @property content The content of the Lime source, as a simple string.
 */
data class Source(val identifier: SourceId, val content: String) {
    // ----- Properties -----

    /**
     * If the source comes from an existing file, its identifier should be the absolute path to it. Thus, we can access
     * it for whatever purpose
     */
    val sourceFile: File? by lazy {
        val file = File(this.identifier)
        if (file.exists() && file.isFile) {
            file
        } else {
            null
        }
    }

    // ----- Methods -----

    /** Get the lines from the source content. */
    fun getLines(): List<String> = this.content.lines()

    /**
     * Get the wanted lines from the source content.
     *
     * @param start The index of the line to start. Be careful, lines are indexed from 1.
     * @param n The number of lines to get from the starting line.
     */
    fun getLines(
        start: Int,
        n: Int,
    ): List<String> = this.getLines().subList(start - 1, start - 1 + n)

    // ----- Overrides -----

    override fun hashCode(): Int = this.identifier.hashCode()

    override fun equals(other: Any?): Boolean =
        when (other) {
            is Source -> this.identifier == other.identifier
            else -> false
        }
}
