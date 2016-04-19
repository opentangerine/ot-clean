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
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This is initial class, should be changed to something else.
 *
 * // FIXME GG: in progress, refactor to more modular form
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
// FIXME GG: in progress, rename to Wipe
interface Cleanable {


    /**
     * Clean. This method should cleanup specific path using provided
     * delete handler.
     *
     * @param delete Deletion handler.
     * @param path Working directory.
     */
    void clean(final Delete delete, final Path path);

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
    Cleanable MAVEN = new Definition(
        "Maven",
        If.fileExists("pom.xml"),
        Then.delete("target")
    );

    /**
     * Cleanup Grails structure.
     */
    Cleanable GRAILS_2 = new Definition(
        "Grails 2",
        If.fileExistsWithRegExp("application.properties", "app.grails.version"),
        Then.delete("target", "**/*.log")
    );

    /**
     * Cleanup using yaml configuration file.
     */
    Cleanable YCLEAN = new Definition(
        ".clean.yml",
        If.fileExists(".clean.yml"),
        Then.useYmlConfig()
    );

    /**
     * List of all available cleaners.
     * // FIXME GG: in progress, convert to map and change this all method.
     * // FIXME GG: in progress, extract types to enumeration
     */
    List<Cleanable> ALL = Lists.newArrayList(
        MAVEN,
        GRAILS_2,
        YCLEAN
    );

    /**
     * Collection of behaviours for matching purposes.
     */
    final class If {

        /**
         * Returns true if file exists.
         *
         * @param name Name of the file.
         * @return Matching behaviour.
         */
        static Function<Path, Boolean> fileExists(String name) {
            return path -> path.resolve(name).toFile().exists();
        }

        /**
         * Returns true if file exists and contains specific phrase.
         *
         * @param name Name of the file.
         * @return Matching behaviour.
         */
        static Function<Path, Boolean> fileExistsWithRegExp(
            String name, String regexp
        ) {
            return path -> {
                try {
                    final File file = path.resolve(name).toFile();
                    return file.exists() && FileUtils
                        .readFileToString(file)
                        .matches(regexp);
                } catch (IOException exc) {
                    throw new IllegalStateException("Unable to read file", exc);
                }
            };
        }
    }

    /**
     * Collection of behaviours for cleaning purposes.
     */
    final class Then {

        /**
         * Executes cleanup using list of paths. RegExps are allowed.
         *
         * @param deletes List of paths.
         * @return Deleting behaviour.
         */
        static BiConsumer<Delete, Path> delete(String... deletes) {
            return (delete, path) -> {
                // FIXME GG: in progress, extract yconf so it's not needed
                // within deletion scope
                Yconfig yconf = new Yconfig();
                yconf.setDeletes(Lists.newArrayList(deletes));
                yconf.filesToDelete(path).forEach(delete::file);
            };
        }

        /**
         * Executes cleanup using deletes section in YML config.
         *
         * @return Deleting behaviour.
         */
        static BiConsumer<Delete, Path> useYmlConfig() {
            return (delete, path) -> {
                // FIXME GG: in progress, extract yconf so it's not needed
                // within deletion scope
                Yconfig
                    .load(path.resolve(".clean.yml").toFile())
                    .filesToDelete(path)
                    .forEach(delete::file);
            };
        }
    }

    final class Definition implements Cleanable {

        /**
         * Name of the cleaning definition.
         */
        private String name;

        /**
         * Matching behaviour.
         */
        private Function<Path, Boolean> matcher;

        /**
         * Cleaning behaviour.
         */
        private BiConsumer<Delete, Path> cleaner;

        /**
         * Ctor.
         *
         * @param cname Name of the cleaner.
         * @param cmatcher Matching behaviour.
         * @param ccleaner Cleaning behaviour.
         */
        public Definition(
            final String cname,
            final Function<Path, Boolean> cmatcher,
            final BiConsumer<Delete, Path> ccleaner
        ) {
            this.name = cname;
            this.matcher = cmatcher;
            this.cleaner = ccleaner;
        }

        @Override
        public void clean(final Delete delete, final Path path) {
            if(this.match(path)) {
                this.cleaner.accept(delete, path);
            }
        }

        @Override
        public boolean match(final Path path) {
            return this.matcher.apply(path);
        }

        @Override
        public void display(final Path path, final Console console) {
            console.print(
                String.format(
                    "[%s]: %s", this.name, path
                )
            );
        }

    }




}
