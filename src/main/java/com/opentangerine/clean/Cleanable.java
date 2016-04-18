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

import com.google.common.collect.Lists;
import com.jcabi.log.Logger;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;

/**
 * This is initial class, should be changed to something else.
 *
 * // FIXME GG: in progress, refactor to more modular form
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
public interface Cleanable {

    /**
     * Clean.
     *
     * @param path Working directory.
     */
    void clean(final Path path);

    /**
     * Display information about matching configuration.
     *
     * @param path Working directory.
     * @param console Console.
     */
    void display(final Path path, final Console console);

    /**
     * Check if matching.
     *
     * @param path Working directory.
     * @return True if cleanable.
     */
    boolean match(final Path path);

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
         * @param cdelete Delete.
         */
        public Maven(final Delete cdelete) {
            this.delete = cdelete;
        }

        @Override
        public void clean(final Path path) {
            if (this.match(path)) {
                this.delete.file(path.resolve("target"));
            }
        }

        @Override
        public boolean match(final Path path) {
            return path.resolve("pom.xml").toFile().exists();
        }

        @Override
        public void display(final Path path, final Console console) {
            console.print(
                String.format(
                    "[Maven]: %s", path
                )
            );
        }
    }

    /**
     * Cleanup Grails structure.
     */
    final class Grails2 implements Cleanable {

        /**
         * Delete operation.
         */
        private final Delete delete;

        /**
         * Ctor.
         * @param cdelete Delete.
         */
        public Grails2(final Delete cdelete) {
            this.delete = cdelete;
        }

        // FIXME GG: in progress, make it more dry
        @Override
        public void clean(final Path path) {
            if (this.match(path)) {
                Yconfig yconf = new Yconfig();
                yconf.setDeletes(Lists.newArrayList("target", "**/*.log"));
                yconf
                    .filesToDelete(path)
                    .forEach(this.delete::file);
            }
        }

        // FIXME GG: in progress, extract to different method
        // and cleanup
        @Override
        public boolean match(final Path path) {
            boolean success;
            final File file = path
                .resolve("application.properties")
                .toFile();
            try {
                success = file.exists() && FileUtils
                    .readFileToString(file)
                    .contains("app.grails.version");
            } catch (IOException ioe) {
                Logger.debug(this, "Unable to read %s %[exception]s", file, ioe);
                success = false;
            }
            return success;
        }

        @Override
        public void display(final Path path, final Console console) {
            console.print(
                String.format(
                    "[Maven]: %s", path
                )
            );
        }
    }

    /**
     * Cleanup using yaml configuration file.
     */
    final class Yclean implements Cleanable {

        /**
         * Delete operation.
         */
        private final Delete delete;

        /**
         * Ctor.
         * @param cdelete Delete.
         */
        public Yclean(final Delete cdelete) {
            this.delete = cdelete;
        }

        @Override
        public void clean(final Path path) {
            if (this.match(path)) {
                Yconfig
                    .load(Yclean.file(path))
                    .filesToDelete(path)
                    .forEach(this.delete::file);
            }
        }

        @Override
        public boolean match(final Path path) {
            return Yclean.file(path).exists();
        }

        @Override
        public void display(final Path path, final Console console) {
            console.print(
                String.format(
                    "[.clean.yml]: %s", path
                )
            );
        }

        /**
         * Returns configuration file.
         *
         * @param path Working directory.
         * @return Clean yml file.
         */
        private static File file(final Path path) {
            return path.resolve(".clean.yml").toFile();
        }

    }
}
