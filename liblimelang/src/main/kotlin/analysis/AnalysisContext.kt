package com.limelanguage.analysis

import com.limelanguage.Source
import java.io.File
import java.nio.charset.Charset

/**
 * This class is the root of the analysis process of Lime sources. It contains all common data for
 * source files analysis and already analysed sources.
 *
 * @property charset The default charset to decode source files in. Its default value is UTF_8.
 */
class AnalysisContext(val charset: Charset = Charsets.UTF_8) {
    // ----- Properties -----

    /** All analysed sources, linked to their analysis units. */
    private val units: MutableMap<Source, AnalysisUnit> = HashMap()

    // ----- Methods ------

    /**
     * Analyse a file and get the resulting analysis unit from it if possible. If the file doesn't
     * exist or is not readable, then return null.
     *
     * @param charset Decode the given file with another charset. Its default value is one provided
     *                during the context initialization.
     * @param reparse Whether to reparse the file, even if it has already been parsed.
     */
    fun analyseFile(
        file: File,
        charset: Charset = this.charset,
        reparse: Boolean = false,
    ): AnalysisUnit? =
        if (file.exists() && file.isFile && file.canRead()) {
            analyseBuffer(file.canonicalPath, file.readText(charset), reparse)
        } else {
            null
        }

    /**
     * Analyse the buffer and return an analysis unit for it. The analysis of a buffer has several
     * steps:
     *  - Parsing the provided buffer
     *
     * @param reparse Whether to reparse the buffer, even if it has already been parsed.
     */
    fun analyseBuffer(
        bufferName: String,
        bufferContent: String,
        reparse: Boolean = false,
    ): AnalysisUnit {
        val source = Source(bufferName, bufferContent)

        // Get the already analysed source if not reparse is necessary
        if (units.containsKey(source) && !reparse) {
            return this.units[source]!!
        }

        // Else, analyse the source and place it in the cache
        val unit = AnalysisUnit(source)
        units[source] = unit
        return unit
    }
}
