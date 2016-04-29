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
import java.io.IOException;
import java.nio.file.Path;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * This is test suit for {@link Clean} class.
 *
 * @author Grzegorz Gajos (grzegorz.gajos@opentangerine.com)
 * @version $Id$
 * @since 0.5
 * @checkstyle MultipleStringLiteralsCheck (2000 lines)
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.AvoidDuplicateLiterals"})
public final class CleanTest {

    /**
     * Temporary dir.
     */
    @Rule
    public transient TemporaryFolder folder = new TemporaryFolder();

    /**
     * Check how clean is working in default mode.
     */
    @Test
    public void canSkipDeleteByDefault() {
        new Check(true, "target").run();
    }

    /**
     * Execute main entry point of application.
     */
    @Test
    public void canWorkOnEmptyDirectory() {
        Clean.main("");
    }

    /**
     * Check how clean is working in delete mode.
     */
    @Test
    public void canFindAndDeleteMavenTarget() {
        new Check("target/file.txt", "target/file2.txt")
            .file("pom.xml")
            .run(Wipe.Type.MAVEN);
    }

    /**
     * Check how clean is working in delete mode on subdirectories.
     */
    @Test
    public void canFindAndDeleteMavenTargetInSubdir() {
        new Check("subdir/target/file2.txt", "subdir/target/simple.txt")
            .file("subdir/pom.xml")
            .run();
    }

    /**
     * Delete project dir using yaml configuration.
     */
    @Test
    public void deleteProjectUsingYamlConfig() {
        new Check("some/file.txt", "some/b.txt")
            .file(".clean.yml", "deletes:\n - some")
            .run(Wipe.Type.OT_CLEAN);
    }

    /**
     * Delete Grails 2.x project without any configuration.
     */
    @Test
    public void defaultGrailsVersionTwo() {
        new Check("target", "subdir.log", "subdir/target/some.log")
            .file("application.properties", "app.grails.version")
            .run(Wipe.Type.GRAILS_2);
    }

    /**
     * Delete Grails 2.x project without any configuration.
     */
    @Test
    public void defaultGrailsVersionThree() {
        new Check("build", "subdir.log", "subdir/target/some.log")
            .file(
                "build.gradle",
                "oiawef\nrsxapply plugin:\"org.grails.grails-web\"vasd"
            )
            .run(Wipe.Type.GRAILS_3);
    }

    /**
     * Delete PlayFramework 2.x project without any configuration.
     */
    @Test
    public void defaultPlayVersionTwo() {
        new Check(
            "logs/a.log",
            "target/a.file",
            "project/target/a.file",
            "project/project/target/a.file",
            ".sbtserver/a.file",
            "subdir.log",
            "subdir/target/some.log"
        )
            .file(
                "build.sbt",
                "oiawef\nrsxenablePlugins(PlayJava)web\"vasd"
            ).run(Wipe.Type.PLAYFRAMEWORK_2);
    }

    /**
     * Execute cleanup from sibling directory using dirs section.
     */
    @Test
    public void cleanupProjectStartingFromDifferentDirectory() {
        new Check(
            "two",
            false,
            "../one/todelete"
        )
            .file("../one/.clean.yml", "deletes:\n - todelete")
            .file("file2.txt")
            .file(
                ".clean.yml",
                StringUtils.join(
                    "dirs:\n - '",
                    "../one",
                    "'"
                )
            )
            .run();
    }

    /**
     * Wildcard testing: Check extension.
     */
    @Test
    public void deleteFileUsingExtensionWildcardMatching() {
        this.removeSimpleFileUsingPattern("subdir/sub/simple.*");
    }

    /**
     * Wildcard testing: Check filename.
     */
    @Test
    public void deleteFileUsingNameWildcardMatching() {
        this.removeSimpleFileUsingPattern("subdir/sub/*.txt");
    }

    /**
     * Wildcard testing: Check star at the end of the line.
     */
    @Test
    public void deleteFileUsingSuffixStarWildcardMatching() {
        this.removeSimpleFileUsingPattern("subdir/sub/*");
    }

    /**
     * Wildcard testing: Check star in the middle.
     */
    @Test
    public void deleteFileUsingMiddleStarWildcardMatching() {
        this.removeSimpleFileUsingPattern("subdir/*/simple.txt");
    }

    /**
     * Wildcard testing: Check double star at the beginning.
     */
    @Test
    public void deleteFileUsingDoubleStarStartWildcardMatching() {
        this.removeSimpleFileUsingPattern("**/simple.txt");
    }

    /**
     * Wildcard testing: Check double star at the beginning.
     */
    @Test
    public void deleteFileUsingExtensionOnlyWildcardMatching() {
        this.removeSimpleFileUsingPattern("**/*.txt");
    }

    /**
     * Wildcard testing: Check star at the beginning.
     */
    @Test
    public void deleteFileUsingSingleStarStartWildcardMatching() {
        this.removeSimpleFileUsingPattern("*/simple.txt", false);
    }

    /**
     * Remove simple.txt file using wildcard.
     * @param pattern Pattern.
     * @param deleted Does file should be deleted.
     */
    private void removeSimpleFileUsingPattern(
        final String pattern,
        final boolean deleted
    ) {
        new Check(!deleted, "subdir/sub/simple.txt")
            .file(".clean.yml", StringUtils.join("deletes:\n - ", pattern))
            .run(Wipe.Type.OT_CLEAN);
    }

    /**
     * Remove simple.txt file using wildcard.
     * @param pattern Pattern.
     */
    private void removeSimpleFileUsingPattern(final String pattern) {
        this.removeSimpleFileUsingPattern(pattern, true);
    }

    /**
     * Class responsible for performing cleanup operation using specific
     * cleaner. It creates and validates files removal with smart way so it
     * minimizes number of possible mistakes and makes tests more DRY.
     */
    public final class Check {
        /**
         * Working dir.
         */
        private final transient Path root;

        /**
         * List of files to create and check if are deleted.
         */
        private final transient String[] files;

        /**
         * In which state file should be after deletion.
         */
        private final transient boolean exists;

        /**
         * Ctor.
         * @param cfiles Files.
         */
        public Check(final String... cfiles) {
            this(false, cfiles);
        }

        /**
         * Ctor.
         * @param cexists In which state file should be after deletion.
         * @param cfiles Files.
         */
        public Check(
            final boolean cexists,
            final String... cfiles
        ) {
            this(".", cexists, cfiles);
        }

        /**
         * Ctor.
         * @param crelative Relative root directory where execute cleaning.
         * @param cexists In which state file should be after deletion.
         * @param cfiles Files.
         */
        public Check(
            final String crelative,
            final boolean cexists,
            final String... cfiles
        ) {
            this.root = CleanTest.this.folder.getRoot().toPath()
                .resolve(crelative);
            this.exists = cexists;
            this.files = cfiles;
        }

        /**
         * Create temporary file with specific content.
         * Skip if file is directory.
         * @param relative Relative path.
         * @param content File content.
         * @return This.
         */
        public Check file(final String relative, final String content) {
            try {
                final File target = this.root
                    .resolve(relative)
                    .toFile()
                    .getCanonicalFile();
                if (!target.exists()) {
                    FileUtils.forceMkdir(new File(target.getParent()));
                    FileUtils.writeStringToFile(target, content);
                    Logger.debug(this, "Created temp file: %s", target);
                }
            } catch (final IOException exc) {
                throw new IllegalStateException(
                    "Unable to create file",
                    exc
                );
            }
            return this;
        }

        /**
         * Create temporary file with dummy content. Skip if file is directory.
         *
         * @param relative Relative path.
         * @return This.
         */
        public Check file(final String relative) {
            return this.file(relative, "two\nlines");
        }

        /**
         * Run cleaning using regular cleaning method.
         */
        public void run() {
            this.exec(() -> new Clean(this.mode()).clean(this.root));
        }

        /**
         * Run cleaning mode for specific type.
         * @param type Type of cleaning.
         */
        public void run(final Wipe.Type type) {
            this.exec(
                () -> Wipe.DEFAULT
                    .stream()
                    .filter(it -> it.match(type))
                    .findAny()
                    .get()
                    .clean(
                        new Delete(this.mode(), new Summary(this.mode())),
                        this.root
                    )
            );
        }

        /**
         * Execute cleaning using specific type. Deleting is done with delete
         * flag and in verbose mode.
         *
         * @param runnable Cleaning operation.
         */
        private void exec(final Runnable runnable) {
            for (final String relative : this.files) {
                this.file(relative);
                MatcherAssert.assertThat(
                    this.root.resolve(relative).toFile().exists(),
                    Matchers.is(true)
                );
            }
            runnable.run();
            for (final String relative : this.files) {
                MatcherAssert.assertThat(
                    String.format("File should be deleted %s", relative),
                    this.root.resolve(relative).toFile().exists(),
                    Matchers.is(this.exists)
                );
            }
        }

        /**
         * Generate verbose mode using checking arguments.
         * @return Cleaning mode.
         */
        private Mode mode() {
            Mode mode = new Mode("-vd");
            if (this.exists) {
                mode = new Mode("-v");
            }
            return mode;
        }
    }
}
