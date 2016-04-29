/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 opentangerine.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.opentangerine.clean;

import com.jcabi.log.Logger;
import java.io.File;
import org.apache.commons.io.FileUtils;

/**
 * Summary is responsible for gathering all statistics data in user
 * friendly way.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.6
 */
public final class Summary {
    /**
     * Clean mode.
     */
    private final transient Mode mode;
    /**
     * Total bytes.
     */
    private transient long total;
    /**
     * Count files and directories.
     */
    private transient long count;

    /**
     * Ctor.
     * @param cmode Execution mode.
     */
    public Summary(final Mode cmode) {
        this.mode = cmode;
    }

    /**
     * Add file to summary.
     *
     * @param file File to add.
     */
    public void add(final File file) {
        this.count += 1;
        this.total += FileUtils.sizeOf(file);
        Logger.info(Clean.class, String.format(
            "%s %s: %s [%s]",
            Summary.info(this.mode.readonly(), "Found", "Deleting"),
            Summary.info(file.isDirectory(), "directory", "file"),
            file,
            FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file))
        ));
    }

    /**
     * Display summary based on current state.
     */
    public void finished() {
        Logger.info(Clean.class, String.format(
            "Summary: Found %s element(s) [%s]",
            this.count,
            FileUtils.byteCountToDisplaySize(this.total)
        ));
    }

    /**
     * Returns different description depends on provided condition.
     *
     * @param condition Condition.
     * @param pos Value if true.
     * @param neg Value if false.
     * @return Value.
     */
    private static String info(
        final boolean condition,
        final String pos,
        final String neg
    ) {
        String operation;
        if (condition) {
            operation = pos;
        } else {
            operation = neg;
        }
        return operation;
    }
}
