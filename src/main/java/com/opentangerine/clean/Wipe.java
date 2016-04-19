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
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.maven.shared.utils.StringUtils;

/**
 * This is initial class, should be changed to something else.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 */
interface Wipe {


    /**
     * Clean. This method should cleanup specific path using provided
     * delete handler.
     *
     * @param delete Deletion handler.
     * @param path Working directory.
     */
    void clean(final Delete delete, final Path path);

    /**
     * Available types of default cleaners.
     */
    enum Type {
        MAVEN,
        GRAILS_2,
        GRAILS_3,
        PLAYFRAMEWORK_2,
        OT_CLEAN;

        /**
         * Generates user friendly name of this enumeration.
         * @return Name.
         */
        public String display() {
            return StringUtils.replace(
                WordUtils.capitalize(
                    name().toLowerCase(Locale.ENGLISH)
                ),
                "_",
                " "
            );
        }
    }

    /**
     * List of default cleaners.
     */
    List<Definition> DEFAULT = Lists.newArrayList(
        new Definition(
            Type.MAVEN,
            If.fileExists("pom.xml"),
            Then.delete("target")
        ),
        new Definition(
            Type.GRAILS_2,
            If.fileExistsWithRegExp(
                "application.properties",
                "app.grails.version"
            ),
            Then.delete("target", "**/*.log")
        ),
        new Definition(
            Type.GRAILS_3,
            If.fileExistsWithRegExp(
                "build.gradle",
                "apply plugin:.*org.grails"
            ),
            Then.delete("build", "**/*.log")
        ),
        new Definition(
            Type.OT_CLEAN,
            If.fileExists(".clean.yml"),
            Then.useYmlConfig()
        ),
        new Definition(
            Type.PLAYFRAMEWORK_2,
            If.fileExistsWithRegExp(
                "build.sbt",
                "enablePlugins\\(PlayJava\\)"
            ),
            Then.delete(
                "logs",
                "target",
                "project/target",
                "project/project/target",
                ".sbtserver",
                "**/*.log"
            )
        )
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
                    return file.exists() && Pattern.compile(regexp).matcher(
                        FileUtils.readFileToString(file)).find();
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
            return (delete, path) ->
                new Scan()
                    .scan(path, deletes)
                    .forEach(delete::file);
        }

        /**
         * Executes cleanup using deletes section in YML config.
         *
         * @return Deleting behaviour.
         */
        static BiConsumer<Delete, Path> useYmlConfig() {
            return (delete, path) ->
                Yconfig
                    .load(path.resolve(".clean.yml").toFile())
                    .filesToDelete(path)
                    .forEach(delete::file);
        }
    }

    final class Definition implements Wipe {

        /**
         * Name of the cleaning definition.
         */
        private Type type;

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
         * @param ctype Type of the cleaner.
         * @param cmatcher Matching behaviour.
         * @param ccleaner Cleaning behaviour.
         */
        public Definition(
            final Type ctype,
            final Function<Path, Boolean> cmatcher,
            final BiConsumer<Delete, Path> ccleaner
        ) {
            this.type = ctype;
            this.matcher = cmatcher;
            this.cleaner = ccleaner;
        }

        @Override
        public void clean(final Delete delete, final Path path) {
            if(this.matcher.apply(path)) {
                new Console().print(
                    String.format(
                        "[%s]: %s", this.type.display(), path
                    )
                );
                this.cleaner.accept(delete, path);
            }
        }

        /**
         * Check if definition is this type.
         *
         * @param that Type to compare.
         * @return True is type match.
         */
        public boolean is(Type that) {
            return this.type == that;
        }
    }

}
