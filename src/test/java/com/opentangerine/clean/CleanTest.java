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

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
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
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class CleanTest {

    /**
     * Simple text file.
     */
    private static final String SIMPLE_TXT = "subdir/sub/simple.txt";

    /**
     * Temporary dir.
     */
    @Rule
    public transient TemporaryFolder folder = new TemporaryFolder();

    /**
     * Check how clean is working in default mode.
     */
    @Test
    public void isNotDeletingByDefault() {
        final Path target = this.createMavenAndGetTarget();
        MatcherAssert.assertThat(
            target.toFile().isDirectory(),
            Matchers.is(true)
        );
        new Clean("").clean(Paths.get(this.folder.getRoot().toURI()));
        MatcherAssert.assertThat(
            target.toFile().isDirectory(),
            Matchers.is(true)
        );
    }

    /**
     * Check how clean is working on random directory.
     */
    @Test
    public void noExceptionOnEmptyDir() {
        new Console().help();
        new Clean("").clean(Paths.get(this.folder.getRoot().toURI()));
    }

    /**
     * Check how clean is working in delete mode.
     */
    @Test
    public void deleteTargetDirectoryForMavenProject() {
        final Path target = this.createMavenAndGetTarget();
        MatcherAssert.assertThat(
            target.toFile().isDirectory(),
            Matchers.is(true)
        );
        new Clean("-d").clean(Paths.get(this.folder.getRoot().toURI()));
        MatcherAssert.assertThat(
            target.toFile().isDirectory(),
            Matchers.is(false)
        );
    }

    /**
     * Check how clean is working in delete mode.
     */
    @Test
    public void deleteTargetSubdirForMavenProject() {
        final Path root = this.createProject();
        final String subdir = "subdir/target";
        MatcherAssert.assertThat(
            root.resolve(subdir).toFile().isDirectory(),
            Matchers.is(true)
        );
        new Clean("dr").clean(Paths.get(this.folder.getRoot().toURI()));
        MatcherAssert.assertThat(
            root.resolve(subdir).toFile().isDirectory(),
            Matchers.is(false)
        );
    }

    /**
     * Delete maven target using yaml configuration.
     */
    @Test
    public void deleteMavenProjectUsingYamlConfig() {
        final Path target = this.createMavenAndGetTarget();
        final Path root = target.getParent();
        this.writeYml(root, "deletes:\n - target");
        MatcherAssert.assertThat(
            target.toFile().isDirectory(),
            Matchers.is(true)
        );
        final Mode mode = new Mode(Mode.Arg.D.getLabel());
        new Cleanable.Yclean(
            new Delete(
                mode,
                new Summary(mode)
            )
        ).clean(root);
        MatcherAssert.assertThat(
            target.toFile().isDirectory(),
            Matchers.is(false)
        );
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
        final Path root = this.createProject();
        this.writeYml(
            root,
            StringUtils.join("deletes:\n - ", pattern)
        );
        MatcherAssert.assertThat(
            root.resolve(CleanTest.SIMPLE_TXT).toFile().exists(),
            Matchers.is(true)
        );
        final Mode mode = new Mode("d");
        new Cleanable.Yclean(new Delete(mode, new Summary(mode))).clean(root);
        MatcherAssert.assertThat(
            root.resolve(CleanTest.SIMPLE_TXT).toFile().exists(),
            Matchers.is(!deleted)
        );
    }

    /**
     * Remove simple.txt file using wildcard.
     * @param pattern Pattern.
     */
    private void removeSimpleFileUsingPattern(final String pattern) {
        this.removeSimpleFileUsingPattern(pattern, true);
    }

    /**
     * Create yml file.
     * @param root Directory.
     * @param content Content.
     */
    private void writeYml(final Path root, final String content) {
        try {
            FileUtils.writeStringToFile(
                root.resolve(".clean.yml").toFile(),
                content
            );
        } catch (final IOException exc) {
            throw new IllegalStateException("Failed", exc);
        }
    }

    /**
     * Create simple maven repo and get target dir.
     * @return Target directory.
     */
    private Path createMavenAndGetTarget() {
        return this.createProject().resolve("target");
    }

    /**
     * Creates maven project structure.
     * @return Temp directory of maven project.
     */
    private Path createProject() {
        try {
            final Path root = this.folder.getRoot().toPath();
            this.tempFile(root.resolve("pom.xml"));
            this.tempFile(root.resolve("target/file.txt"));
            this.tempFile(root.resolve("target/file2.txt"));
            this.tempFile(root.resolve("subdir/target/file2.txt"));
            this.tempFile(root.resolve("subdir/pom.xml"));
            this.tempFile(root.resolve(CleanTest.SIMPLE_TXT));
            return root;
        } catch (final IOException exc) {
            throw new IllegalStateException(
                "Unable to create maven project structure",
                exc
            );
        }
    }

    /**
     * Create temporary file with dummy date. Skip if file is directory.
     *
     * @param path Target path.
     * @throws IOException Unknown IO Exception.
     */
    private void tempFile(final Path path) throws IOException {
        FileUtils.touch(path.toFile());
        if(!path.toFile().isDirectory()) {
            FileUtils.writeStringToFile(path.toFile(), "two\blines");
        }
    }

}
