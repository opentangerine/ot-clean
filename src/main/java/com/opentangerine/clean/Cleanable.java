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
import java.nio.file.Path;

/**
 * This is initial class, should be changed to something else.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 */
public interface Cleanable {

    /**
     * Clean.
     *
     * @param path Working directory.
     */
    void clean(final Path path);

    /**
     * Cleanup maven structure.
     */
    final class Maven implements Cleanable {

        /**
         * Delete operation.
         */
        private final Delete delete;

        /**
         * Ctor.
         * @param mode Mode.
         */
        public Maven(final Mode mode) {
            this.delete = new Delete(mode);
        }

        @Override
        public void clean(final Path path) {
            if (path.resolve("pom.xml").toFile().exists()) {
                this.delete.directory(path.resolve("target"));
            }
        }
    }

    /**
     * Cleanup using yaml configuration file.
     */
    final class Yaml implements Cleanable {

        @Override
        public void clean(final Path path) {
            if (path.resolve("clean.yml").toFile().exists()) {
                Logger.debug(this, path.toString());
            }
        }

    }
}
