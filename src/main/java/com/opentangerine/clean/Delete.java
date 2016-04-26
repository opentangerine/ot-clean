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

import java.io.File;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

/**
 * Class responsible for all delete operations.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
public final class Delete {
    /**
     * Clean mode.
     */
    private final transient Mode mode;
    /**
     * Summary.
     */
    private final transient Summary summary;

    /**
     * Ctor.
     * @param cmode Clean mode.
     * @param csummary Summary.
     */
    public Delete(final Mode cmode, final Summary csummary) {
        this.mode = cmode;
        this.summary = csummary;
    }

    /**
     * Deletes file/dir under given path.
     * @param path Path.
     */
    public void file(final Path path) {
        this.file(path.toFile());
    }

    /**
     * Deletes file/dir under given path.
     * @param file File.
     */
    public void file(final File file) {
        if (file.exists()) {
            this.summary.add(file);
            if (!this.mode.readonly()) {
                FileUtils.deleteQuietly(file);
            }
        }
    }

    /**
     * Mode.
     * @return Mode.
     */
    public Mode getMode() {
        return this.mode;
    }
}
