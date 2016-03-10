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
import org.apache.commons.io.FilenameUtils;

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
    private transient long total = 0L;
    /**
     * Count files and directories.
     */
    private transient long count = 0L;

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
        if (this.mode.readonly()) {
            this.info(file, "Found");
        } else {
            this.info(file, "Deleting");
        }
    }

    /**
     * Display summary based on current state.
     */
    public void finished() {
        // FIXME GG: in progress, connect this informations into single line
        // FIXME GG: in progress, add test cases
        Logger.info(
            this,
            "Summary"
        );
        Logger.info(
            this,
            " - Total directories and files: %s",
            this.count
        );
        Logger.info(
            this,
            " - Total occupied space: %s",
            FileUtils.byteCountToDisplaySize(this.total)
        );
    }

    /**
     * Display human readable prefix for file resource.
     * @param file File.
     * @return String.
     */
    private static String prefix(final File file) {
        String prefix = "File";
        if (file.isDirectory()) {
            prefix = "Directory";
        }
        return prefix;
    }

    private void info(final File file, final String operation) {
        Logger.info(
            this,
            "%s: %s '%s' [%s]",
            operation,
            prefix(file),
            file,
            FileUtils.byteCountToDisplaySize(FileUtils.sizeOf(file))
        );
    }

}
